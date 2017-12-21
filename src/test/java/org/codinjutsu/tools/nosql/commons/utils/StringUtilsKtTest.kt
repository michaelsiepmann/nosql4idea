package org.codinjutsu.tools.nosql.commons.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class StringUtilsKtTest {

    @Test
    internal fun abbreviateInCenter() {
        val value = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz"
        assertEquals("abcdefghijklmnopq...ghijklmnopqrstuvwxyz", value.abbreviateInCenter(40))
    }

    @Test
    fun parseNumber() {
        assertEquals(1, "1".parseNumber())
        assertEquals(1.000000000001, "1.000000000001".parseNumber())
        assertEquals(1000000000000000L, "1000000000000000".parseNumber())
    }
}