package org.codinjutsu.tools.nosql.commons.view.action.paging

import com.intellij.icons.AllIcons
import org.codinjutsu.tools.nosql.commons.view.panel.Pageable
import org.codinjutsu.tools.nosql.commons.view.panel.query.Page
import org.codinjutsu.tools.nosql.i18n.getResourceString

internal class LastPageAction(pageable: Pageable) : AbstractPageableAction(pageable, getResourceString("pagebuttons.lastpage.label"), icon = AllIcons.Actions.AllRight) {

    override fun followingPage(page: Page) = page.copy(pageIndex = page.itemCount / page.pageSize)

    override fun canSwitchToPage(page: Page) = page.pageIndex * page.pageSize < page.itemCount
}