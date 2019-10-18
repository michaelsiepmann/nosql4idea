package org.codinjutsu.tools.nosql.commons.view.action.paging

import com.intellij.icons.AllIcons
import org.codinjutsu.tools.nosql.commons.view.panel.Pageable
import org.codinjutsu.tools.nosql.commons.view.panel.query.Page
import org.codinjutsu.tools.nosql.i18n.getResourceString

internal class NextPageAction(pageable: Pageable) : AbstractPageableAction(pageable, getResourceString("pagebuttons.nextpage.label"), icon = AllIcons.General.ArrowRight) {

    override fun followingPage(page: Page) = page.copy(pageIndex = page.pageIndex + 1)

    override fun canSwitchToPage(page: Page) = page.pageIndex * page.pageSize < page.itemCount
}