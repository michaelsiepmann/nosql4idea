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

import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.actions.LazyRunConfigurationProducer;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public class MongoScriptRunConfigurationProducer extends LazyRunConfigurationProducer<MongoRunConfiguration> implements Cloneable {

    public MongoScriptRunConfigurationProducer() {
    }

    @Override
    protected boolean setupConfigurationFromContext(@NotNull MongoRunConfiguration configuration, @NotNull ConfigurationContext context, @NotNull Ref<PsiElement> sourceElement) {
        PsiElement element = sourceElement.get();
        PsiFile sourceFile = element.getContainingFile();
        if (sourceFile == null || !sourceFile.getFileType().getName().toLowerCase().contains("javascript")) { //NON-NLS
            return false;
        }
        VirtualFile file = sourceFile.getVirtualFile();
        configuration.setName(file.getName());
        configuration.setScriptPath(file.getPath());
        Module module = ModuleUtil.findModuleForPsiElement(element);
        if (module != null) {
            configuration.setModule(module);
        }
        return true;
    }

    @Override
    public boolean isConfigurationFromContext(@NotNull MongoRunConfiguration configuration, @NotNull ConfigurationContext context) {
        return configuration.getType() == MongoRunConfigurationType.getInstance();
    }

    @NotNull
    @Override
    public ConfigurationFactory getConfigurationFactory() {
        return new MongoFactory(MongoRunConfigurationType.getInstance());
    }
}
