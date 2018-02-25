package org.codinjutsu.tools.nosql.mongo.model.internal

import com.mongodb.DBObject
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.internal.InternalDatabasePrimitive
import org.codinjutsu.tools.nosql.commons.view.wrapper.ObjectWrapper

internal class DelegatingMongoObjectWrapper(private val delegate: ObjectWrapper) : ObjectWrapper by delegate {

    override fun get(name: String): Any? {
        val element = delegate.get(name)
        return when (element) {
            is DBObject -> convert(element)
            else -> InternalDatabasePrimitive(element)
        }
    }
}