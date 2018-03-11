package org.codinjutsu.tools.nosql.couchbase.view;

import com.intellij.openapi.project.Project;
import org.codinjutsu.tools.nosql.commons.model.DataType;
import org.codinjutsu.tools.nosql.commons.view.DatabasePanel;
import org.codinjutsu.tools.nosql.commons.view.panel.DefaultTreeBuilder;
import org.codinjutsu.tools.nosql.commons.view.panel.NoSQLResultPanel;
import org.codinjutsu.tools.nosql.commons.view.panel.TreePreparator;
import org.codinjutsu.tools.nosql.couchbase.model.CouchbaseContext;

public class CouchbasePanel extends DatabasePanel {

    public CouchbasePanel(Project project, CouchbaseContext context) {
        super(project, context, "_id"); //NON-NLS
    }

    @Override
    protected NoSQLResultPanel createResultPanel(Project project, String idDescriptor, DataType[] dataTypes) {
        return new NoSQLResultPanel(project, this, false, new CouchbaseNodeDescriptorFactory(), idDescriptor, dataTypes, new DefaultTreeBuilder(), TreePreparator.Companion.getNOOP());
    }
}
