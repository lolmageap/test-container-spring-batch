package kotlin.batch.settlement

import org.springframework.batch.item.ItemProcessor

class ExampleItemProcessor : ItemProcessor<Example, Example> {
    override fun process(
        item: Example,
    ): Example {
        val plusOneAge = item.age + ONE_YEAR
        return item.copy(age = plusOneAge)
    }

    companion object {
        const val ONE_YEAR = 1
    }
}