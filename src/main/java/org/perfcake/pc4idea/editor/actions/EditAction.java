package org.perfcake.pc4idea.editor.actions;

import com.intellij.icons.AllIcons;
import org.perfcake.pc4idea.editor.gui.AbstractComponentGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Stanislav Kaleta on 3/16/15.
 */
public class EditAction extends AbstractAction{
    private AbstractComponentGUI target;
    private String actionName;

    public EditAction(AbstractComponentGUI target, String actionName){
        super(actionName, AllIcons.Actions.Edit);
        this.target = target;
        this.actionName = actionName;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object model = target.openEditorDialog();
        if (model != null){
            target.setComponentModel(model);
        }
        target.commitChanges(actionName);
    }
}
