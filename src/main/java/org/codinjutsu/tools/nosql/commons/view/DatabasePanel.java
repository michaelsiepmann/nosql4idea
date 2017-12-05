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
import org.apache.commons.lang.StringUtils;
import org.codinjutsu.tools.nosql.ServerConfiguration;
import org.codinjutsu.tools.nosql.commons.logic.DatabaseClient;
import org.codinjutsu.tools.nosql.commons.model.SearchResult;
import org.codinjutsu.tools.nosql.commons.utils.GuiUtils;
import org.codinjutsu.tools.nosql.commons.view.action.AddDocumentAction;
import org.codinjutsu.tools.nosql.commons.view.action.CloseFindEditorAction;
import org.codinjutsu.tools.nosql.commons.view.action.CopyResultAction;
import org.codinjutsu.tools.nosql.commons.view.action.EditDocumentAction;
import org.codinjutsu.tools.nosql.commons.view.action.ExecuteQuery;
import org.codinjutsu.tools.nosql.commons.view.action.ImportAction;
import org.codinjutsu.tools.nosql.commons.view.action.OpenFindAction;
import org.codinjutsu.tools.nosql.commons.view.action.paging.FirstPageAction;
import org.codinjutsu.tools.nosql.commons.view.action.paging.LastPageAction;
import org.codinjutsu.tools.nosql.commons.view.action.paging.NextPageAction;
import org.codinjutsu.tools.nosql.commons.view.action.paging.PreviousPageAction;
import org.codinjutsu.tools.nosql.commons.view.filedialogs.ImportFileDialog;
import org.codinjutsu.tools.nosql.commons.view.filedialogs.ImportResultState;
import org.codinjutsu.tools.nosql.commons.view.panel.AbstractNoSQLResultPanel;
import org.codinjutsu.tools.nosql.commons.view.panel.ErrorPanel;
import org.codinjutsu.tools.nosql.commons.view.panel.Pageable;
import org.codinjutsu.tools.nosql.commons.view.panel.query.Page;
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptions;
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptionsImpl;
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryPanel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.function.Function;

public abstract class DatabasePanel<SERVERCONFIGURATION extends ServerConfiguration, CLIENT extends DatabaseClient, CONTEXT extends DatabaseContext<CLIENT, SERVERCONFIGURATION>, RESULT extends SearchResult, DOCUMENT> extends NoSqlResultView {

    private final LoadingDecorator loadingDecorator;
    private JPanel rootPanel;
    private Splitter splitter;
    private JPanel toolBar;
    private JPanel errorPanel;
    private final JTextField rowLimitField = new JTextField("");
    private AbstractNoSQLResultPanel<RESULT, DOCUMENT> resultPanel;
    private final QueryPanel queryPanel;

    private final Project project;
    private final CONTEXT context;

    private Page currentPage = null;

    protected DatabasePanel(Project project, CONTEXT context) {
        this(project, context, p -> null);
    }

    protected DatabasePanel(Project project, CONTEXT context, Function<Project, QueryPanel> queryPanelFactory) {
        this.project = project;
        this.context = context;

        errorPanel.setLayout(new BorderLayout());

        queryPanel = queryPanelFactory.apply(project);
        if (queryPanel != null) {
            queryPanel.setVisible(false);
        }

        resultPanel = createResultPanel(project, context);

        loadingDecorator = new LoadingDecorator(resultPanel, this, 0);

        splitter.setOrientation(true);
        splitter.setProportion(0.2f);
        splitter.setSecondComponent(loadingDecorator.getComponent());

        setLayout(new BorderLayout());
        add(rootPanel);

        initToolBar();
    }

    protected abstract AbstractNoSQLResultPanel<RESULT, DOCUMENT> createResultPanel(Project project, CONTEXT context);

    protected JPanel initToolBar() {
        toolBar.setLayout(new BorderLayout());

        rowLimitField.setColumns(5);
        rowLimitField.setDocument(new NumberDocument());

        JPanel rowLimitPanel = new NonOpaquePanel();
        rowLimitPanel.add(new JLabel("Row limit:"), BorderLayout.WEST);
        rowLimitPanel.add(rowLimitField, BorderLayout.CENTER);
        rowLimitPanel.add(Box.createHorizontalStrut(5), BorderLayout.EAST);
        toolBar.add(rowLimitPanel, BorderLayout.WEST);

        installResultPanelActions();
        return toolBar;
    }

