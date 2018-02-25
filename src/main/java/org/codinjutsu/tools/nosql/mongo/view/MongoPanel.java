package org.codinjutsu.tools.nosql.mongo.view;

import com.intellij.openapi.project.Project;
import org.codinjutsu.tools.nosql.commons.model.DatabaseContext;
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement;
import org.codinjutsu.tools.nosql.commons.view.DatabasePanel;
import org.codinjutsu.tools.nosql.commons.view.columninfo.WriteableColumnInfoDecider;
import org.codinjutsu.tools.nosql.commons.view.panel.NoSQLResultPanel;
import org.codinjutsu.tools.nosql.mongo.model.MongoContext;
import org.codinjutsu.tools.nosql.mongo.view.columninfo.MongoWriteableColumnInfoDecider;
import org.codinjutsu.tools.nosql.mongo.view.model.MongoNodeDescriptorFactory;
import org.codinjutsu.tools.nosql.mongo.view.panel.query.MongoQueryPanel;
import org.jetbrains.annotations.NotNull;

public class MongoPanel extends DatabasePanel<DatabaseElement> {

    public MongoPanel(Project project, DatabaseContext context) {
        super(project, context, "_id", MongoQueryPanel::new); //NON-NLS
    }

    @Override
    protected NoSQLResultPanel<DatabaseElement> createResultPanel(Project project, String idDescriptor) {
        return new NoSQLResultPanel<DatabaseElement>(project, this, true, new MongoNodeDescriptorFactory(), idDescriptor) {
            @NotNull
            @Override
            protected WriteableColumnInfoDecider writeableColumnInfoDecider() {
                return new MongoWriteableColumnInfoDecider();
            }
        };
    }

    @Override
    public Object getRecords() {
        return ((MongoContext) getContext().getDelegatedContext()).getMongoCollection();
    }
}
