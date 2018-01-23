package org.codinjutsu.tools.nosql.mongo.view.wrapper

import com.mongodb.DBObject
import org.codinjutsu.tools.nosql.commons.view.wrapper.ObjectWrapper

internal class MongoObjectWrapper(private val dbObject: DBObject) : ObjectWrapper {

    override val names: Collection<String>
        get() = dbObject.keySet()

    override fun get(name: String): Any? = dbObject.get(name)
}