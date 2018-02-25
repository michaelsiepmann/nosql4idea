package org.codinjutsu.tools.nosql.commons.view.nodedescriptor.internal

internal class TreeModelFactoryKtTest {
/*
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
        assertTrue(element is TypedKeyValueDescriptor<*>) {
            "First element should be a TypedKeyValueDescriptor"
        }
        element = (children.nextElement() as NoSqlTreeNode).userObject
        assertTrue(element is TypedKeyValueDescriptor<*>) {
            "Second element should be a TypedKeyValueDescriptor"
        }
    }

    @Test
    fun processWithJson() {
        val factory = DatabaseTreeModelFactory()
        val gson = Gson().fromJson<JsonElement>(JSON, JsonElement::class.java)

        process(gson, currentValueNode, factory)

        assertEquals(2, currentValueNode.childCount)
        val children = currentValueNode.children()
        var element = (children.nextElement() as NoSqlTreeNode).userObject
        assertTrue(element is DatabaseKeyValueDescriptor<*>) {
            "First element should be a DatabaseKeyValueDescriptor"
        }
        element = (children.nextElement() as NoSqlTreeNode).userObject
        assertTrue(element is DatabaseKeyValueDescriptor<*>) {
            "Second element should be a DatabaseKeyValueDescriptor"
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
*/
}