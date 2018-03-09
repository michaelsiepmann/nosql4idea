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

    STRING(getResourceString("datatype.string.name"), new StringFieldWrapper()),
    NUMBER(getResourceString("datatype.number.name"), new NumberFieldWrapper()),
    BOOLEAN(getResourceString("datatype.boolean.name"), new BooleanFieldWrapper()),
    ARRAY(getResourceString("datatype.array.name"), new JsonFieldArrayWrapper()),
    OBJECT(getResourceString("datatype.object.name"), new JsonFieldObjectWrapper()),
    NULL(getResourceString("datatype.null.name"), new NullFieldWrapper()),
    DATE(getResourceString("datatype.date.name"), new DateTimeFieldWrapper());

    private final String type;
    private final TextFieldWrapper textFieldWrapper;

    DataType(String type, TextFieldWrapper textFieldWrapper) {
        this.type = type;
        this.textFieldWrapper = textFieldWrapper;
    }

    public String getType() {
        return type;
    }

    public TextFieldWrapper getTextFieldWrapper() {
        return textFieldWrapper;
    }
}
