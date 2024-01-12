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
import org.codinjutsu.tools.nosql.commons.view.DatabasePanel
import java.awt.event.KeyEvent.CTRL_DOWN_MASK
import java.awt.event.KeyEvent.VK_F

class OpenFindAction(private val databasePanel: DatabasePanel) :
    AnAction("Find", "Open Find editor", AllIcons.Actions.Find), DumbAware {

    init {
        registerCustomShortcutSet(VK_F, CTRL_DOWN_MASK, databasePanel)
    }

    override fun actionPerformed(e: AnActionEvent) {
        if (!databasePanel.isFindEditorOpened) {
            databasePanel.openFindEditor()
        } else {
            databasePanel.focusOnEditor()
        }
    }
}
