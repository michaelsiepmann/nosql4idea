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

package org.codinjutsu.tools.nosql.redis.view.action

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.ui.Messages
import com.intellij.util.ArrayUtil
import org.codinjutsu.tools.nosql.redis.view.RedisPanel
import java.util.*

class SetSeparatorAction(private val redisPanel: RedisPanel) : AnAction(AllIcons.General.Ellipsis) {

    init {
        myPredefinedSeparators.add(".")
        myPredefinedSeparators.add(":")
    }

    override fun actionPerformed(event: AnActionEvent) {
        val strings = ArrayUtil.toStringArray(myPredefinedSeparators)
        val current = redisPanel.groupSeparator
        val separator = Messages.showEditableChooseDialog("Redis Keys Separator",
                "Select Separator",
                Messages.getQuestionIcon(),
                strings, current, null) ?: return

        if (redisPanel.groupSeparator == separator) {
            return
        }

        redisPanel.groupSeparator = separator

        myPredefinedSeparators.add(separator)
        update(event)
    }

    override fun update(event: AnActionEvent?) {
        val currentSeparator = redisPanel.groupSeparator
        val textToDisplay = String.format("Group by '%s'", currentSeparator ?: "Nothing")
        event!!.presentation.text = textToDisplay
        event.presentation.description = textToDisplay
        event.presentation.isEnabled = redisPanel.isGroupDataEnabled
    }

    companion object {
        private val myPredefinedSeparators = LinkedHashSet<String>()
    }
}
