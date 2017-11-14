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

package org.codinjutsu.tools.nosql.mongo.view.nodedescriptor;

import com.intellij.ui.ColoredTableCellRenderer;
import com.intellij.ui.ColoredTreeCellRenderer;
import com.intellij.ui.SimpleTextAttributes;
import com.mongodb.DBObject;
import org.codinjutsu.tools.nosql.commons.style.StyleAttributesProvider;
import org.codinjutsu.tools.nosql.commons.utils.DateUtils;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.AbstractNodeDecriptor;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.ValueDescriptor;
import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class MongoValueDescriptor extends AbstractNodeDecriptor implements ValueDescriptor {

    private final int index;
    protected Object value;
    private final SimpleTextAttributes valueTextAttributes;

    @NotNull
    public static MongoValueDescriptor createDescriptor(int index, Object value) {
        if (value == null) {
            return new MongoNullValueDescriptor(index);
        }

        if (value instanceof String) {
            return new MongoStringValueDescriptor(index, (String) value);
        } else if (value instanceof Boolean) {
            return new MongoValueDescriptor(index, value, StyleAttributesProvider.getBooleanAttribute()) {
                @Override
                public void setValue(Object value) {
                    this.value = Boolean.parseBoolean((String) value);
                }
            };
        } else if (value instanceof Number) {
            return new MongoValueDescriptor(index, value, StyleAttributesProvider.getNumberAttribute()) {
                @Override
                public void setValue(Object value) {
                    this.value = Integer.parseInt((String) value);
                }
            };
        } else if (value instanceof Date) {
            return new MongoDateValueDescriptor(index, (Date) value);
        } else if (value instanceof DBObject) {
            return new MongoValueDescriptor(index, value, StyleAttributesProvider.getObjectAttribute());
        } else {
            return new MongoValueDescriptor(index, value, StyleAttributesProvider.getStringAttribute());
        }
    }

    private MongoValueDescriptor(int index, Object value, SimpleTextAttributes valueTextAttributes) {
        this.index = index;
        this.value = value;
        this.valueTextAttributes = valueTextAttributes;
    }

    @Override
    public void renderValue(ColoredTableCellRenderer cellRenderer, boolean isNodeExpanded) {
        if (!isNodeExpanded) {
            cellRenderer.append(getFormattedValue(), valueTextAttributes);
        }
    }

    @Override
    public void renderNode(ColoredTreeCellRenderer cellRenderer) {
        cellRenderer.append(getFormattedKey(), StyleAttributesProvider.getIndexAttribute());
    }

    @Override
    public String getFormattedKey() {
        return String.format("[%s]", index);
    }

    @Override
    public String getFormattedValue() {
        return String.format("%s", getValueAndAbbreviateIfNecessary());
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    private static class MongoStringValueDescriptor extends MongoValueDescriptor {

        private MongoStringValueDescriptor(int index, String value) {
            super(index, value, StyleAttributesProvider.getStringAttribute());
        }

        @Override
        public String getFormattedValue() {
            return String.format("\"%s\"", getValueAndAbbreviateIfNecessary());
        }
    }

    private static class MongoNullValueDescriptor extends MongoValueDescriptor {

        private MongoNullValueDescriptor(int index) {
            super(index, null, StyleAttributesProvider.getNullAttribute());
        }

        @Override
        public String getFormattedValue() {
            return "null";
        }

        @Override
        public String toString() {
            return "null";
        }
    }

    private static class MongoDateValueDescriptor extends MongoValueDescriptor {

        private static final DateFormat DATE_FORMAT = DateUtils.utcDateTime(Locale.getDefault());

        private static final String TO_STRING_FOR_DATE_VALUE_TEMPLATE = "\"%s\"";

        private MongoDateValueDescriptor(int index, Date value) {
            super(index, value, StyleAttributesProvider.getStringAttribute());
        }

        @NotNull
        @Override
        protected String getValueAndAbbreviateIfNecessary() {
            return getFormattedDate();
        }

        @Override
        public String toString() {
            return String.format(TO_STRING_FOR_DATE_VALUE_TEMPLATE, getFormattedDate());
        }

        private String getFormattedDate() {
            return DATE_FORMAT.format(value);
        }
    }
}
