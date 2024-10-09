package cherhy.batch.settlement.lib

import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters
import org.jeasy.random.api.Randomizer
import java.util.stream.LongStream
import kotlin.reflect.KProperty

internal inline fun <reified T> T.nextObject(
    param: EasyRandomParameters = EasyRandomParameters()
): T {
    if (!T::class.isData) throw IllegalArgumentException("Only data class is supported")
    return EasyRandom(param).nextObject(T::class.java)
}

internal fun <T, R> EasyRandomParameters.randomize(
    property: KProperty<T>,
    randomizer: Randomizer<R>,
): EasyRandomParameters =
    randomize(
        { it.name == property.name },
        randomizer,
    )

internal fun <T> Number.mapParallel(
    block: () -> T,
): List<T> =
    LongStream.range(0, this.toLong())
        .parallel()
        .mapToObj {
            block()
        }
        .toList()