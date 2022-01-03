package org.codinjutsu.tools.nosql.commons.view;

import com.intellij.ide.CommonActionsManager;
import com.intellij.ide.TreeExpander;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.LoadingDecorator;
import com.intellij.openapi.ui.Splitter;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.NumberDocument;
import com.intellij.ui.components.panels.NonOpaquePanel;
import org.apache.commons.lang.StringUtils;
import org.codinjutsu.tools.nosql.commons.model.DataType;
import org.codinjutsu.tools.nosql.commons.model.DatabaseContext;
import org.codinjutsu.tools.nosql.commons.model.SearchResult;
import org.codinjutsu.tools.nosql.commons.model.scheme.SchemeItem;
import org.codinjutsu.tools.nosql.commons.utils.GuiUtils;
import org.codinjutsu.tools.nosql.commons.view.action.AddDocumentAction;
import org.codinjutsu.tools.nosql.commons.view.action.CloseFindEditorAction;
import org.codinjutsu.tools.nosql.commons.view.action.CopyResultAction;
import org.codinjutsu.tools.nosql.commons.view.action.EditDocumentAction;
import org.codinjutsu.tools.nosql.commons.view.action.ExecuteQuery;
import org.codinjutsu.tools.nosql.commons.view.action.ImportAction;
import org.codinjutsu.tools.nosql.commons.view.action.OpenFindAction;
import org.codinjutsu.tools.nosql.commons.view.action.RunScriptAction;
import org.codinjutsu.tools.nosql.commons.view.action.paging.FirstPageAction;
import org.codinjutsu.tools.nosql.commons.view.action.paging.LastPageAction;
import org.codinjutsu.tools.nosql.commons.view.action.paging.NextPageAction;
import org.codinjutsu.tools.nosql.commons.view.action.paging.PreviousPageAction;
import org.codinjutsu.tools.nosql.commons.view.filedialogs.ImportFileDialog;
import org.codinjutsu.tools.nosql.commons.view.filedialogs.ImportResultState;
import org.codinjutsu.tools.nosql.commons.view.panel.ErrorPanel;
import org.codinjutsu.tools.nosql.commons.view.panel.NoSQLResultPanel;
import org.codinjutsu.tools.nosql.commons.view.panel.Pageable;
import org.codinjutsu.tools.nosql.commons.view.panel.query.Page;
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptions;
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryOptionsImpl;
import org.codinjutsu.tools.nosql.commons.view.panel.query.QueryPanel;
import org.codinjutsu.tools.nosql.commons.view.scripting.ScriptExecutor;
import org.codinjutsu.tools.nosql.commons.view.scripting.ScriptingDatabaseWrapper;
import org.jetbrains.annotations.NotNull;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.util.function.Function;

public abstract class DatabasePanel extends JPanel implements Disposable {

    private final LoadingDecorator loadingDecorator;
    private JPanel rootPanel;
    private Splitter splitter;
    private JPanel toolBar;
    private JPanel errorPanel;
    private final JTextField rowLimitField = new JTextField("");
    private final NoSQLResultPanel resultPanel;
    private final QueryPanel queryPanel;

    private final Project project;
    private final DatabaseContext context;

    private Page currentPage = null;

    protected DatabasePanel(Project project, DatabaseContext context, String idDescriptor) {
        this(project, context, idDescriptor, p -> null);
    }

    protected DatabasePanel(Project project, DatabaseContext context, String idDescriptor, Function<Project, QueryPanel> queryPanelFactory) {
        this.project = project;
        this.context = context;

        errorPanel.setLayout(new BorderLayout());

        queryPanel = queryPanelFactory.apply(project);
        if (queryPanel != null) {
            queryPanel.setVisible(false);
        }

        resultPanel = createResultPanel(project, idDescriptor, DataType.values());

        loadingDecorator = new LoadingDecorator(resultPanel, this, 0);

        splitter.setOrientation(true);
        splitter.setProportion(0.2f);
        splitter.setSecondComponent(loadingDecorator.getComponent());

        setLayout(new BorderLayout());
        add(rootPanel);

        initToolBar();
    }

    protected abstract NoSQLResultPanel createResultPanel(Project project, String idDescriptor, DataType[] dataTypes);

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
        DefaultActionGroup actionResultGroup = new DefaultActionGroup("NoSQLResultGroup", true); //NON-NLS
        if (ApplicationManager.getApplication() != null) {
            actionResultGroup.add(new ExecuteQuery(this));
            if (queryPanel != null) {
                actionResultGroup.add(new OpenFindAction(this));
                queryPanel.addActions(actionResultGroup);
            }
            actionResultGroup.addSeparator();
            if (resultPanel.getEditable()) {
                actionResultGroup.add(new AddDocumentAction(resultPanel));
                actionResultGroup.add(new EditDocumentAction(resultPanel));
                actionResultGroup.add(new ImportAction(this));
                actionResultGroup.add(new RunScriptAction(this));
            }

            actionResultGroup.add(new CopyResultAction(resultPanel));

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

    public DatabaseContext getContext() {
        return context;
    }

    public Object getRecords() {
        return null;
    }

    public void showResults() {
        executeQuery();
    }

    public void executeQuery() {
        executeQuery(currentPage);
    }

    private void executeQuery(final Page currentPage) {
        errorPanel.setVisible(false);
        validateQuery();
        ProgressManager.getInstance().run(new Task.Backgroundable(project, "Executing query", true) {
            @Override
            public void run(@NotNull final ProgressIndicator indicator) {
                try {
                    GuiUtils.runInSwingThread(() -> loadingDecorator.startLoading(false));
                    GuiUtils.runInSwingThread(() -> resultPanel.updateResultTableTree(getSearchResult(currentPage)));
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

    protected SearchResult getSearchResult() {
        SearchResult searchResult = getSearchResult(currentPage);
        currentPage = null;
        return searchResult;
    }

    private SearchResult getSearchResult(Page currentPage) {
        return getSearchResult(context, createQueryOptions(currentPage));
    }

    @NotNull
    protected QueryOptions createQueryOptions(Page currentPage) {
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

    @NotNull
    protected SearchResult getSearchResult(DatabaseContext context, QueryOptions queryOptions) {
        return context.getClient().loadRecords(context, queryOptions);
    }

    private void validateQuery() {
        if (queryPanel != null) {
            queryPanel.validateQuery();
        }
    }

    @Override
    public void dispose() {
        resultPanel.dispose();
    }

    public NoSQLResultPanel getResultPanel() {
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

    @SuppressWarnings("unused")
    @NotNull
    public Page getCurrentPage() {
        if (currentPage == null) {
            currentPage = new Page(10, 0, getSearchResult().getTotalCount());
        }
        return currentPage;
    }

    @SuppressWarnings("unused")
    public void moveToPage(@NotNull Page page) {
        executeQuery(page);
        currentPage = page;
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

    public void runScript() {
        VirtualFile chooseFile = FileChooser.chooseFile(new FileChooserDescriptor(true, false, false, false, false, false), project, null);
        if (chooseFile == null) {
            return;
        }
        GuiUtils.runInSwingThread(() -> {
            try {
                String content = new String(chooseFile.contentsToByteArray());
                createScriptExecutor(content, project, context, chooseFile.getExtension()).execute();
            } catch (IOException e) {
                updateErrorPanel(e);
            }
        });
    }

    @NotNull
    private ScriptExecutor createScriptExecutor(String content, Project project, DatabaseContext context, String extension) {
        return new ScriptExecutor(extension, content, project, new ScriptingDatabaseWrapper(context), context);
    }

    SchemeItem getScheme() {
        return context.getClient().getScheme(context);
    }
}
