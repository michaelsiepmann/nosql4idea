package org.codinjutsu.tools.nosql.commons.view.action.paging

import org.codinjutsu.tools.nosql.commons.view.panel.Pageable
import org.codinjutsu.tools.nosql.commons.view.panel.query.Page

internal class LastPageAction(pageable: Pageable) : AbstractPageableAction(pageable) {

    override fun followingPage(page: Page) = page.copy(pageIndex = page.itemCount / page.pageSize)

    override fun canSwitchToPage(page: Page) = page.pageIndex * page.pageSize < page.itemCount
}