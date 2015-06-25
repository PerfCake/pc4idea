package org.perfcake.pc4idea.impl.wizard;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.impl.manager.DslScenarioManager;
import org.perfcake.pc4idea.impl.manager.XmlScenarioManager;
import org.perfcake.pc4idea.impl.module.PerfCakeIconPatcher;

/**
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
        Module module = e.getData(DataKeys.MODULE);
        Project project = e.getProject();
        if (file == null || module == null || project == null){
            return;
        }
        String dirURI = (file.isDirectory()) ? file.getUrl() : file.getParent().getUrl();

        WizardPanel wizard = new WizardPanel(dirURI, module);
        WizardDialog wizardDialog = new WizardDialog(wizard);
        wizardDialog.show();
        wizard.stopCheckingValidity();
        if (wizardDialog.getExitCode() == 0) {
            VirtualFile scenarioDirectory = wizard.getScenarioDirectory();
            String name = wizard.getScenarioName();
            String type = wizard.getScenarioType();
            Scenario model = wizard.getScenarioModel();

            switch (type) {
                case "XML":
                    new XmlScenarioManager(project).createScenario(scenarioDirectory, name, model);
                    break;
                case "DSL":
                    new DslScenarioManager(project).createScenario(scenarioDirectory, name, model);
                    break;
            }
        }
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        VirtualFile file = e.getData(DataKeys.VIRTUAL_FILE);
        if (file == null) {
            e.getPresentation().setEnabledAndVisible(false);
        }
    }
}