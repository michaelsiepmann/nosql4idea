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

package org.codinjutsu.tools.nosql.redis.view.console;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import org.codinjutsu.tools.nosql.DatabaseVendor;
import org.codinjutsu.tools.nosql.commons.configuration.ServerConfiguration;
import org.codinjutsu.tools.nosql.commons.model.Database;
import org.codinjutsu.tools.nosql.commons.view.console.AbstractNoSQLConsoleRunner;
import org.jetbrains.annotations.NotNull;


public class RedisConsoleRunner extends AbstractNoSQLConsoleRunner {

    private static final Key<Boolean> SHELL_FILE = Key.create("REDIS_SHELL_FILE");
    private static final String CONSOLE_TYPE_ID = "Redis Shell";
    private final Database database;

    public RedisConsoleRunner(@NotNull Project project, ServerConfiguration serverConfiguration, Database database) {
        super(project, CONSOLE_TYPE_ID, "/tmp", serverConfiguration);
        this.database = database;
    }

    @Override
    @NotNull
    protected String getShellConsoleTitle() {
        return "Redis Console";
    }

    @Override
    @NotNull
    protected Key<Boolean> getShellFile() {
        return SHELL_FILE;
    }

    @NotNull
    @Override
    protected Process createProcess(@NotNull GeneralCommandLine commandLine, @NotNull ServerConfiguration serverConfiguration) throws ExecutionException {
        commandLine.addParameters("-n", database.getName());

        setWorkingDirectory(commandLine, serverConfiguration);

        addShellArguments(commandLine, serverConfiguration);

        return commandLine.createProcess();
    }

    @NotNull
    @Override
    protected DatabaseVendor getDatabaseVendor() {
        return DatabaseVendor.REDIS;
    }

    @Override
    @NotNull
    protected String getConsoleTypeId() {
        return CONSOLE_TYPE_ID;
    }
}
