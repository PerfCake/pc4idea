package org.perfcake.pc4idea.editor.actions;

import com.intellij.icons.AllIcons;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.MessagesValidationSync;
import org.perfcake.pc4idea.editor.ScenarioDialogEditor;
import org.perfcake.pc4idea.editor.editors.AttachValidatorEditor;
import org.perfcake.pc4idea.editor.modelwrapper.MessageModelWrapper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Stanislav Kaleta on 3/27/15.
 */
public class AttachValidatorAction extends AbstractAction {
    private MessageModelWrapper target;
    private String actionName;
    private MessagesValidationSync sync;

    public AttachValidatorAction(MessageModelWrapper target, String actionName, MessagesValidationSync sync){
        super(actionName, AllIcons.General.Add);
        this.target = target;
        this.actionName = actionName;
        this.sync = sync;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        AttachValidatorEditor editor = new AttachValidatorEditor(computeUnattachedIDs());
        ScenarioDialogEditor dialog = new ScenarioDialogEditor(editor);
        dialog.show();
        if (dialog.getExitCode() == 0) {
            Scenario.Messages.Message.ValidatorRef ref = editor.getAttachedValidatorRef();
            target.attachValidator(ref);
            target.getGUI().commitChanges(actionName);
            target.getGUI().updateGUI();
        }
    }

    private Set<String> computeUnattachedIDs(){
        Set<String> allIDs = sync.getValidatorIDs();
        Set<String> notAttachedIDs = new TreeSet<>();
        for (String id : allIDs){
            boolean isRef = false;
            Scenario.Messages.Message message = (Scenario.Messages.Message) target.retrieveModel();
            for (Scenario.Messages.Message.ValidatorRef ref : message.getValidatorRef()){
                if(id.equals(ref.getId())){
                    isRef = true;
                }
            }
            if (!isRef){
                notAttachedIDs.add(id);
            }
        }
        return notAttachedIDs;
    }
}
