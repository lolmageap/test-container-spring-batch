package cherhy.batch.settlement

import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.support.JdbcTransactionManager
import javax.sql.DataSource

@Configuration
class DataSourceConfiguration(
    private val dataSourceProperty: DataSourceProperty,
) {
    @Bean
    fun dataSource(): DataSource =
        DataSourceBuilder.create()
            .url(dataSourceProperty.url)
            .username(dataSourceProperty.username)
            .password(dataSourceProperty.password)
            .build()

    @Bean
    fun jobRepository() =
        JobRepositoryFactoryBean()
            .apply {
                setDataSource(dataSource())
                transactionManager = transactionManager(dataSource())
            }
            .getObject()

    @Bean
    fun transactionManager(
        dataSource: DataSource,
    ) =
        JdbcTransactionManager(dataSource)
}