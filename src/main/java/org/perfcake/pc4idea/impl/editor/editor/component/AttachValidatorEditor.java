package org.perfcake.pc4idea.impl.editor.editor.component;

import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.components.JBList;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.editor.component.AbstractEditor;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 26.11.2014
 */
public class AttachValidatorEditor extends AbstractEditor {
    private JList<String> validatorIDList;

    public AttachValidatorEditor(Set<String> validatorIDToChoose){
        validatorIDList = new JBList(validatorIDToChoose);

        this.setLayout(new GridLayout(1,1));
        this.add(validatorIDList);
    }

    public Scenario.Messages.Message.ValidatorRef getAttachedValidatorRef(){
        Scenario.Messages.Message.ValidatorRef newRef = new Scenario.Messages.Message.ValidatorRef();
        newRef.setId(validatorIDList.getSelectedValue());
        return newRef;
    }

    @Override
    public String getTitle() {
        return "Attach Validator";
    }

    @Override
    public ValidationInfo areInsertedValuesValid() {
        ValidationInfo info = null;
        if (validatorIDList.getSelectedIndex() == -1){
            info = new ValidationInfo("validation ID wasn't selected");
        }
        return info;
    }
}
