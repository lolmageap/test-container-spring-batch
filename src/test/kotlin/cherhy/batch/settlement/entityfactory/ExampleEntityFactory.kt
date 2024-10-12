package cherhy.batch.settlement.entityfactory

import cherhy.batch.settlement.model.Example
import cherhy.batch.settlement.lib.randomize
import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters
import org.jeasy.random.randomizers.text.StringRandomizer
import java.math.BigDecimal
import kotlin.random.Random

internal object ExampleEntityFactory {
    fun generate(
        id: Long = 0,
        price: BigDecimal,
        name: String = "test",
    ) =
        Example(
            id = id,
            name = name,
            price = price,
        )

    fun generateRandom(): Example {
        val parameter = EasyRandomParameters()
            .excludeField {
                it.name == Example::id.name
            }
            .randomize(Example::name) {
                StringRandomizer(1, 10, Random.nextLong(1, 100)).randomValue
            }
            .randomize(Example::price) {
                (100..1_000_000).random().toBigDecimal()
            }

        return EasyRandom(parameter).nextObject(Example::class.java)
    }
}