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

package org.codinjutsu.tools.nosql.commons.view.editor

import com.intellij.codeHighlighting.BackgroundEditorHighlighter
import com.intellij.ide.structureView.StructureViewBuilder
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.fileEditor.FileEditorStateLevel
import com.intellij.openapi.util.UserDataHolderBase
import org.codinjutsu.tools.nosql.commons.view.NoSqlResultView
import java.beans.PropertyChangeListener
import javax.swing.JComponent
import javax.swing.JPanel

class NoSqlDatabaseDataEditor internal constructor(private var panel: NoSqlResultView?) : UserDataHolderBase(), FileEditor {
    private var disposed = false

    init {
        ApplicationManager.getApplication().invokeLater { panel!!.showResults() }
    }

    override fun getComponent(): JComponent = if (disposed) JPanel() else panel!!

    override fun getPreferredFocusedComponent() = if (panel == null) null else panel!!.resultPanel

    override fun getName() = "NoSql Data"

    override fun dispose() {
        if (!disposed) {
            panel!!.dispose()
            panel = null
            disposed = true
        }
    }

    override fun getState(level: FileEditorStateLevel): FileEditorState = FileEditorState.INSTANCE

    //    Unused methods

    override fun setState(state: FileEditorState) {}

    override fun isModified() = false

    override fun isValid() = true

    override fun selectNotify() {}

    override fun deselectNotify() {}

    override fun addPropertyChangeListener(listener: PropertyChangeListener) {}

    override fun removePropertyChangeListener(listener: PropertyChangeListener) {}

    override fun getBackgroundHighlighter(): BackgroundEditorHighlighter? = null

    override fun getCurrentLocation(): FileEditorLocation? = null

    override fun getStructureViewBuilder(): StructureViewBuilder? = null
}
