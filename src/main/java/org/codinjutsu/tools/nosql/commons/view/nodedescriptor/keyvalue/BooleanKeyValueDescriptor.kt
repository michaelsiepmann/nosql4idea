package org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue

import org.codinjutsu.tools.nosql.commons.style.StyleAttributesProvider.getBooleanAttribute

class BooleanKeyValueDescriptor(key: String, value: Boolean) : TypedKeyValueDescriptor<Boolean>(key, value, getBooleanAttribute())