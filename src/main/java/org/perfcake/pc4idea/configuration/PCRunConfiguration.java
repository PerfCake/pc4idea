package org.perfcake.pc4idea.configuration;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.perfcake.pc4idea.execution.PCProfileState;
import org.perfcake.pc4idea.module.PCModuleUtil;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 5.12.2014
 */
public class PCRunConfiguration extends LocatableConfigurationBase {
    private VirtualFile scenarioFile;
    private Module module;
    private boolean isInitialized;


    public PCRunConfiguration(Project project, ConfigurationFactory factory, String name){
        super(project,factory,name);
        isInitialized = false;
        scenarioFile = null;
        module = null;
    }

    public void setScenarioFile(VirtualFile scenarioFile){
        this.scenarioFile = scenarioFile;
    }
    public VirtualFile getScenarioFile(){
        return scenarioFile;
    }

    public void setModule(Module module){
        this.module = module;
    }
    public Module getMoudle(){
        return module;
    }

    public void tryFindScenarioFileByName(String fileName, Module module) {
        if (module != null) {
            VirtualFile file = PCModuleUtil.findScenario(fileName, module);
            scenarioFile = (file != null) ? file : null;
        } else {
            for (Module m : ModuleManager.getInstance(this.getProject()).getModules()) {
                VirtualFile file = PCModuleUtil.findScenario(fileName, m);
                if (file != null) {
                    scenarioFile = file;
                    this.module = m;
                }
            }
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
        if (getName().contains(".xml") && scenarioFile == null){   /*TODO if editing and name valid scenrio name and text editor isnt !!!!!*/
            tryFindScenarioFileByName(getName(),module);
        }
        if (!isInitialized) {
            throw new RuntimeConfigurationException("Run Configuration is not initialized");
        }
        if (scenarioFile == null) {
            isInitialized = false;
            throw new RuntimeConfigurationException("Can't find scenario file!");
        }
        if (module == null) {
            isInitialized = false;
            throw new RuntimeConfigurationException("Can't find module!");
        }
    }

    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment executionEnvironment) throws ExecutionException {
        return new PCProfileState(executionEnvironment,scenarioFile);
    }


}
