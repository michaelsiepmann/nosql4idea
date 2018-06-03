package org.codinjutsu.tools.nosql.commons.configuration

import org.jdom.Element

internal interface ConfigurationElement {

    fun readElement(element: Element)

    fun createElement(): Element
}