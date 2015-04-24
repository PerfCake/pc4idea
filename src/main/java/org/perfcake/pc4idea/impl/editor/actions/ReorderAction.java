package org.perfcake.pc4idea.impl.editor.actions;

import org.perfcake.pc4idea.api.editor.modelwrapper.component.ComponentModelWrapper;
import org.perfcake.pc4idea.api.editor.modelwrapper.component.HasGUIChildren;
import org.perfcake.pc4idea.api.util.Messages;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Created by Stanislav Kaleta on 4/6/15.
 */
public class ReorderAction extends AbstractAction {
    private HasGUIChildren target;
    String childName;

    public ReorderAction(HasGUIChildren target, String childName) {
        super(Messages.Command.REORDER + " " + childName + "s in " +target.getName());
        this.target = target;
        this.childName = childName;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // not used
    }

    public void actionPerformed(List<ComponentModelWrapper> childrenModels){
        target.setChildrenFromModels(childrenModels);
        target.setChildrenFromModels(childrenModels);
        target.commit(Messages.Command.REORDER + " " + childName + "s in " +target.getName());
        target.updateGui();
    }
}
