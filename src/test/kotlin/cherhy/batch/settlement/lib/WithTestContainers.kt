package cherhy.batch.settlement.lib

import cherhy.batch.settlement.lib.DataSource.Property.MASTER_DATABASE_SOURCE_PASSWORD
import cherhy.batch.settlement.lib.DataSource.Property.MASTER_DATABASE_SOURCE_URL
import cherhy.batch.settlement.lib.DataSource.Property.MASTER_DATABASE_SOURCE_USERNAME
import cherhy.batch.settlement.lib.DataSource.Postgres
import cherhy.batch.settlement.lib.DataSource.Property.SLAVE_DATABASE_SOURCE_PASSWORD
import cherhy.batch.settlement.lib.DataSource.Property.SLAVE_DATABASE_SOURCE_URL
import cherhy.batch.settlement.lib.DataSource.Property.SLAVE_DATABASE_SOURCE_USERNAME
import com.github.dockerjava.api.model.ExposedPort
import com.github.dockerjava.api.model.PortBinding
import com.github.dockerjava.api.model.Ports
import org.flywaydb.core.Flyway
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer

internal interface WithTestContainers {
    companion object {
        @JvmStatic
        @DynamicPropertySource
        fun initTestContainers(
            registry: DynamicPropertyRegistry,
        ) {
            activeTestContainers.parallelStream().forEach { it.start() }
            injectProperties(registry)
            migrate()
        }

        private fun injectProperties(
            registry: DynamicPropertyRegistry,
        ) {
            registry.add(MASTER_DATABASE_SOURCE_URL) { postgresMaster.jdbcUrl }
            registry.add(MASTER_DATABASE_SOURCE_USERNAME) { postgresMaster.username }
            registry.add(MASTER_DATABASE_SOURCE_PASSWORD) { postgresMaster.password }
            registry.add(SLAVE_DATABASE_SOURCE_URL) { postgresSlave.jdbcUrl }
            registry.add(SLAVE_DATABASE_SOURCE_USERNAME) { postgresSlave.username }
            registry.add(SLAVE_DATABASE_SOURCE_PASSWORD) { postgresSlave.password }
        }

        private val postgresMaster by lazy {
            PostgreSQLContainer<Nothing>(Postgres.Master.IMAGE).apply {
                withCreateContainerCmdModifier {
                    it.withName(Postgres.Master.NAME)
                        .hostConfig
                        ?.portBindings
                        ?.add(
                            PortBinding(
                                Ports.Binding.bindPort(Postgres.Master.BIND_PORT),
                                ExposedPort(Postgres.Master.PORT),
                            )
                        )
                }
                withExposedPorts(Postgres.Master.PORT)
                withDatabaseName(Postgres.Master.DATABASE_NAME)
                withUsername(Postgres.Master.USERNAME)
                withPassword(Postgres.Master.PASSWORD)
            //  TODO: withCommand() <- 여기에 slaveDB 동기화 로직을 넣어야함
            }
        }

        private val postgresSlave by lazy {
            PostgreSQLContainer<Nothing>(Postgres.Slave.IMAGE).apply {
                withCreateContainerCmdModifier {
                    it.withName(Postgres.Slave.NAME)
                        .hostConfig
                        ?.portBindings
                        ?.add(
                            PortBinding(
                                Ports.Binding.bindPort(Postgres.Slave.BIND_PORT),
                                ExposedPort(Postgres.Slave.PORT),
                            )
                        )
                }
                withExposedPorts(Postgres.Slave.PORT)
                withDatabaseName(Postgres.Slave.DATABASE_NAME)
                withUsername(Postgres.Slave.USERNAME)
                withPassword(Postgres.Slave.PASSWORD)
            }
        }

        private fun migrate() {
            Flyway.configure()
                .dataSource(
                    postgresMaster.jdbcUrl,
                    postgresMaster.username,
                    postgresMaster.password,
                )
                .load()
                .migrate()
        }

        private val activeTestContainers = listOf(postgresMaster)
    }
}