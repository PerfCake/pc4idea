package org.perfcake.pc4idea.editor.modelwrapper;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.gui.AbstractComponentGUI;
import org.perfcake.pc4idea.editor.gui.ValidationGUI;
import org.perfcake.pc4idea.editor.interfaces.ModelWrapper;

import javax.swing.*;

/**
 * Created by Stanislav Kaleta on 3/18/15.
 */
public class ValidationModel implements ModelWrapper {
    private Scenario.Validation validationModel;

    private ValidationGUI validationGUI;

    public ValidationModel(ActionMap baseActionMap){
        validationGUI = new ValidationGUI(this, baseActionMap);
    }

    @Override
    public AbstractComponentGUI getGUI() {
        return validationGUI;
    }

    @Override
    public void updateModel(Object componentModel, boolean doCommit) {

    }

    @Override
    public Object retrieveModel() {
        return null;
    }
}
