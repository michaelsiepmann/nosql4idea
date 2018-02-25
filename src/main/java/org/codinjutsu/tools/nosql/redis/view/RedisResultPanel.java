package org.codinjutsu.tools.nosql.redis.view;

import com.intellij.openapi.project.Project;
import org.codinjutsu.tools.nosql.commons.model.SearchResult;
import org.codinjutsu.tools.nosql.commons.view.DatabasePanel;
import org.codinjutsu.tools.nosql.commons.view.JsonTreeTableView;
import org.codinjutsu.tools.nosql.commons.view.panel.NoSQLResultPanel;
import org.codinjutsu.tools.nosql.redis.model.RedisSearchResult;
import org.codinjutsu.tools.nosql.redis.view.nodedescriptor.RedisNodeDescriptorFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.tree.DefaultMutableTreeNode;

import static org.apache.commons.lang.StringUtils.isNotBlank;

public class RedisResultPanel extends NoSQLResultPanel {

    private boolean groupByPrefix;
    private String separator;

    RedisResultPanel(@NotNull Project project, DatabasePanel databasePanel) {
        super(project, databasePanel, true, new RedisNodeDescriptorFactory(), "_id"); //NON-NLS
    }

    void prepareTable(boolean groupByPrefix, String separator) {
        this.groupByPrefix = groupByPrefix;
        this.separator = separator;
    }

    @NotNull
    @Override
    protected JsonTreeTableView createTableView(@NotNull SearchResult searchResult) {
        DefaultMutableTreeNode renderedNode = createTreeNode((RedisSearchResult) searchResult);
        return new JsonTreeTableView(renderedNode, JsonTreeTableView.COLUMNS_FOR_READING);
    }

    private DefaultMutableTreeNode createTreeNode(@NotNull RedisSearchResult searchResult) {
        DefaultMutableTreeNode rootNode = RedisTreeModel.buildTree(searchResult);
        if (groupByPrefix && isNotBlank(separator)) {
            return RedisFragmentedKeyTreeModel.wrapNodes(rootNode, separator);
        }
        return rootNode;
    }
}
