/*
 * Copyright (c) 2015 David Boissier
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.codinjutsu.tools.nosql.commons.view.explorer

import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBTabbedPane
import org.codinjutsu.tools.nosql.DatabaseVendorClientManager
import java.awt.BorderLayout
import java.awt.BorderLayout.CENTER
import javax.swing.JPanel


internal class NoSqlExplorerPanel internal constructor(project: Project, databaseVendorClientManager: DatabaseVendorClientManager) : JPanel() {

    private val databaseListPanel: DatabaseListPanel
    private val historyListPanel: HistoryListPanel

    init {
        val tabPanel = JBTabbedPane()

        databaseListPanel = DatabaseListPanel(project, databaseVendorClientManager)
        tabPanel.addTab("Database", databaseListPanel)

        historyListPanel = HistoryListPanel(project)
        tabPanel.addTab("History", historyListPanel)

        layout = BorderLayout()
        add(tabPanel, CENTER)
    }

    fun initializeExplorerPanel() {
        databaseListPanel.initializeTree()
        historyListPanel.initializeTree()
    }

    fun installActions() {
        databaseListPanel.installActions()
        historyListPanel.installActions()
    }
}
