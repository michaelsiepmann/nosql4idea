package org.codinjutsu.tools.nosql.mongo.runner;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunConfigurationModule;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

class MongoFactory extends ConfigurationFactory {

    MongoFactory(ConfigurationType type) {
        super(type);
    }

    @NotNull
    @Override
    public String getId() {
        return getName();
    }

    @Override
    @NotNull
    public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
        return new MongoRunConfiguration(new RunConfigurationModule(project), this);
    }
}
