package org.perfcake.pc4idea.impl.wizard;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VirtualFile;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.impl.manager.XMLScenarioManager;
import org.perfcake.pc4idea.impl.module.PerfCakeIconPatcher;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 9.9.2014
 */
public class NewScenarioAction extends AnAction {

    public NewScenarioAction() {
        super(PerfCakeIconPatcher.loadIcon());
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        VirtualFile file = e.getData(DataKeys.VIRTUAL_FILE);
        if (file == null){
            return;
        }
        String dirURI = (file.isDirectory()) ? file.getUrl() : file.getParent().getUrl();

        Module module = e.getData(DataKeys.MODULE);
        WizardPanel wizard = new WizardPanel(dirURI, module);
        WizardDialog wizardDialog = new WizardDialog(wizard);
        wizardDialog.show();
        wizard.stopCheckingValidity();
        if (wizardDialog.getExitCode() == 0) {
            VirtualFile scenarioDirectory = wizard.getScenarioDirectory();
            String name = wizard.getScenarioName();
            name = (name.contains(".xml")) ? name : name + ".xml";/*TODO remove this*/
            Scenario model = wizard.getScenarioModel();

            /*TODO switch scenario type*/
            if (e.getProject() != null){
                new XMLScenarioManager(e.getProject()).createScenario(scenarioDirectory, name, model);
            } else {
                /*TODO log shit*/
            }

        }
    }

    @Override
    public void update(AnActionEvent e) {
        VirtualFile file = e.getData(DataKeys.VIRTUAL_FILE);
        if (file == null) {
            e.getPresentation().setEnabledAndVisible(false);
        }
    }
}