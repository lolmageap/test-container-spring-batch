package cherhy.batch.settlement.entityfactory

import cherhy.batch.settlement.Example
import cherhy.batch.settlement.lib.randomize
import org.jeasy.random.EasyRandom
import org.jeasy.random.EasyRandomParameters
import org.jeasy.random.randomizers.text.StringRandomizer
import org.springframework.data.mapping.toDotPath
import kotlin.random.Random

internal object ExampleEntityFactory {
    fun generate(
        id: Long = 0,
        name: String = "test",
        age: Int = 20,
    ) =
        Example(
            id = id,
            name = name,
            age = age,
        )

    fun generateRandom(): Example {
        val parameter = EasyRandomParameters()
            .excludeField {
                it.name == Example::id.toDotPath()
            }
            .randomize(Example::name) {
                StringRandomizer(1, 10, Random.nextLong(1, 100)).randomValue
            }
            .randomize(Example::age) {
                (20..80).random()
            }

        return EasyRandom(parameter).nextObject(Example::class.java)
    }
}