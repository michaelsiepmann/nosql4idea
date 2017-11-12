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

import com.intellij.ui.SimpleTextAttributes;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.codinjutsu.tools.nosql.commons.style.StyleAttributesProvider;
import org.codinjutsu.tools.nosql.commons.utils.DateUtils;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.AbstractKeyValueDescriptor;
import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class MongoKeyValueDescriptor extends AbstractKeyValueDescriptor {

    public static MongoKeyValueDescriptor createDescriptor(String key, Object value) {
        if (value == null) {
            return new MongoKeyNullValueDescriptor(key);
        }
        if (value instanceof Boolean) {
            return new MongoKeyValueDescriptor(key, value, StyleAttributesProvider.getBooleanAttribute()) {
                @Override
                public void setValue(Object value) {
                    super.setValue(Boolean.valueOf((String) value));
                }
            };
        }
        if (value instanceof Integer) {
            return new MongoKeyValueDescriptor(key, value, StyleAttributesProvider.getNumberAttribute()) {
                @Override
                public void setValue(Object value) {
                    super.setValue(Integer.valueOf((String) value));
                }
            };
        }
        if (value instanceof Double) {
            return new MongoKeyValueDescriptor(key, value, StyleAttributesProvider.getNumberAttribute()) {
                @Override
                public void setValue(Object value) {
                    super.setValue(Double.valueOf((String) value));
                }
            };
        }
        if (value instanceof Long) {
            return new MongoKeyValueDescriptor(key, value, StyleAttributesProvider.getNumberAttribute()) {
                @Override
                public void setValue(Object value) {
                    super.setValue(Long.valueOf((String) value));
                }
            };
        }
        if (value instanceof String) {
            return new MongoKeyStringValueDescriptor(key, (String) value);
        }
        if (value instanceof Date) {
            return new MongoKeyDateValueDescriptor(key, (Date) value);
        }
        if (value instanceof ObjectId) {
            return new MongoKeyValueDescriptor(key, value, StyleAttributesProvider.getObjectIdAttribute());
        }
        if (value instanceof DBObject) {
            return new MongoKeyValueDescriptor(key, value, StyleAttributesProvider.getObjectAttribute());
        }
        return new MongoKeyValueDescriptor(key, value, StyleAttributesProvider.getStringAttribute());
    }

    private MongoKeyValueDescriptor(String key, Object value, SimpleTextAttributes valueTextAttributes) {
        super(key, value, valueTextAttributes);
    }

    private static class MongoKeyNullValueDescriptor extends MongoKeyValueDescriptor {

        private MongoKeyNullValueDescriptor(String key) {
            super(key, null, StyleAttributesProvider.getNullAttribute());
        }

        @Override
        @NotNull
        protected String getValueAndAbbreviateIfNecessary() {
            return "null";
        }
    }

    private static class MongoKeyStringValueDescriptor extends MongoKeyValueDescriptor {

        private static final String STRING_SURROUNDED = "\"%s\"";
        private static final String TO_STRING_FOR_STRING_VALUE_TEMPLATE = "\"%s\" : \"%s\"";

        private MongoKeyStringValueDescriptor(String key, String value) {
            super(key, value, StyleAttributesProvider.getStringAttribute());
        }

        @NotNull
        @Override
        protected String getValueAndAbbreviateIfNecessary() {
            return String.format(STRING_SURROUNDED, getValue());
        }

        @Override
        public String toString() {
            return String.format(TO_STRING_FOR_STRING_VALUE_TEMPLATE, getKey(), getValue());
        }
    }

    private static class MongoKeyDateValueDescriptor extends MongoKeyValueDescriptor {

        private static final DateFormat DATE_FORMAT = DateUtils.utcDateTime(Locale.getDefault());

        private static final String TO_STRING_FOR_DATE_VALUE_TEMPLATE = "\"%s\" : \"%s\"";

        private MongoKeyDateValueDescriptor(String key, Date value) {
            super(key, value, StyleAttributesProvider.getStringAttribute());
        }

        @NotNull
        @Override
        protected String getValueAndAbbreviateIfNecessary() {
            return getFormattedDate();
        }

        @Override
        public String toString() {
            return String.format(TO_STRING_FOR_DATE_VALUE_TEMPLATE, getValue(), getFormattedDate());
        }

        private String getFormattedDate() {
            return DATE_FORMAT.format(getValue());
        }
    }
}
