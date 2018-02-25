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
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.openapi.project.DumbAware
import org.codinjutsu.tools.nosql.commons.view.panel.NoSQLResultPanel
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.awt.event.KeyEvent

internal class CopyResultAction<DOCUMENT>(private val resultPanel: NoSQLResultPanel<DOCUMENT>) : AnAction("Copy", "Copy results to clipboard", AllIcons.Actions.Copy), DumbAware {

    init {
        registerCustomShortcutSet(KeyEvent.VK_C, Toolkit.getDefaultToolkit().menuShortcutKeyMask, resultPanel)
    }

    override fun actionPerformed(anActionEvent: AnActionEvent) {
        CopyPasteManager.getInstance().setContents(StringSelection(resultPanel.getSelectedNodeStringifiedValue()))
    }
}
