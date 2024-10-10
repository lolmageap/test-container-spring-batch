package cherhy.batch.settlement

import cherhy.batch.settlement.ConfigurationConstants.Bean.MASTER_JDBC_TEMPLATE
import mu.KotlinLogging
import org.springframework.batch.core.annotation.AfterJob
import org.springframework.batch.core.annotation.BeforeJob
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class ExampleJobCompletionNotificationListener(
    @Qualifier(MASTER_JDBC_TEMPLATE) private val jdbcTemplate: JdbcTemplate,
) {
    private val logger = KotlinLogging.logger {}

    @BeforeJob
    fun beforeJob() {
        jdbcTemplate.query("SELECT id, name, age FROM example") {
            logger.info {
                "beforeJob::start"
            }
            logger.info {
                "id: ${it.getLong("id")}, name: ${it.getString("name")}, age: ${it.getInt("age")}"
            }
            logger.info {
                "beforeJob::end"
            }
        }
    }

    @AfterJob
    fun afterJob() {
        jdbcTemplate.query("SELECT id, name, age FROM example") {
            logger.info {
                "afterJob::start"
            }
            logger.info {
                "id: ${it.getLong("id")}, name: ${it.getString("name")}, age: ${it.getInt("age")}"
            }
            logger.info {
                "afterJob::end"
            }
        }
    }
}