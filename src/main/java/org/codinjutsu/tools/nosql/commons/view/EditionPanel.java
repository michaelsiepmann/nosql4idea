package org.codinjutsu.tools.nosql.commons.view;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.PopupHandler;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.util.ui.tree.TreeUtil;
import org.codinjutsu.tools.nosql.commons.logic.DatabaseClient;
import org.codinjutsu.tools.nosql.commons.model.DatabaseContext;
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement;
import org.codinjutsu.tools.nosql.commons.view.action.edition.AddKeyAction;
import org.codinjutsu.tools.nosql.commons.view.action.edition.AddValueAction;
import org.codinjutsu.tools.nosql.commons.view.action.edition.DeleteKeyAction;
import org.codinjutsu.tools.nosql.commons.view.columninfo.WritableColumnInfo;
import org.codinjutsu.tools.nosql.commons.view.columninfo.WriteableColumnInfoDecider;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.NodeDescriptor;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.NodeDescriptorFactory;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.KeyValueDescriptor;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.value.IndexedValueDescriptor;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import static org.codinjutsu.tools.nosql.commons.view.nodedescriptor.TreeModelFactoryKt.buildDBObject;
import static org.codinjutsu.tools.nosql.commons.view.nodedescriptor.TreeModelFactoryKt.processObject;

public class EditionPanel extends JPanel implements Disposable {

    private JButton saveButton;
    private JButton cancelButton;
    private JPanel editionTreePanel;
    private JPanel mainPanel;
    private JButton deleteButton;

    private JsonTreeTableView editTableView;

    private final NodeDescriptorFactory nodeDescriptorFactory;
    private final WriteableColumnInfoDecider writeableColumnInfoDecider;

    public EditionPanel(NodeDescriptorFactory nodeDescriptorFactory, WriteableColumnInfoDecider writeableColumnInfoDecider) {
        super(new BorderLayout());

        this.nodeDescriptorFactory = nodeDescriptorFactory;
        this.writeableColumnInfoDecider = writeableColumnInfoDecider;

        add(mainPanel);
        editionTreePanel.setLayout(new BorderLayout());

        saveButton.setName("saveButton"); //NON-NLS
        cancelButton.setName("cancelButton"); //NON-NLS
        deleteButton.setName("deleteButton"); //NON-NLS
    }

