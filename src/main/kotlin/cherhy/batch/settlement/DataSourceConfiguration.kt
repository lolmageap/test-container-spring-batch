package cherhy.batch.settlement

import com.github.dockerjava.api.model.ExposedPort
import com.github.dockerjava.api.model.PortBinding
import com.github.dockerjava.api.model.Ports
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.*
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.support.JdbcTransactionManager
import org.testcontainers.containers.PostgreSQLContainer
import javax.sql.DataSource

@Configuration
class DataSourceConfiguration(
    private val dataSourceProperty: DataSourceProperty,
) {
    @Bean
    fun dataSource(): DataSource =
        DataSourceBuilder.create()
            .driverClassName(dataSourceProperty.driverClassName)
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

@Profile("test")
@Configuration
class TestDataSourceConfiguration {
    val postgreSQLContainer =
        PostgreSQLContainer<Nothing>("postgres:15.0").apply {
            withCreateContainerCmdModifier {
                it.withName("postgres-test-container")
                    .hostConfig
                    ?.portBindings
                    ?.add(
                        PortBinding(
                            Ports.Binding.bindPort(15432),
                            ExposedPort(5432)
                        )
                    )
            }
            withExposedPorts(5432)
            withDatabaseName("cherhy")
            withUsername("postgres")
            withPassword("1234")
            start()
        }

    @Bean
    @Primary
    fun testDataSource(): DataSource =
        DataSourceBuilder.create()
            .driverClassName(postgreSQLContainer.driverClassName)
            .url(postgreSQLContainer.jdbcUrl)
            .username(postgreSQLContainer.username)
            .password(postgreSQLContainer.password)
            .build()
}