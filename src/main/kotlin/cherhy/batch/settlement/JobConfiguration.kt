package cherhy.batch.settlement

import org.springframework.batch.core.Step
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.support.JdbcTransactionManager
import javax.sql.DataSource

@Configuration
class JobConfiguration(
    private val dataSource: DataSource,
    private val jobRepository: JobRepository,
    private val exampleJobCompletionNotificationListener: ExampleJobCompletionNotificationListener,
) {
    @Bean
    fun firstStep(
        transactionManager: JdbcTransactionManager,
    ) =
        StepBuilder(
            "startStep",
            jobRepository,
        ).chunk<Example, Example>(CHUNK_SIZE, transactionManager)
            .reader(exampleItemReader())
            .processor(exampleItemProcessor())
            .writer(exampleItemWriter())
            .build()

    @Bean
    fun lastStep(
        transactionManager: JdbcTransactionManager,
    ) =
        StepBuilder(
            "lastStep",
            jobRepository,
        ).tasklet({ _: StepContribution?, _: ChunkContext? ->
            RepeatStatus.FINISHED
        }, transactionManager)
            .build()

    @Bean
    fun job(
        firstStep: Step,
        lastStep: Step,
    ) =
        JobBuilder("exampleJob", jobRepository)
            .start(firstStep)
            .next(lastStep)
            .listener(exampleJobCompletionNotificationListener)
            .build()

    @Bean
    fun exampleItemReader() =
        JdbcCursorItemReaderBuilder<Example>()
            .name("exampleItemReader")
            .dataSource(dataSource)
            .fetchSize(CHUNK_SIZE)
            .sql("SELECT id, name, age FROM example")
            .rowMapper(BeanPropertyRowMapper(Example::class.java))
            .build()

    @Bean
    fun exampleItemProcessor() =
        ExampleItemProcessor()

    @Bean
    fun exampleItemWriter(
    ) =
        JdbcBatchItemWriterBuilder<Example>()
            .dataSource(dataSource)
            .itemSqlParameterSourceProvider(BeanPropertyItemSqlParameterSourceProvider())
            .sql("INSERT INTO example (id, name, age) VALUES (:id, :name, :age)")
            .beanMapped()
            .build()
}
