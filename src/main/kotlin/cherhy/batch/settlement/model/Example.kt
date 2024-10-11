package cherhy.batch.settlement.model

import cherhy.batch.settlement.annotation.Table

@Table
data class Example(
    val id: Long,
    val name: String,
    val age: Int,
)