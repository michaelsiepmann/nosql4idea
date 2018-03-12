package org.codinjutsu.tools.nosql.redis.view.nodedescriptor

import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.NodeDescriptorFactory
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.KeyValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.result.NullResultDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.value.IndexedValueDescriptor

internal class RedisNodeDescriptorFactory : NodeDescriptorFactory {

    override fun createResultDescriptor(name: String) = NullResultDescriptor()

    override fun createKeyValueDescriptor(key: String, value: DatabaseElement): KeyValueDescriptor<*> =
            RedisKeyValueDescriptor.createDescriptor(key, value)

    override fun createIndexValueDescriptor(index: Int, value: DatabaseElement): IndexedValueDescriptor<*> =
            RedisIndexedValueDescriptor.createDescriptor(index, value)
}
