package org.perfcake.pc4idea.impl.editor.actions;

import com.intellij.icons.AllIcons;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.openapi.ui.EditorDialog;
import org.perfcake.pc4idea.api.util.Messages;
import org.perfcake.pc4idea.api.util.MessagesValidationSync;
import org.perfcake.pc4idea.impl.editor.editor.component.AttachValidatorEditor;
import org.perfcake.pc4idea.impl.editor.modelwrapper.component.MessageModelWrapper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Set;

/**
 * Created by Stanislav Kaleta on 3/27/15.
 */
public class AttachValidatorAction extends AbstractAction {
    private MessageModelWrapper target;
    private MessagesValidationSync sync;

    public AttachValidatorAction(MessageModelWrapper target, MessagesValidationSync sync){
        super(Messages.Command.ATTACH + " " +Messages.Scenario.VALIDATOR, AllIcons.General.Add);
        this.target = target;
        this.sync = sync;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Set<String> unattachedIDs = sync.getUnattachedValidatorIds((Scenario.Messages.Message) target.retrieveModel());
        AttachValidatorEditor editor = new AttachValidatorEditor(unattachedIDs);
        EditorDialog dialog = new EditorDialog(editor);
        dialog.show();
        if (dialog.getExitCode() == 0) {
            Scenario.Messages.Message.ValidatorRef ref = editor.getAttachedValidatorRef();
            target.attachValidator(ref);
            target.commit(Messages.Command.ATTACH + " " +Messages.Scenario.VALIDATOR);
            target.updateGui();
        }
    }
}
