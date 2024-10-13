package cherhy.batch.settlement

import cherhy.batch.settlement.entityfactory.ExampleEntityFactory
import cherhy.batch.settlement.lib.mapParallel
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.comparables.shouldBeLessThan
import io.kotest.matchers.shouldBe
import kotlin.time.DurationUnit
import kotlin.time.measureTimedValue

class CreateManyDataTest : StringSpec({
    "데이터 10,000개를 병렬로 생성하고 몇 초 걸리는지 확인한다" {
        val randomExamples =
            measureTimedValue {
                10_000.mapParallel(ExampleEntityFactory::generateRandom)
            }.apply {
                println("elapsedTime: $duration seconds")
            }

        randomExamples.duration.toInt(DurationUnit.SECONDS) shouldBeLessThan 20
        randomExamples.value.size shouldBe 10_000
    }

    "데이터 10,000개를 단일 스레드로 생성하고 몇 초 걸리는지 확인한다" {
        val randomExamples =
            measureTimedValue {
                (1..10_000).map { ExampleEntityFactory.generateRandom() }
            }.apply {
                println("elapsedTime: $duration seconds")
            }

        randomExamples.duration.toInt(DurationUnit.SECONDS) shouldBeGreaterThan 60
        randomExamples.value.size shouldBe 10_000
    }
})