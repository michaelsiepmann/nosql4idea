package org.codinjutsu.tools.nosql.solr.view

import org.apache.solr.common.SolrDocument
import org.codinjutsu.tools.nosql.commons.view.NoSqlTreeNode
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.NodeDescriptor
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.NodeDescriptorFactory
import org.codinjutsu.tools.nosql.commons.view.wrapper.ObjectWrapper

internal class SolrTreeModelFactory : NodeDescriptorFactory<SolrDocument> {

    override fun createResultDescriptor(name: String): NodeDescriptor {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createKeyValueDescriptor(key: String, value: Any?): NodeDescriptor {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createValueDescriptor(index: Int, value: Any): NodeDescriptor {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun processObject(parentNode: NoSqlTreeNode, value: Any?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun buildDBObject(rootNode: NoSqlTreeNode): SolrDocument {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isArray(value: Any?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun toArray(value: Any): Iterator<Any> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isObject(value: Any?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createObjectWrapper(value: Any?): ObjectWrapper {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}