    public void init(DatabasePanel databasePanel, ActionCallback actionCallback) {
        cancelButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                actionCallback.onOperationCancelled("Modification canceled...");
            }
        });

        saveButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    DatabaseContext context = databasePanel.getContext();
                    DatabaseClient<DatabaseElement> client = (DatabaseClient<DatabaseElement>) context.getClient();
                    client.update(context, buildEditedDocument());
                    databasePanel.executeQuery();
                    actionCallback.onOperationSuccess("Document saved...");
                } catch (Exception exception) {
                    actionCallback.onOperationFailure(exception);
                }
            }
        });

        deleteButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    DatabaseContext context = databasePanel.getContext();
                    DatabaseClient<DatabaseElement> client = (DatabaseClient<DatabaseElement>) context.getClient();
                    client.delete(context, getDocumentId());
                    databasePanel.executeQuery();
                    actionCallback.onOperationSuccess("Document deleted...");
                } catch (Exception exception) {
                    actionCallback.onOperationFailure(exception);
                }
            }
        });
    }

    private Object getDocumentId() {
        NoSqlTreeNode rootNode = (NoSqlTreeNode) editTableView.getTree().getModel().getRoot();
        return findObjectIdNodeDescriptor(rootNode).getDescriptor().getValue();
    }

    private NoSqlTreeNode findObjectIdNodeDescriptor(NoSqlTreeNode rootNode) {
        return ((NoSqlTreeNode) rootNode.getChildAt(0));//TODO crappy
    }

    private DatabaseElement buildEditedDocument() {
        return buildDBObject((NoSqlTreeNode) editTableView.getTree().getModel().getRoot());
    }

    public void updateEditionTree(DatabaseElement document) {
        String panelTitle = "New document";
        if (document != null) {
            panelTitle = "Edition";
        }

        mainPanel.setBorder(IdeBorderFactory.createTitledBorder(panelTitle, true));
        editTableView = new JsonTreeTableView(buildJsonTree(document), JsonTreeTableView.KEY, new WritableColumnInfo(writeableColumnInfoDecider));
        editTableView.setName("editionTreeTable"); //NON-NLS

        editionTreePanel.invalidate();
        editionTreePanel.removeAll();
        editionTreePanel.add(new JBScrollPane(editTableView));
        editionTreePanel.validate();

        buildPopupMenu();
    }

    private TreeNode buildJsonTree(DatabaseElement document) {
        NoSqlTreeNode rootNode = new NoSqlTreeNode(nodeDescriptorFactory.createResultDescriptor(""));//TODO crappy
        processObject(rootNode, document, nodeDescriptorFactory);
        return rootNode;
    }

    public boolean containsKey(String key) {
        NoSqlTreeNode parentNode = getParentNode();
        if (parentNode == null) {
            return false;
        }

        Enumeration<NoSqlTreeNode> children = parentNode.children();
        while (children.hasMoreElements()) {
            NodeDescriptor descriptor = children.nextElement().getDescriptor();
            if (descriptor.isSameKey(key)) {
                return true;
            }
        }
        return false;
    }

    private NoSqlTreeNode getParentNode() {
        NoSqlTreeNode lastPathComponent = getSelectedNode();
        if (lastPathComponent == null) {
            return null;
        }
        return (NoSqlTreeNode) lastPathComponent.getParent();
    }

    public NoSqlTreeNode getSelectedNode() {
        return (NoSqlTreeNode) editTableView.getTree().getLastSelectedPathComponent();
    }

    public void addKey(String key, Object value) {

        List<TreeNode> node = new LinkedList<>();
        NoSqlTreeNode treeNode = new NoSqlTreeNode(nodeDescriptorFactory.createKeyValueDescriptor(key, value));

        processObject(treeNode, value, nodeDescriptorFactory);

        node.add(treeNode);

        NoSqlTreeNode parentNode = getParentNode();
        DefaultTreeModel treeModel = (DefaultTreeModel) editTableView.getTree().getModel();
        if (parentNode == null) {
            parentNode = (NoSqlTreeNode) treeModel.getRoot();
        }
        TreeUtil.addChildrenTo(parentNode, node);
        treeModel.reload(parentNode);
    }

    protected void buildPopupMenu() {
        DefaultActionGroup actionPopupGroup = new DefaultActionGroup("AbstractEditorPopupGroup", true); //NON-NLS
        if (ApplicationManager.getApplication() != null) {
            actionPopupGroup.add(new AddKeyAction(this));
            actionPopupGroup.add(new AddValueAction(this));
            actionPopupGroup.add(new DeleteKeyAction(this));
        }

        PopupHandler.installPopupHandler(editTableView, actionPopupGroup, "POPUP", ActionManager.getInstance()); //NON-NLS
    }

    public void addValue(Object value) {
        List<TreeNode> node = new LinkedList<>();

        NoSqlTreeNode parentNode = getParentNode();

        if (parentNode != null) {
            NoSqlTreeNode treeNode = new NoSqlTreeNode(nodeDescriptorFactory.createIndexValueDescriptor(parentNode.getChildCount(), value));
            processObject(treeNode, value, nodeDescriptorFactory);

            node.add(treeNode);

            DefaultTreeModel treeModel = (DefaultTreeModel) editTableView.getTree().getModel();
            TreeUtil.addChildrenTo(parentNode, node);
            treeModel.reload(parentNode);
        }
    }

    public boolean canAddKey() {
        NoSqlTreeNode selectedNode = getSelectedNode();
        return selectedNode != null && selectedNode.getDescriptor() instanceof KeyValueDescriptor;
    }

    public boolean canAddValue() {
        NoSqlTreeNode selectedNode = getSelectedNode();
        return selectedNode != null && selectedNode.getDescriptor() instanceof IndexedValueDescriptor;
    }

    public void removeSelectedKey() {
        NoSqlTreeNode selectedNode = getSelectedNode();
        if (selectedNode != null) {
            TreeUtil.removeSelected(editTableView.getTree());
        }
    }

    @Override
    public void dispose() {
        editTableView = null;
    }
}
