package org.codinjutsu.tools.nosql.commons.view.action.paging

import org.codinjutsu.tools.nosql.commons.view.panel.Pageable
import org.codinjutsu.tools.nosql.commons.view.panel.query.Page
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.mockito.Mockito.mock

internal class NextPageActionTest {

    private val pageable = mock(Pageable::class.java)

    @ParameterizedTest(name = "Suppose, we are on page {0}, the page size is {1} and we have {2} total items, then the result should be {3}")
    @CsvSource(
            "0,10,100,true",
            "9,10,100,true",
            "9,10,99,true",
            "10,10,100,false"
    )
    @DisplayName("Check, if the decision to enable the nextpage-action is right")
    internal fun switchToPage(pageNumber: Int, pageSize: Int, total: Int, expected: Boolean) {
        val page = Page(pageSize, pageNumber, total)
        assertEquals(expected, NextPageAction(pageable).canSwitchToPage(page))
    }
}