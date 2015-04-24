package org.perfcake.pc4idea.impl.editor.actions;

import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.editor.ContextProvider;
import org.perfcake.pc4idea.api.manager.ScenarioManager;
import org.perfcake.pc4idea.api.util.PerfCakeScenarioUtil;
import org.perfcake.pc4idea.impl.editor.editor.ScenarioEditor;

import javax.swing.*;
import java.awt.event.ActionEvent;


/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 4.3.2015
 */
public class CommitAction extends AbstractAction {
    private ContextProvider contextProvider;

    public CommitAction(ContextProvider contextProvider) {
        this.contextProvider = contextProvider;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FileEditorManager editorManager = FileEditorManager.getInstance(contextProvider.getProject());

        /*TODO*/

        FileEditor selectedEditor = editorManager.getSelectedEditor(contextProvider.getVirtualFile());
        if (selectedEditor != null){
            if (selectedEditor instanceof ScenarioEditor){
                Scenario model = ((ScenarioEditor) selectedEditor).getModel().getScenarioModel();
                if (PerfCakeScenarioUtil.isPerfCakeScenario(contextProvider.getVirtualFile())){
                    ScenarioManager manager = PerfCakeScenarioUtil.getScenarioManager(contextProvider.getProject(), contextProvider.getVirtualFile());
                    manager.updateScenario(model,e.getActionCommand());
                }
            }
        }
    }
}
