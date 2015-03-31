package org.perfcake.pc4idea.editor.modelwrapper;

import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.gui.AbstractComponentGUI;
import org.perfcake.pc4idea.editor.gui.ValidatorGUI;
import org.perfcake.pc4idea.editor.interfaces.CanAddProperty;
import org.perfcake.pc4idea.editor.interfaces.ModelWrapper;

import javax.swing.*;

/**
 * Created by Stanislav Kaleta on 3/31/15.
 */
public class ValidatorModelWrapper implements ModelWrapper, CanAddProperty {
    private Scenario.Validation.Validator validatorModel;

    private ValidatorGUI validatorGUI;

    public ValidatorModelWrapper(ValidationModelWrapper parent, ActionMap baseActionMap) {
        validatorGUI = new ValidatorGUI(this, parent, baseActionMap);
    }

    @Override
    public AbstractComponentGUI getGUI() {
        return validatorGUI;
    }

    @Override
    public void updateModel(Object componentModel) {
        Scenario.Validation.Validator tempModel = (Scenario.Validation.Validator) componentModel;
        if (validatorModel == null){
            validatorModel = tempModel;
        } else {
            validatorModel.setClazz(tempModel.getClazz());
            validatorModel.setId(tempModel.getId());
            validatorModel.getProperty().clear();
            validatorModel.getProperty().addAll(tempModel.getProperty());
        }

    }

    @Override
    public Object retrieveModel() {
        return validatorModel;
    }

    @Override
    public void addProperty(Property property) {
        validatorModel.getProperty().add(property);
    }

}
