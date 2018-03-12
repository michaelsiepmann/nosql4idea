package org.codinjutsu.tools.nosql.commons.view.add

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.intellij.openapi.project.Project
import org.codinjutsu.tools.nosql.commons.model.internal.toDatabaseElement

internal class JsonFieldArrayWrapper(project: Project) : JsonEditorWrapper(project) {

    override fun getValue() =
            Gson().fromJson(editor.document.text, JsonArray::class.java).toDatabaseElement()

    override fun reset() {
        editor.document.setText("[]")
    }
}
