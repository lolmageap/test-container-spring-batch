package cherhy.batch.settlement.util.extension

import org.springframework.jdbc.core.JdbcTemplate
import java.math.BigDecimal
import java.sql.ResultSet
import java.util.*
import kotlin.reflect.KClass

inline fun <T : Any> JdbcTemplate.findOne(
    receiver: KClass<T>,
    block: () -> String,
) =
    this.queryForObject(
        block.invoke(),
    ) { resultSet, _ ->
        receiver.constructors.first().call(
            *resultSet.extractConvertMethod(receiver),
        )
    }

inline fun <T : Any> JdbcTemplate.findAll(
    receiver: KClass<T>,
    block: () -> String,
): List<T> =
    this.query(block()) { resultSet, _ ->
        receiver.constructors.first().call(
            *resultSet.extractConvertMethod(receiver),
        )
    }

fun <T: Any> ResultSet.extractConvertMethod(
    receiver: KClass<T>,
): Array<Any> {
    val constructor = receiver.constructors.first()
    return constructor.parameters.map {
        when(it.type) {
            Boolean::class -> this.getBoolean(it.name)
            Byte::class -> this.getByte(it.name)
            Int::class -> this.getInt(it.name)
            Long::class -> this.getLong(it.name)
            Double::class -> this.getDouble(it.name)
            Float::class -> this.getFloat(it.name)
            Short::class -> this.getShort(it.name)
            Date::class -> this.getDate(it.name)
            String::class -> this.getString(it.name)
            BigDecimal::class -> this.getBigDecimal(it.name)
            else -> this.getObject(it.name)
        }
    }.toTypedArray()
}