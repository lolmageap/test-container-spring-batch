package cherhy.batch.settlement.lib

import cherhy.batch.settlement.lib.DataSource.Postgres.Property.HOST

object DataSource {

    object Postgres {
        object Property {
            const val IMAGE = "postgres:15.0"
            const val HOST = "localhost"
            const val PORT = 5432
        }

        object Master {
            const val NAME = "postgres-master-test-container"
            const val BIND_PORT = 15432
            const val DATABASE_NAME = "cherhy"
            const val USERNAME = "postgres"
            const val PASSWORD = "1234"
            const val URL = "jdbc:postgresql://$HOST:$BIND_PORT/$DATABASE_NAME"
        }

        object Slave {
            const val NAME = "postgres-slave-test-container"
            const val BIND_PORT = 15433
            const val DATABASE_NAME = "cherhy"
            const val USERNAME = "postgres"
            const val PASSWORD = "1234"
            const val URL = "jdbc:postgresql://$HOST:$BIND_PORT/$DATABASE_NAME"
        }
    }

    object Command{
        const val POSTGRES = "postgres"
        const val ADD_OPTION = "-c"
        const val WAL_LEVEL = "wal_level=replica"
        const val MAX_WAL_SENDERS = "max_wal_senders=3"
        const val MAX_REPLICATION_SLOTS = "max_replication_slots=3"
        const val HOT_STANDBY = "hot_standby=on"
    }

    object Key {
        const val MASTER_DATABASE_SOURCE_URL = "spring.datasource.master.url"
        const val MASTER_DATABASE_SOURCE_USERNAME = "spring.datasource.master.username"
        const val MASTER_DATABASE_SOURCE_PASSWORD = "spring.datasource.master.password"

        const val SLAVE_DATABASE_SOURCE_URL = "spring.datasource.slave.url"
        const val SLAVE_DATABASE_SOURCE_USERNAME = "spring.datasource.slave.username"
        const val SLAVE_DATABASE_SOURCE_PASSWORD = "spring.datasource.slave.password"
    }
}