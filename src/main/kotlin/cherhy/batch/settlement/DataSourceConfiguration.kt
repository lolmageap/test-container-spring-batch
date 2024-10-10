package cherhy.batch.settlement

import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
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
    fun transactionManager(
        dataSource: DataSource,
    ) =
        JdbcTransactionManager(dataSource)

    @Bean
    fun jdbcTemplate(
        dataSource: DataSource,
    ) =
        JdbcTemplate(dataSource)
}