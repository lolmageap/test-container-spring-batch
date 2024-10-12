package cherhy.batch.settlement.model

import cherhy.batch.settlement.annotation.Table
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
                Example::id.name,
                Example::name.name,
                Example::price.name,
            )
    }
}