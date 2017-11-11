package org.codinjutsu.tools.nosql.elasticsearch.view;

import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.treeStructure.treetable.TreeTableTree;
import com.intellij.util.ui.tree.TreeUtil;
import org.apache.commons.lang.StringUtils;
import org.codinjutsu.tools.nosql.ServerConfiguration;
import org.codinjutsu.tools.nosql.commons.view.AbstractSearchPanel;
import org.codinjutsu.tools.nosql.commons.view.NoSqlTreeNode;
import org.codinjutsu.tools.nosql.elasticsearch.logic.ElasticsearchClient;
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchCollection;
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchDatabase;
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchQuery;
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchResult;
import org.codinjutsu.tools.nosql.mongo.view.JsonTreeTableView;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

import static org.codinjutsu.tools.nosql.commons.view.nodedescriptor.TreeModelFactoryKt.buildTree;

public class ElasticsearchPanel extends AbstractSearchPanel<ElasticsearchDatabase> {

    private JPanel mainPanel;
    private JPanel containerPanel;
    private JPanel toolBarPanel;
    private JPanel errorPanel;

    private final ElasticsearchClient client;
    private final ServerConfiguration serverConfiguration;
    private final ElasticsearchDatabase database;
    private final ElasticsearchCollection collection;
    private JsonTreeTableView resultTableView;

    public ElasticsearchPanel(Project project, ElasticsearchClient client, ServerConfiguration serverConfiguration, ElasticsearchDatabase database, ElasticsearchCollection collection) {
        super(project);
        this.client = client;
        this.serverConfiguration = serverConfiguration;
        this.database = database;
        this.collection = collection;
    }

    @Override
    protected void expandAll() {
        TreeUtil.expandAll(resultTableView.getTree());
    }

    @Override
    protected void collapseAll() {
        TreeTableTree tree = resultTableView.getTree();
        TreeUtil.collapseAll(tree, 1);
    }

    @NotNull
    @Override
    protected JPanel getToolBarPanel() {
        return toolBarPanel;
    }

    @NotNull
    @Override
    protected JPanel getErrorPanel() {
        return errorPanel;
    }

    @NotNull
    @Override
    protected JPanel getMainPanel() {
        return mainPanel;
    }

    @Override
    public void showResults() {
        executeQuery();
    }

    @NotNull
    @Override
    protected JPanel getContainerPanel() {
        return containerPanel;
    }

    @Override
    protected void loadAndDisplayResults(int limit) throws Exception {
        ElasticsearchResult elasticsearchResult = client.loadRecords(serverConfiguration, database, new ElasticsearchQuery(limit, collection));
        if (elasticsearchResult.hasErrors()) {
            throw new Exception(StringUtils.join(elasticsearchResult.getErrors(), " "));
        }
        updateResultTableTree(elasticsearchResult);
    }

    private void updateResultTableTree(ElasticsearchResult elasticsearchResult) {
        NoSqlTreeNode rootNode = buildTree(elasticsearchResult, new ElasticsearchTreeModelFactory());
        resultTableView = new JsonTreeTableView(rootNode, JsonTreeTableView.COLUMNS_FOR_READING);
        resultTableView.setName("resultTreeTable");

        JPanel resultPanel = getResultPanel();
        resultPanel.invalidate();
        resultPanel.removeAll();
        resultPanel.add(new JBScrollPane(resultTableView));
        resultPanel.validate();
    }

    @Override
    public ElasticsearchDatabase getRecords() {
        return database;
    }

    @Override
    public void dispose() {

    }
}
