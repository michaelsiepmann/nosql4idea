package org.codinjutsu.tools.nosql.commons.view;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.ui.PopupHandler;
import com.intellij.util.ui.tree.TreeUtil;
import com.mongodb.DBObject;
import org.apache.commons.lang.StringUtils;
import org.codinjutsu.tools.nosql.commons.view.action.edition.AddKeyAction;
import org.codinjutsu.tools.nosql.commons.view.action.edition.AddValueAction;
import org.codinjutsu.tools.nosql.commons.view.action.edition.DeleteKeyAction;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.AbstractKeyValueDescriptor;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.NodeDescriptor;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.ValueDescriptor;
import org.codinjutsu.tools.nosql.mongo.view.JsonTreeTableView;
import org.codinjutsu.tools.nosql.mongo.view.model.JsonTreeModel;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractEditionPanel extends JPanel implements Disposable {

    protected AbstractEditionPanel(LayoutManager layout) {
        super(layout);
    }

    public boolean containsKey(String key) {
        NoSqlTreeNode parentNode = getParentNode();
        if (parentNode == null) {
            return false;
        }

        Enumeration children = parentNode.children();
        while (children.hasMoreElements()) {
            NoSqlTreeNode childNode = (NoSqlTreeNode) children.nextElement();
            NodeDescriptor descriptor = childNode.getDescriptor();
            if (descriptor instanceof AbstractKeyValueDescriptor) {
                AbstractKeyValueDescriptor keyValueDescriptor = (AbstractKeyValueDescriptor) descriptor;
                if (StringUtils.equals(key, keyValueDescriptor.getKey())) {
                    return true;
                }
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
        return (NoSqlTreeNode) getEditTableView().getTree().getLastSelectedPathComponent();
    }

    public void addKey(String key, Object value) {

        List<TreeNode> node = new LinkedList<>();
        NoSqlTreeNode treeNode = new NoSqlTreeNode(createKeyValueDescriptor(key, value));

        if (value instanceof DBObject) {
            JsonTreeModel.processDbObject(treeNode, (DBObject) value);
        }

        node.add(treeNode);

        NoSqlTreeNode parentNode = getParentNode();
        DefaultTreeModel treeModel = (DefaultTreeModel) getEditTableView().getTree().getModel();
        if (parentNode == null) {
            parentNode = (NoSqlTreeNode) treeModel.getRoot();
        }
        TreeUtil.addChildrenTo(parentNode, node);
        treeModel.reload(parentNode);
    }

    protected void buildPopupMenu() {
        DefaultActionGroup actionPopupGroup = new DefaultActionGroup("AbstractEditorPopupGroup", true);
        if (ApplicationManager.getApplication() != null) {
            actionPopupGroup.add(new AddKeyAction(this));
            actionPopupGroup.add(new AddValueAction(this));
            actionPopupGroup.add(new DeleteKeyAction(this));
        }

        PopupHandler.installPopupHandler(getEditTableView(), actionPopupGroup, "POPUP", ActionManager.getInstance());
    }

    protected abstract NodeDescriptor createKeyValueDescriptor(String key, Object value);

    public void addValue(Object value) {
        List<TreeNode> node = new LinkedList<>();

        NoSqlTreeNode parentNode = getParentNode();

        if (parentNode != null) {
            NoSqlTreeNode treeNode = new NoSqlTreeNode(createValueDescriptor(parentNode.getChildCount(), value));
            if (value instanceof DBObject) {
                JsonTreeModel.processDbObject(treeNode, (DBObject) value);
            }

            node.add(treeNode);

            DefaultTreeModel treeModel = (DefaultTreeModel) getEditTableView().getTree().getModel();
            TreeUtil.addChildrenTo(parentNode, node);
            treeModel.reload(parentNode);
        }
    }

    protected abstract NodeDescriptor createValueDescriptor(int index, Object value);

    public boolean canAddKey() {
        NoSqlTreeNode selectedNode = getSelectedNode();
        return selectedNode != null && selectedNode.getDescriptor() instanceof AbstractKeyValueDescriptor;
    }

    public boolean canAddValue() {
        NoSqlTreeNode selectedNode = getSelectedNode();
        return selectedNode != null && selectedNode.getDescriptor() instanceof ValueDescriptor;
    }

    public void removeSelectedKey() {
        NoSqlTreeNode selectedNode = getSelectedNode();
        if (selectedNode != null) {
            TreeUtil.removeSelected(getEditTableView().getTree());
        }
    }

    protected abstract JsonTreeTableView getEditTableView();
}
