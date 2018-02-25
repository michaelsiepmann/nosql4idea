package org.codinjutsu.tools.nosql.couchbase.view;

import com.couchbase.client.java.document.json.JsonObject;
import com.intellij.openapi.project.Project;
import org.codinjutsu.tools.nosql.commons.view.DatabasePanel;
import org.codinjutsu.tools.nosql.commons.view.panel.NoSQLResultPanel;
import org.codinjutsu.tools.nosql.couchbase.model.CouchbaseContext;

public class CouchbasePanel extends DatabasePanel<JsonObject> {

    public CouchbasePanel(Project project, CouchbaseContext context) {
        super(project, context, "_id"); //NON-NLS
    }

    @Override
    protected NoSQLResultPanel<JsonObject> createResultPanel(Project project, String idDescriptor) {
        return new NoSQLResultPanel<>(project, this, false, new CouchbaseNodeDescriptorFactory(), idDescriptor);
    }
}
