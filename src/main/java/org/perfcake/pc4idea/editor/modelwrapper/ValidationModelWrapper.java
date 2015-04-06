package org.perfcake.pc4idea.editor.modelwrapper;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.MessagesValidationSync;
import org.perfcake.pc4idea.editor.gui.AbstractComponentGUI;
import org.perfcake.pc4idea.editor.gui.ValidationGUI;
import org.perfcake.pc4idea.editor.interfaces.HasGUIChildren;
import org.perfcake.pc4idea.editor.interfaces.ModelWrapper;
import org.perfcake.pc4idea.editor.interfaces.Togglable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stanislav Kaleta on 3/18/15.
 */
public class ValidationModelWrapper implements ModelWrapper, HasGUIChildren, Togglable {
    private Scenario.Validation validationModel;

    private ValidationGUI validationGUI;

    private MessagesValidationSync sync;

    public ValidationModelWrapper(ActionMap baseActionMap, MessagesValidationSync sync){
        validationGUI = new ValidationGUI(this, baseActionMap);
        this.sync = sync;
    }

    @Override
    public AbstractComponentGUI getGUI() {
        return validationGUI;
    }

    @Override
    public void updateModel(Object componentModel) {
        validationModel = (componentModel != null) ? (Scenario.Validation) componentModel : new Scenario.Validation();
    }

    @Override
    public Object retrieveModel() {
        return (validationModel.getValidator().isEmpty()) ? null : validationModel;
    }

    public void addValidator(Scenario.Validation.Validator validator){
        validationModel.getValidator().add(validator);
    }

    @Override
    public List<ModelWrapper> getChildrenModels() {
        List<ModelWrapper> childrenModelList = new ArrayList<>();
        for (Scenario.Validation.Validator validator : validationModel.getValidator()){
            ModelWrapper validatorModelWrapper = new ValidatorModelWrapper(this, validationGUI.getBaseActionMap());
            validatorModelWrapper.updateModel(validator);
            validatorModelWrapper.getGUI().updateGUI();
            childrenModelList.add(validatorModelWrapper);
        }
        return childrenModelList;
    }

    @Override
    public void setChildrenFromModels(List<ModelWrapper> childrenModels) {
        validationModel.getValidator().clear();
        for (ModelWrapper childModel : childrenModels){
            validationModel.getValidator().add((Scenario.Validation.Validator) childModel.retrieveModel());
        }
    }

    @Override
    public void deleteChild(ModelWrapper childModelWrapper) {
        Scenario.Validation.Validator validatorToDel = (Scenario.Validation.Validator) childModelWrapper.retrieveModel();
        validationModel.getValidator().remove(validatorToDel);
    }

    @Override
    public void setToggle(boolean toggle) {
        validationModel.setEnabled(toggle);
    }

    public MessagesValidationSync getSync(){
        return sync;
    }
}
