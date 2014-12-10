package org.perfcake.pc4idea.configuration;

import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.actions.RunConfigurationProducer;
import com.intellij.openapi.util.Ref;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import org.perfcake.pc4idea.editor.PerfCakeEditorProvider;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 5.12.2014
 */
public class PCRunConfigurationProducer extends RunConfigurationProducer<PCRunConfiguration> {

    public PCRunConfigurationProducer(){
        super(PCConfigurationType.getInstance());
    }

    @Override
    protected boolean setupConfigurationFromContext(PCRunConfiguration pcRunConfig, ConfigurationContext configContext, Ref<PsiElement> psiElementRef) {
        if ((configContext.getLocation() != null) && (configContext.getLocation().getVirtualFile() != null) &&
                new PerfCakeEditorProvider().accept(configContext.getProject(), configContext.getLocation().getVirtualFile())){
            if (!pcRunConfig.isInitialized()) {
                VirtualFile file = configContext.getLocation().getVirtualFile();
                /*TODO set module*/
                pcRunConfig.setScenarioFile(file);
                pcRunConfig.setName(file.getName());
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isConfigurationFromContext(PCRunConfiguration pcRunConfig, ConfigurationContext configContext) {
        if ((configContext.getLocation() != null) && (configContext.getLocation().getVirtualFile() != null)){
            VirtualFile file = configContext.getLocation().getVirtualFile();
            return file.equals(pcRunConfig.getScenarioFile()) && pcRunConfig.isInitialized();
        } else {
            return false;
        }
    }
}
