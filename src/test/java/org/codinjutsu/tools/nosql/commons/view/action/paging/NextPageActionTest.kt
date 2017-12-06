package org.codinjutsu.tools.nosql.commons.view.action.paging

import org.codinjutsu.tools.nosql.commons.view.panel.Pageable
import org.codinjutsu.tools.nosql.commons.view.panel.query.Page
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.Mockito.mock

internal class NextPageActionTest {

    private val pageable = mock(Pageable::class.java)

    @Test
    @DisplayName("The page-index should be incremented")
    internal fun followingPage() {
        val page = Page(10, 2, 100)
        val next = NextPageAction(pageable).followingPage(page)
        assertAll(
                Executable { assertEquals(10, next.pageSize) },
                Executable { assertEquals(3, next.pageIndex) },
                Executable { assertEquals(100, next.itemCount) }
        )
    }

    @ParameterizedTest(name = "Suppose, we are on page {0}, the page size is {1} and we have {2} total items, then the result should be {3}")
    @CsvSource(
            "0,10,0,false",
            "0,10,100,true",
            "9,10,100,true",
            "9,10,99,true",
            "10,10,100,false"
    )
    @DisplayName("Check, if the decision to enable the nextpage-action is right")
    internal fun canSwitchToPage(pageNumber: Int, pageSize: Int, total: Int, expected: Boolean) {
        val page = Page(pageSize, pageNumber, total)
        assertEquals(expected, NextPageAction(pageable).canSwitchToPage(page))
    }
}