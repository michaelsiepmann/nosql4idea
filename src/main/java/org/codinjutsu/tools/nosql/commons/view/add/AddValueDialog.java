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

import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.ValidationInfo;
import org.codinjutsu.tools.nosql.commons.model.DataType;
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement;
import org.codinjutsu.tools.nosql.commons.view.EditionPanel;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.BorderLayout;

public class AddValueDialog extends AbstractAddDialog {

    private final DataType[] dataTypes;
    private ComboBox<DataType> typeCombobox;
    private JPanel valuePanel;
    private JPanel mainPanel;

    private AddValueDialog(EditionPanel editionPanel, DataType[] dataTypes) {
        super(editionPanel);
        valuePanel.setLayout(new BorderLayout());
        typeCombobox.setName("valueType"); //NON-NLS
        typeCombobox.requestFocus();
        this.dataTypes = dataTypes;
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return mainPanel;
    }

    public static AddValueDialog createDialog(EditionPanel parentPanel, DataType[] values) {
        AddValueDialog dialog = new AddValueDialog(parentPanel, values);
        dialog.init();
        dialog.setTitle("Add A Value");
        return dialog;
    }

    @Override
    protected void init() {
        super.init();

        initTypeComboBox(typeCombobox, valuePanel, dataTypes);
    }

    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        try {
            currentEditor.validate();
        } catch (Exception ex) {
            return new ValidationInfo(ex.getMessage());
        }

        return null;
    }

    @Override
    public DatabaseElement getValue() {
        return currentEditor.getValue();
    }

    @Nullable
    @Override
    public JComponent getPreferredFocusedComponent() {
        return typeCombobox;
    }
}