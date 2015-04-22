package org.perfcake.pc4idea.impl.run.configuration;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.components.PathMacroManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizerUtil;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.refactoring.listeners.RefactoringElementAdapter;
import com.intellij.refactoring.listeners.RefactoringElementListener;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.perfcake.pc4idea.api.util.PerfCakeModuleUtil;
import org.perfcake.pc4idea.impl.run.execution.PerfCakeRunProfileState;

import java.io.File;
import java.io.IOException;

/**
 * Created by miron on 4.2.2014. + me todo check see super
 * @see super
 */
public class PerfCakeRunConfiguration extends LocatableConfigurationBase implements RefactoringListenerProvider {
    private String scenarioPath;
    private String scenariosDirPath;
    private String messagesDirPath;
    private String libDirPath;

    private boolean initialized;

    protected PerfCakeRunConfiguration(Project project, ConfigurationFactory factory, String name) {
        super(project, factory, name);
        initialized = false;
        scenarioPath = "";
        scenariosDirPath = "";
        messagesDirPath = "";
        libDirPath = "";
    }


    public void setup(@NotNull Module module, @NotNull VirtualFile scenario){
        VirtualFile[] dirs = PerfCakeModuleUtil.findPerfCakeModuleDirs(module);

        scenariosDirPath = dirs[0].getPath();
        messagesDirPath = dirs[1].getPath();
        libDirPath = dirs[2].getPath();

        for (VirtualFile file : dirs[0].getChildren()) {
            if (file.getName().equals(scenario.getName())) {
                scenarioPath = scenario.getPath();
            }
        }
    }

    public void setPaths(String[] paths) {
        scenarioPath = paths[0];
        scenariosDirPath = paths[1];
        messagesDirPath = paths[2];
        libDirPath = paths[3];
    }

    public String[] getPaths(){
        return new String[]{scenarioPath, scenariosDirPath, messagesDirPath, libDirPath};
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public String isValid(){
        if (scenarioPath.trim().isEmpty()){
            return "Scenario isn't specified!";
        }
        if (scenariosDirPath.trim().isEmpty() || messagesDirPath.trim().isEmpty() || libDirPath.trim().isEmpty()) {
            return "PerfCake run properties aren't properly set!";
        }

        return null;
    }

    @NotNull
    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new PerfCakeRunConfigurationEditor();
    }

    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment env) throws ExecutionException {
        return new PerfCakeRunProfileState(this);
    }

    @Override
    public void checkConfiguration() throws RuntimeConfigurationException {
        String errorMessage = isValid();
        if (errorMessage != null){
            throw  new RuntimeConfigurationException(errorMessage);
        }
    }

    @Override
    public String suggestedName() {
        if (scenarioPath != null) {
            return (new File(scenarioPath)).getName();
        }
        return null;
    }

    @Override
    public void readExternal(Element element) throws InvalidDataException {
        PathMacroManager.getInstance(getProject()).expandPaths(element);
        super.readExternal(element);

        scenarioPath = JDOMExternalizerUtil.readField(element, "SCENARIO_PATH");
        scenariosDirPath = JDOMExternalizerUtil.readField(element, "SCENARIOS_DIR_PATH");
        messagesDirPath = JDOMExternalizerUtil.readField(element, "MESSAGES_DIR_PATH");
        libDirPath = JDOMExternalizerUtil.readField(element, "LIB_DIR_PATH");

        isValid();
    }

    @Override
    public void writeExternal(Element element) throws WriteExternalException {
        super.writeExternal(element);

        JDOMExternalizerUtil.writeField(element, "SCENARIO_PATH", scenarioPath);
        JDOMExternalizerUtil.writeField(element, "SCENARIOS_DIR_PATH", scenariosDirPath);
        JDOMExternalizerUtil.writeField(element, "MESSAGES_DIR_PATH", messagesDirPath);
        JDOMExternalizerUtil.writeField(element, "LIB_DIR_PATH", libDirPath);

        PathMacroManager.getInstance(getProject()).collapsePathsRecursively(element);
    }

    @Nullable
    @Override
    public RefactoringElementListener getRefactoringElementListener(PsiElement element) {
        if (element instanceof PsiFile) {
            VirtualFile virtualFile = ((PsiFile) element).getVirtualFile();
            try {
                if (virtualFile != null && (new File(virtualFile.getPath())).getCanonicalPath().equals((new File(scenarioPath)).getCanonicalPath())) {
                    return new RefactoringElementAdapter() {
                        @Override
                        protected void elementRenamedOrMoved(@NotNull PsiElement newElement) {
                            VirtualFile newFile = ((PsiFile) newElement).getVirtualFile();
                            if (newFile != null) {
                                scenarioPath = FileUtil.toSystemIndependentName(newFile.getPath());
                            }
                        }

                        @Override
                        public void undoElementMovedOrRenamed(@NotNull PsiElement newElement, @NotNull String oldQualifiedName) {
                            scenarioPath = FileUtil.toSystemIndependentName(oldQualifiedName);
                        }
                    };
                }
            } catch (IOException ignored) {
                //just return null
            }
        }
        return null;
    }
}
