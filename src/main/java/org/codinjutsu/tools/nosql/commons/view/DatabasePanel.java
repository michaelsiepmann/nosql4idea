package org.codinjutsu.tools.nosql.commons.view;

import com.intellij.ide.CommonActionsManager;
import com.intellij.ide.TreeExpander;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.LoadingDecorator;
import com.intellij.openapi.ui.Splitter;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.NumberDocument;
import com.intellij.ui.components.panels.NonOpaquePanel;
import org.codinjutsu.tools.nosql.ServerConfiguration;
import org.codinjutsu.tools.nosql.commons.logic.DatabaseClient;
import org.codinjutsu.tools.nosql.commons.model.NoSQLCollection;
import org.codinjutsu.tools.nosql.commons.model.SearchResult;
import org.codinjutsu.tools.nosql.commons.utils.GuiUtils;
import org.codinjutsu.tools.nosql.commons.view.action.CopyResultAction;
import org.codinjutsu.tools.nosql.commons.view.action.EditDocumentAction;
import org.codinjutsu.tools.nosql.commons.view.action.ExecuteQuery;
import org.codinjutsu.tools.nosql.commons.view.panel.AbstractNoSQLResultPanel;
import org.codinjutsu.tools.nosql.commons.view.panel.ErrorPanel;
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptions;
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryPanel;
import org.codinjutsu.tools.nosql.mongo.view.action.AddMongoDocumentAction;
import org.codinjutsu.tools.nosql.mongo.view.action.EnableAggregateAction;
import org.codinjutsu.tools.nosql.mongo.view.action.OpenFindAction;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public abstract class DatabasePanel<CLIENT extends DatabaseClient, COLLECTION extends NoSQLCollection, RESULT extends SearchResult, DOCUMENT> extends NoSqlResultView<COLLECTION> {

    private final LoadingDecorator loadingDecorator;
    private JPanel rootPanel;
    private Splitter splitter;
    private JPanel toolBar;
    private JPanel errorPanel;
    private final JTextField rowLimitField = new JTextField("");
    private AbstractNoSQLResultPanel<RESULT, DOCUMENT> resultPanel;
    private final QueryPanel queryPanel;

    private final Project project;
    private final CLIENT client;
    private final ServerConfiguration configuration;
    private final COLLECTION collection;

    protected DatabasePanel(Project project, CLIENT client, ServerConfiguration configuration, COLLECTION collection) {
        this.project = project;
        this.client = client;
        this.configuration = configuration;
        this.collection = collection;

        errorPanel.setLayout(new BorderLayout());

        queryPanel = new QueryPanel(project);
        queryPanel.setVisible(false);

        resultPanel = createResultPanel(project, client, configuration, collection);

        loadingDecorator = new LoadingDecorator(resultPanel, this, 0);

        splitter.setOrientation(true);
        splitter.setProportion(0.2f);
        splitter.setSecondComponent(loadingDecorator.getComponent());

        setLayout(new BorderLayout());
        add(rootPanel);

        initToolBar();
    }

    protected abstract AbstractNoSQLResultPanel<RESULT, DOCUMENT> createResultPanel(Project project, CLIENT client, ServerConfiguration configuration, COLLECTION collection);

    private void initToolBar() {
        toolBar.setLayout(new BorderLayout());

        rowLimitField.setColumns(5);
        rowLimitField.setDocument(new NumberDocument());

        JPanel rowLimitPanel = new NonOpaquePanel();
        rowLimitPanel.add(new JLabel("Row limit:"), BorderLayout.WEST);
        rowLimitPanel.add(rowLimitField, BorderLayout.CENTER);
        rowLimitPanel.add(Box.createHorizontalStrut(5), BorderLayout.EAST);
        toolBar.add(rowLimitPanel, BorderLayout.WEST);

        installResultPanelActions();
    }

    private void installResultPanelActions() {
        DefaultActionGroup actionResultGroup = new DefaultActionGroup("MongoResultGroup", true);
        if (ApplicationManager.getApplication() != null) {
            actionResultGroup.add(new ExecuteQuery<>(this));
            actionResultGroup.add(new OpenFindAction(this));
            actionResultGroup.add(new EnableAggregateAction(queryPanel));
            actionResultGroup.addSeparator();
            actionResultGroup.add(new AddMongoDocumentAction(resultPanel));
            actionResultGroup.add(new EditDocumentAction<>(resultPanel));
            actionResultGroup.add(new CopyResultAction<>(resultPanel));
        }
        final TreeExpander treeExpander = new TreeExpander() {
            @Override
            public void expandAll() {
                resultPanel.expandAll();
            }

            @Override
            public boolean canExpand() {
                return true;
            }

            @Override
            public void collapseAll() {
                resultPanel.collapseAll();
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

        actionResultGroup.addSeparator();
        actionResultGroup.add(expandAllAction);
        actionResultGroup.add(collapseAllAction);
        // // TODO: 17.11.2017  actionResultGroup.add(new CloseFindEditorAction(this));

        ActionToolbar actionToolBar = ActionManager.getInstance().createActionToolbar("MongoResultGroupActions", actionResultGroup, true);
        actionToolBar.setLayoutPolicy(ActionToolbar.AUTO_LAYOUT_POLICY);
        JComponent actionToolBarComponent = actionToolBar.getComponent();
        actionToolBarComponent.setBorder(null);
        actionToolBarComponent.setOpaque(false);

        toolBar.add(actionToolBarComponent, BorderLayout.CENTER);
    }

    @Override
    public COLLECTION getRecords() {
        return collection;
    }

    public void showResults() {
        executeQuery();
    }

    public void executeQuery() {
        errorPanel.setVisible(false);
        validateQuery();
        ProgressManager.getInstance().run(new Task.Backgroundable(project, "Executing query", true) {
            @Override
            public void run(@NotNull final ProgressIndicator indicator) {
                try {
                    GuiUtils.runInSwingThread(() -> loadingDecorator.startLoading(false));
                    GuiUtils.runInSwingThread(() -> {
                        RESULT searchResult = getSearchResult(client, configuration, collection, queryPanel.getQueryOptions(rowLimitField.getText()));
                        resultPanel.updateResultTableTree(searchResult);
                    });
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

    protected abstract RESULT getSearchResult(CLIENT client, ServerConfiguration configuration, COLLECTION collection, QueryOptions queryOptions);

    private void validateQuery() {
        queryPanel.validateQuery();
    }

    @Override
    public void dispose() {
        resultPanel.dispose();
    }

    public AbstractNoSQLResultPanel<RESULT, DOCUMENT> getResultPanel() {
        return resultPanel;
    }

    public void openFindEditor() {
        queryPanel.setVisible(true);
        splitter.setFirstComponent(queryPanel);
        GuiUtils.runInSwingThread(this::focusOnEditor);
    }

    public void closeFindEditor() {
        splitter.setFirstComponent(null);
        queryPanel.setVisible(false);
    }

    public void focusOnEditor() {
        queryPanel.requestFocusOnEditor();
    }

    public boolean isFindEditorOpened() {
        return splitter.getFirstComponent() == queryPanel;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
