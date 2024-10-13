package cherhy.batch.settlement.file.process

import mu.KotlinLogging
import org.springframework.batch.core.annotation.AfterJob
import org.springframework.batch.core.annotation.BeforeJob
import org.springframework.stereotype.Component

@Component
class ExampleFileJobCompletionNotificationListener {
    private val logger = KotlinLogging.logger {}

    @BeforeJob
    fun beforeJob() {
        logger.info { "Before File Job::start" }
    }

    @AfterJob
    fun afterJob() {
        logger.info { "After File Job::start" }
    }
}