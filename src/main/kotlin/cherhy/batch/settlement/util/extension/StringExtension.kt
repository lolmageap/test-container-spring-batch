package cherhy.batch.settlement.util.extension

import java.util.*

fun String.toSnakeCase() = this.replace(Regex("([a-z])([A-Z]+)"), "$1_$2").lowercase(Locale.getDefault())

fun String.toCamelCase() =
    this.split("_").joinToString { word ->
        word.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }
    }