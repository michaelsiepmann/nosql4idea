package org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue

import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.NodeDescriptor

internal interface KeyValueDescriptor<VALUE> : NodeDescriptor<VALUE> {
    val key: String
}