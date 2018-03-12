/*
 * Copyright (c) 2015 David Boissier
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.codinjutsu.tools.nosql.redis.view

import org.codinjutsu.tools.nosql.commons.model.SearchResult
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement
import org.codinjutsu.tools.nosql.commons.view.NoSqlTreeNode
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.StringKeyValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.result.NullResultDescriptor
import org.codinjutsu.tools.nosql.redis.model.RedisKeyType
import org.codinjutsu.tools.nosql.redis.model.internal.RedisDatabaseHash
import org.codinjutsu.tools.nosql.redis.model.internal.RedisDatabaseList
import org.codinjutsu.tools.nosql.redis.model.internal.RedisDatabaseSet
import org.codinjutsu.tools.nosql.redis.model.internal.RedisDatabaseSortedSet
import org.codinjutsu.tools.nosql.redis.model.internal.RedisDatabaseString
import org.codinjutsu.tools.nosql.redis.view.nodedescriptor.RedisIndexedValueDescriptor
import org.codinjutsu.tools.nosql.redis.view.nodedescriptor.RedisKeyValueDescriptor

internal fun buildTree(searchResult: SearchResult): NoSqlTreeNode {
    val rootNode = NoSqlTreeNode(NullResultDescriptor())
    searchResult.records.forEach { redisRecord ->
        redisRecord.names()
                .forEach { name ->
                    val databaseElement = redisRecord[name]
                    if (databaseElement != null) {
                        databaseElement.createTreeNode(name)?.let {
                            rootNode.add(it)
                        }
                    }
                }
    }
    return rootNode
}

private fun DatabaseElement.createTreeNode(key: String) =
        when (this) {
            is RedisDatabaseList -> createTreeNode(key)
            is RedisDatabaseSet -> createTreeNode(key)
            is RedisDatabaseSortedSet -> createTreeNode(key)
            is RedisDatabaseHash -> createTreeNode(key)
            is RedisDatabaseString -> createTreeNode(key)
            else -> null
        }

private fun RedisDatabaseList.createTreeNode(key: String) =
        addChildElements(key, iterator())

private fun RedisDatabaseSet.createTreeNode(key: String) =
        addChildElements(key, iterator())

private fun RedisDatabaseSortedSet.createTreeNode(key: String) =
        addChildElements(key, iterator())

private fun RedisDatabaseString.createTreeNode(key: String) =
        NoSqlTreeNode(RedisKeyValueDescriptor.createDescriptor(RedisKeyType.STRING, key, asString()))

private fun DatabaseElement.addChildElements(key: String, iterator: Iterator<DatabaseElement>): NoSqlTreeNode {
    val treeNode = NoSqlTreeNode(RedisKeyValueDescriptor.createDescriptor(key, this))
    iterator.withIndex().forEach { (index, value) ->
        treeNode.add(NoSqlTreeNode(RedisIndexedValueDescriptor.createDescriptor(index, value)))
    }
    return treeNode
}

private fun RedisDatabaseHash.createTreeNode(key: String): NoSqlTreeNode {
    val treeNode = NoSqlTreeNode(RedisKeyValueDescriptor.createDescriptor(RedisKeyType.HASH, key, asObject().toString()))
    names().forEach {
        val value = get(it)
        if (value != null) {
            treeNode.add(NoSqlTreeNode(StringKeyValueDescriptor(it, value.toString())))
        }
    }
    return treeNode
}
