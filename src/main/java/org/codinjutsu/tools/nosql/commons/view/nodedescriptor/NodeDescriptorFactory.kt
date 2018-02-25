package org.codinjutsu.tools.nosql.commons.view.nodedescriptor

import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.KeyValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.result.ResultDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.value.IndexedValueDescriptor

internal interface NodeDescriptorFactory {

    fun createResultDescriptor(name: String): ResultDescriptor<*>

    fun createKeyValueDescriptor(key: String, value: Any?): KeyValueDescriptor<*>

    fun createIndexValueDescriptor(index: Int, value: Any): IndexedValueDescriptor<*>
}