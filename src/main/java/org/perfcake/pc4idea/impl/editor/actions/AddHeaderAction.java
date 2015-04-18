package org.perfcake.pc4idea.impl.editor.actions;

import com.intellij.icons.AllIcons;
import org.perfcake.model.Header;
import org.perfcake.pc4idea.api.editor.openapi.ui.EditorDialog;
import org.perfcake.pc4idea.impl.editor.editor.component.HeaderEditor;
import org.perfcake.pc4idea.impl.editor.modelwrapper.MessageModelWrapper;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Stanislav Kaleta on 3/27/15.
 */
public class AddHeaderAction extends AbstractAction {
    private MessageModelWrapper target;
    private String actionName;

    public AddHeaderAction(MessageModelWrapper target, String actionName){
        super(actionName, AllIcons.General.Add);
        this.target = target;
        this.actionName = actionName;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        HeaderEditor editor = new HeaderEditor();
        EditorDialog dialog = new EditorDialog(editor);
        dialog.show();
        if (dialog.getExitCode() == 0){
            Header header = editor.getHeader();
            target.addHeader(header);
            target.getGUI().commitChanges(actionName);
            target.getGUI().updateGUI();
        }

    }
}
