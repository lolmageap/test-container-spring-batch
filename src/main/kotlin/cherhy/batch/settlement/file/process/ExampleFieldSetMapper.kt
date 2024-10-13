package cherhy.batch.settlement.file.process

import cherhy.batch.settlement.model.Example
import org.springframework.batch.item.file.mapping.FieldSetMapper
import org.springframework.batch.item.file.transform.FieldSet

class ExampleFieldSetMapper : FieldSetMapper<Example> {
    override fun mapFieldSet(
        fieldSet: FieldSet,
    ) =
        with(fieldSet) {
            Example(
                readLong(0),
                readString(1),
                readBigDecimal(2),
            )
        }
}