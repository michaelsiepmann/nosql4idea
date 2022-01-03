package org.codinjutsu.tools.nosql.commons.view.action.paging

import com.intellij.icons.AllIcons
import org.codinjutsu.tools.nosql.commons.view.panel.Pageable
import org.codinjutsu.tools.nosql.commons.view.panel.query.Page
import org.codinjutsu.tools.nosql.i18n.getResourceString

internal class FirstPageAction(pageable: Pageable) : AbstractPageableAction(pageable, getResourceString("pagebuttons.firstpage.label"), icon = AllIcons.Actions.Play_first) {

    override fun followingPage(page: Page) = page.copy(pageIndex = 0)

    override fun canSwitchToPage(page: Page) = page.pageIndex > 0
}