package org.perfcake.pc4idea.execution;

import com.intellij.execution.actions.ConfigurationContext;
import com.intellij.execution.actions.RunConfigurationProducer;
import com.intellij.openapi.util.Ref;
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
                String fileName = configContext.getLocation().getVirtualFile().getName();
                pcRunConfig.setScenarioName(fileName);
                pcRunConfig.setName(fileName);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isConfigurationFromContext(PCRunConfiguration pcRunConfig, ConfigurationContext configContext) {
        if ((configContext.getLocation() != null) && (configContext.getLocation().getVirtualFile() != null)){
            String fileName = configContext.getLocation().getVirtualFile().getName();
            return fileName.equals(pcRunConfig.getScenarioName()) && pcRunConfig.isInitialized();
        } else {
            return false;
        }
    }
}
