package cherhy.batch.settlement

import cherhy.batch.settlement.entityfactory.JobParameterFactory
import cherhy.batch.settlement.file.process.ExampleFileJobCompletionNotificationListener
import cherhy.batch.settlement.lib.WithTestContainers
import cherhy.batch.settlement.util.property.BatchProperties.Job.EXAMPLE_FILE_JOB
import cherhy.batch.settlement.util.property.FilePath.INPUT_CSV_FILE_NAME
import cherhy.batch.settlement.util.property.FilePath.OUTPUT_CSV_FILE_NAME
import cherhy.batch.settlement.util.property.FilePath.OUTPUT_TEXT_FILE_NAME
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.springframework.batch.core.Job
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.batch.test.JobRepositoryTestUtils
import org.springframework.batch.test.context.SpringBatchTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import java.io.File

@SpringBootTest
@SpringBatchTest
internal class FileJobTest(
    @Autowired private val jobLauncherTestUtils: JobLauncherTestUtils,
    @Autowired private val jobRepositoryTestUtils: JobRepositoryTestUtils,
    @MockkBean private val exampleFileJobCompletionNotificationListener: ExampleFileJobCompletionNotificationListener,
    @Autowired @Qualifier(EXAMPLE_FILE_JOB) private val fileJob: Job
) : WithTestContainers, StringSpec({
    afterEach {
        jobRepositoryTestUtils.removeJobExecutions()
        clearAllMocks()
        File(OUTPUT_TEXT_FILE_NAME).delete()
        File(OUTPUT_CSV_FILE_NAME).delete()
    }

    "Job을 실행하고 listener가 실행되는지 확인한다" {
        jobLauncherTestUtils.job = fileJob
        val exampleJob = JobParameterFactory.create(EXAMPLE_FILE_JOB)

        every { exampleFileJobCompletionNotificationListener.beforeJob() } just Runs
        every { exampleFileJobCompletionNotificationListener.afterJob() } just Runs

        jobLauncherTestUtils.launchJob(exampleJob)

        verify(exactly = 1) { exampleFileJobCompletionNotificationListener.beforeJob() }
        verify(exactly = 1) { exampleFileJobCompletionNotificationListener.afterJob() }
    }

    "Job을 실행하고 output 파일이 생성되고 내용이 적용되는지 확인한다" {
        jobLauncherTestUtils.job = fileJob
        val exampleJob = JobParameterFactory.create(EXAMPLE_FILE_JOB)

        every { exampleFileJobCompletionNotificationListener.beforeJob() } just Runs
        every { exampleFileJobCompletionNotificationListener.afterJob() } just Runs

        jobLauncherTestUtils.launchJob(exampleJob)

        File(OUTPUT_TEXT_FILE_NAME).exists() shouldBe true
        File(OUTPUT_CSV_FILE_NAME).readLines().size shouldBe File(INPUT_CSV_FILE_NAME).readLines().size

        val beforePrices = File(INPUT_CSV_FILE_NAME).readLines().map { it.split(",")[2].toBigDecimal() }
        val afterPrices = File(OUTPUT_CSV_FILE_NAME).readLines().map { it.split(",")[2].toBigDecimal() }
        beforePrices.map { it - 1000.toBigDecimal() } shouldBe afterPrices
    }
})