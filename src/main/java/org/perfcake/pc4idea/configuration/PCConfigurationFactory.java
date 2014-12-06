package org.perfcake.pc4idea.configuration;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 6.12.2014
 */
public class PCConfigurationFactory extends ConfigurationFactory{
    protected PCConfigurationFactory(@NotNull ConfigurationType type) {
        super(type);
    }
    @Override
    public RunConfiguration createTemplateConfiguration(Project project) {
        return new PCRunConfiguration(project, this, "PerfCake");
    }
}
