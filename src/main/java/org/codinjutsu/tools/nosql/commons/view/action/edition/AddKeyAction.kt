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

package org.codinjutsu.tools.nosql.commons.view.action.edition

import com.intellij.icons.AllIcons.General.Add
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.commons.model.DataType
import org.codinjutsu.tools.nosql.commons.view.EditionPanel
import org.codinjutsu.tools.nosql.commons.view.add.AddKeyDialog
import java.awt.event.KeyEvent
import java.awt.event.KeyEvent.ALT_DOWN_MASK
import java.awt.event.KeyEvent.VK_INSERT

class AddKeyAction(private val editionPanel: EditionPanel, private val project: Project, private val dataTypes : Array<DataType>) : AnAction("Add a key", "Add a key", Add) {

    init {
        registerCustomShortcutSet(VK_INSERT, ALT_DOWN_MASK, editionPanel)
    }

    override fun actionPerformed(anActionEvent: AnActionEvent) {
        val dialog = AddKeyDialog.createDialog(editionPanel, editionPanel.selectionPath, dataTypes, project)
        dialog.show()

        if (dialog.isOK) {
            editionPanel.addKey(dialog.key, dialog.value)
        }
    }

    override fun update(event: AnActionEvent) {
        event.presentation.isVisible = editionPanel.canAddKey()
    }

    override fun getActionUpdateThread() = ActionUpdateThread.BGT
}
