package cherhy.batch.settlement.lib

object DataSource {
    object Postgres {
        object Master {
            const val IMAGE = "postgres:15.0"
            private const val HOST = "localhost"
            const val NAME = "postgres-test-container"
            const val PORT = 5432
            const val BIND_PORT = 15432
            const val DATABASE_NAME = "cherhy"
            const val USERNAME = "postgres"
            const val PASSWORD = "1234"
            const val URL = "jdbc:postgresql://$HOST:$BIND_PORT/$DATABASE_NAME"
        }
        object Slave {
            const val IMAGE = "postgres:15.0"
            private const val HOST = "localhost"
            const val NAME = "postgres-test-container"
            const val PORT = 5432
            const val BIND_PORT = 15432
            const val DATABASE_NAME = "cherhy"
            const val USERNAME = "postgres"
            const val PASSWORD = "1234"
            const val URL = "jdbc:postgresql://$HOST:$BIND_PORT/$DATABASE_NAME"
        }
    }

    object Property {
        const val MASTER_DATABASE_SOURCE_URL = "spring.datasource.url"
        const val MASTER_DATABASE_SOURCE_USERNAME = "spring.datasource.username"
        const val MASTER_DATABASE_SOURCE_PASSWORD = "spring.datasource.password"

        const val SLAVE_DATABASE_SOURCE_URL = "spring.datasource.url"
        const val SLAVE_DATABASE_SOURCE_USERNAME = "spring.datasource.username"
        const val SLAVE_DATABASE_SOURCE_PASSWORD = "spring.datasource.password"
    }
}