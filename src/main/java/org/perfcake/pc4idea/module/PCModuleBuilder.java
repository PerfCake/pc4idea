package org.perfcake.pc4idea.module;

import com.intellij.ide.util.projectWizard.JavaModuleBuilder;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.jetbrains.annotations.NonNls;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 8.9.2014
 */
public class PCModuleBuilder extends JavaModuleBuilder {
    @NonNls private final String messagesDirName = "messages";
    @NonNls private final String scenariosDirName = "scenarios";


    @Override
    public ModuleType getModuleType() {
        return PCModuleType.getInstance();
    }

    @Override
    public void setupRootModel(final ModifiableRootModel rootModel)  throws ConfigurationException {
        super.setupRootModel(rootModel);

        StartupManager.getInstance(rootModel.getProject()).runWhenProjectIsInitialized(new Runnable() {
            public void run() {
                ApplicationManager.getApplication().runWriteAction(new Runnable() {
                    public void run() {
                        try {
                            if (rootModel.getContentRootUrls().length == 1) {
                                VirtualFile moduleDir = (VirtualFileManager.getInstance().findFileByUrl(rootModel.getContentRootUrls()[0]));
                                if (moduleDir != null){
                                    VirtualFile messageDir = moduleDir.createChildDirectory(null, messagesDirName);
                                    VirtualFile scenariosDir = moduleDir.createChildDirectory(null, scenariosDirName);
                                       /*TODO is it needed?*/
//                                    if (messageDir != null) {
//                                        FileEditorManager.getInstance(rootModel.getProject()).openFile(messageDir, true);
//                                    }
//                                    if (scenariosDir != null) {
//                                        FileEditorManager.getInstance(rootModel.getProject()).openFile(messageDir, true);
//                                    }
                                } else {
                                    /*TODO log*/
                                }



                            } else {
                                /*TODO log crus not 1, gcrus.length or throw new IOEx*/
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
