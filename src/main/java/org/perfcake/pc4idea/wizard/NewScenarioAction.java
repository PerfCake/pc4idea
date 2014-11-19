package org.perfcake.pc4idea.wizard;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import org.perfcake.model.Scenario;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 9.9.2014
 */
public class NewScenarioAction extends AnAction {
    public void actionPerformed(AnActionEvent e) {
        WizardPanel wizard = new WizardPanel();
        WizardDialog wizardDialog = new WizardDialog(wizard);
        wizardDialog.show();
        wizard.stopCheckingValidity();
        if (wizardDialog.getExitCode() == 0){
            VirtualFile file = e.getData(DataKeys.VIRTUAL_FILE);   /*TODO maven cant find CommonDataKeys.VIRTUAL_FILE*/
            file = (file.isDirectory()) ? file : file.getParent();

            String uri = file.getPath()+"/"+wizard.getScenarioName()+".xml";
            Scenario model = wizard.getScenarioModel();

            createScenario(uri,model);
        }
    }

    @Override
    public void update(AnActionEvent e) {
        try {
            String moduleType = e.getData(DataKeys.MODULE).getOptionValue("type");
            VirtualFile file = e.getData(DataKeys.VIRTUAL_FILE);
            String dirName = (file.isDirectory()) ? file.getName() : file.getParent().getName();

            if (!moduleType.equals("PERFCAKE_MODULE")){
                e.getPresentation().setEnabledAndVisible(false);
            } else {
                if (!dirName.equals("scenarios")){
                    e.getPresentation().setEnabled(false);
                }
            }
        } catch (NullPointerException ex) {
            e.getPresentation().setEnabledAndVisible(false);
        }
    }

    private void createScenario(String uri, Scenario model){
      /*TODO create file*/
    }
}