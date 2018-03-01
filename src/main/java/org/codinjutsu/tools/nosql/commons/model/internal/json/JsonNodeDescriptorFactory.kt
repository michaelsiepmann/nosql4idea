package org.codinjutsu.tools.nosql.commons.model.internal.json

import com.intellij.icons.AllIcons.Json.Array
import com.intellij.icons.AllIcons.Json.Object
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabasePrimitive
import org.codinjutsu.tools.nosql.commons.style.StyleAttributesProvider
import org.codinjutsu.tools.nosql.commons.utils.getSimpleTextAttributes
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.NodeDescriptorFactory
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.internal.DatabaseIndexedValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.internal.DatabaseResultDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.BooleanKeyValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.DefaultKeyValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.KeyValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.NullKeyValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.NumberKeyValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.StringKeyValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.TypedKeyValueDescriptor

internal class JsonNodeDescriptorFactory : NodeDescriptorFactory {

    override fun createResultDescriptor(name: String) = DatabaseResultDescriptor(name)

    override fun createKeyValueDescriptor(key: String, value: Any?): KeyValueDescriptor<*> =
            when (value) {
                null -> NullKeyValueDescriptor(key)
                is DatabasePrimitive -> when {
                    value.isBoolean() -> BooleanKeyValueDescriptor(key, value.asBoolean())
                    value.isNumber() -> NumberKeyValueDescriptor(key, value.asNumber())
                    value.isString() -> StringKeyValueDescriptor(key, value.asString())
                    else -> DefaultKeyValueDescriptor(key, value, getSimpleTextAttributes(value))
                }
                is String -> StringKeyValueDescriptor(key, value)
                is DatabaseElement -> TypedKeyValueDescriptor(key, value, StyleAttributesProvider.getObjectAttribute(), findIcon(value))
                else -> DefaultKeyValueDescriptor(key, value, getSimpleTextAttributes(value))
            }

    override fun createIndexValueDescriptor(index: Int, value: Any?) =
            DatabaseIndexedValueDescriptor.createDescriptor(index, value)

    private fun findIcon(value: DatabaseElement) =
            when {
                value.isArray() -> Array
                value.isObject() -> Object
                else -> null
            }
}