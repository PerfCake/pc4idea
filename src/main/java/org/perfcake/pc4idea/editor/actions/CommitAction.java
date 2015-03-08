package org.perfcake.pc4idea.editor.actions;

import org.jetbrains.annotations.NotNull;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.editor.PerfCakeEditor;
import org.perfcake.pc4idea.editor.gui.PerfCakeEditorGUI;
import org.perfcake.pc4idea.editor.manager.ScenarioManager;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 4.3.2015
 */
public class CommitAction extends AbstractAction {
    private ScenarioManager manager;
    private PerfCakeEditor editor;
    public CommitAction(@NotNull ScenarioManager manager, @NotNull PerfCakeEditor editor){
        this.manager = manager;
        this.editor = editor;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Scenario scenarioModel = editor.getComponent().getScenarioGUI().getScenarioModel();
        manager.saveScenario(scenarioModel,e.getActionCommand());
        editor.getComponent().getScenarioGUI().setScenarioModel(scenarioModel);
    }
}
