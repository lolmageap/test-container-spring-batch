package cherhy.batch.settlement.util.extension

import java.util.*
import kotlin.reflect.KProperty

fun String.toSnakeCase(): String {
    val value =
        if (this.contains("-")) return this.replace("-", "_", true)
        else this
    return value.replace(Regex("([a-z])([A-Z]+)"), "$1_$2").lowercase(Locale.getDefault())
}

fun String.toKebabCase(): String {
    val value =
        if (this.contains("_")) return this.replace("_", "-", true)
        else this
    return value.replace(Regex("([a-z])([A-Z]+)"), "$1-$2").lowercase(Locale.getDefault())
}

fun String.toCamelCase(): String {
    val value = this.replace(Regex("([a-z])([A-Z]+)"), "$1_$2").lowercase(Locale.getDefault())
    val characters = value.split("_", "-")
    val firstWord = characters.first().lowercase(Locale.getDefault())
    val camelCase =
        characters.drop(1)
            .joinToString("") { word ->
                word.replaceFirstChar { it.uppercaseChar() }
            }
    return firstWord + camelCase
}

fun String.toPascalCase(): String {
    val value = this.replace(Regex("([a-z])([A-Z]+)"), "$1_$2").lowercase(Locale.getDefault())
    val characters = value.split("_", "-")
    val firstWord = characters.first().replaceFirstChar { it.uppercaseChar() }
    val pascalCase =
        characters.drop(1)
            .joinToString("") { word ->
                word.replaceFirstChar { it.titlecase(Locale.getDefault()) }
            }
    return firstWord + pascalCase
}

fun KProperty<*>.toSnakeCase() = this.name.toSnakeCase()
fun KProperty<*>.toCamelCase() = this.name.toCamelCase()
fun KProperty<*>.toPascalCase() = this.name.toPascalCase()
fun KProperty<*>.toKebabCase() = this.name.toKebabCase()