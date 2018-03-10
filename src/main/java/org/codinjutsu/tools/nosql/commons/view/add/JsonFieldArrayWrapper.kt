package org.codinjutsu.tools.nosql.commons.view.add

import com.google.gson.Gson
import com.google.gson.JsonArray
import org.codinjutsu.tools.nosql.commons.model.internal.toDatabaseElement

internal class JsonFieldArrayWrapper : JTextFieldWrapper() {

    override fun getValue() =
            Gson().fromJson(component.text, JsonArray::class.java).toDatabaseElement()
}
