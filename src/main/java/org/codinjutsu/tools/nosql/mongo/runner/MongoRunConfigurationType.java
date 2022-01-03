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

package org.codinjutsu.tools.nosql.mongo.runner;

import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.ConfigurationTypeUtil;
import org.codinjutsu.tools.nosql.mongo.view.editor.MongoObjectFile;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.Icon;

public class MongoRunConfigurationType implements ConfigurationType {
    private final MongoFactory myConfigurationFactory;

    public MongoRunConfigurationType() {
        myConfigurationFactory = new MongoFactory(this);
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "Mongo";
    }

    @Override
    public String getConfigurationTypeDescription() {
        return "Mongo configuration";
    }

    @Override
    public Icon getIcon() {
        return MongoObjectFile.Companion.getIcon();
    }

    @Override
    @NonNls
    @NotNull
    public String getId() {
        return "MongoRunConfiguration";
    }

    @Override
    public MongoFactory[] getConfigurationFactories() {
        return new MongoFactory[]{myConfigurationFactory};
    }

    @NotNull
    public static MongoRunConfigurationType getInstance() {
        return ConfigurationTypeUtil.findConfigurationType(MongoRunConfigurationType.class);
    }
}
