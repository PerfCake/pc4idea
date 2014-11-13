package org.perfcake.pc4idea.wizard;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 9.9.2014
 */
public class NewScenarioAction extends AnAction {
    public void actionPerformed(AnActionEvent e) {
        WizardDialog wizardDialog = new WizardDialog();
        wizardDialog.show();
        if (wizardDialog.getExitCode() == 0){

        }
    }

    @Override
    public void update(AnActionEvent e) {
        try {
            String moduleType = e.getData(DataKeys.MODULE).getOptionValue("type");
            VirtualFile file = e.getData(CommonDataKeys.VIRTUAL_FILE);
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
}