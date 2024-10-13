package cherhy.batch.settlement

import cherhy.batch.settlement.entityfactory.JobParameterFactory
import cherhy.batch.settlement.jdbc.example.process.ExampleJobCompletionNotificationListener
import cherhy.batch.settlement.lib.WithTestContainers
import cherhy.batch.settlement.util.property.BatchProperties.Job.EXAMPLE_JOB
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.StringSpec
import io.mockk.*
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.batch.test.JobRepositoryTestUtils
import org.springframework.batch.test.context.SpringBatchTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@SpringBatchTest
internal class JdbcJobTest(
    @Autowired private val jobLauncherTestUtils: JobLauncherTestUtils,
    @Autowired private val jobRepositoryTestUtils: JobRepositoryTestUtils,
    @MockkBean private val exampleJobCompletionNotificationListener: ExampleJobCompletionNotificationListener,
) : WithTestContainers, StringSpec({
    afterEach {
        jobRepositoryTestUtils.removeJobExecutions()
        clearAllMocks()
    }

    "Job을 실행하고 listener가 실행되는지 확인한다" {
        val exampleJob = JobParameterFactory.create(EXAMPLE_JOB)

        every { exampleJobCompletionNotificationListener.beforeJob() } just Runs
        every { exampleJobCompletionNotificationListener.afterJob() } just Runs

        jobLauncherTestUtils.launchJob(exampleJob)

        verify(exactly = 1) { exampleJobCompletionNotificationListener.beforeJob() }
        verify(exactly = 1) { exampleJobCompletionNotificationListener.afterJob() }
    }
})