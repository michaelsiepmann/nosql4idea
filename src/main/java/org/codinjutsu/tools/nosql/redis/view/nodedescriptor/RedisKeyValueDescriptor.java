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

package org.codinjutsu.tools.nosql.redis.view.nodedescriptor;

import com.intellij.ui.ColoredTreeCellRenderer;
import com.intellij.ui.SimpleTextAttributes;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.KeyValueDescriptor;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.keyvalue.TypedKeyValueDescriptor;
import org.codinjutsu.tools.nosql.redis.model.RedisKeyType;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.Tuple;

import javax.swing.Icon;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.intellij.icons.AllIcons.Json.Property_braces;
import static com.intellij.icons.AllIcons.Json.Property_brackets;
import static org.codinjutsu.tools.nosql.commons.style.StyleAttributesProvider.getIndexAttribute;
import static org.codinjutsu.tools.nosql.commons.style.StyleAttributesProvider.getStringAttribute;
import static org.codinjutsu.tools.nosql.redis.RedisUtils.stringifySet;
import static org.codinjutsu.tools.nosql.redis.RedisUtils.stringifySortedSet;

public class RedisKeyValueDescriptor extends TypedKeyValueDescriptor<Object> {

    private final RedisKeyType keyType;

    public static KeyValueDescriptor<?> createDescriptor(String key, String value) {
        return createDescriptor(null, key, value);
    }

    public static RedisKeyValueDescriptor   createDescriptor(RedisKeyType keyType, String key, Object value) {
        return new RedisKeyValueDescriptor(keyType, key, value, getStringAttribute(), findIcon(value));
    }

    private static Icon findIcon(Object object) {
        if (object instanceof List) {
            return Property_brackets;
        }
        if (object instanceof Set || object instanceof Map) {
            return Property_braces;
        }
        return null;
    }

    private RedisKeyValueDescriptor(RedisKeyType keyType, String key, Object value, SimpleTextAttributes valueTextAttributes, Icon icon) {
        super(key, value, valueTextAttributes, icon);
        this.keyType = keyType;
    }

    @Override
    public void renderNode(@NotNull ColoredTreeCellRenderer cellRenderer) {
        if (keyType != null) {
            cellRenderer.append(keyType.name(), getIndexAttribute());
            cellRenderer.append(" ");
        }
        super.renderNode(cellRenderer);
    }

    @NotNull
    @Override
    public String getFormattedValue() {
        Object value = getValue();
        if (value == null) {
            return "";
        }
        if (RedisKeyType.ZSET.equals(keyType)) {
            return getValueAndAbbreviateIfNecessary(stringifySortedSet((Set<Tuple>) value));
        }
        if (RedisKeyType.SET.equals(keyType)) {
            return getValueAndAbbreviateIfNecessary(stringifySet((Set) value));
        }
        return super.getFormattedValue();
    }

    @Override
    public void setValue(Object value) {
    }

    public RedisKeyType getKeyType() {
        return keyType;
    }
}
