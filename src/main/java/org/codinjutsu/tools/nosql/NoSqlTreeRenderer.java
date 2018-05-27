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

package org.codinjutsu.tools.nosql;

import com.intellij.icons.AllIcons;
import com.intellij.ui.ColoredTreeCellRenderer;
import org.codinjutsu.tools.nosql.commons.model.explorer.TreeCellItem;
import org.codinjutsu.tools.nosql.commons.utils.GuiUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

public class NoSqlTreeRenderer extends ColoredTreeCellRenderer {

    public static final Icon DATABASE = GuiUtils.loadIcon("database.png"); //NON-NLS
    public static final Icon ICON_COLLECTION = AllIcons.Nodes.Folder;
    public static final Icon ICON_PINNED = AllIcons.General.Pin_tab;

    @Override
    public void customizeCellRenderer(@NotNull JTree mongoTree, Object value, boolean isSelected, boolean isExpanded, boolean isLeaf, int row, boolean focus) {
        Object object = ((DefaultMutableTreeNode) value).getUserObject();
        if ((object instanceof TreeCellItem)) {
            ((TreeCellItem) object).updateTreeCell(this);
        }
    }
}
