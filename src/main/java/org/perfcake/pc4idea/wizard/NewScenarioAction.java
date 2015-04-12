package org.perfcake.pc4idea.wizard;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.manager.XMLScenarioManager;
import org.perfcake.pc4idea.module.PerfCakeIconPatcher;

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
        String dirURI = (file.isDirectory()) ? file.getUrl() : file.getParent().getUrl();

        WizardPanel wizard = new WizardPanel(dirURI);
        WizardDialog wizardDialog = new WizardDialog(wizard);
        wizardDialog.show();
        wizard.stopCheckingValidity();
        if (wizardDialog.getExitCode() == 0) {
            VirtualFile scenarioDirectory = wizard.getScenarioDirectory();
            String name = wizard.getScenarioName();
            name = (name.contains(".xml")) ? name : name + ".xml";
            Scenario model = wizard.getScenarioModel();

            /*TODO switch scenario type*/
            new XMLScenarioManager(scenarioDirectory, e.getProject()).createScenario(name, model);
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