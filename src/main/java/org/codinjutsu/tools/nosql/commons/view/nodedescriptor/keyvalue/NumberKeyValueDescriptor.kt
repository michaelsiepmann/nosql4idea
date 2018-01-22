package org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue

import org.codinjutsu.tools.nosql.commons.style.StyleAttributesProvider.getNumberAttribute

class NumberKeyValueDescriptor(key: String, value: Number) : TypedKeyValueDescriptor<Number>(key, value, getNumberAttribute())