package cherhy.batch.settlement.jdbc.example.job

import cherhy.batch.settlement.jdbc.example.process.ExampleItemProcessor
import cherhy.batch.settlement.jdbc.example.process.ExampleJobCompletionNotificationListener
import cherhy.batch.settlement.model.Example
import cherhy.batch.settlement.util.property.BatchProperties.CHUNK_SIZE
import cherhy.batch.settlement.util.property.BatchProperties.ItemProcessor.EXAMPLE_ITEM_PROCESSOR
import cherhy.batch.settlement.util.property.BatchProperties.ItemReader.EXAMPLE_ITEM_READER
import cherhy.batch.settlement.util.property.BatchProperties.ItemWriter.EXAMPLE_ITEM_WRITER
import cherhy.batch.settlement.util.property.BatchProperties.Job.EXAMPLE_JOB
import cherhy.batch.settlement.util.property.BatchProperties.Step.EXAMPLE_FIRST_STEP
import cherhy.batch.settlement.util.property.BatchProperties.Step.EXAMPLE_LAST_STEP
import cherhy.batch.settlement.util.property.ConfigurationConstants.Bean.MASTER_DATA_SOURCE
import cherhy.batch.settlement.util.property.ConfigurationConstants.Bean.MASTER_TRANSACTION_MANAGER
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

@Configuration
class JobConfiguration(
    private val jobRepository: JobRepository,
    @Qualifier(MASTER_DATA_SOURCE) private val masterDataSource: DataSource,
    private val exampleJobCompletionNotificationListener: ExampleJobCompletionNotificationListener,
    @Qualifier(MASTER_TRANSACTION_MANAGER) private val transactionManager: PlatformTransactionManager,
) {
    @Bean(EXAMPLE_FIRST_STEP)
    fun firstStep() =
        StepBuilder(
            EXAMPLE_FIRST_STEP,
            jobRepository,
        ).chunk<Example, Example>(CHUNK_SIZE, transactionManager)
            .reader(exampleItemReader())
            .processor(exampleItemProcessor())
            .writer(exampleItemWriter())
            .build()

    @Bean(EXAMPLE_LAST_STEP)
    fun lastStep() =
        StepBuilder(
            EXAMPLE_LAST_STEP,
            jobRepository,
        ).tasklet({ _: StepContribution?, _: ChunkContext? ->
            RepeatStatus.FINISHED
        }, transactionManager)
            .build()

    @Bean(EXAMPLE_JOB)
    fun job() =
        JobBuilder(EXAMPLE_JOB, jobRepository)
            .start(firstStep())
            .next(lastStep())
            .listener(exampleJobCompletionNotificationListener)
            .build()

    @Bean(EXAMPLE_ITEM_READER)
    fun exampleItemReader() =
        JdbcCursorItemReaderBuilder<Example>()
            .name(EXAMPLE_ITEM_READER)
            .dataSource(masterDataSource)
            .fetchSize(CHUNK_SIZE)
            .sql("SELECT id, name, age FROM example")
            .rowMapper(BeanPropertyRowMapper(Example::class.java))
            .build()

    @Bean(EXAMPLE_ITEM_PROCESSOR)
    fun exampleItemProcessor() =
        ExampleItemProcessor()

    @Bean(EXAMPLE_ITEM_WRITER)
    fun exampleItemWriter(
    ) =
        JdbcBatchItemWriterBuilder<Example>()
            .dataSource(masterDataSource)
            .itemSqlParameterSourceProvider(BeanPropertyItemSqlParameterSourceProvider())
            .sql("INSERT INTO example (id, name, age) VALUES (:id, :name, :age)")
            .beanMapped()
            .build()
}
