package org.codinjutsu.tools.nosql.commons.view.panel.query

import com.intellij.openapi.actionSystem.DefaultActionGroup
import javax.swing.JComponent

internal interface QueryPanel {

    fun addActions(actionResultGroup: DefaultActionGroup) {
    }

    fun setVisible(visible: Boolean)

    fun getQueryOptions(rowLimit: String, page: Page?): QueryOptions

    fun validateQuery()

    fun requestFocusOnEditor()

    fun getComponent(): JComponent
}