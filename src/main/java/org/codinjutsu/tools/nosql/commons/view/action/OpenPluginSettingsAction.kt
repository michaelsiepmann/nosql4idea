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
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project

class OpenPluginSettingsAction : AnAction("NoSql Settings", "Edit the NoSql settings for the current project", AllIcons.General.Settings), DumbAware {

    override fun actionPerformed(event: AnActionEvent) {
        showSettingsFor(getProject(event))
    }

    private fun showSettingsFor(project: Project?) {
        ShowSettingsUtil.getInstance().showSettingsDialog(project, "NoSql Servers")
    }

    private fun getProject(event: AnActionEvent) = PlatformDataKeys.PROJECT.getData(event.dataContext)
}

