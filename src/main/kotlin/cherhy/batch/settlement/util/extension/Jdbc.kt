package cherhy.batch.settlement.util.extension

import org.springframework.jdbc.core.JdbcTemplate
import kotlin.reflect.KClass

inline fun <T : Any> JdbcTemplate.findOne(
    receiver: KClass<T>,
    block: () -> String,
) =
    this.queryForObject(
        block(),
        receiver.java,
    )

inline fun <T : Any> JdbcTemplate.findAll(
    receiver: KClass<T>,
    block: () -> String,
): List<T> =
    this.queryForList(
        block(),
        receiver.java,
    )