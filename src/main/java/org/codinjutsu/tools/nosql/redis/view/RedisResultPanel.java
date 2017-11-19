package org.codinjutsu.tools.nosql.redis.view;

import com.intellij.openapi.project.Project;
import org.apache.commons.lang.StringUtils;
import org.codinjutsu.tools.nosql.commons.view.EditionPanel;
import org.codinjutsu.tools.nosql.commons.view.JsonTreeTableView;
import org.codinjutsu.tools.nosql.commons.view.NoSQLResultPanelDocumentOperations;
import org.codinjutsu.tools.nosql.commons.view.panel.AbstractNoSQLResultPanel;
import org.codinjutsu.tools.nosql.redis.model.RedisResult;
import org.codinjutsu.tools.nosql.redis.view.nodedescriptor.RedisTreeModelFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.tree.DefaultMutableTreeNode;

public class RedisResultPanel extends AbstractNoSQLResultPanel<RedisResult, Object> {

    private boolean groupByPrefix;
    private String separator;

    public RedisResultPanel(@NotNull Project project, @NotNull NoSQLResultPanelDocumentOperations<Object> documentOperations) {
        super(project, documentOperations, new RedisTreeModelFactory());
    }

    @Nullable
    @Override
    protected EditionPanel<Object> createEditionPanel() {
        return null;
    }

    public void prepareTable(boolean groupByPrefix, String separator) {
        this.groupByPrefix = groupByPrefix;
        this.separator = separator;
    }

    @NotNull
    @Override
    protected JsonTreeTableView createTableView(@NotNull RedisResult searchResult) {
        DefaultMutableTreeNode rootNode = RedisTreeModel.buildTree(searchResult);
        DefaultMutableTreeNode renderedNode = rootNode;
        if (groupByPrefix && StringUtils.isNotBlank(separator)) {
            renderedNode = RedisFragmentedKeyTreeModel.wrapNodes(rootNode, separator);
        }
        return new JsonTreeTableView(renderedNode, JsonTreeTableView.COLUMNS_FOR_READING);
    }
}
