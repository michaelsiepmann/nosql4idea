package org.codinjutsu.tools.nosql.mongo.view.model

import com.mongodb.DBObject
import org.bson.types.ObjectId
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabasePrimitive
import org.codinjutsu.tools.nosql.commons.style.StyleAttributesProvider
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.NodeDescriptorFactory
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.BooleanKeyValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.DateKeyValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.DefaultKeyValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.NullKeyValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.NumberKeyValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.StringKeyValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.TypedKeyValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.result.StandardResultDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.value.StandardDateIndexedValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.value.StandardIndexedValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.value.StandardStringIndexedValueDescriptor
import java.util.*

internal class MongoNodeDescriptorFactory : NodeDescriptorFactory {
    override fun createResultDescriptor(name: String) = StandardResultDescriptor()

    override fun createKeyValueDescriptor(key: String, value: DatabaseElement): TypedKeyValueDescriptor<out Any?> {
        return createTypedKeyValueDescriptor(key, toInternalValue(value))
    }

    private fun createTypedKeyValueDescriptor(key: String, value: Any?) =
            when (value) {
                null -> NullKeyValueDescriptor(key)
                is Boolean -> BooleanKeyValueDescriptor(key, value.toString().toBoolean())
                is Int -> NumberKeyValueDescriptor(key, value.toString().toInt())
                is Double -> object : TypedKeyValueDescriptor<Double>(key, value, StyleAttributesProvider.getNumberAttribute()) {
                    override fun setValue(value: Any?) {
                        super.setValue(value.toString().toDouble())
                    }
                }
                is Long -> object : TypedKeyValueDescriptor<Long>(key, value, StyleAttributesProvider.getNumberAttribute()) {
                    override fun setValue(value: Any?) {
                        super.setValue(value.toString().toLong())
                    }
                }
                is String -> StringKeyValueDescriptor(key, value)
                is Date -> DateKeyValueDescriptor(key, value)
                is ObjectId -> TypedKeyValueDescriptor(key, value, StyleAttributesProvider.getObjectIdAttribute())
                is DBObject -> TypedKeyValueDescriptor(key, value, StyleAttributesProvider.getObjectAttribute())
                is DatabaseElement -> TypedKeyValueDescriptor(key, value, StyleAttributesProvider.getObjectAttribute(), findIcon(value))
                else -> DefaultKeyValueDescriptor(key, value, StyleAttributesProvider.getStringAttribute())
            }

    private fun toInternalValue(value: DatabaseElement) =
            if (value is DatabasePrimitive) {
                value.value()
            } else {
                value
            }

    override fun createIndexValueDescriptor(index: Int, value: DatabaseElement): StandardIndexedValueDescriptor<*> {
        return createStandardIndexedValueDescriptor(index, toInternalValue(value)!!)
    }

    private fun createStandardIndexedValueDescriptor(index: Int, value: Any) =
            when (value) {
                is String -> StandardStringIndexedValueDescriptor(index, value)
                is Boolean -> object : StandardIndexedValueDescriptor<Boolean>(index, value, StyleAttributesProvider.getBooleanAttribute()) {
                    override fun setValue(value: Any) {
                        super.setValue(value.toString().toBoolean())
                    }
                }
                is Number -> object : StandardIndexedValueDescriptor<Number>(index, value, StyleAttributesProvider.getNumberAttribute()) {
                    override fun setValue(value: Any) {
                        var number = value as String
                        number = number.replace("(.*)\\..*".toRegex(), "$1")
                        super.setValue(number.toInt())
                    }
                }
                is Date -> StandardDateIndexedValueDescriptor(index, value)
                is DBObject -> StandardIndexedValueDescriptor(index, value, StyleAttributesProvider.getObjectAttribute())
                else -> StandardIndexedValueDescriptor(index, value, StyleAttributesProvider.getStringAttribute())
            }
}