package org.perfcake.pc4idea.impl.run.configuration;

import com.intellij.execution.Location;
import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.actions.RunConfigurationProducer;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import org.perfcake.pc4idea.api.util.PerfCakeModuleUtil;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 5.12.2014
 */
public class PerfCakeRunConfigurationProducer extends RunConfigurationProducer<PerfCakeRunConfiguration> {

    public PerfCakeRunConfigurationProducer() {
        super(PerfCakeConfigurationType.getInstance());
    }

    @Override
    protected boolean setupConfigurationFromContext(PerfCakeRunConfiguration pcRunConfig,
                                                    ConfigurationContext configContext,
                                                    Ref<PsiElement> psiElementRef) {
        Location location = configContext.getLocation();
        if (location == null) {
            return false;
        }
        VirtualFile file = location.getVirtualFile();
        if (file == null) {
            return false;
        }
        Module module = configContext.getModule();
        if (module == null) {
            return false;
        }
        if (PerfCakeModuleUtil.isPerfCakeScenarioFile(module, file)) {
            if (!pcRunConfig.isInitialized()) {
                pcRunConfig.setup(module, file);
                pcRunConfig.setName(file.getName());
            }
            return true;
        }

        return false;
    }

    @Override
    public boolean isConfigurationFromContext(PerfCakeRunConfiguration pcRunConfig,
                                              ConfigurationContext configContext) {
        Location location = configContext.getLocation();
        if (location == null) {
            return false;
        }
        VirtualFile file = location.getVirtualFile();
        if (file == null) {
            return false;
        }
        Module module = configContext.getModule();
        if (module == null) {
            return false;
        }

        if (pcRunConfig.isInitialized()) {
            VirtualFile[] dirs = PerfCakeModuleUtil.findPerfCakeModuleDirs(module);
            if (file.getPath().equals(pcRunConfig.getPaths()[0]) &&
                    dirs[0].getPath().equals(pcRunConfig.getPaths()[1]) &&
                    dirs[1].getPath().equals(pcRunConfig.getPaths()[2]) &&
                    dirs[2].getPath().equals(pcRunConfig.getPaths()[3])) {
                return true;
            }
        }

        return false;
    }
}
