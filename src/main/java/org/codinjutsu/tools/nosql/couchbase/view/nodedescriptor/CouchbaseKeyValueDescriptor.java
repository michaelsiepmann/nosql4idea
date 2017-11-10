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

package org.codinjutsu.tools.nosql.couchbase.view.nodedescriptor;

import com.intellij.ui.SimpleTextAttributes;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.AbstractKeyValueDescriptor;
import org.jetbrains.annotations.NotNull;

import static org.codinjutsu.tools.nosql.commons.style.StyleAttributesProvider.getNullAttribute;
import static org.codinjutsu.tools.nosql.commons.style.StyleAttributesProvider.getStringAttribute;
import static org.codinjutsu.tools.nosql.commons.utils.SimpleTextAttributesUtilsKt.getSimpleTextAttributes;

public class CouchbaseKeyValueDescriptor extends AbstractKeyValueDescriptor {

    public static CouchbaseKeyValueDescriptor createDescriptor(String key, Object value) {
        if (value == null) {
            return new CouchbaseKeyNullValueDescriptor(key);
        }
        if (value instanceof String) {
            return new CouchbaseKeyStringValueDescriptor(key, (String) value);
        }

        return new CouchbaseKeyValueDescriptor(key, value, getSimpleTextAttributes(value));
    }

    private CouchbaseKeyValueDescriptor(String key, Object value, SimpleTextAttributes valueTextAttributes) {
        super(key, value, valueTextAttributes);
    }

    private static class CouchbaseKeyNullValueDescriptor extends CouchbaseKeyValueDescriptor {

        private CouchbaseKeyNullValueDescriptor(String key) {
            super(key, null, getNullAttribute());
        }

        @NotNull
        @Override
        protected String getValueAndAbbreviateIfNecessary() {
            return "null";
        }
    }

    private static class CouchbaseKeyStringValueDescriptor extends CouchbaseKeyValueDescriptor {

        private static final String STRING_SURROUNDED = "\"%s\"";
        private static final String TO_STRING_FOR_STRING_VALUE_TEMPLATE = "\"%s\" : \"%s\"";

        private CouchbaseKeyStringValueDescriptor(String key, String value) {
            super(key, value, getStringAttribute());
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

}
