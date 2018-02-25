package org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue

import com.intellij.ui.SimpleTextAttributes
import javax.swing.Icon

open class DefaultKeyValueDescriptor(key: String, value: Any?, textAttributes: SimpleTextAttributes, icon: Icon? = null) :
        TypedKeyValueDescriptor<Any?>(key, value, textAttributes, icon)