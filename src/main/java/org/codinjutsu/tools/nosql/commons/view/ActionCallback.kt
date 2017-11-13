package org.codinjutsu.tools.nosql.commons.view

interface ActionCallback {

    fun onOperationSuccess(message: String)

    fun onOperationFailure(exception: Exception)

    fun onOperationCancelled(message: String)
}
