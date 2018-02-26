package org.codinjutsu.tools.nosql.couchbase.view;

import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.NodeDescriptorFactory;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.DefaultKeyValueDescriptor;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.KeyValueDescriptor;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.NullKeyValueDescriptor;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.StringKeyValueDescriptor;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.result.ResultDescriptor;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.result.StandardResultDescriptor;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.value.IndexedValueDescriptor;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.value.StandardIndexedValueDescriptor;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.value.StandardStringIndexedValueDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static org.codinjutsu.tools.nosql.commons.style.StyleAttributesProvider.getStringAttribute;
import static org.codinjutsu.tools.nosql.commons.utils.SimpleTextAttributesUtilsKt.getSimpleTextAttributes;

public class CouchbaseNodeDescriptorFactory implements NodeDescriptorFactory {

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

        return new DefaultKeyValueDescriptor(key, value, getSimpleTextAttributes(value), null);
    }

    @NotNull
    @Override
    public IndexedValueDescriptor<?> createIndexValueDescriptor(int index, Object value) {
        if (value instanceof String) {
            return new StandardStringIndexedValueDescriptor(index, (String) value);
        }
        return new StandardIndexedValueDescriptor<>(index, value, getStringAttribute());
    }
}
