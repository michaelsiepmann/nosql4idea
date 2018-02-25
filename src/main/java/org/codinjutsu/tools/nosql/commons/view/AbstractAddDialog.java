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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.components.JBCheckBox;
import org.codinjutsu.tools.nosql.commons.model.internal.layer.DatabaseElement;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.internal.InternalDatabasePrimitive;
import org.codinjutsu.tools.nosql.commons.view.table.DateTimePicker;
import org.codinjutsu.tools.nosql.mongo.model.JsonDataType;
import org.jetbrains.annotations.NotNull;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.codinjutsu.tools.nosql.commons.model.internal.json.JsonHelperKt.convert;
import static org.codinjutsu.tools.nosql.commons.utils.StringUtilsKt.parseNumber;

public abstract class AbstractAddDialog extends DialogWrapper {
    private static final Map<JsonDataType, TextFieldWrapper> UI_COMPONENT_BY_JSON_DATATYPE = new HashMap<>();

    static {
        UI_COMPONENT_BY_JSON_DATATYPE.put(JsonDataType.STRING, new StringFieldWrapper());
        UI_COMPONENT_BY_JSON_DATATYPE.put(JsonDataType.BOOLEAN, new BooleanFieldWrapper());
        UI_COMPONENT_BY_JSON_DATATYPE.put(JsonDataType.NUMBER, new NumberFieldWrapper());
        UI_COMPONENT_BY_JSON_DATATYPE.put(JsonDataType.NULL, new NullFieldWrapper());
        UI_COMPONENT_BY_JSON_DATATYPE.put(JsonDataType.DATE, new DateTimeFieldWrapper());
        UI_COMPONENT_BY_JSON_DATATYPE.put(JsonDataType.OBJECT, new JsonFieldObjectWrapper());
        UI_COMPONENT_BY_JSON_DATATYPE.put(JsonDataType.ARRAY, new JsonFieldArrayWrapper());
    }

    final EditionPanel editionPanel;
    TextFieldWrapper currentEditor = null;

    AbstractAddDialog(EditionPanel editionPanel) {
        super(editionPanel, true);
        this.editionPanel = editionPanel;
    }

    void initCombo(final JComboBox<JsonDataType> combobox, final JPanel parentPanel) {
        combobox.setModel(new DefaultComboBoxModel<>(JsonDataType.values()));
        combobox.setRenderer(new ColoredListCellRenderer<JsonDataType>() {

            @Override
            protected void customizeCellRenderer(JList jList, JsonDataType o, int i, boolean b, boolean b2) {
                append(o.type);
            }
        });

        combobox.setSelectedItem(null);
        combobox.addItemListener(itemEvent -> {
            JsonDataType selectedType = (JsonDataType) combobox.getSelectedItem();
            currentEditor = UI_COMPONENT_BY_JSON_DATATYPE.get(selectedType);
            currentEditor.reset();

            parentPanel.invalidate();
            parentPanel.removeAll();
            JComponent component = currentEditor.getComponent();
            component.setName("valueEditor");
            parentPanel.add(component, BorderLayout.CENTER);
            parentPanel.validate();
        });

        combobox.setSelectedItem(JsonDataType.STRING);
    }

    public abstract Object getValue();

    public static abstract class TextFieldWrapper<T extends JComponent> {

        protected final T component;

        private TextFieldWrapper(T component) {
            this.component = component;
        }

        public abstract DatabaseElement getValue();

        public abstract void reset();

        public boolean isValueSet() {
            return true;
        }

        public T getComponent() {
            return component;
        }

        public void validate() {
            if (!isValueSet()) {
                throw new IllegalArgumentException("Value is not set");
            }
        }
    }

    public static abstract class JTextFieldWrapper extends TextFieldWrapper<JTextField> {

        private JTextFieldWrapper() {
            super(new JTextField());
        }

        @Override
        public boolean isValueSet() {
            return isNotBlank(component.getText());
        }

        @Override
        public void reset() {
            component.setText("");
        }
    }

    private static class StringFieldWrapper extends JTextFieldWrapper {

        @Override
        public DatabaseElement getValue() {
            return new InternalDatabasePrimitive(component.getText());
        }
    }

    private static class JsonFieldArrayWrapper extends JTextFieldWrapper {

        @Override
        public DatabaseElement getValue() {
            return convert(new Gson().fromJson(component.getText(), JsonArray.class));
        }
    }

    private static class JsonFieldObjectWrapper extends JTextFieldWrapper {

        @Override
        public DatabaseElement getValue() {
            return convert(new Gson().fromJson(component.getText(), JsonObject.class));
        }
    }

    private static class NumberFieldWrapper extends JTextFieldWrapper {

        @Override
        public DatabaseElement getValue() {
            return new InternalDatabasePrimitive(parseValue());
        }

        @NotNull
        private Number parseValue() {
            return parseNumber(component.getText());
        }

        @Override
        public void validate() {
            super.validate();
            parseValue();
        }
    }

    private static class BooleanFieldWrapper extends TextFieldWrapper<JBCheckBox> {

        private BooleanFieldWrapper() {
            super(new JBCheckBox());
        }

        @Override
        public DatabaseElement getValue() {
            return new InternalDatabasePrimitive(component.isSelected());
        }

        @Override
        public void reset() {
            component.setSelected(false);
        }
    }

    private static class NullFieldWrapper extends TextFieldWrapper<JLabel> {

        private NullFieldWrapper() {
            super(new JLabel("null"));
        }

        @Override
        public DatabaseElement getValue() {
            return null;
        }

        @Override
        public void reset() {
        }
    }

    private static class DateTimeFieldWrapper extends TextFieldWrapper<DateTimePicker> {

        private DateTimeFieldWrapper() {
            super(DateTimePicker.create());
            component.getEditor().setEditable(false);
        }

        @Override
        public DatabaseElement getValue() {
            return new InternalDatabasePrimitive(component.getDate());
        }

        @Override
        public boolean isValueSet() {
            return component.getDate() != null;
        }

        @Override
        public void reset() {
            component.setDate(GregorianCalendar.getInstance().getTime());
        }
    }
}
