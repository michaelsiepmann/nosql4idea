/*
 * Copyright (c) 2015 David Boissier
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.codinjutsu.tools.nosql.mongo.view;

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
import com.mongodb.DBObject;
import org.codinjutsu.tools.nosql.ServerConfiguration;
import org.codinjutsu.tools.nosql.commons.utils.GuiUtils;
import org.codinjutsu.tools.nosql.commons.view.ErrorPanel;
import org.codinjutsu.tools.nosql.commons.view.NoSQLResultPanelDocumentOperations;
import org.codinjutsu.tools.nosql.commons.view.NoSqlResultView;
import org.codinjutsu.tools.nosql.commons.view.action.CopyResultAction;
import org.codinjutsu.tools.nosql.commons.view.action.EditDocumentAction;
import org.codinjutsu.tools.nosql.commons.view.action.ExecuteQuery;
import org.codinjutsu.tools.nosql.mongo.logic.MongoClient;
import org.codinjutsu.tools.nosql.mongo.model.MongoCollection;
import org.codinjutsu.tools.nosql.mongo.model.MongoResult;
import org.codinjutsu.tools.nosql.mongo.view.action.AddMongoDocumentAction;
import org.codinjutsu.tools.nosql.mongo.view.action.CloseFindEditorAction;
import org.codinjutsu.tools.nosql.mongo.view.action.EnableAggregateAction;
import org.codinjutsu.tools.nosql.mongo.view.action.OpenFindAction;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class MongoPanel extends NoSqlResultView<MongoCollection> {

    private final LoadingDecorator loadingDecorator;
    private JPanel rootPanel;
    private Splitter splitter;
    private JPanel toolBar;
    private JPanel errorPanel;
    private final JTextField rowLimitField = new JTextField("");
    private final MongoResultPanel resultPanel;
    private final QueryPanel queryPanel;

    private final Project project;
    private final MongoClient mongoClient;
    private final ServerConfiguration configuration;
    private final MongoCollection mongoCollection;

    public MongoPanel(Project project, final MongoClient mongoClient, final ServerConfiguration configuration, final MongoCollection mongoCollection) {
        this.project = project;
        this.mongoClient = mongoClient;
        this.mongoCollection = mongoCollection;
        this.configuration = configuration;

        errorPanel.setLayout(new BorderLayout());

        queryPanel = new QueryPanel(project);
        queryPanel.setVisible(false);

        resultPanel = new MongoResultPanel(project, new NoSQLResultPanelDocumentOperations<DBObject>() {

            public DBObject getDocument(Object _id) {
                return mongoClient.findMongoDocument(configuration, mongoCollection, _id);
            }

            public void updateDocument(DBObject document) {
                mongoClient.update(configuration, mongoCollection, document);
                executeQuery();
            }

            public void deleteDocument(Object document) {
                mongoClient.delete(configuration, mongoCollection, document);
                executeQuery();
            }
        });

        loadingDecorator = new LoadingDecorator(resultPanel, this, 0);


        splitter.setOrientation(true);
        splitter.setProportion(0.2f);
        splitter.setSecondComponent(loadingDecorator.getComponent());

        setLayout(new BorderLayout());
        add(rootPanel);

        initToolBar();
    }

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
        actionResultGroup.add(new CloseFindEditorAction(this));

        ActionToolbar actionToolBar = ActionManager.getInstance().createActionToolbar("MongoResultGroupActions", actionResultGroup, true);
        actionToolBar.setLayoutPolicy(ActionToolbar.AUTO_LAYOUT_POLICY);
        JComponent actionToolBarComponent = actionToolBar.getComponent();
        actionToolBarComponent.setBorder(null);
        actionToolBarComponent.setOpaque(false);

        toolBar.add(actionToolBarComponent, BorderLayout.CENTER);
    }

    public MongoCollection getRecords() {
        return mongoCollection;
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

                    final MongoResult mongoResult = mongoClient.loadCollectionValues(configuration, mongoCollection, queryPanel.getQueryOptions(rowLimitField.getText()));
                    GuiUtils.runInSwingThread(() -> resultPanel.updateResultTableTree(mongoResult));
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

    private void validateQuery() {
        queryPanel.validateQuery();
    }

    @Override
    public void dispose() {
        resultPanel.dispose();
    }

    public MongoResultPanel getResultPanel() {
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

}
