package org.codinjutsu.tools.nosql.commons.view.nodedescriptor

import com.intellij.icons.AllIcons
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.KeyValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.result.ResultDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.value.IndexedValueDescriptor

internal interface NodeDescriptorFactory {

    fun createResultDescriptor(name: String): ResultDescriptor<*>

    fun createKeyValueDescriptor(key: String, value: DatabaseElement): KeyValueDescriptor<*>

    fun createIndexValueDescriptor(index: Int, value: DatabaseElement): IndexedValueDescriptor<*>

    fun findIcon(value: DatabaseElement) =
            when {
                value.isArray() -> AllIcons.Json.Array
                value.isObject() -> AllIcons.Json.Object
                else -> null
            }
}