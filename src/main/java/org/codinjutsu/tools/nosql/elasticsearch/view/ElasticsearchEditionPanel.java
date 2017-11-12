package org.codinjutsu.tools.nosql.elasticsearch.view;

import org.codinjutsu.tools.nosql.commons.view.AbstractEditionPanel;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.NodeDescriptor;
import org.codinjutsu.tools.nosql.elasticsearch.view.nodedescriptor.ElasticsearchKeyValueDescriptor;
import org.codinjutsu.tools.nosql.elasticsearch.view.nodedescriptor.ElasticsearchValueDescriptor;
import org.codinjutsu.tools.nosql.mongo.view.JsonTreeTableView;

import javax.swing.*;
import java.awt.*;

public class ElasticsearchEditionPanel extends AbstractEditionPanel {
    private JPanel mainPanel;
    private JPanel editionPanel;
    private JButton saveButton;
    private JButton cancelButton;
    private JButton deleteButton;

    private JsonTreeTableView editTableView;

    public ElasticsearchEditionPanel() {
        super(new BorderLayout());

        add(mainPanel);
        editionPanel.setLayout(new BorderLayout());

        saveButton.setName("saveButton");
        cancelButton.setName("cancelButton");
        deleteButton.setName("deleteButton");
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
