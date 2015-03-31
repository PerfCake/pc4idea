package org.perfcake.pc4idea.editor.actions;

import com.intellij.icons.AllIcons;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.ScenarioDialogEditor;
import org.perfcake.pc4idea.editor.editors.ValidatorEditor;
import org.perfcake.pc4idea.editor.modelwrapper.ValidationModelWrapper;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Stanislav Kaleta on 3/31/15.
 */
public class AddValidatorAction extends AbstractAction {
    private ValidationModelWrapper target;
    private String actionName;

    public AddValidatorAction(ValidationModelWrapper target, String actionName){
        super(actionName, AllIcons.General.Add);
        this.target = target;
        this.actionName = actionName;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        actionPerformedWrapper(null);
    }

    public void actionPerformedWrapper(String validatorClass){
        ValidatorEditor editor = new ValidatorEditor(target.getSync().getValidatorIDs());
        if (validatorClass != null){
            Scenario.Validation.Validator validator = new Scenario.Validation.Validator();
            validator.setClazz(validatorClass);
            editor.setValidator(validator, false);
        }
        ScenarioDialogEditor dialog = new ScenarioDialogEditor(editor);
        dialog.show();
        if (dialog.getExitCode() == 0) {
            Scenario.Validation.Validator validator = editor.getValidator();
            target.addValidator(validator);
            target.getGUI().commitChanges(actionName);
            target.getGUI().updateGUI();
        }
    }
}
