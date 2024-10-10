package cherhy.batch.settlement

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.jdbc.support.JdbcTransactionManager
import org.springframework.transaction.support.TransactionSynchronizationManager
import javax.sql.DataSource

@Configuration
class DataSourceConfiguration(
    private val dataSourceProperty: DataSourceProperty,
) {
    @Bean(MASTER_DATA_SOURCE)
    fun masterDataSource() =
        DataSourceBuilder.create()
            .url(dataSourceProperty.master.url)
            .username(dataSourceProperty.master.username)
            .password(dataSourceProperty.master.password)
            .build()!!

    @Bean(SLAVE_DATA_SOURCE)
    fun slaveDataSource() =
        DataSourceBuilder.create()
            .url(dataSourceProperty.slave.url)
            .username(dataSourceProperty.slave.username)
            .password(dataSourceProperty.slave.password)
            .build()!!

    @Bean
    @DependsOn(MASTER_DATA_SOURCE, SLAVE_DATA_SOURCE)
    fun routingDataSource(
        @Qualifier(MASTER_DATA_SOURCE) masterDataSource: DataSource,
        @Qualifier(SLAVE_DATA_SOURCE) slaveDataSource: DataSource,
    ): DataSource {
        val dataSources = hashMapOf<Any, Any>().apply {
            this[MASTER] = masterDataSource
            this[SLAVE] = slaveDataSource
        }

        return RoutingDataSource().apply {
            setTargetDataSources(dataSources)
            setDefaultTargetDataSource(masterDataSource)
        }
    }

    @Bean
    @Primary
    @DependsOn(ROUTING_DATA_SOURCE)
    fun dataSource(
        routingDataSource: DataSource,
    ) =
        LazyConnectionDataSourceProxy(routingDataSource)

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

class RoutingDataSource: AbstractRoutingDataSource() {
    override fun determineCurrentLookupKey() =
        when {
            TransactionSynchronizationManager.isCurrentTransactionReadOnly() -> SLAVE
            else -> MASTER
        }
}

private const val ROUTING_DATA_SOURCE = "routingDataSource"
private const val MASTER_DATA_SOURCE = "masterDataSource"
private const val SLAVE_DATA_SOURCE = "slaveDataSource"
private const val MASTER = "master"
private const val SLAVE = "slave"