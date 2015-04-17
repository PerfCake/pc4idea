package org.perfcake.pc4idea.impl.editor.actions;

import com.intellij.icons.AllIcons;
import org.perfcake.model.Property;
import org.perfcake.pc4idea.api.editor.modelwrapper.CanAddProperty;
import org.perfcake.pc4idea.api.editor.openapi.ui.EditorDialog;
import org.perfcake.pc4idea.impl.editor.editor.component.PropertyEditor;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Stanislav Kaleta on 3/16/15.
 */
public class AddPropertyAction extends AbstractAction {
    private CanAddProperty target;
    private String actionName;

    public AddPropertyAction(CanAddProperty target, String actionName) {
        super(actionName, AllIcons.General.Add);
        this.target = target;
        this.actionName = actionName;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        PropertyEditor editor = new PropertyEditor();
        EditorDialog dialog = new EditorDialog(editor);
        dialog.show();
        if (dialog.getExitCode() == 0) {
            Property property = editor.getProperty();
            target.addProperty(property);
            target.getGUI().commitChanges(actionName);
            target.getGUI().updateGUI();
        }
    }
}
