package kotlin.batch.settlement

import mu.KotlinLogging
import org.springframework.batch.core.annotation.AfterJob
import org.springframework.batch.core.annotation.BeforeJob
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

@Component
class ExampleJobCompletionNotificationListener(
    private val jdbcTemplate: JdbcTemplate,
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