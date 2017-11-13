package org.codinjutsu.tools.nosql.elasticsearch.view;

import com.google.gson.JsonObject;
import com.intellij.ide.CommonActionsManager;
import com.intellij.ide.TreeExpander;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.LoadingDecorator;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.NumberDocument;
import com.intellij.ui.components.panels.NonOpaquePanel;
import org.apache.commons.lang.StringUtils;
import org.codinjutsu.tools.nosql.ServerConfiguration;
import org.codinjutsu.tools.nosql.commons.utils.GuiUtils;
import org.codinjutsu.tools.nosql.commons.view.ErrorPanel;
import org.codinjutsu.tools.nosql.commons.view.NoSQLResultPanelDocumentOperations;
import org.codinjutsu.tools.nosql.commons.view.NoSqlResultView;
import org.codinjutsu.tools.nosql.commons.view.action.ExecuteQuery;
import org.codinjutsu.tools.nosql.elasticsearch.logic.ElasticsearchClient;
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchCollection;
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchDatabase;
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchQuery;
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchResult;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class ElasticsearchPanel extends NoSqlResultView<ElasticsearchCollection> {

    private JPanel mainPanel;
    private JPanel containerPanel;
    private JPanel toolBarPanel;
    private JPanel errorPanel;

    private ElasticsearchResultPanel resultPanel;
    private final LoadingDecorator loadingDecorator;
    private final JTextField rowLimitField = new JTextField("");

    private final Project project;

    private final ElasticsearchClient client;
    private final ServerConfiguration serverConfiguration;
    private final ElasticsearchDatabase database;
    private final ElasticsearchCollection collection;

    public ElasticsearchPanel(Project project, ElasticsearchClient client, ServerConfiguration serverConfiguration, ElasticsearchDatabase database, ElasticsearchCollection collection) {
        this.project = project;
        this.client = client;
        this.serverConfiguration = serverConfiguration;
        this.database = database;
        this.collection = collection;
        this.resultPanel = new ElasticsearchResultPanel(project, new NoSQLResultPanelDocumentOperations<JsonObject>() {
            @Override
            public JsonObject getDocument(@NotNull Object _id) {
                return client.findDocument(serverConfiguration, database, collection, _id);
            }

            @Override
            public void deleteDocument(@NotNull Object document) {
                client.delete(serverConfiguration, database, collection, document);
                executeQuery();
            }

            @Override
            public void updateDocument(JsonObject document) {
                client.update(serverConfiguration, database, collection, document);
                executeQuery();
            }
        });

        loadingDecorator = new LoadingDecorator(resultPanel, this, 0);

        containerPanel.add(loadingDecorator.getComponent());

        initToolbar();

        setLayout(new BorderLayout());
        add(mainPanel);
    }

    private void initToolbar() {
        toolBarPanel.setLayout(new BorderLayout());

        rowLimitField.setColumns(5);
        rowLimitField.setDocument(new NumberDocument());
        rowLimitField.setText("100");

        JPanel rowLimitPanel = new NonOpaquePanel();
        rowLimitPanel.add(new JLabel("Row limit:"), BorderLayout.WEST);
        rowLimitPanel.add(rowLimitField, BorderLayout.CENTER);
        rowLimitPanel.add(Box.createHorizontalStrut(5), BorderLayout.EAST);
        toolBarPanel.add(rowLimitPanel, BorderLayout.WEST);

        addCommonsActions();
    }

    private void addCommonsActions() {
        final TreeExpander treeExpander = new TreeExpander() {
            @Override
            public void expandAll() {
                ElasticsearchPanel.this.expandAll();
            }

            @Override
            public boolean canExpand() {
                return true;
            }

            @Override
            public void collapseAll() {
                ElasticsearchPanel.this.collapseAll();
            }

            @Override
            public boolean canCollapse() {
                return true;
            }
        };

        CommonActionsManager actionsManager = CommonActionsManager.getInstance();

        final AnAction expandAllAction = actionsManager.createExpandAllAction(treeExpander, resultPanel);
        final AnAction collapseAllAction = actionsManager.createCollapseAllAction(treeExpander, resultPanel);

        Disposer.register(this, () -> {
            collapseAllAction.unregisterCustomShortcutSet(resultPanel);
            expandAllAction.unregisterCustomShortcutSet(resultPanel);
        });

        DefaultActionGroup actionResultGroup = new DefaultActionGroup("CouchbaseResultGroup", true);
        actionResultGroup.add(new ExecuteQuery<>(this));
        actionResultGroup.addSeparator();
        actionResultGroup.add(expandAllAction);
        actionResultGroup.add(collapseAllAction);

        ActionToolbar actionToolBar = ActionManager.getInstance().createActionToolbar("CouchbaseResultGroupActions", actionResultGroup, true);
        actionToolBar.setLayoutPolicy(ActionToolbar.AUTO_LAYOUT_POLICY);
        JComponent actionToolBarComponent = actionToolBar.getComponent();
        actionToolBarComponent.setBorder(null);
        actionToolBarComponent.setOpaque(false);

        toolBarPanel.add(actionToolBarComponent, BorderLayout.CENTER);
    }

    private void loadAndDisplayResults(int limit) throws Exception {
        ElasticsearchResult elasticsearchResult = client.loadRecords(serverConfiguration, database, new ElasticsearchQuery(limit, collection));
        if (elasticsearchResult.hasErrors()) {
            throw new Exception(StringUtils.join(elasticsearchResult.getErrors(), " "));
        }
        GuiUtils.runInSwingThread(() -> updateResultTableTree(elasticsearchResult));
    }

    private void expandAll() {
        resultPanel.expandAll();
    }

    private void collapseAll() {
        resultPanel.collapseAll();
    }

    @Override
    public void showResults() {
        executeQuery();
    }

    private void updateResultTableTree(ElasticsearchResult elasticsearchResult) {
        resultPanel.updateResultTableTree(elasticsearchResult);
    }

    @Override
    public JPanel getResultPanel() {
        return resultPanel;
    }

    private int getLimit() {
        return Integer.parseInt(rowLimitField.getText());
    }

    @Override
    public void executeQuery() {
        errorPanel.setVisible(false);
        // // TODO: 13.11.2017  validateQuery();
        ProgressManager.getInstance().run(new Task.Backgroundable(project, "Executing query", true) { //TODO need to abstract this method
            @Override
            public void run(@NotNull final ProgressIndicator indicator) {
                try {
                    GuiUtils.runInSwingThread(() -> loadingDecorator.startLoading(false));
                    loadAndDisplayResults(getLimit());
                } catch (final Exception ex) {
                    GuiUtils.runInSwingThread(() -> {
                        errorPanel.invalidate();
                        errorPanel.removeAll();
                        errorPanel.add(new ErrorPanel(ex), BorderLayout.CENTER);
                        errorPanel.validate();
                        errorPanel.setVisible(true);
                    });
                } finally {
                    GuiUtils.runInSwingThread(loadingDecorator::stopLoading);
                }
            }
        });
    }

    @Override
    public ElasticsearchCollection getRecords() {
        return collection;
    }

    @Override
    public void dispose() {

    }
}
