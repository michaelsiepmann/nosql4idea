package org.codinjutsu.tools.nosql.commons.view.add

import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.commons.view.createJSONEditor
import javax.swing.JComponent

internal abstract class JsonEditorWrapper(project: Project) : TextFieldWrapper<JComponent> {

    protected val editor = createJSONEditor(project)

    override val component: JComponent
        get() = editor.component

    override val isValueSet: Boolean
        get() = editor.document.textLength > 0

    override fun validate() {
    }
}