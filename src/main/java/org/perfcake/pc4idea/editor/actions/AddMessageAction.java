package org.perfcake.pc4idea.editor.actions;

import com.intellij.icons.AllIcons;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.ScenarioDialogEditor;
import org.perfcake.pc4idea.editor.editors.MessageEditor;
import org.perfcake.pc4idea.editor.modelwrapper.MessagesModelWrapper;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Stanislav Kaleta on 3/27/15.
 */
public class AddMessageAction extends AbstractAction {
    private MessagesModelWrapper target;
    private String actionName;

    public AddMessageAction(MessagesModelWrapper target, String actionName){
        super(actionName, AllIcons.General.Add);
        this.target = target;
        this.actionName = actionName;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        MessageEditor editor = new MessageEditor(target.getSync().getValidatorIDs());
        ScenarioDialogEditor dialog = new ScenarioDialogEditor(editor);
        dialog.show();
        if (dialog.getExitCode() == 0) {
            Scenario.Messages.Message message = editor.getMessage();
            target.addMessage(message);
            target.getGUI().commitChanges(actionName);
            target.getGUI().updateGUI();
        }
    }
}
