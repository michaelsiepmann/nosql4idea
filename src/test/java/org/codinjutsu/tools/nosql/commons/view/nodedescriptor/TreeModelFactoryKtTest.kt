package org.codinjutsu.tools.nosql.commons.view.nodedescriptor

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.mongodb.BasicDBObject
import org.codinjutsu.tools.nosql.commons.view.NoSqlTreeNode
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.json.JsonKeyValueDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.json.JsonTreeModelFactory
import org.codinjutsu.tools.nosql.mongo.view.model.MongoTreeModelFactory
import org.codinjutsu.tools.nosql.mongo.view.nodedescriptor.MongoKeyValueDescriptor
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock

internal class TreeModelFactoryKtTest {

    private lateinit var currentValueNode: NoSqlTreeNode

    @BeforeEach
    fun setUp() {
        val nodeDescriptor = mock(NodeDescriptor::class.java)
        currentValueNode = NoSqlTreeNode(nodeDescriptor)
    }

    @Test
    fun processWithMongoObject() {
        val factory = MongoTreeModelFactory()
        val obj = BasicDBObject()
        obj.append("stringtest", "abc")
        val testobj = BasicDBObject()
        testobj.append("subelement", "def")
        obj.append("objecttest", testobj)

        process(obj, currentValueNode, factory)

        assertEquals(2, currentValueNode.childCount)
        val children = currentValueNode.children()
        var element = (children.nextElement() as NoSqlTreeNode).userObject
        assertTrue(element is MongoKeyValueDescriptor) {
            "First element should be a MongoKeyValueDescriptor"
        }
        element = (children.nextElement() as NoSqlTreeNode).userObject
        assertTrue(element is MongoKeyValueDescriptor) {
            "Second element should be a MongoKeyValueDescriptor"
        }
    }

    @Test
    fun processWithJson() {
        val factory = JsonTreeModelFactory()
        val gson = Gson().fromJson<JsonElement>(JSON, JsonElement::class.java)

        process(gson, currentValueNode, factory)

        assertEquals(2, currentValueNode.childCount)
        val children = currentValueNode.children()
        var element = (children.nextElement() as NoSqlTreeNode).userObject
        assertTrue(element is JsonKeyValueDescriptor) {
            "First element should be a JsonKeyValueDescriptor"
        }
        element = (children.nextElement() as NoSqlTreeNode).userObject
        assertTrue(element is JsonKeyValueDescriptor) {
            "Second element should be a JsonKeyValueDescriptor"
        }
    }

    companion object {
        @Language("json")
        private const val JSON = """{
  "stringtest": "abc",
  "objecttest": {
    "subelement": "def"
  }
}"""
    }
}