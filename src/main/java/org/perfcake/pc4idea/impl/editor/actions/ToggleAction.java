package org.perfcake.pc4idea.impl.editor.actions;

import org.perfcake.pc4idea.api.editor.modelwrapper.Togglable;
import org.perfcake.pc4idea.todo.Messages;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Stanislav Kaleta on 4/6/15.
 */
public class ToggleAction extends AbstractAction {
    private Togglable target;
    private String actionName;
    private String targetName;
    boolean toggle = false;

    public ToggleAction(Togglable target, String actionName, Icon icon, boolean toggle){
        super(actionName, icon);
        this.target = target;
        this.actionName = actionName;
        this.toggle = toggle;

    }

    public ToggleAction(Togglable target, String targetName){
        this.target = target;
        this.targetName = targetName;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        toggle = !toggle;
        target.setToggle(toggle);
        target.getGUI().commitChanges(actionName);
        target.getGUI().updateGUI();
    }

    public void preActionPerformed(boolean oldToggle) {
        toggle = oldToggle;
        actionName = (oldToggle) ? Messages.BUNDLE.getString("DISABLE") + " " + targetName
                : Messages.BUNDLE.getString("ENABLE") + " " + targetName;
        this.actionPerformed(null);
    }
}
