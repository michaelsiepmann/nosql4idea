package org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue

import com.intellij.ui.SimpleTextAttributes

open class DefaultKeyValueDescriptor(key: String, value: Any?, textAttributes: SimpleTextAttributes) : TypedKeyValueDescriptor<Any?>(key, value, textAttributes)