package org.codinjutsu.tools.nosql.commons.view.action.paging

import com.intellij.icons.AllIcons
import org.codinjutsu.tools.nosql.commons.view.panel.Pageable
import org.codinjutsu.tools.nosql.commons.view.panel.query.Page
import org.codinjutsu.tools.nosql.i18n.getResourceString

internal class PreviousPageAction(pageable: Pageable) : AbstractPageableAction(pageable, getResourceString("pagebuttons.previouspage.label"), icon = AllIcons.General.ArrowLeft) {

    override fun followingPage(page: Page) = page.copy(pageIndex = page.pageIndex - 1)

    override fun canSwitchToPage(page: Page) = page.pageIndex > 0
}