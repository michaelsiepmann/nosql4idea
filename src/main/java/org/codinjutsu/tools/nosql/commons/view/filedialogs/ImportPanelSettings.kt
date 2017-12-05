package org.codinjutsu.tools.nosql.commons.view.filedialogs

import javax.swing.JPanel

interface ImportPanelSettings {

    fun getPanel() : JPanel? = null

    fun getExtensions() : Array<String>? = null
}