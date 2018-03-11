package org.codinjutsu.tools.nosql.redis.view;

import org.codinjutsu.tools.nosql.commons.view.panel.TreePreparator;
import org.jetbrains.annotations.NotNull;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import static org.apache.commons.lang.StringUtils.isNotBlank;

class RedisTreePreparator implements TreePreparator {

    private boolean groupByPrefix;
    private String separator;

    void prepareTable(boolean groupByPrefix, String separator) {
        this.groupByPrefix = groupByPrefix;
        this.separator = separator;
    }

    @NotNull
    @Override
    public TreeNode prepare(@NotNull TreeNode treeNode) {
        if (groupByPrefix && isNotBlank(separator)) {
            return RedisFragmentedKeyTreeModel.wrapNodes((DefaultMutableTreeNode) treeNode, separator);
        }
        return treeNode;

    }
}
