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

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.ToggleAction
import org.codinjutsu.tools.nosql.commons.utils.GuiUtils
import org.codinjutsu.tools.nosql.mongo.view.panel.query.MongoQueryPanel

internal class EnableAggregateAction(private val mongoQueryPanel: MongoQueryPanel) : ToggleAction(ENABLE_AGGREGATION_MODE, QUERY_FIND_SAMPLE, AGGREGATION_ICON) {

    private var enableAggregation = false

    override fun isSelected(anActionEvent: AnActionEvent) = enableAggregation

    override fun setSelected(event: AnActionEvent, enableAggregation: Boolean) {
        this.enableAggregation = enableAggregation
        if (enableAggregation) {
            mongoQueryPanel.toggleToAggregation()
        } else {
            mongoQueryPanel.toggleToFind()
        }
    }

    override fun update(event: AnActionEvent) {
        event.presentation.apply {
            text = if (isSelected(event)) ENABLE_FIND_MODE else ENABLE_AGGREGATION_MODE
            description = if (isSelected(event)) QUERY_AGGREGATION_SAMPLE else QUERY_FIND_SAMPLE
            isVisible = mongoQueryPanel.isVisible
        }
    }

    companion object {
        private val ENABLE_FIND_MODE = "Toggle to Find Mode"
        private val ENABLE_AGGREGATION_MODE = "Toggle to Aggregation Mode"
        private val AGGREGATION_ICON = GuiUtils.loadIcon("sqlGroupByType.png")
        private val QUERY_FIND_SAMPLE = "ex: {'name': 'foo'}"
        private val QUERY_AGGREGATION_SAMPLE = "ex: [{'\$match': {'name': 'foo'}, {'\$project': {'address': 1}}]"
    }
}
