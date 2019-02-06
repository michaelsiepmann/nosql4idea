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

import com.intellij.execution.CantRunException;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ModuleBasedConfiguration;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunConfigurationModule;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.configurations.RuntimeConfigurationError;
import com.intellij.execution.configurations.RuntimeConfigurationException;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.components.PathMacroManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizer;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.codinjutsu.tools.nosql.NoSqlConfiguration;
import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration;
import org.codinjutsu.tools.nosql.mongo.configuration.MongoServerConfiguration;
import org.codinjutsu.tools.nosql.mongo.model.MongoDatabase;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

import static java.util.Arrays.asList;
import static org.codinjutsu.tools.nosql.DatabaseVendor.MONGO;

class MongoRunConfiguration extends ModuleBasedConfiguration<RunConfigurationModule, RunProfileState> {

    private final String mongoShell;
    private String scriptPath;
    private String shellParameters;
    private ServerConfiguration serverConfiguration;
    private MongoDatabase database;
    private String shellWorkingDir;

    MongoRunConfiguration(RunConfigurationModule runConfigurationModule, ConfigurationFactory factory) {
        super("Mongo Script", runConfigurationModule, factory);
        mongoShell = NoSqlConfiguration.getInstance(getProject()).getShellPath(MONGO);
    }

    @NotNull
    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new MongoRunConfigurationEditor(getProject());
    }

    @Override
    public Collection<Module> getValidModules() {
        return asList(ModuleManager.getInstance(getProject()).getModules());
    }

    @Override
    public void readExternal(Element element) throws InvalidDataException {
        PathMacroManager.getInstance(getProject()).expandPaths(element);
        super.readExternal(element);
        scriptPath = JDOMExternalizer.readString(element, "path");
        shellParameters = JDOMExternalizer.readString(element, "shellParams");
//        serverConfiguration = JDOMExternalizer.readBoolean(element, "serverConfiguration");
    }

    @Override
    public void writeExternal(Element element) throws WriteExternalException {
        super.writeExternal(element);
        JDOMExternalizer.write(element, "path", scriptPath);
        JDOMExternalizer.write(element, "shellParams", shellParameters);
//        JDOMExternalizer.write(element, "serverConfiguration", serverConfiguration);

        PathMacroManager.getInstance(getProject()).collapsePathsRecursively(element);
    }

    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment env) throws ExecutionException {
        final VirtualFile script = getScriptPath();
        if (script == null) {
            throw new CantRunException("Cannot find script " + scriptPath);
        }

        final MongoCommandLineState state = new MongoCommandLineState(this, env);
        state.setConsoleBuilder(TextConsoleBuilderFactory.getInstance().createBuilder(getProject()));
        return state;
    }

    @Override
    public void checkConfiguration() throws RuntimeConfigurationException {
        if (mongoShell == null) {
            throw new RuntimeConfigurationError("Mongo shell path is not set.");
        }

        if (scriptPath == null) {
            throw new RuntimeConfigurationError("Script path is not set.");
        }

        if (serverConfiguration == null) {
            throw new RuntimeConfigurationError("Server is not set.");
        }

        if (database == null) {
            throw new RuntimeConfigurationError("Database is not set.");
        }
    }

    public VirtualFile getScriptPath() {
        if (scriptPath == null) return null;
        return LocalFileSystem.getInstance().findFileByPath(FileUtil.toSystemIndependentName(scriptPath));
    }

    public void setScriptPath(String scriptPath) {
        this.scriptPath = scriptPath;
    }

    public ServerConfiguration getServerConfiguration() {
        return serverConfiguration;
    }

    public void setServerConfiguration(MongoServerConfiguration serverConfiguration) {
        this.serverConfiguration = serverConfiguration;
    }

    public String getShellParameters() {
        return shellParameters;
    }

    public void setShellParameters(String shellParameters) {
        this.shellParameters = shellParameters;
    }

    public String getMongoShell() {
        return mongoShell;
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public void setDatabase(MongoDatabase database) {
        this.database = database;
    }

    public String getShellWorkingDir() {
        return shellWorkingDir;
    }

    public void setShellWorkingDir(String shellWorkingDir) {
        this.shellWorkingDir = shellWorkingDir;
    }
}
