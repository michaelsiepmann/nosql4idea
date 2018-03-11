package org.codinjutsu.tools.nosql.redis.view.nodedescriptor

import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.NodeDescriptorFactory
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.result.NullResultDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.value.IndexedValueDescriptor

internal class RedisNodeDescriptorFactory : NodeDescriptorFactory {

    override fun createResultDescriptor(name: String) = NullResultDescriptor()

    override fun createKeyValueDescriptor(key: String, value: Any?) =
            RedisKeyValueDescriptor.createDescriptor(key, value?.toString())

    override fun createIndexValueDescriptor(index: Int, value: Any?) =
            RedisIndexedValueDescriptor.createDescriptor(index, value)
}
