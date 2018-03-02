package org.codinjutsu.tools.nosql.i18n

import org.jetbrains.annotations.PropertyKey
import java.text.MessageFormat
import java.util.*

private const val BUNDLE = "org.codinjutsu.tools.nosql.i18n.translations"

private var resourceBundle: ResourceBundle? = null

private fun getResourceBundle(): ResourceBundle {
    if (resourceBundle == null) {
        try {
            resourceBundle = ResourceBundle.getBundle(BUNDLE, Locale.getDefault())
        } catch (e: MissingResourceException) {
            throw MissingResourceException("Missing Resource bundle: " + Locale.getDefault() + ' ', BUNDLE, "")
        }
    }
    return resourceBundle!!
}

fun getResourceString(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg params: Any): String {
    try {
        val string = getResourceBundle().getString(key)
        if (params.isNotEmpty() && string.indexOf('{') >= 0) {
            return MessageFormat.format(string, *params)
        }
        return string
    } catch (e: MissingResourceException) {
        throw MissingResourceException("Missing Resource: " + Locale.getDefault() + " - key: " + key + "  - resources: " + BUNDLE, BUNDLE, key)
    }
}
