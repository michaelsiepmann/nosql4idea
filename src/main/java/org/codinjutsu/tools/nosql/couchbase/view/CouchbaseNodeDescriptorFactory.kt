package org.codinjutsu.tools.nosql.couchbase.view

import org.codinjutsu.tools.nosql.commons.style.StyleAttributesProvider.getStringAttribute
import org.codinjutsu.tools.nosql.commons.utils.getSimpleTextAttributes
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.NodeDescriptorFactory
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.DefaultKeyValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.NullKeyValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.StringKeyValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.result.StandardResultDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.value.StandardIndexedValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.value.StandardStringIndexedValueDescriptor

internal class CouchbaseNodeDescriptorFactory : NodeDescriptorFactory {

    override fun createResultDescriptor(name: String) = StandardResultDescriptor(name)

    override fun createKeyValueDescriptor(key: String, value: Any?) = when (value) {
        null -> NullKeyValueDescriptor(key)
        is String -> StringKeyValueDescriptor(key, (value as String?)!!)
        else -> DefaultKeyValueDescriptor(key, value, getSimpleTextAttributes(value), null)
    }

    override fun createIndexValueDescriptor(index: Int, value: Any?) =
            if (value is String) {
                StandardStringIndexedValueDescriptor(index, (value as String?)!!)
            } else {
                StandardIndexedValueDescriptor(index, value!!, getStringAttribute())
            }
}
