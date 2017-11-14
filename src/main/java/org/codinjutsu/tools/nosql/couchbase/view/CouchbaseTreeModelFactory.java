package org.codinjutsu.tools.nosql.couchbase.view;

import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import org.codinjutsu.tools.nosql.commons.view.NoSqlTreeNode;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.NodeDescriptor;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.NodeDescriptorFactory;
import org.codinjutsu.tools.nosql.couchbase.view.nodedescriptor.CouchbaseKeyValueDescriptor;
import org.codinjutsu.tools.nosql.couchbase.view.nodedescriptor.CouchbaseResultDescriptor;
import org.codinjutsu.tools.nosql.couchbase.view.nodedescriptor.CouchbaseValueDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

public class CouchbaseTreeModelFactory implements NodeDescriptorFactory<JsonObject> {

    @NotNull
    @Override
    public NodeDescriptor createResultDescriptor(@NotNull String name) {
        return new CouchbaseResultDescriptor(name);
    }

    @NotNull
    @Override
    public NodeDescriptor createKeyValueDescriptor(@NotNull String key, @Nullable Object value) {
        return CouchbaseKeyValueDescriptor.createDescriptor(key, value);
    }

    @NotNull
    @Override
    public NodeDescriptor createValueDescriptor(int index, @NotNull Object value) {
        return CouchbaseValueDescriptor.createDescriptor(index, value);
    }

    @Override
    public void processObject(@NotNull NoSqlTreeNode parentNode, @Nullable Object value) {

    }

    @Override
    public JsonObject buildDBObject(@NotNull NoSqlTreeNode rootNode) {
        return null;
    }

    @Override
    public boolean isArray(@Nullable Object value) {
        return value instanceof JsonArray;
    }

    @NotNull
    @Override
    public Iterator<Object> toArray(@NotNull Object value) {
        return ((JsonArray) value).iterator();
    }
}
