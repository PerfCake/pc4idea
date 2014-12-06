package org.perfcake.pc4idea.configuration;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.perfcake.pc4idea.execution.PCRunProfileState;
import org.perfcake.pc4idea.module.PCModuleUtil;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 5.12.2014
 */
public class PCRunConfiguration extends LocatableConfigurationBase {
    private static final Logger LOG = Logger.getInstance("#org.perfcake.pc4idea.configuration.PCRunConfiguration");

    private VirtualFile scenarioFile;
    private boolean isInitialized;
    private Project project;

    public PCRunConfiguration(Project project, ConfigurationFactory factory, String name){
        super(project,factory,name);
        isInitialized = false;
        scenarioFile = null;
        this.project = project;
    }

    public void setScenarioFile(VirtualFile scenarioFile){
        this.scenarioFile = scenarioFile;
    }
    public VirtualFile getScenarioFile(){
        return scenarioFile;
    }

    public void findScenarioFileByName(String fileName){
        int controlCounter = 0;
        for (Module m : ModuleManager.getInstance(project).getModules()) {
            VirtualFile file = PCModuleUtil.findScenario(fileName, m);
            if (file != null) {
                scenarioFile = file;
                controlCounter++;
            }

        }
        if (controlCounter > 1){
            LOG.warn("Configuration Warning: More scenarios with name "+fileName+" found in this project!");
            Notifications.Bus.notify(new Notification("PerfCake Plugin", "Configuration Warning",
                    "More scenarios with name "+fileName+" found in this project!",
                    NotificationType.WARNING), project);
        }
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
        if (getName().contains(".xml") && scenarioFile == null){
            findScenarioFileByName(getName());
        }
        if (!isInitialized) {
            throw new RuntimeConfigurationException("Run Configuration is not initialized");
        }
        if (scenarioFile == null) {
            isInitialized = false;
            throw new RuntimeConfigurationException("Can't find scenario file!");
        }
    }

    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment executionEnvironment) throws ExecutionException {
        return new PCRunProfileState(executionEnvironment,scenarioFile);
    }


}
