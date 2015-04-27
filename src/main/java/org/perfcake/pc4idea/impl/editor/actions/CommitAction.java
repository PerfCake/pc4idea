package org.perfcake.pc4idea.impl.editor.actions;

import org.perfcake.pc4idea.api.editor.editor.ContextProvider;
import org.perfcake.pc4idea.api.manager.ScenarioManager;
import org.perfcake.pc4idea.api.util.PerfCakeScenarioUtil;

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
        ScenarioManager manager = PerfCakeScenarioUtil.getScenarioManager(
                contextProvider.getProject(), contextProvider.getVirtualFile());
        manager.updateScenario(contextProvider.getModel().getScenarioModel(), e.getActionCommand());
    }
}
