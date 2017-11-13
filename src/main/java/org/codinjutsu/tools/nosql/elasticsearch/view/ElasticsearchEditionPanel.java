package org.codinjutsu.tools.nosql.elasticsearch.view;

import com.google.gson.JsonObject;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.components.JBScrollPane;
import org.codinjutsu.tools.nosql.commons.view.AbstractEditionPanel;
import org.codinjutsu.tools.nosql.commons.view.ActionCallback;
import org.codinjutsu.tools.nosql.commons.view.NoSQLResultPanelDocumentOperations;
import org.codinjutsu.tools.nosql.commons.view.NoSqlTreeNode;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.NodeDescriptor;
import org.codinjutsu.tools.nosql.elasticsearch.model.ElasticsearchResult;
import org.codinjutsu.tools.nosql.elasticsearch.view.nodedescriptor.ElasticsearchKeyValueDescriptor;
import org.codinjutsu.tools.nosql.elasticsearch.view.nodedescriptor.ElasticsearchResultDescriptor;
import org.codinjutsu.tools.nosql.elasticsearch.view.nodedescriptor.ElasticsearchValueDescriptor;
import org.codinjutsu.tools.nosql.mongo.view.JsonTreeTableView;

import javax.swing.*;
import javax.swing.tree.TreeNode;
import java.awt.*;

public class ElasticsearchEditionPanel extends AbstractEditionPanel<ElasticsearchResult, JsonObject> {
    private JPanel mainPanel;
    private JPanel editionPanel;
    private JButton saveButton;
    private JButton cancelButton;
    private JButton deleteButton;

    private JsonTreeTableView editTableView;

    public ElasticsearchEditionPanel() {
        super(new BorderLayout(), new ElasticsearchTreeModelFactory());

        add(mainPanel);
        editionPanel.setLayout(new BorderLayout());

        saveButton.setName("saveButton");
        cancelButton.setName("cancelButton");
        deleteButton.setName("deleteButton");
    }

    public void init(NoSQLResultPanelDocumentOperations<JsonObject> documentOperations, ActionCallback actionCallback) {
        init(documentOperations, actionCallback, cancelButton, saveButton, deleteButton);
    }

    @Override
    public void updateEditionTree(JsonObject jsonObject) {
        String panelTitle = "New document";
        if (jsonObject != null) {
            panelTitle = "Edition";
        }

        mainPanel.setBorder(IdeBorderFactory.createTitledBorder(panelTitle, true));
        editTableView = new JsonTreeTableView(buildJsonTree(jsonObject), JsonTreeTableView.COLUMNS_FOR_WRITING);
        editTableView.setName("editionTreeTable");

        editionPanel.invalidate();
        editionPanel.removeAll();
        editionPanel.add(new JBScrollPane(editTableView));
        editionPanel.validate();

        buildPopupMenu();
    }

    private TreeNode buildJsonTree(JsonObject jsonObject) {
        NoSqlTreeNode rootNode = new NoSqlTreeNode(new ElasticsearchResultDescriptor(""));//TODO collectionName
        new ElasticsearchTreeModelFactory().processObject(rootNode, jsonObject);
        return rootNode;
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
    protected JsonObject buildMongoDocument() {
        NoSqlTreeNode rootNode = (NoSqlTreeNode) editTableView.getTree().getModel().getRoot();
        return new ElasticsearchTreeModelFactory().buildDBObject(rootNode);
    }

    @Override
    public void dispose() {
        editTableView = null;
    }

    @Override
    protected JsonTreeTableView getEditTableView() {
        return editTableView;
    }

    @Override
    protected NodeDescriptor createKeyValueDescriptor(String key, Object value) {
        return ElasticsearchKeyValueDescriptor.Companion.createDescriptor(key, value);
    }

    @Override
    protected NodeDescriptor createValueDescriptor(int index, Object value) {
        return ElasticsearchValueDescriptor.Companion.createDescriptor(index, value);
    }
}
