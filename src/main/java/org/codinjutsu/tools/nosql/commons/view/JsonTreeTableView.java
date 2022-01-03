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

package org.codinjutsu.tools.nosql.commons.view;

import com.intellij.ui.TreeTableSpeedSearch;
import com.intellij.ui.treeStructure.treetable.ListTreeTableModelOnColumns;
import com.intellij.ui.treeStructure.treetable.TreeTable;
import com.intellij.ui.treeStructure.treetable.TreeTableModel;
import com.intellij.ui.treeStructure.treetable.TreeTableTree;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.UIUtil;
import com.intellij.util.ui.tree.TreeUtil;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.NodeDescriptor;
import org.codinjutsu.tools.nosql.commons.view.renderer.KeyCellRenderer;
import org.codinjutsu.tools.nosql.commons.view.renderer.ValueCellRenderer;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class JsonTreeTableView extends TreeTable {

    public static final ColumnInfo KEY = new ColumnInfo("Key") {

        public Object valueOf(Object obj) {
            NoSqlTreeNode node = (NoSqlTreeNode) obj;
            return node.getDescriptor();
        }

        @Override
        public Class getColumnClass() {
            return TreeTableModel.class;
        }

        @Override
        public boolean isCellEditable(Object o) {
            return false;
        }
    };

    public static final ColumnInfo READONLY_VALUE = new ReadOnlyValueColumnInfo();

    public static final ColumnInfo[] COLUMNS_FOR_READING = new ColumnInfo[]{KEY, READONLY_VALUE};

    private final ColumnInfo[] columns;

    public JsonTreeTableView(TreeNode rootNode, ColumnInfo... columnInfos) {
        super(new ListTreeTableModelOnColumns(rootNode, columnInfos));
        columns = columnInfos;

        final TreeTableTree tree = getTree();

        tree.setShowsRootHandles(true);
        tree.setRootVisible(false);
        setTreeCellRenderer(new KeyCellRenderer());

        TreeUtil.expand(tree, 2);

        new TreeTableSpeedSearch(this, path -> {
            final NoSqlTreeNode node = (NoSqlTreeNode) path.getLastPathComponent();
            NodeDescriptor descriptor = node.getDescriptor();
            return descriptor.getFormattedKey();
        });
    }

    @Override
    public TableCellRenderer getCellRenderer(int row, int column) {
        TreePath treePath = getTree().getPathForRow(row);
        if (treePath == null) return super.getCellRenderer(row, column);

        NoSqlTreeNode node = (NoSqlTreeNode) treePath.getLastPathComponent();

        TableCellRenderer renderer = columns[column].getRenderer(node);
        return renderer == null ? super.getCellRenderer(row, column) : renderer;
    }

    @Override
    public TableCellEditor getCellEditor(int row, int column) {
        TreePath treePath = getTree().getPathForRow(row);
        if (treePath == null) return super.getCellEditor(row, column);

        NoSqlTreeNode node = (NoSqlTreeNode) treePath.getLastPathComponent();
        TableCellEditor editor = columns[column].getEditor(node);
        return editor == null ? super.getCellEditor(row, column) : editor;
    }

    private static class ReadOnlyValueColumnInfo extends ColumnInfo<NoSqlTreeNode, NodeDescriptor> {
        private final TableCellRenderer myRenderer = new ValueCellRenderer();

        ReadOnlyValueColumnInfo() {
            super("Value");
        }

        public NodeDescriptor valueOf(NoSqlTreeNode treeNode) {
            return treeNode.getDescriptor();
        }

        @Override
        public TableCellRenderer getRenderer(NoSqlTreeNode o) {
            return myRenderer;
        }

        @Override
        public boolean isCellEditable(NoSqlTreeNode o) {
            return false;
        }
    }

}