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

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.ConfigurationTypeUtil;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunConfigurationModule;
import com.intellij.openapi.project.Project;
import org.codinjutsu.tools.nosql.commons.utils.GuiUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.Icon;

public class MongoRunConfigurationType implements ConfigurationType {
    private final MongoFactory myConfigurationFactory;

    public MongoRunConfigurationType() {
        myConfigurationFactory = new MongoFactory(this);
    }

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
        return GuiUtils.loadIcon("mongodb.png"); //NON-NLS
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

    public static MongoRunConfigurationType getInstance() {
        return ConfigurationTypeUtil.findConfigurationType(MongoRunConfigurationType.class);
    }

    private static class MongoFactory extends ConfigurationFactory {
        MongoFactory(ConfigurationType type) {
            super(type);
        }

        @Override
        @NotNull
        public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
            return new MongoRunConfiguration(new RunConfigurationModule(project), this);
        }
    }
}
