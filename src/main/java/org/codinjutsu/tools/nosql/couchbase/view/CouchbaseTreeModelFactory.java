package org.codinjutsu.tools.nosql.couchbase.view;

import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;
import org.codinjutsu.tools.nosql.commons.view.NoSqlTreeNode;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.NodeDescriptorFactory;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.DefaultKeyValueDescriptor;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.KeyValueDescriptor;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.NullKeyValueDescriptor;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.StringKeyValueDescriptor;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.result.ResultDescriptor;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.result.StandardResultDescriptor;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.value.StandardStringValueDescriptor;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.value.StandardValueDescriptor;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.value.ValueDescriptor;
import org.codinjutsu.tools.nosql.commons.view.wrapper.ObjectWrapper;
import org.codinjutsu.tools.nosql.couchbase.model.CouchbaseObjectWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

import static org.codinjutsu.tools.nosql.commons.style.StyleAttributesProvider.getStringAttribute;
import static org.codinjutsu.tools.nosql.commons.utils.SimpleTextAttributesUtilsKt.getSimpleTextAttributes;

public class CouchbaseTreeModelFactory implements NodeDescriptorFactory<JsonObject> {

    @NotNull
    @Override
    public ResultDescriptor<?> createResultDescriptor(@NotNull String name) {
        return new StandardResultDescriptor(name);
    }

    @NotNull
    @Override
    public KeyValueDescriptor<?> createKeyValueDescriptor(@NotNull String key, @Nullable Object value) {
        if (value == null) {
            return new NullKeyValueDescriptor(key);
        }
        if (value instanceof String) {
            return new StringKeyValueDescriptor(key, (String) value);
        }

        return new DefaultKeyValueDescriptor(key, value, getSimpleTextAttributes(value));
    }

    @NotNull
    @Override
    public ValueDescriptor<?> createValueDescriptor(int index, @NotNull Object value) {
        if (value instanceof String) {
            return new StandardStringValueDescriptor(index, (String) value);
        }
        return new StandardValueDescriptor<>(index, value, getStringAttribute());
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

    @Override
    public boolean isObject(@Nullable Object value) {
        return value instanceof JsonObject;
    }

    @NotNull
    @Override
    public ObjectWrapper createObjectWrapper(@Nullable Object value) {
        return new CouchbaseObjectWrapper((JsonObject) value);
    }
}
