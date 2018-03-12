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

package org.codinjutsu.tools.nosql.commons.model;

import com.intellij.openapi.project.Project;
import org.codinjutsu.tools.nosql.commons.view.add.BooleanFieldWrapper;
import org.codinjutsu.tools.nosql.commons.view.add.DateTimeFieldWrapper;
import org.codinjutsu.tools.nosql.commons.view.add.JsonFieldArrayWrapper;
import org.codinjutsu.tools.nosql.commons.view.add.JsonFieldObjectWrapper;
import org.codinjutsu.tools.nosql.commons.view.add.NullFieldWrapper;
import org.codinjutsu.tools.nosql.commons.view.add.NumberFieldWrapper;
import org.codinjutsu.tools.nosql.commons.view.add.StringFieldWrapper;
import org.codinjutsu.tools.nosql.commons.view.add.TextFieldWrapper;

import static org.codinjutsu.tools.nosql.i18n.ResourcesLoaderKt.getResourceString;

public enum DataType {

    STRING(getResourceString("datatype.string.name")) {
        @Override
        public TextFieldWrapper createTextFieldWrapper(Project project) {
            return new StringFieldWrapper();
        }
    },
    NUMBER(getResourceString("datatype.number.name")) {
        @Override
        public TextFieldWrapper createTextFieldWrapper(Project project) {
            return new NumberFieldWrapper();
        }
    },
    BOOLEAN(getResourceString("datatype.boolean.name")) {
        @Override
        public TextFieldWrapper createTextFieldWrapper(Project project) {
            return new BooleanFieldWrapper();
        }
    },
    ARRAY(getResourceString("datatype.array.name")) {
        @Override
        public TextFieldWrapper createTextFieldWrapper(Project project) {
            return new JsonFieldArrayWrapper(project);
        }
    },
    OBJECT(getResourceString("datatype.object.name")) {
        @Override
        public TextFieldWrapper createTextFieldWrapper(Project project) {
            return new JsonFieldObjectWrapper(project);
        }
    },
    NULL(getResourceString("datatype.null.name")) {
        @Override
        public TextFieldWrapper createTextFieldWrapper(Project project) {
            return new NullFieldWrapper();
        }
    },
    DATE(getResourceString("datatype.date.name")) {
        @Override
        public TextFieldWrapper createTextFieldWrapper(Project project) {
            return new DateTimeFieldWrapper();
        }
    };

    private final String type;

    DataType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public abstract TextFieldWrapper createTextFieldWrapper(Project project);
}
