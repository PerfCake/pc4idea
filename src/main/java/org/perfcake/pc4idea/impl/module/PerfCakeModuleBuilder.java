package org.perfcake.pc4idea.impl.module;

import com.intellij.ide.util.projectWizard.JavaModuleBuilder;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.jetbrains.annotations.NonNls;

import java.io.IOException;

/**
 * Created by miron on 21.1.2014. + changes
 */
public class PerfCakeModuleBuilder extends JavaModuleBuilder {
    private static final Logger LOG = Logger.getInstance(PerfCakeModuleBuilder.class);

    @NonNls private final String messagesDirName = "messages";
    @NonNls private final String scenariosDirName = "scenarios";
    @NonNls private final String libDirName = "lib";



    @Override
    public ModuleType getModuleType() {
        return PerfCakeModuleType.getInstance();
    }

    @Override
    public String getGroupName() {
        return "PerfCake";
    }

    /**
     * Creates module directories on module creation. + ...
     *
     * @param rootModel
     * @throws com.intellij.openapi.options.ConfigurationException
     */
    public void setupRootModel(ModifiableRootModel rootModel) throws ConfigurationException {
        super.setupRootModel(rootModel);

        StartupManager.getInstance(rootModel.getProject()).runWhenProjectIsInitialized(new Runnable() {
            public void run() {
                ApplicationManager.getApplication().runWriteAction(new Runnable() {
                    public void run() {
                        VirtualFile moduleDir = (VirtualFileManager.getInstance().findFileByUrl(rootModel.getContentRootUrls()[0]));
                        if (moduleDir != null) {
                            try {
                                moduleDir.createChildDirectory(this, messagesDirName);
                                moduleDir.createChildDirectory(this, scenariosDirName);
                                moduleDir.createChildDirectory(this, libDirName);
                            } catch (IOException e) {
                                LOG.error("Error creating PerfCake Module Structure", e);
                            }
                        }
                    }
                });
            }
        });
    }
}