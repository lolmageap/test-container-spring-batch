package cherhy.batch.settlement

import cherhy.batch.settlement.TestContainers.postgresContainer
import cherhy.batch.settlement.entityfactory.ExampleEntityFactory
import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.testcontainers.perSpec
import kotlin.time.measureTimedValue

class BatchTest : StringSpec({
    beforeTest {
        postgresContainer.start()
        listener(postgresContainer.perSpec())
    }

    "test" {
        val randomExamples =
            measureTimedValue {
                10_000.mapParallel(ExampleEntityFactory::generateRandom)
            }.also {
                println("elapsedTime: ${it.duration} seconds")
            }.value

        println("randomExamples: ${randomExamples.size}")
    }
})