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

package org.codinjutsu.tools.nosql.commons.view.action

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAware
import org.codinjutsu.tools.nosql.NoSqlExplorerPanel

class ViewCollectionValuesAction(private val noSqlExplorerPanel: NoSqlExplorerPanel) : AnAction("View collection content", "View collection content", ICON), DumbAware {

    override fun actionPerformed(anActionEvent: AnActionEvent) {
        noSqlExplorerPanel.loadRecords()
    }

    override fun update(event: AnActionEvent) {
        event.presentation.isVisible = noSqlExplorerPanel.isDatabaseSelected
    }

    companion object {
        private val ICON = AllIcons.Nodes.DataSchema
    }
}
