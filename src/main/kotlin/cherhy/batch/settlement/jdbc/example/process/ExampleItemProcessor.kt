package cherhy.batch.settlement.jdbc.example.process

import cherhy.batch.settlement.model.Example
import org.springframework.batch.item.ItemProcessor

class ExampleItemProcessor : ItemProcessor<Example, Example> {
    override fun process(
        item: Example,
    ): Example {
        val discountedPrice = item.price - DISCOUNT_PRICE
        return item.copy(price = discountedPrice)
    }

    companion object {
        val DISCOUNT_PRICE = 1000.toBigDecimal()
        val DISCOUNT_RATE = 0.1.toBigDecimal()
    }
}