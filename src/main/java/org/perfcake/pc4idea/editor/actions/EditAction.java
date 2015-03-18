package org.perfcake.pc4idea.editor.actions;

import com.intellij.icons.AllIcons;
import org.perfcake.pc4idea.editor.interfaces.ModelWrapper;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Stanislav Kaleta on 3/16/15.
 */
public class EditAction extends AbstractAction{
    private ModelWrapper target;

    public EditAction(ModelWrapper target, String actionName){
        super(actionName, AllIcons.Actions.Edit);
        this.target = target;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object model = target.getGUI().openEditorDialogAndGetResult();
        if (model != null){
            target.updateModel(model, true);
        }
    }
}
