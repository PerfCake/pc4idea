package org.perfcake.pc4idea.impl.editor.actions;

import com.intellij.icons.AllIcons;
import org.perfcake.pc4idea.api.editor.modelwrapper.ModelWrapper;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Stanislav Kaleta on 3/16/15.
 */
public class EditAction extends AbstractAction{
    private ModelWrapper target;
    private String actionName;

    public EditAction(ModelWrapper target, String actionName){
        super(actionName, AllIcons.Actions.Edit);
        this.target = target;
        this.actionName = actionName;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object model = target.getGUI().openEditorDialogAndGetResult();
        if (model != null){
            target.updateModel(model);
            target.getGUI().commitChanges(actionName);
            target.getGUI().updateGUI();
        }
    }
}
