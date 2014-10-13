package org.perfcake.pc4idea.module;

import com.intellij.ide.util.projectWizard.JavaModuleBuilder;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NonNls;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 8.9.2014
 * To change this template use File | Settings | File Templates.
 */
public class PCModuleBuilder extends JavaModuleBuilder {
    @NonNls private static final String messagesDirName = "messages";
    @NonNls private static final String scenariosDirName = "scenarios";


    @Override
    public ModuleType getModuleType() {
        return PCModuleType.getInstance();
    }
//    @Override
//    public String getGroupName() {
//        return "PerfCake";
//    }
    @Override
    public void setupRootModel(final ModifiableRootModel rootModel) throws ConfigurationException {
        super.setupRootModel(rootModel);

        final Module module = rootModel.getModule();
        final Project project = module.getProject();

        StartupManager.getInstance(project).runWhenProjectIsInitialized(new Runnable() {
            public void run() {
                ApplicationManager.getApplication().runWriteAction(new Runnable() {
                    public void run() {
                        try {
                            VirtualFile messageDir = project.getBaseDir().createChildDirectory(null, messagesDirName);
                            if (messageDir != null) {
                                FileEditorManager.getInstance(project).openFile(messageDir, true);
                            }
                            VirtualFile scenariosDir = project.getBaseDir().createChildDirectory(null, scenariosDirName);
                            if (scenariosDir != null) {
                                FileEditorManager.getInstance(project).openFile(scenariosDir, true);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();    /*TODO log*/
                        }
                    }
                });

            }
        });
    }

}
