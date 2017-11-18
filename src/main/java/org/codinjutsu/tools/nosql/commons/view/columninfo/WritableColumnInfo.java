package org.codinjutsu.tools.nosql.commons.view.columninfo;

import com.intellij.util.ui.ColumnInfo;
import org.codinjutsu.tools.nosql.commons.view.NoSqlTreeNode;
import org.codinjutsu.tools.nosql.commons.view.renderer.ValueCellRenderer;
import org.codinjutsu.tools.nosql.commons.view.table.CellEditor;
import org.codinjutsu.tools.nosql.commons.view.table.NoSQLDatePickerCellEditor;
import org.jetbrains.annotations.Nullable;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.util.Date;

public class WritableColumnInfo extends ColumnInfo<NoSqlTreeNode, Object> {

    private final TableCellRenderer myRenderer = new ValueCellRenderer();
    private final TableCellEditor defaultEditor = new CellEditor();
    private final WriteableColumnInfoDecider writeableColumnInfoDecider;

    public WritableColumnInfo(WriteableColumnInfoDecider writeableColumnInfoDecider) {
        super("Value");
        this.writeableColumnInfoDecider = writeableColumnInfoDecider;
    }

    @Override
    public TableCellRenderer getRenderer(NoSqlTreeNode o) {
        return myRenderer;
    }

    @Override
    public boolean isCellEditable(NoSqlTreeNode treeNode) {
        return writeableColumnInfoDecider.isNodeWriteable(treeNode);
    }

    @Nullable
    @Override
    public TableCellEditor getEditor(final NoSqlTreeNode treeNode) {
        Object value = treeNode.getDescriptor().getValue();
        if (value instanceof Date) {
            return buildDateCellEditor(treeNode);
        }
        return defaultEditor;
    }

    private static NoSQLDatePickerCellEditor buildDateCellEditor(final NoSqlTreeNode treeNode) {
        final NoSQLDatePickerCellEditor dateEditor = new NoSQLDatePickerCellEditor();

//  Note from dev: Quite ugly because when clicking on the button to open popup calendar, stopCellEdition is invoked.
//                 From that point, impossible to set the selected data in the node description
        dateEditor.addActionListener(actionEvent -> treeNode.getDescriptor().setValue(dateEditor.getCellEditorValue()));
        return dateEditor;
    }

    public Object valueOf(NoSqlTreeNode treeNode) {
        return treeNode.getDescriptor().getValue();

    }

    @Override
    public void setValue(NoSqlTreeNode treeNode, Object value) {
        treeNode.getDescriptor().setValue(value);
    }
}
