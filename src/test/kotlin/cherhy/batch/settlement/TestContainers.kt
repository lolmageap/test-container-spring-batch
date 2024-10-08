package cherhy.batch.settlement

import com.github.dockerjava.api.model.ExposedPort
import com.github.dockerjava.api.model.PortBinding
import com.github.dockerjava.api.model.Ports
import org.testcontainers.containers.PostgreSQLContainer
import cherhy.batch.settlement.TestContainers.DataSource.Postgres

internal object TestContainers {
    val postgresContainer by lazy {
        PostgreSQLContainer<Nothing>(Postgres.IMAGE).apply {
            withCreateContainerCmdModifier {
                it.withName(Postgres.NAME)
                    .hostConfig
                    ?.portBindings
                    ?.add(
                        PortBinding(
                            Ports.Binding.bindPort(Postgres.BIND_PORT),
                            ExposedPort(Postgres.PORT)
                        )
                    )
            }
            withExposedPorts(Postgres.PORT)
            withDatabaseName(Postgres.DATABASE_NAME)
            withUsername(Postgres.USERNAME)
            withPassword(Postgres.PASSWORD)
        }
    }

    object DataSource {
        object Postgres {
            const val IMAGE = "postgres:17.0"
            const val HOST = "localhost"
            const val NAME = "postgres-test-container"
            const val PORT = 5432
            const val BIND_PORT = 15432
            const val DATABASE_NAME = "cherhy"
            const val USERNAME = "postgres"
            const val PASSWORD = "1234"
        }
    }
}