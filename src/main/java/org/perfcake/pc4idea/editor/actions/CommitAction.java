package org.perfcake.pc4idea.editor.actions;

import org.jetbrains.annotations.NotNull;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.PerfCakeEditorUtil;
import org.perfcake.pc4idea.editor.editor.PerfCakeEditor;
import org.perfcake.pc4idea.editor.manager.ScenarioManager;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 4.3.2015
 */
public class CommitAction extends AbstractAction {
    private PerfCakeEditorUtil util;

    public CommitAction(@NotNull PerfCakeEditorUtil util) {
        this.util = util;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Scenario scenarioModel = util.getEditor().getComponent().getScenarioGUI().getScenarioModel();
        util.getManager().updateScenario(scenarioModel, e.getActionCommand());
    }
}
