package org.codinjutsu.tools.nosql.mongo.view;

import com.intellij.openapi.project.Project;
import org.codinjutsu.tools.nosql.commons.model.DataType;
import org.codinjutsu.tools.nosql.commons.model.DatabaseContext;
import org.codinjutsu.tools.nosql.commons.view.DatabasePanel;
import org.codinjutsu.tools.nosql.commons.view.columninfo.WriteableColumnInfoDecider;
import org.codinjutsu.tools.nosql.commons.view.panel.NoSQLResultPanel;
import org.codinjutsu.tools.nosql.mongo.model.MongoContext;
import org.codinjutsu.tools.nosql.mongo.view.columninfo.MongoWriteableColumnInfoDecider;
import org.codinjutsu.tools.nosql.mongo.view.model.MongoNodeDescriptorFactory;
import org.codinjutsu.tools.nosql.mongo.view.panel.query.MongoQueryPanel;
import org.jetbrains.annotations.NotNull;

import static org.codinjutsu.tools.nosql.mongo.logic.MongoClient.ID_DESCRIPTOR_KEY;

public class MongoPanel extends DatabasePanel {

    public MongoPanel(Project project, DatabaseContext context) {
        super(project, context, ID_DESCRIPTOR_KEY, MongoQueryPanel::new);
    }

    @Override
    protected NoSQLResultPanel createResultPanel(Project project, String idDescriptor, DataType[] dataTypes) {
        return new NoSQLResultPanel(project, this, true, new MongoNodeDescriptorFactory(), idDescriptor, dataTypes) {
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
