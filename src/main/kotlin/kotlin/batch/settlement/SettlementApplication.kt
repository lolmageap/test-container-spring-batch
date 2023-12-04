package kotlin.batch.settlement

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableBatchProcessing
class SettlementApplication

fun main(args: Array<String>) {
    runApplication<SettlementApplication>(*args)
}
