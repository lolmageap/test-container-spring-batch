package cherhy.batch.settlement

import cherhy.batch.settlement.ConfigurationConstants.Bean.JDBC_TEMPLATE
import cherhy.batch.settlement.ConfigurationConstants.Bean.MASTER_DATA_SOURCE
import cherhy.batch.settlement.ConfigurationConstants.Bean.MASTER_JDBC_TEMPLATE
import cherhy.batch.settlement.ConfigurationConstants.Bean.MASTER_TRANSACTION_MANAGER
import cherhy.batch.settlement.ConfigurationConstants.Bean.ROUTING_DATA_SOURCE
import cherhy.batch.settlement.ConfigurationConstants.Bean.SLAVE_DATA_SOURCE
import cherhy.batch.settlement.ConfigurationConstants.Bean.TRANSACTION_MANAGER
import cherhy.batch.settlement.ConfigurationConstants.DatabaseProfile.MASTER
import cherhy.batch.settlement.ConfigurationConstants.DatabaseProfile.SLAVE
import com.zaxxer.hikari.HikariDataSource
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
            .type(HikariDataSource::class.java)
            .build()!!

    @Bean(SLAVE_DATA_SOURCE)
    fun slaveDataSource() =
        DataSourceBuilder.create()
            .url(dataSourceProperty.slave.url)
            .username(dataSourceProperty.slave.username)
            .password(dataSourceProperty.slave.password)
            .type(HikariDataSource::class.java)
            .build()!!

    @Bean(ROUTING_DATA_SOURCE)
    @DependsOn(MASTER_DATA_SOURCE, SLAVE_DATA_SOURCE)
    fun routingDataSource(
        @Qualifier(MASTER_DATA_SOURCE) masterDataSource: DataSource,
        @Qualifier(SLAVE_DATA_SOURCE) slaveDataSource: DataSource,
    ): DataSource {
        val dataSources = hashMapOf<Any, Any>().apply {
            this[MASTER] = masterDataSource
            this[SLAVE] = slaveDataSource
        }

        return object : AbstractRoutingDataSource() {
            override fun determineCurrentLookupKey() =
                when {
                    TransactionSynchronizationManager.isCurrentTransactionReadOnly() -> SLAVE
                    else -> MASTER
                }
        }.apply {
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

    @Bean(TRANSACTION_MANAGER)
    fun transactionManager(
        dataSource: DataSource,
    ) =
        JdbcTransactionManager(dataSource)

    @Bean(MASTER_TRANSACTION_MANAGER)
    fun masterTransactionManager(
        @Qualifier(MASTER_DATA_SOURCE) masterDataSource: DataSource,
    ) =
        JdbcTransactionManager(masterDataSource)

    @Bean(JDBC_TEMPLATE)
    fun jdbcTemplate(
        dataSource: DataSource,
    ) =
        JdbcTemplate(dataSource)

    @Bean(MASTER_JDBC_TEMPLATE)
    fun masterJdbcTemplate(
        @Qualifier(MASTER_DATA_SOURCE) masterDataSource: DataSource,
    ) =
        JdbcTemplate(masterDataSource)
}
