package cherhy.batch.settlement.file

import cherhy.batch.settlement.model.Example
import org.springframework.batch.item.file.mapping.FieldSetMapper
import org.springframework.batch.item.file.transform.FieldSet

class ExampleFieldSetMapper : FieldSetMapper<Example> {
    override fun mapFieldSet(
        fieldSet: FieldSet
    ) =
        with(fieldSet) {
            Example(
                readLong(Example::id.name),
                readString(Example::name.name),
                readBigDecimal(Example::price.name),
            )
        }
}