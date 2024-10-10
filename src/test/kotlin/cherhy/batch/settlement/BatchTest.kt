package cherhy.batch.settlement

import cherhy.batch.settlement.entityfactory.ExampleEntityFactory
import cherhy.batch.settlement.lib.WithTestContainers
import cherhy.batch.settlement.lib.mapParallel
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.verify
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.batch.test.JobRepositoryTestUtils
import org.springframework.batch.test.context.SpringBatchTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import kotlin.time.measureTimedValue

@SpringBootTest
@SpringBatchTest
internal class BatchTest(
    @Autowired private val jdbcTemplate: JdbcTemplate,
    @Autowired private val jobLauncherTestUtils: JobLauncherTestUtils,
    @Autowired private val jobRepositoryTestUtils: JobRepositoryTestUtils,
    @MockkBean private val exampleJobCompletionNotificationListener: ExampleJobCompletionNotificationListener,
) : WithTestContainers, StringSpec({
    afterEach {
        jobRepositoryTestUtils.removeJobExecutions()
    }

    "데이터를 10,000개 병렬로 생성하고 몇 초 걸리는지 확인한다" {
        val randomExamples =
            measureTimedValue {
                10_000.mapParallel(ExampleEntityFactory::generateRandom)
            }.also {
                println("elapsedTime: ${it.duration} seconds")
            }.value

        randomExamples.size shouldBe 10_000
    }

    "Job을 실행하고 listener가 실행되는지 확인한다" {
        jdbcTemplate.execute("insert into EXAMPLE (name, age) values ('test', 10)")

        every { exampleJobCompletionNotificationListener.beforeJob() } just Runs
        every { exampleJobCompletionNotificationListener.afterJob() } just Runs

        jobLauncherTestUtils.launchJob()

        verify(exactly = 1) { exampleJobCompletionNotificationListener.beforeJob() }
        verify(exactly = 1) { exampleJobCompletionNotificationListener.afterJob() }
    }
})