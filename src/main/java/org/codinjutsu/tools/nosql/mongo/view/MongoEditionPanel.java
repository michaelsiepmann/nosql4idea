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

import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.components.JBScrollPane;
import com.mongodb.DBObject;
import org.codinjutsu.tools.nosql.commons.view.AbstractEditionPanel;
import org.codinjutsu.tools.nosql.commons.view.ActionCallback;
import org.codinjutsu.tools.nosql.commons.view.NoSQLResultPanelDocumentOperations;
import org.codinjutsu.tools.nosql.commons.view.NoSqlTreeNode;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.NodeDescriptor;
import org.codinjutsu.tools.nosql.mongo.model.MongoResult;
import org.codinjutsu.tools.nosql.mongo.view.model.MongoTreeModelFactory;
import org.codinjutsu.tools.nosql.mongo.view.nodedescriptor.MongoKeyValueDescriptor;
import org.codinjutsu.tools.nosql.mongo.view.nodedescriptor.MongoResultDescriptor;
import org.codinjutsu.tools.nosql.mongo.view.nodedescriptor.MongoValueDescriptor;

import javax.swing.*;
import javax.swing.tree.TreeNode;
import java.awt.*;

public class MongoEditionPanel extends AbstractEditionPanel<MongoResult, DBObject> {
    private JButton saveButton;
    private JButton cancelButton;
    private JPanel editionTreePanel;
    private JPanel mainPanel;
    private JButton deleteButton;

    private JsonTreeTableView editTableView;

    public MongoEditionPanel() {
        super(new BorderLayout(), new MongoTreeModelFactory());

        add(mainPanel);
        editionTreePanel.setLayout(new BorderLayout());

        saveButton.setName("saveButton");
        cancelButton.setName("cancelButton");
        deleteButton.setName("deleteButton");
    }

    public void init(NoSQLResultPanelDocumentOperations<DBObject> documentOperations, ActionCallback actionCallback) {
        init(documentOperations, actionCallback, cancelButton, saveButton, deleteButton);
    }

    public void updateEditionTree(DBObject mongoDocument) {
        String panelTitle = "New document";
        if (mongoDocument != null) {
            panelTitle = "Edition";
        }

        mainPanel.setBorder(IdeBorderFactory.createTitledBorder(panelTitle, true));
        editTableView = new JsonTreeTableView(buildJsonTree(mongoDocument), JsonTreeTableView.COLUMNS_FOR_WRITING);
        editTableView.setName("editionTreeTable");

        editionTreePanel.invalidate();
        editionTreePanel.removeAll();
        editionTreePanel.add(new JBScrollPane(editTableView));
        editionTreePanel.validate();

        buildPopupMenu();
    }

    private TreeNode buildJsonTree(DBObject mongoObject) {
        NoSqlTreeNode rootNode = new NoSqlTreeNode(new MongoResultDescriptor());//TODO crappy
        new MongoTreeModelFactory().processObject(rootNode, mongoObject);
        return rootNode;
    }

    @Override
    protected NodeDescriptor createKeyValueDescriptor(String key, Object value) {
        return MongoKeyValueDescriptor.createDescriptor(key, value);
    }

    @Override
    protected NodeDescriptor createValueDescriptor(int index, Object value) {
        return MongoValueDescriptor.createDescriptor(index, value);
    }

    @Override
    protected DBObject buildMongoDocument() {
        NoSqlTreeNode rootNode = (NoSqlTreeNode) editTableView.getTree().getModel().getRoot();
        return new MongoTreeModelFactory().buildDBObject(rootNode);
    }

    @Override
    public void dispose() {
        editTableView = null;
    }

    @Override
    protected Object getDocumentId() {
        NoSqlTreeNode rootNode = (NoSqlTreeNode) editTableView.getTree().getModel().getRoot();

        return findObjectIdNodeDescriptor(rootNode).getDescriptor().getValue();
    }

    private NoSqlTreeNode findObjectIdNodeDescriptor(NoSqlTreeNode rootNode) {
        return ((NoSqlTreeNode) rootNode.getChildAt(0));//TODO crappy
    }

    @Override
    protected JsonTreeTableView getEditTableView() {
        return editTableView;
    }
}
