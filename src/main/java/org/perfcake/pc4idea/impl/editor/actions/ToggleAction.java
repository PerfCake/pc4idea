package org.perfcake.pc4idea.impl.editor.actions;

import com.intellij.icons.AllIcons;
import org.perfcake.pc4idea.api.editor.modelwrapper.component.Togglable;
import org.perfcake.pc4idea.api.util.Messages;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Stanislav Kaleta on 4/6/15.
 */
public class ToggleAction extends AbstractAction {
    private final String disableCommandText = Messages.Command.DISABLE + " ";
    private final String enableCommandText = Messages.Command.ENABLE + " ";
    private Togglable target;
    private String actionName;
    private String targetName;
    boolean toggle = false;

    public ToggleAction(Togglable target, boolean toggle) {
        super((toggle) ?
                        Messages.Command.DISABLE + " " + target.getName() :
                        Messages.Command.ENABLE + " " + target.getName(),
                (toggle) ?
                        AllIcons.Debugger.MuteBreakpoints :
                        AllIcons.Debugger.Db_muted_verified_breakpoint);
        this.target = target;
        this.toggle = toggle;
        targetName = target.getName();
        actionName = (toggle) ?
                disableCommandText + targetName :
                enableCommandText + targetName;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        toggle = !toggle;
        target.setToggle(toggle);
        target.commit(actionName);
        target.updateGui();
    }

    public void setCurrentState(boolean toggle) {
        this.toggle = toggle;
        actionName = (toggle) ?
                disableCommandText + targetName :
                enableCommandText + targetName;
    }
}
