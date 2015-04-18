package org.perfcake.pc4idea.impl.editor.actions;

import com.intellij.icons.AllIcons;
import org.perfcake.pc4idea.api.editor.modelwrapper.Togglable;
import org.perfcake.pc4idea.todo.Messages;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Stanislav Kaleta on 4/6/15.
 */
public class ToggleAction extends AbstractAction {
    private final String disableCommandText = Messages.BUNDLE.getString("DISABLE") + " ";
    private final String enableCommandText = Messages.BUNDLE.getString("ENABLE") + " ";
    private Togglable target;
    private String actionName;
    private String targetName;
    boolean toggle = false;

    public ToggleAction(Togglable target, String targetName, boolean toggle) {
        super((toggle) ?
                        Messages.BUNDLE.getString("DISABLE") + " " + targetName :
                        Messages.BUNDLE.getString("ENABLE") + " " + targetName,
                (toggle) ?
                        AllIcons.Debugger.MuteBreakpoints :
                        AllIcons.Debugger.Db_muted_verified_breakpoint);
        this.target = target;
        this.toggle = toggle;
        this.targetName = targetName;

        actionName = (toggle) ?
                disableCommandText + targetName :
                enableCommandText + targetName;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        toggle = !toggle;
        target.setToggle(toggle);
        target.getGUI().commitChanges(actionName);
        target.getGUI().updateGUI();
    }

    public void setCurrentState(boolean toggle) {
        this.toggle = toggle;
        actionName = (toggle) ?
                disableCommandText + targetName :
                enableCommandText + targetName;
    }
}
