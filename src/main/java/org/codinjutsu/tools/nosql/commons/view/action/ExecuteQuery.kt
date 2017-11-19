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
import org.codinjutsu.tools.nosql.commons.view.NoSqlResultView

import java.awt.event.InputEvent
import java.awt.event.KeyEvent

class ExecuteQuery(private val resultView: NoSqlResultView) : AnAction("Execute query", "Execute query with options", AllIcons.Actions.Execute) {

    init {
        registerCustomShortcutSet(KeyEvent.VK_ENTER, InputEvent.CTRL_MASK, resultView)
    }

    override fun actionPerformed(anActionEvent: AnActionEvent) {
        resultView.executeQuery()
    }

    override fun update(event: AnActionEvent?) {
        event!!.presentation.isEnabled = resultView.records != null
    }
}
