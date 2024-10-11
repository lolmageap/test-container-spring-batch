package cherhy.batch.settlement.util.property

object ConfigurationConstants {
    object Bean {
        const val ROUTING_DATA_SOURCE = "routingDataSource"
        const val MASTER_DATA_SOURCE = "masterDataSource"
        const val SLAVE_DATA_SOURCE = "slaveDataSource"
        const val JDBC_TEMPLATE = "jdbcTemplate"
        const val MASTER_JDBC_TEMPLATE = "masterJdbcTemplate"
        const val TRANSACTION_MANAGER = "transactionManager"
        const val MASTER_TRANSACTION_MANAGER = "masterTransactionManager"
    }

    object DatabaseProfile {
        const val MASTER = "master"
        const val SLAVE = "slave"
    }
}