package org.codinjutsu.tools.nosql.redis.view.nodedescriptor;

import org.codinjutsu.tools.nosql.commons.view.NoSqlTreeNode;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.NodeDescriptor;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.NodeDescriptorFactory;
import org.codinjutsu.tools.nosql.commons.view.wrapper.ObjectWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

public class RedisTreeModelFactory implements NodeDescriptorFactory<Object> {

    @NotNull
    @Override
    public NodeDescriptor createResultDescriptor(@NotNull String name) {
        return new RedisResultDescriptor();
    }

    @NotNull
    @Override
    public NodeDescriptor createKeyValueDescriptor(@NotNull String key, @Nullable Object value) {
        return RedisKeyValueDescriptor.createDescriptor(key, value.toString());
    }

    @NotNull
    @Override
    public NodeDescriptor createValueDescriptor(int index, @NotNull Object value) {
        return RedisValueDescriptor.createDescriptor(index, value);
    }

    @Override
    public void processObject(@NotNull NoSqlTreeNode parentNode, @Nullable Object value) {

    }

    @Override
    public Object buildDBObject(@NotNull NoSqlTreeNode rootNode) {
        return null;
    }

    @Override
    public boolean isArray(@Nullable Object value) {
        return false;
    }

    @NotNull
    @Override
    public Iterator<Object> toArray(@NotNull Object value) {
        return null;
    }

    @Override
    public boolean isObject(@Nullable Object value) {
        return false;
    }

    @NotNull
    @Override
    public ObjectWrapper createObjectWrapper(@Nullable Object value) {
        return null;
    }
}
