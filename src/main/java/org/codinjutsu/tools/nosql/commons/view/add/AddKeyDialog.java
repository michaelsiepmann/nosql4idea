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

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.CollectionComboBoxModel;
import org.codinjutsu.tools.nosql.commons.model.DataType;
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement;
import org.codinjutsu.tools.nosql.commons.model.scheme.SchemeItem;
import org.codinjutsu.tools.nosql.commons.utils.GuiUtils;
import org.codinjutsu.tools.nosql.commons.view.EditionPanel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.codinjutsu.tools.nosql.i18n.ResourcesLoaderKt.getResourceString;

public class AddKeyDialog extends AbstractAddDialog {

    private final DataType[] dataTypes;
    private ComboBox<DataType> typeCombobox;
    private JPanel valuePanel;
    private JPanel mainPanel;
    private JPanel nameTextfieldPanel;
    private final JComponent nameTextfield;

    private AddKeyDialog(EditionPanel editionPanel, List<String> selectionPath, DataType[] dataTypes) {
        super(editionPanel);
        mainPanel.setPreferredSize(GuiUtils.enlargeWidth(mainPanel.getPreferredSize(), 1.5d));
        valuePanel.setLayout(new BorderLayout());
        SchemeItem keysFromScheme = editionPanel.getScheme().findItem(selectionPath);
        nameTextfield = keysFromScheme == null || keysFromScheme.getHasChildren() ? new JTextField() : createComboBox(keysFromScheme.getChildren());
        nameTextfieldPanel.setLayout(new BorderLayout());
        nameTextfieldPanel.add(nameTextfield);
        nameTextfield.setName("keyName"); //NON-NLS
        typeCombobox.setName("valueType"); //NON-NLS
        this.dataTypes = dataTypes;
    }

    @NotNull
    private ComboBox<SchemeItem> createComboBox(Collection<SchemeItem> schemes) {
        ComboBox<SchemeItem> comboBox = new ComboBox<>();
        comboBox.setEditable(true);
        comboBox.setModel(new CollectionComboBoxModel<>(new ArrayList<>(schemes)));
        comboBox.setSelectedIndex(-1);
        comboBox.addItemListener(event -> {
            int index = comboBox.getSelectedIndex();
            if (index >= 0) {
                SchemeItem item = (SchemeItem) comboBox.getEditor().getItem();
                for (int i = 0; i < typeCombobox.getItemCount(); i++) {
                    if (typeCombobox.getItemAt(i) == item.getType()) {
                        typeCombobox.setSelectedIndex(i);
                        ApplicationManager.getApplication().invokeLater(() -> currentEditor.getComponent().requestFocusInWindow());
                    }
                }
            }
        });
        return comboBox;
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return mainPanel;
    }

    public static AddKeyDialog createDialog(EditionPanel parentPanel, List<String> selectionPath, DataType[] dataTypes) {
        AddKeyDialog dialog = new AddKeyDialog(parentPanel, selectionPath, dataTypes);
        dialog.init();
        dialog.setTitle(getResourceString("edition.dialog.addkey.title"));
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
        String keyName = getKey();
        if (isBlank(keyName)) {
            return new ValidationInfo(getResourceString("edition.dialog.addkey.validation.nokeyname"));
        }

        if (editionPanel.containsKey(keyName)) {
            return new ValidationInfo(getResourceString("edition.dialog.addkey.validation.keyexists", keyName));
        }

        try {
            currentEditor.validate();
        } catch (Exception ex) {
            return new ValidationInfo(ex.getMessage());
        }

        return null;
    }

    @Nullable
    @Override
    public JComponent getPreferredFocusedComponent() {
        return nameTextfield;
    }

    public String getKey() {
        if (nameTextfield instanceof JTextComponent) {
            return ((JTextComponent) nameTextfield).getText();
        }
        ComboBox<SchemeItem> nameTextfield = (ComboBox<SchemeItem>) this.nameTextfield;
        return nameTextfield.getEditor().getItem().toString();
    }

    @Override
    public DatabaseElement getValue() {
        return currentEditor.getValue();
    }
}