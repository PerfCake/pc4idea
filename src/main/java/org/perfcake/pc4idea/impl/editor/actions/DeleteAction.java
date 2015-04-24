package org.perfcake.pc4idea.impl.editor.actions;

import com.intellij.icons.AllIcons;
import org.perfcake.pc4idea.api.editor.modelwrapper.component.ComponentModelWrapper;
import org.perfcake.pc4idea.api.editor.modelwrapper.component.HasGUIChildren;
import org.perfcake.pc4idea.api.util.Messages;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Stanislav Kaleta on 3/18/15.
 */
public class DeleteAction extends AbstractAction {
    private HasGUIChildren target;
    private ComponentModelWrapper childToDel;

    public DeleteAction(HasGUIChildren target, ComponentModelWrapper childToDel){
        super(Messages.Command.DEL + " " + childToDel.getName() +" in " + target.getName(), AllIcons.Actions.Delete);
        this.target = target;
        this.childToDel = childToDel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        target.deleteChild(childToDel);
        target.commit(Messages.Command.DEL + " " + childToDel.getName() + " in " + target.getName());
        target.updateGui();
    }
}
