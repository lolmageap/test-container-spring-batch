package cherhy.batch.settlement

import cherhy.batch.settlement.TestContainers.postgresContainer
import cherhy.batch.settlement.entityfactory.ExampleEntityFactory
import cherhy.batch.settlement.lib.mapParallel
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.testcontainers.perSpec
import io.mockk.verify
import org.springframework.batch.core.JobExecution
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.time.measureTimedValue

@SpringBootTest
class BatchTest(
    @Autowired private val jobConfiguration: JobConfiguration,
    @MockkBean private val exampleJobCompletionNotificationListener: ExampleJobCompletionNotificationListener,
) : StringSpec({
    beforeTest {
        postgresContainer.start()
        listener(postgresContainer.perSpec())
    }

    "데이터를 10,000개 생성하고 몇 초 걸리는지 확인한다" {
        val randomExamples =
            measureTimedValue {
                10_000.mapParallel(ExampleEntityFactory::generateRandom)
            }.also {
                println("elapsedTime: ${it.duration} seconds")
            }.value

        println("randomExamples: ${randomExamples.size}")
    }

    "Job을 실행하고 listener가 실행되는지 확인한다" {
        val job = jobConfiguration.job()
        job.execute(JobExecution(1))

        verify(exactly = 1) { exampleJobCompletionNotificationListener.beforeJob() }
        verify(exactly = 1) { exampleJobCompletionNotificationListener.afterJob() }
    }
})