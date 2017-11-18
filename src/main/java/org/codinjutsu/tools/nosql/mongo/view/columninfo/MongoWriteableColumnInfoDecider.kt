package org.codinjutsu.tools.nosql.mongo.view.columninfo

import com.mongodb.DBObject
import org.bson.types.ObjectId
import org.codinjutsu.tools.nosql.commons.view.NoSqlTreeNode
import org.codinjutsu.tools.nosql.commons.view.columninfo.WriteableColumnInfoDecider

class MongoWriteableColumnInfoDecider : WriteableColumnInfoDecider {
    override fun isNodeWriteable(treeNode: NoSqlTreeNode): Boolean {
        val value = treeNode.descriptor.value
        return value !is DBObject && value !is ObjectId
    }
}