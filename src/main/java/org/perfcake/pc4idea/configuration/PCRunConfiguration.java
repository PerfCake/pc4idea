package org.perfcake.pc4idea.configuration;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.perfcake.pc4idea.execution.PCRunProfileState;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 5.12.2014
 */
public class PCRunConfiguration extends LocatableConfigurationBase {
    private String scenarioName;
    private boolean isInitialized;

    public PCRunConfiguration(Project project, ConfigurationFactory factory, String name){
        super(project,factory,name);
        isInitialized = false;
    }

    public void setScenarioName(String scenarioName){
        this.scenarioName = scenarioName;
    }
    public String getScenarioName(){
        return scenarioName;
    }

    public boolean isInitialized(){
        return isInitialized;
    }
    public void  setInitialized(boolean isInitialized){
        this.isInitialized = isInitialized;
    }

    @NotNull
    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new PCConfigurationEditor();
    }

    @Override
    public void checkConfiguration() throws RuntimeConfigurationException {
        if (getName().contains(".xml")){
            setScenarioName(getName());
        }
        if (!isInitialized) {
            throw new RuntimeConfigurationException("Run Configuration is not initialized");
        }
        if (scenarioName == null) {
            isInitialized = false;
            throw new RuntimeConfigurationException("Scenario name can't be null");
        }
    }

    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment executionEnvironment) throws ExecutionException {
        return new PCRunProfileState(executionEnvironment.getProject(),scenarioName);
    }
}
