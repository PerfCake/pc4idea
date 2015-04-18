package org.perfcake.pc4idea.impl.editor.actions;

import org.perfcake.pc4idea.api.editor.modelwrapper.HasGUIChildren;
import org.perfcake.pc4idea.api.editor.modelwrapper.ModelWrapper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Created by Stanislav Kaleta on 4/6/15.
 */
public class ReorderAction extends AbstractAction {
    private HasGUIChildren target;
    private String actionName;
    List<ModelWrapper> childrenModels;

    public ReorderAction(HasGUIChildren target, String actionName) {
        super(actionName);
        this.target = target;
        this.actionName = actionName;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        target.setChildrenFromModels(childrenModels);
        target.getGUI().updateGUI();
        target.getGUI().commitChanges(actionName);

    }

    public void preActionPerformed(List<ModelWrapper> childrenModels){
        this.childrenModels = childrenModels;
        actionPerformed(null);
    }
}