    private void installResultPanelActions() {
        DefaultActionGroup actionResultGroup = new DefaultActionGroup("NoSQLResultGroup", true);
        if (ApplicationManager.getApplication() != null) {
            actionResultGroup.add(new ExecuteQuery(this));
            if (queryPanel != null) {
                actionResultGroup.add(new OpenFindAction(this));
                queryPanel.addActions(actionResultGroup);
            }
            actionResultGroup.addSeparator();
            if (resultPanel.isEditable()) {
                actionResultGroup.add(new AddDocumentAction(resultPanel));
                actionResultGroup.add(new EditDocumentAction<>(resultPanel));
                actionResultGroup.add(new ImportAction(this));
            }

            actionResultGroup.add(new CopyResultAction<>(resultPanel));

            if (this instanceof Pageable) {
                Pageable pageable = (Pageable) this;
                actionResultGroup.addSeparator();
                actionResultGroup.add(new FirstPageAction(pageable));
                actionResultGroup.add(new PreviousPageAction(pageable));
                actionResultGroup.add(new NextPageAction(pageable));
                actionResultGroup.add(new LastPageAction(pageable));
            }
        }

        final TreeExpander treeExpander = new TreeExpander() {
            @Override
            public void expandAll() {
                DatabasePanel.this.expandAll();
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

        addActions(actionResultGroup, expandAllAction, collapseAllAction);

        ActionToolbar actionToolBar = ActionManager.getInstance().createActionToolbar("NoSQLResultGroupActions", actionResultGroup, true);
        actionToolBar.setLayoutPolicy(ActionToolbar.AUTO_LAYOUT_POLICY);
        JComponent actionToolBarComponent = actionToolBar.getComponent();
        actionToolBarComponent.setBorder(null);
        actionToolBarComponent.setOpaque(false);

        toolBar.add(actionToolBarComponent, BorderLayout.CENTER);
    }

    protected void addActions(DefaultActionGroup actionResultGroup, AnAction expandAllAction, AnAction collapseAllAction) {
        actionResultGroup.addSeparator();
        actionResultGroup.add(expandAllAction);
        actionResultGroup.add(collapseAllAction);
        actionResultGroup.add(new CloseFindEditorAction(this));
    }

    public void expandAll() {
        resultPanel.expandAll();
    }

    public void collapseAll() {
        resultPanel.collapseAll();
    }

    protected CONTEXT getContext() {
        return context;
    }

    @Override
    public Object getRecords() {
        return null;
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
                    GuiUtils.runInSwingThread(() -> resultPanel.updateResultTableTree(getSearchResult()));
                } catch (final Exception ex) {
                    GuiUtils.runInSwingThread(() -> updateErrorPanel(ex));
                } finally {
                    GuiUtils.runInSwingThread(loadingDecorator::stopLoading);
                }
            }
        });
    }

    private void updateErrorPanel(Exception ex) {
        errorPanel.invalidate();
        errorPanel.removeAll();
        errorPanel.add(new ErrorPanel(ex), BorderLayout.CENTER);
        errorPanel.validate();
        errorPanel.setVisible(true);
    }

    protected RESULT getSearchResult() {
        QueryOptions queryOptions = createQueryOptions();
        currentPage = null;
        return getSearchResult(context, queryOptions);
    }

    @NotNull
    protected QueryOptions createQueryOptions() {
        String rowLimitFieldText = rowLimitField.getText();
        if (queryPanel == null) {
            QueryOptionsImpl queryOptions = new QueryOptionsImpl();
            queryOptions.setPage(currentPage);
            if (StringUtils.isNotEmpty(rowLimitFieldText)) {
                queryOptions.setResultLimit(Integer.parseInt(rowLimitFieldText));
            }
            return queryOptions;
        }
        return queryPanel.getQueryOptions(rowLimitFieldText, currentPage);
    }

    protected abstract RESULT getSearchResult(CONTEXT context, QueryOptions queryOptions);

    private void validateQuery() {
        if (queryPanel != null) {
            queryPanel.validateQuery();
        }
    }

    @Override
    public void dispose() {
        resultPanel.dispose();
    }

    @Override
    public AbstractNoSQLResultPanel<RESULT, DOCUMENT> getResultPanel() {
        return resultPanel;
    }

    public void openFindEditor() {
        queryPanel.setVisible(true);
        splitter.setFirstComponent(queryPanel.getComponent());
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

    @NotNull
    public Page getCurrentPage() {
        if (currentPage == null) {
            currentPage = new Page(10, 0, getSearchResult().getCount());
        }
        return currentPage;
    }

    public void moveToPage(@NotNull Page page) {

    }

    public void startImport() {
        ImportFileDialog dialog = new ImportFileDialog(context.getImportPanelSettings(), project);
        if (dialog.showAndGet()) {
            File file = dialog.getSelectedFile();
            if (file.exists()) {
                try {
                    GuiUtils.runInSwingThread(() -> loadingDecorator.startLoading(false));
                    GuiUtils.runInSwingThread(() -> {
                        ImportResultState result = context.getClient().importFile(context, file);
                    });
                } catch (Exception e) {
                    updateErrorPanel(e);
                } finally {
                    GuiUtils.runInSwingThread(loadingDecorator::stopLoading);
                }
            }
        }
    }
}
