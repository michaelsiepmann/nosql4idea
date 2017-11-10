package org.codinjutsu.tools.nosql.couchbase.view;

import org.codinjutsu.tools.nosql.commons.model.SearchResult;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.NodeDescriptor;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.NodeDescriptorFactory;
import org.codinjutsu.tools.nosql.couchbase.view.nodedescriptor.CouchbaseKeyValueDescriptor;
import org.codinjutsu.tools.nosql.couchbase.view.nodedescriptor.CouchbaseResultDescriptor;
import org.codinjutsu.tools.nosql.couchbase.view.nodedescriptor.CouchbaseValueDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CouchbaseTreeModelFactory implements NodeDescriptorFactory {
    @NotNull
    @Override
    public NodeDescriptor createResultDescriptor(@NotNull SearchResult searchResult) {
        return new CouchbaseResultDescriptor(searchResult.getName());
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
}
