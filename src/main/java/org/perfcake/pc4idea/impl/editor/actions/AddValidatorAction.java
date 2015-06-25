package org.perfcake.pc4idea.impl.editor.actions;

import com.intellij.icons.AllIcons;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.openapi.ui.EditorDialog;
import org.perfcake.pc4idea.api.util.Messages;
import org.perfcake.pc4idea.impl.editor.editor.component.ValidatorEditor;
import org.perfcake.pc4idea.impl.editor.modelwrapper.component.ValidationModelWrapper;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Stanislav Kaleta on 3/31/15.
 */
public class AddValidatorAction extends AbstractAction {
    private ValidationModelWrapper target;

    public AddValidatorAction(ValidationModelWrapper target){
        super(Messages.Command.ADD + " " + Messages.Scenario.VALIDATOR, AllIcons.General.Add);
        this.target = target;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        actionPerformedWrapper(null);
    }

    public void actionPerformedWrapper(String validatorClass){
        ValidatorEditor editor = new ValidatorEditor(target.getContext().getModule(), target.getSync());
        if (validatorClass != null){
            Scenario.Validation.Validator validator = new Scenario.Validation.Validator();
            validator.setClazz(validatorClass);
            editor.setValidator(validator);
        }
        EditorDialog dialog = new EditorDialog(editor);
        dialog.show();
        if (dialog.getExitCode() == 0) {
            Scenario.Validation.Validator validator = editor.getValidator();
            target.addValidator(validator);
            target.commit(Messages.Command.ADD + " " + Messages.Scenario.VALIDATOR);
            target.updateGui();
        }
    }
}
