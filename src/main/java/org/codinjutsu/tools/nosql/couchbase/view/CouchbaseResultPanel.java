package org.codinjutsu.tools.nosql.couchbase.view;

import com.couchbase.client.java.document.json.JsonObject;
import com.intellij.openapi.project.Project;
import org.codinjutsu.tools.nosql.commons.view.EditionPanel;
import org.codinjutsu.tools.nosql.commons.view.NoSQLResultPanelDocumentOperations;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.NodeDescriptorFactory;
import org.codinjutsu.tools.nosql.commons.view.panel.AbstractNoSQLResultPanel;
import org.codinjutsu.tools.nosql.couchbase.model.CouchbaseResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CouchbaseResultPanel extends AbstractNoSQLResultPanel<CouchbaseResult, JsonObject> {

    public CouchbaseResultPanel(@NotNull Project project, @NotNull NoSQLResultPanelDocumentOperations<JsonObject> documentOperations) {
        super(project, documentOperations, new CouchbaseTreeModelFactory(), "_id");
    }

    @Nullable
    @Override
    protected EditionPanel<JsonObject> createEditionPanel() {
        return null;
    }
}
