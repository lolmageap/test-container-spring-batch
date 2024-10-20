package cherhy.batch.settlement.model

import cherhy.batch.settlement.annotation.Table
import cherhy.batch.settlement.util.extension.toSnakeCase
import java.math.BigDecimal

@Table
data class Example(
    val id: Long,
    val name: String,
    val price: BigDecimal,
) {
    companion object {
        val memberPropertyNames
            get() = arrayOf(
                Example::id.toSnakeCase(),
                Example::name.toSnakeCase(),
                Example::price.toSnakeCase(),
            )

        fun empty() =
            Example(
                id = 0,
                name = "",
                price = BigDecimal.ZERO,
            )
    }
}