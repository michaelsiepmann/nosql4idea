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

package org.codinjutsu.tools.nosql.couchbase.view;

import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.treeStructure.treetable.TreeTableTree;
import com.intellij.util.ui.tree.TreeUtil;
import org.apache.commons.lang.StringUtils;
import org.codinjutsu.tools.nosql.ServerConfiguration;
import org.codinjutsu.tools.nosql.commons.view.AbstractSearchPanel;
import org.codinjutsu.tools.nosql.commons.view.NoSqlTreeNode;
import org.codinjutsu.tools.nosql.couchbase.logic.CouchbaseClient;
import org.codinjutsu.tools.nosql.couchbase.model.CouchbaseDatabase;
import org.codinjutsu.tools.nosql.couchbase.model.CouchbaseQuery;
import org.codinjutsu.tools.nosql.couchbase.model.CouchbaseResult;
import org.codinjutsu.tools.nosql.mongo.view.JsonTreeTableView;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

import static org.codinjutsu.tools.nosql.commons.view.nodedescriptor.TreeModelFactoryKt.buildTree;

public class CouchbasePanel extends AbstractSearchPanel<CouchbaseDatabase> {
    private JPanel mainPanel;
    private JPanel toolBarPanel;
    private JPanel containerPanel;
    private JPanel errorPanel;

    private final CouchbaseClient couchbaseClient;
    private final ServerConfiguration configuration;
    private final CouchbaseDatabase database;
    private JsonTreeTableView resultTableView;


    public CouchbasePanel(Project project, CouchbaseClient couchbaseClient, ServerConfiguration configuration, CouchbaseDatabase database) {
        super(project);
        this.couchbaseClient = couchbaseClient;
        this.configuration = configuration;
        this.database = database;
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

    @NotNull
    @Override
    protected JPanel getContainerPanel() {
        return containerPanel;
    }

    @Override
    protected void loadAndDisplayResults(final int limit) throws Exception {
        CouchbaseResult couchbaseResult = couchbaseClient.loadRecords(configuration, database, new CouchbaseQuery(limit));
        if (couchbaseResult.hasErrors()) {
            throw new Exception(StringUtils.join(couchbaseResult.getErrors(), " ")); //TODO need to improve it
        }
        updateResultTableTree(couchbaseResult);
    }

    void updateResultTableTree(CouchbaseResult couchbaseResult) {
        NoSqlTreeNode rootNode = buildTree(couchbaseResult, new CouchbaseTreeModelFactory());
        resultTableView = new JsonTreeTableView(rootNode, JsonTreeTableView.COLUMNS_FOR_READING);
        resultTableView.setName("resultTreeTable");

        JPanel resultPanel = getResultPanel();
        resultPanel.invalidate();
        resultPanel.removeAll();
        resultPanel.add(new JBScrollPane(resultTableView));
        resultPanel.validate();
    }

    @Override
    public void expandAll() {
        TreeUtil.expandAll(resultTableView.getTree());
    }

    @Override
    public void collapseAll() {
        TreeTableTree tree = resultTableView.getTree();
        TreeUtil.collapseAll(tree, 1);
    }

    @Override
    public void showResults() {
        executeQuery();
    }

    @Override
    public CouchbaseDatabase getRecords() {
        return database;
    }

    @Override
    public void dispose() {

    }
}
