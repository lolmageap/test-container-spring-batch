package cherhy.batch.settlement.file

import cherhy.batch.settlement.model.Example
import cherhy.batch.settlement.util.extension.toCamelCase
import org.springframework.batch.item.file.mapping.FieldSetMapper
import org.springframework.batch.item.file.transform.FieldSet

// TODO: 여기 테스트 해봐야함.
class ExampleFieldSetMapper : FieldSetMapper<Example> {
    override fun mapFieldSet(
        fieldSet: FieldSet
    ) =
        with(fieldSet) {
            Example(
                readLong(Example::id.name.toCamelCase()),
                readString(Example::name.name.toCamelCase()),
                readBigDecimal(Example::price.name.toCamelCase()),
            )
        }
}