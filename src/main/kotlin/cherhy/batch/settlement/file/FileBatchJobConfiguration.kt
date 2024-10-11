package cherhy.batch.settlement.file

import cherhy.batch.settlement.model.Example
import cherhy.batch.settlement.util.property.BatchProperties.CHUNK_SIZE
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.FileSystemResource
import org.springframework.transaction.PlatformTransactionManager

@Configuration
class FileBatchJobConfiguration(
    private val jobRepository: JobRepository,
    private val platformTransactionManager: PlatformTransactionManager,
) {
    private val inputFileResource = FileSystemResource(TODO)
    private val outputFileResource = FileSystemResource(TODO)

    @Bean
    fun fileJob() = JobBuilder(TODO, jobRepository)
        .start(fileStep())
        .build()

    @Bean
    fun fileStep() = StepBuilder(
        "fileStep",
        jobRepository,
    ).chunk<Example, Example>(CHUNK_SIZE, platformTransactionManager)
//        .reader(fileItemReader())
//        .processor(fileItemProcessor())
//        .writer(fileItemWriter())
        .build()

    @Bean
    fun fileItemReader() = TODO

    @Bean
    fun fileItemWriter() = TODO

    @Bean
    fun fileItemProcessor() = TODO

    companion object {
        private const val TODO = "파일 경로 넣기"
    }
}