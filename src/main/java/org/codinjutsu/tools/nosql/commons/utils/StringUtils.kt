package org.codinjutsu.tools.nosql.commons.utils

private const val ELLIPSIS = "..."

fun String.abbreviateInCenter(length: Int): String {
    val halfLength = length / 2
    val firstPartLastIndex = halfLength - ELLIPSIS.length
    return String.format("%s%s%s",
            substring(0, firstPartLastIndex),
            ELLIPSIS,
            substring(this.length - halfLength, this.length))
}

fun String.parseNumber(): Number {
    try {
        return toInt()
    } catch (ex: NumberFormatException) {
    }//UGLY :(

    try {
        return toLong()
    } catch (ex: NumberFormatException) {
    }//UGLY :(

    return toDouble()
}

fun explode(key: String?, separator: String): Array<String> {
    return key?.split(separator)?.dropLastWhile { it.isEmpty() }?.toTypedArray() ?: emptyArray()
}
