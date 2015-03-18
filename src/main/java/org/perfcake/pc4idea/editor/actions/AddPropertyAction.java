package org.perfcake.pc4idea.editor.actions;

import com.intellij.icons.AllIcons;
import org.perfcake.model.Property;
import org.perfcake.pc4idea.editor.ScenarioDialogEditor;
import org.perfcake.pc4idea.editor.editors.PropertyEditor;
import org.perfcake.pc4idea.editor.interfaces.CanAddProperty;
import org.perfcake.pc4idea.editor.interfaces.ModelWrapper;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Stanislav Kaleta on 3/16/15.
 */
public class AddPropertyAction extends AbstractAction {
    private CanAddProperty target;

    public AddPropertyAction(CanAddProperty target, String actionName) {
        super(actionName, AllIcons.General.Add);
        this.target = target;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        PropertyEditor editor = new PropertyEditor();
        ScenarioDialogEditor dialog = new ScenarioDialogEditor(editor);
        dialog.show();
        if (dialog.getExitCode() == 0) {
            Property property = editor.getProperty();
            target.addProperty(property);
        }
    }
}
