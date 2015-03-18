package org.perfcake.pc4idea.editor.actions;

import com.intellij.icons.AllIcons;
import org.perfcake.pc4idea.editor.interfaces.HasGUIChildren;
import org.perfcake.pc4idea.editor.interfaces.ModelWrapper;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Stanislav Kaleta on 3/18/15.
 */
public class DeleteAction extends AbstractAction {
    private HasGUIChildren target;
    private ModelWrapper childToDel;

    public DeleteAction(HasGUIChildren target, ModelWrapper childToDel, String actionName){
        super(actionName, AllIcons.Actions.Delete);
        this.target = target;
        this.childToDel = childToDel;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        target.deleteChild(childToDel);
    }
}
