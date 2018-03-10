package org.codinjutsu.tools.nosql.commons.view.add

import com.google.gson.Gson
import com.google.gson.JsonObject
import org.codinjutsu.tools.nosql.commons.model.internal.toDatabaseElement

internal class JsonFieldObjectWrapper : JTextFieldWrapper() {

    override fun getValue() =
            Gson().fromJson(component.text, JsonObject::class.java).toDatabaseElement()
}
