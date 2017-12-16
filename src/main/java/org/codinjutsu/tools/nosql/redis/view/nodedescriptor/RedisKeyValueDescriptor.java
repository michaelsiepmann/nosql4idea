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

import com.intellij.icons.AllIcons;
import com.intellij.ui.ColoredTreeCellRenderer;
import com.intellij.ui.SimpleTextAttributes;
import org.codinjutsu.tools.nosql.commons.style.StyleAttributesProvider;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.AbstractKeyValueDescriptor;
import org.codinjutsu.tools.nosql.commons.view.nodedescriptor.NodeDescriptor;
import org.codinjutsu.tools.nosql.redis.RedisUtils;
import org.codinjutsu.tools.nosql.redis.model.RedisKeyType;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.Tuple;

import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RedisKeyValueDescriptor extends AbstractKeyValueDescriptor {

    private final RedisKeyType keyType;
    private final Icon icon;

    public static NodeDescriptor createDescriptor(String key, String value) {
        return createDescriptor(null, key, value);
    }

    public static RedisKeyValueDescriptor createDescriptor(RedisKeyType keyType, String key, Object value) {
        return new RedisKeyValueDescriptor(keyType, key, value, StyleAttributesProvider.getStringAttribute());
    }

    private RedisKeyValueDescriptor(RedisKeyType keyType, String key, Object value, SimpleTextAttributes valueTextAttributes) {
        super(key, value, valueTextAttributes);
        this.keyType = keyType;
        this.icon = findIcon(value);
    }

    private Icon findIcon(Object object) {
        if (object instanceof List) {
            return AllIcons.Json.Property_brackets;
        }
        if (object instanceof Set || object instanceof Map) {
            return AllIcons.Json.Property_braces;
        }
        return null;
    }

    @Override
    public void renderNode(@NotNull ColoredTreeCellRenderer cellRenderer) {
        cellRenderer.setIcon(icon);
        if (keyType != null) {
            cellRenderer.append(keyType.name(), StyleAttributesProvider.getIndexAttribute());
            cellRenderer.append(" ");
        }
        cellRenderer.append(getFormattedKey(), StyleAttributesProvider.getKeyValueAttribute());
    }

    @NotNull
    @Override
    public String getFormattedValue() {
        if (RedisKeyType.ZSET.equals(keyType)) {
            return getValueAndAbbreviateIfNecessary(RedisUtils.stringifySortedSet((Set<Tuple>) getValue()));
        }
        if (RedisKeyType.SET.equals(keyType)) {
            return getValueAndAbbreviateIfNecessary(RedisUtils.stringifySet((Set) getValue()));
        }
        return getValueAndAbbreviateIfNecessary(getValue().toString());
    }

    @Override
    public void setValue(Object value) {
    }

    public RedisKeyType getKeyType() {
        return keyType;
    }
}
