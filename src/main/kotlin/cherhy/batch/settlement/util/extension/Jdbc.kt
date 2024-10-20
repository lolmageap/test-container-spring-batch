package cherhy.batch.settlement.util.extension

import org.springframework.jdbc.core.JdbcTemplate
import kotlin.reflect.KClass

inline fun <T : Any> JdbcTemplate.query(
    receiver: KClass<T>,
    block: () -> String,
): T? {
    return this.queryForObject(
        block(),
        receiver.java,
    )
}