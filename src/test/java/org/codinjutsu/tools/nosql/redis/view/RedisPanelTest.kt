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

package org.codinjutsu.tools.nosql.redis.view

import com.intellij.ide.CommonActionsManager
import com.intellij.mock.MockEditorFactory
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionToolbar
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.command.impl.DummyProject
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorFactory
import com.intellij.openapi.editor.EditorSettings
import com.intellij.openapi.editor.colors.EditorColorsScheme
import com.intellij.openapi.editor.colors.impl.DefaultColorsScheme
import com.intellij.openapi.editor.ex.EditorEx
import com.intellij.openapi.editor.impl.SettingsImpl
import com.intellij.openapi.project.Project
import com.intellij.testFramework.PlatformLiteFixture
import org.codinjutsu.tools.nosql.commons.model.Database
import org.codinjutsu.tools.nosql.commons.model.SearchResult
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptions
import org.codinjutsu.tools.nosql.redis.configuration.RedisServerConfiguration
import org.codinjutsu.tools.nosql.redis.logic.RedisClient
import org.codinjutsu.tools.nosql.redis.model.RedisContext
import org.fest.swing.edt.GuiActionRunner
import org.fest.swing.edt.GuiQuery
import org.fest.swing.fixture.Containers
import org.fest.swing.fixture.FrameFixture
import org.mockito.Matchers.any
import org.mockito.Matchers.anyBoolean
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import javax.swing.JPanel

//@Disabled("Doesn't work at the moment.")
class RedisPanelTest : PlatformLiteFixture() {

    private var redisPanelWrapper: RedisPanel? = null

    private var frameFixture: FrameFixture? = null

    private val dummyProject = DummyProject.getInstance()

    private val redisClientMock = mock(RedisClient::class.java)

    @Throws(Exception::class)
    public override fun tearDown() {
        super.tearDown()
        frameFixture!!.cleanUp()
    }

    @Throws(Exception::class)
    public override fun setUp() {
        super.setUp()
        initApplication()
        val application = PlatformLiteFixture.getApplication()

        val editor = mock(EditorEx::class.java)
        `when`<EditorSettings>(editor.settings).thenReturn(SettingsImpl())
        `when`<EditorColorsScheme>(editor.colorsScheme).thenReturn(DefaultColorsScheme())
        `when`(editor.component).thenReturn(JPanel())

        val editorFactory = object : MockEditorFactory() {
            override fun createEditor(document: Document, project: Project?): Editor {
                return editor
            }
        }

        val actionManager = mock(ActionManager::class.java)

        val actionToolbar = mock(ActionToolbar::class.java)
        `when`(actionToolbar.component).thenReturn(JPanel())

        `when`(actionManager.createActionToolbar(any(), any<ActionGroup>(), anyBoolean())).thenReturn(actionToolbar)

        val commonActionsManager = mock(CommonActionsManager::class.java)

        application.addComponent(EditorFactory::class.java, editorFactory)
        application.addComponent(ActionManager::class.java, actionManager)
        application.registerService(CommonActionsManager::class.java, commonActionsManager)

        `when`(redisClientMock.loadRecords(any(RedisContext::class.java), any(QueryOptions::class.java))).thenReturn(SearchResult("", emptyList(), 0))

        redisPanelWrapper = GuiActionRunner.execute(object : GuiQuery<RedisPanel>() {
            override fun executeInEDT(): RedisPanel? {
                return object : RedisPanel(dummyProject, RedisContext(redisClientMock, RedisServerConfiguration(), Database("0"))) {
                    override fun addActions(actionResultGroup: DefaultActionGroup, expandAllAction: AnAction, collapseAllAction: AnAction) {}
                }
            }
        })

        frameFixture = Containers.showInFrame(redisPanelWrapper!!)
    }
}