package org.perfcake.pc4idea.impl.editor.modelwrapper;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.gui.component.AbstractComponentGUI;
import org.perfcake.pc4idea.api.editor.modelwrapper.HasGUIChildren;
import org.perfcake.pc4idea.api.editor.modelwrapper.ModelWrapper;
import org.perfcake.pc4idea.api.editor.modelwrapper.Togglable;
import org.perfcake.pc4idea.api.util.MessagesValidationSync;
import org.perfcake.pc4idea.impl.editor.gui.component.ValidationGUI;

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
        sync.syncValidatorRef();
    }

    @Override
    public Object retrieveModel() {
        return (validationModel.getValidator().isEmpty()) ? null : validationModel;
    }

    public void addValidator(Scenario.Validation.Validator validator){
        if (validator == null) {
            throw new NullPointerException("validator to add is null");
        }
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
        sync.syncValidatorRef();
    }

    @Override
    public void setToggle(boolean toggle) {
        validationModel.setEnabled(toggle);
    }

    public MessagesValidationSync getSync(){
        return sync;
    }
}
