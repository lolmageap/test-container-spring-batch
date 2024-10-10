package cherhy.batch.settlement.lib

import cherhy.batch.settlement.lib.DataSource.Command.HOT_STANDBY
import cherhy.batch.settlement.lib.DataSource.Command.MAX_REPLICATION_SLOTS
import cherhy.batch.settlement.lib.DataSource.Command.MAX_WAL_SENDERS
import cherhy.batch.settlement.lib.DataSource.Command.ADD_OPTION
import cherhy.batch.settlement.lib.DataSource.Command.POSTGRES
import cherhy.batch.settlement.lib.DataSource.Command.WAL_LEVEL
import cherhy.batch.settlement.lib.DataSource.Postgres
import cherhy.batch.settlement.lib.DataSource.Key.MASTER_DATABASE_SOURCE_PASSWORD
import cherhy.batch.settlement.lib.DataSource.Key.MASTER_DATABASE_SOURCE_URL
import cherhy.batch.settlement.lib.DataSource.Key.MASTER_DATABASE_SOURCE_USERNAME
import cherhy.batch.settlement.lib.DataSource.Key.SLAVE_DATABASE_SOURCE_PASSWORD
import cherhy.batch.settlement.lib.DataSource.Key.SLAVE_DATABASE_SOURCE_URL
import cherhy.batch.settlement.lib.DataSource.Key.SLAVE_DATABASE_SOURCE_USERNAME
import com.github.dockerjava.api.model.ExposedPort
import com.github.dockerjava.api.model.PortBinding
import com.github.dockerjava.api.model.Ports.Binding.bindPort
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
            registry.add(MASTER_DATABASE_SOURCE_URL) { masterPostgres.jdbcUrl }
            registry.add(MASTER_DATABASE_SOURCE_USERNAME) { masterPostgres.username }
            registry.add(MASTER_DATABASE_SOURCE_PASSWORD) { masterPostgres.password }

            registry.add(SLAVE_DATABASE_SOURCE_URL) { salvePostgres.jdbcUrl }
            registry.add(SLAVE_DATABASE_SOURCE_USERNAME) { salvePostgres.username }
            registry.add(SLAVE_DATABASE_SOURCE_PASSWORD) { salvePostgres.password }
        }

        private val masterPostgres by lazy {
            PostgreSQLContainer<Nothing>(Postgres.Property.IMAGE).apply {
                withCreateContainerCmdModifier {
                    it.withName(Postgres.Master.NAME)
                        .hostConfig
                        ?.portBindings
                        ?.add(
                            PortBinding(
                                bindPort(Postgres.Master.BIND_PORT),
                                ExposedPort(Postgres.Property.PORT),
                            )
                        )
                }
                withExposedPorts(Postgres.Property.PORT)
                withDatabaseName(Postgres.Master.DATABASE_NAME)
                withUsername(Postgres.Master.USERNAME)
                withPassword(Postgres.Master.PASSWORD)
                withCommand(
                    POSTGRES,
                    ADD_OPTION, WAL_LEVEL,
                    ADD_OPTION, MAX_WAL_SENDERS,
                    ADD_OPTION, MAX_REPLICATION_SLOTS,
                    ADD_OPTION, HOT_STANDBY,
                )
            }
        }

        private val salvePostgres by lazy {
            PostgreSQLContainer<Nothing>(Postgres.Property.IMAGE).apply {
                withCreateContainerCmdModifier {
                    it.withName(Postgres.Slave.NAME)
                        .hostConfig
                        ?.portBindings
                        ?.add(
                            PortBinding(
                                bindPort(Postgres.Slave.BIND_PORT),
                                ExposedPort(Postgres.Property.PORT),
                            )
                        )
                }
                withExposedPorts(Postgres.Property.PORT)
                withDatabaseName(Postgres.Slave.DATABASE_NAME)
                withUsername(Postgres.Slave.USERNAME)
                withPassword(Postgres.Slave.PASSWORD)
            }
        }

        private fun migrate() {
            Flyway.configure()
                .dataSource(
                    masterPostgres.jdbcUrl,
                    masterPostgres.username,
                    masterPostgres.password,
                )
                .load()
                .migrate()
        }

        private val activeTestContainers = listOf(masterPostgres, salvePostgres)
    }
}