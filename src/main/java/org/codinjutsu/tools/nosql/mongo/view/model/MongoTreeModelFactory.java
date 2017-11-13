package org.codinjutsu.tools.nosql.mongo.view.model;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.codinjutsu.tools.nosql.commons.model.SearchResult;
import org.codinjutsu.tools.nosql.commons.view.NoSqlTreeNode;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.NodeDescriptor;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.NodeDescriptorFactory;
import org.codinjutsu.tools.nosql.mongo.model.MongoResult;
import org.codinjutsu.tools.nosql.mongo.view.nodedescriptor.MongoKeyValueDescriptor;
import org.codinjutsu.tools.nosql.mongo.view.nodedescriptor.MongoResultDescriptor;
import org.codinjutsu.tools.nosql.mongo.view.nodedescriptor.MongoValueDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.tree.TreeNode;
import java.util.Enumeration;
import java.util.List;

public class MongoTreeModelFactory implements NodeDescriptorFactory<DBObject> {

    @NotNull
    @Override
    public NodeDescriptor createResultDescriptor(@NotNull SearchResult searchResult) {
        return new MongoResultDescriptor();
    }

    @NotNull
    @Override
    public NodeDescriptor createKeyValueDescriptor(@NotNull String key, @Nullable Object value) {
        return MongoKeyValueDescriptor.createDescriptor(key, value);
    }

    @NotNull
    @Override
    public NodeDescriptor createValueDescriptor(int index, @NotNull Object value) {
        return MongoValueDescriptor.createDescriptor(index, value);
    }

    @NotNull
    // todo
    public TreeNode buildJsonTree(@NotNull MongoResult mongoResult) {
        NoSqlTreeNode rootNode = new NoSqlTreeNode(new MongoResultDescriptor(mongoResult.getName()));

        List<DBObject> mongoObjects = mongoResult.getMongoObjects();
        int i = 0;
        for (DBObject mongoObject : mongoObjects) {
            if (mongoObject instanceof BasicDBList) {
                processDbObject(rootNode, mongoObject);
            } else if (mongoObject instanceof BasicDBObject) {//dead code?
                NoSqlTreeNode currentNode = new NoSqlTreeNode(MongoValueDescriptor.createDescriptor(i++, mongoObject));
                processDbObject(currentNode, mongoObject);
                rootNode.add(currentNode);
            }
        }
        return rootNode;
    }

    @Override
    public void processObject(@NotNull NoSqlTreeNode parentNode, @Nullable Object value) {
        if (value instanceof DBObject) {
            processDbObject(parentNode, (DBObject) value);
        }
    }

    void processDbObject(@NotNull NoSqlTreeNode parentNode, DBObject mongoObject) {
        if (mongoObject instanceof BasicDBList) {
            BasicDBList mongoObjectList = (BasicDBList) mongoObject;
            for (int i = 0; i < mongoObjectList.size(); i++) {
                Object mongoObjectOfList = mongoObjectList.get(i);
                NoSqlTreeNode currentNode = new NoSqlTreeNode(MongoValueDescriptor.createDescriptor(i, mongoObjectOfList));
                if (mongoObjectOfList instanceof DBObject) {
                    processDbObject(currentNode, (DBObject) mongoObjectOfList);
                }
                parentNode.add(currentNode);
            }
        } else if (mongoObject instanceof BasicDBObject) {
            BasicDBObject basicDBObject = (BasicDBObject) mongoObject;
            for (String key : basicDBObject.keySet()) {
                Object value = basicDBObject.get(key);
                NoSqlTreeNode currentNode = new NoSqlTreeNode(MongoKeyValueDescriptor.createDescriptor(key, value));
                if (value instanceof DBObject) {
                    processDbObject(currentNode, (DBObject) value);
                }
                parentNode.add(currentNode);
            }
        }
    }

    @NotNull
    @Override
    public DBObject buildDBObject(@NotNull NoSqlTreeNode rootNode) {
        BasicDBObject basicDBObject = new BasicDBObject();
        Enumeration children = rootNode.children();
        while (children.hasMoreElements()) {
            NoSqlTreeNode node = (NoSqlTreeNode) children.nextElement();
            MongoKeyValueDescriptor descriptor = (MongoKeyValueDescriptor) node.getDescriptor();
            Object value = descriptor.getValue();
            if (value instanceof DBObject) {
                if (value instanceof BasicDBList) {
                    basicDBObject.put(descriptor.getKey(), buildDBList(node));
                } else {
                    basicDBObject.put(descriptor.getKey(), buildDBObject(node));
                }
            } else {
                basicDBObject.put(descriptor.getKey(), value);
            }
        }

        return basicDBObject;
    }

    private DBObject buildDBList(NoSqlTreeNode parentNode) {
        BasicDBList basicDBList = new BasicDBList();
        Enumeration children = parentNode.children();
        while (children.hasMoreElements()) {
            NoSqlTreeNode node = (NoSqlTreeNode) children.nextElement();
            MongoValueDescriptor descriptor = (MongoValueDescriptor) node.getDescriptor();
            Object value = descriptor.getValue();
            if (value instanceof DBObject) {
                if (value instanceof BasicDBList) {
                    basicDBList.add(buildDBList(node));
                } else {
                    basicDBList.add(buildDBObject(node));
                }
            } else {
                basicDBList.add(value);
            }
        }
        return basicDBList;
    }
}
