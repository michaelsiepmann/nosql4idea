package org.codinjutsu.tools.nosql.redis.view.nodedescriptor;

import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.NodeDescriptorFactory;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.KeyValueDescriptor;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.result.NullResultDescriptor;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.result.ResultDescriptor;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.value.IndexedValueDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RedisNodeDescriptorFactory implements NodeDescriptorFactory {

    @NotNull
    @Override
    public ResultDescriptor<?> createResultDescriptor(@NotNull String name) {
        return new NullResultDescriptor();
    }

    @NotNull
    @Override
    public KeyValueDescriptor<?> createKeyValueDescriptor(@NotNull String key, @Nullable Object value) {
        return RedisKeyValueDescriptor.createDescriptor(key, value.toString());
    }

    @NotNull
    @Override
    public IndexedValueDescriptor<?> createIndexValueDescriptor(int index, @NotNull Object value) {
        return RedisIndexedValueDescriptor.createDescriptor(index, value);
    }
}
