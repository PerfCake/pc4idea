package org.perfcake.pc4idea.impl.editor.actions;

import com.intellij.icons.AllIcons;
import org.perfcake.pc4idea.api.editor.gui.component.ComponentGui;
import org.perfcake.pc4idea.api.editor.modelwrapper.component.ComponentModelWrapper;
import org.perfcake.pc4idea.api.util.Messages;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Stanislav Kaleta on 3/16/15.
 */
public class EditAction extends AbstractAction{
    private ComponentModelWrapper target;

    public EditAction(ComponentModelWrapper target){
        super(Messages.Command.EDIT + " " + target.getName(), AllIcons.Actions.Edit);
        this.target = target;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object model = ((ComponentGui)target.getGui()).openEditorDialogAndGetResult();
        if (model != null){
            target.updateModel(model);
            target.commit(Messages.Command.EDIT + " " + target.getName());
            target.updateGui();
        }
    }
}
