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

package org.codinjutsu.tools.nosql.commons.view.add;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.ColoredListCellRenderer;
import org.codinjutsu.tools.nosql.commons.model.DataType;
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement;
import org.codinjutsu.tools.nosql.commons.view.EditionPanel;
import org.jetbrains.annotations.NotNull;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import java.awt.BorderLayout;

public abstract class AbstractAddDialog extends DialogWrapper {

    final EditionPanel editionPanel;
    TextFieldWrapper currentEditor = null;

    AbstractAddDialog(EditionPanel editionPanel) {
        super(editionPanel, true);
        this.editionPanel = editionPanel;
    }

    void initTypeComboBox(final JComboBox<DataType> combobox, final JPanel parentPanel, DataType[] dataTypes) {
        combobox.setModel(new DefaultComboBoxModel<>(dataTypes));
        combobox.setRenderer(new ColoredListCellRenderer<DataType>() {

            @Override
            protected void customizeCellRenderer(@NotNull JList jList, DataType o, int i, boolean b, boolean b2) {
                append(o.getType());
            }
        });

        combobox.setSelectedItem(null);
        combobox.addItemListener(itemEvent -> {
            DataType selectedType = (DataType) combobox.getSelectedItem();
            if (selectedType != null) {
                currentEditor = selectedType.getTextFieldWrapper();
                currentEditor.reset();

                parentPanel.invalidate();
                parentPanel.removeAll();
                JComponent component = currentEditor.getComponent();
                component.setName("valueEditor"); //NON-NLS
                parentPanel.add(component, BorderLayout.CENTER);
                parentPanel.validate();
            }
        });

        combobox.setSelectedItem(DataType.STRING);
    }

    public abstract DatabaseElement getValue();
}
