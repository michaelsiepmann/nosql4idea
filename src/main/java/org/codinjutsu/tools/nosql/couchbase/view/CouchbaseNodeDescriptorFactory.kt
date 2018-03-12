package org.codinjutsu.tools.nosql.couchbase.view

import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement
import org.codinjutsu.tools.nosql.commons.style.StyleAttributesProvider.getStringAttribute
import org.codinjutsu.tools.nosql.commons.utils.getSimpleTextAttributes
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.NodeDescriptorFactory
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.DefaultKeyValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.result.StandardResultDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.value.StandardIndexedValueDescriptor

internal class CouchbaseNodeDescriptorFactory : NodeDescriptorFactory {

    override fun createResultDescriptor(name: String) = StandardResultDescriptor(name)

    override fun createKeyValueDescriptor(key: String, value: DatabaseElement) =
            DefaultKeyValueDescriptor(key, value, getSimpleTextAttributes(value), null)

    override fun createIndexValueDescriptor(index: Int, value: DatabaseElement) =
            StandardIndexedValueDescriptor(index, value, getStringAttribute())
}
