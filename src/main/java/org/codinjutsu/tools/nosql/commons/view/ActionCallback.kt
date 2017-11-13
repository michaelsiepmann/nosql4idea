package org.codinjutsu.tools.nosql.commons.view

internal interface ActionCallback {

    fun onOperationSuccess(message: String)

    fun onOperationFailure(exception: Exception)

    fun onOperationCancelled(message: String)
}
