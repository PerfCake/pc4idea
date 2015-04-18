package org.perfcake.pc4idea.impl.editor.actions;

import org.jetbrains.annotations.NotNull;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.manager.ScenarioManager;
import org.perfcake.pc4idea.impl.editor.editor.PerfCakeEditor;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 4.3.2015
 */
public class CommitAction extends AbstractAction {
    private PerfCakeEditor editor;
    private ScenarioManager manager;

    public CommitAction(@NotNull PerfCakeEditor editor, @NotNull ScenarioManager manager) {
        this.editor = editor;
        this.manager = manager;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Scenario scenarioModel = editor.getComponent().getScenarioGUI().getScenarioModel();
        manager.updateScenario(scenarioModel, e.getActionCommand());
    }
}
