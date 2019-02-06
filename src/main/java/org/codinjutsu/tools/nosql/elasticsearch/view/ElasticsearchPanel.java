package org.codinjutsu.tools.nosql.elasticsearch.view;

import com.intellij.openapi.project.Project;
import org.codinjutsu.tools.nosql.commons.model.DatabaseContext;
import org.codinjutsu.tools.nosql.commons.view.SimpleDatabasePanel;
import org.codinjutsu.tools.nosql.commons.view.panel.Pageable;
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchContext;
import org.codinjutsu.tools.nosql.elasticsearch.view.panel.query.ElasticsearchQueryPanel;

public class ElasticsearchPanel extends SimpleDatabasePanel implements Pageable {

    public ElasticsearchPanel(Project project, DatabaseContext context) {
        super(project, context, "_id", ElasticsearchQueryPanel::new); //NON-NLS
    }

    @Override
    public Object getRecords() {
        return ((ElasticsearchContext) getContext()).getType();
    }
}
