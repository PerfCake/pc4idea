package org.perfcake.pc4idea.impl.editor.actions;

import com.intellij.icons.AllIcons;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.openapi.ui.EditorDialog;
import org.perfcake.pc4idea.api.util.Messages;
import org.perfcake.pc4idea.impl.editor.editor.component.MessageEditor;
import org.perfcake.pc4idea.impl.editor.modelwrapper.component.MessagesModelWrapper;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Stanislav Kaleta on 3/27/15.
 */
public class AddMessageAction extends AbstractAction {
    private MessagesModelWrapper target;


    public AddMessageAction(MessagesModelWrapper target){
        super(Messages.Command.ADD + " " + Messages.Scenario.MESSAGES, AllIcons.General.Add);
        this.target = target;

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        MessageEditor editor = new MessageEditor(target.getSync());
        EditorDialog dialog = new EditorDialog(editor);
        dialog.show();
        if (dialog.getExitCode() == 0) {
            Scenario.Messages.Message message = editor.getMessage();
            target.addMessage(message);
            target.commit(Messages.Command.ADD + " " + Messages.Scenario.MESSAGES);
            target.updateGui();
        }
    }
}
