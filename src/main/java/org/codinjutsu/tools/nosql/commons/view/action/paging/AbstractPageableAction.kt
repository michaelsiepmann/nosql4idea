package org.codinjutsu.tools.nosql.commons.view.action.paging

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import org.codinjutsu.tools.nosql.commons.view.panel.Pageable
import org.codinjutsu.tools.nosql.commons.view.panel.query.Page
import javax.swing.Icon

internal abstract class AbstractPageableAction(private val pageable: Pageable, text: String? = null, description: String? = null, icon: Icon? = null) : AnAction(text, description, icon) {

    override fun actionPerformed(e: AnActionEvent) {
        pageable.moveToPage(followingPage(pageable.getCurrentPage()))
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = canSwitchToPage(currentPage())
    }

    abstract fun followingPage(page: Page): Page

    abstract fun canSwitchToPage(page: Page): Boolean

    private fun currentPage(): Page = pageable.getCurrentPage()
}