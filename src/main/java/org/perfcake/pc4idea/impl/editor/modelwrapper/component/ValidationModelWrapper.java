package org.perfcake.pc4idea.impl.editor.modelwrapper.component;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.editor.ContextProvider;
import org.perfcake.pc4idea.api.editor.modelwrapper.component.AccessibleModel;
import org.perfcake.pc4idea.api.editor.modelwrapper.component.HasGUIChildren;
import org.perfcake.pc4idea.api.editor.modelwrapper.component.Togglable;
import org.perfcake.pc4idea.api.util.Messages;
import org.perfcake.pc4idea.api.util.MessagesValidationSync;
import org.perfcake.pc4idea.impl.editor.actions.CommitAction;
import org.perfcake.pc4idea.impl.editor.gui.component.ValidationGui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stanislav Kaleta on 3/18/15.
 */
public class ValidationModelWrapper implements AccessibleModel, HasGUIChildren, Togglable {
    private Scenario.Validation validationModel;

    private ValidationGui validationGui;

    private ContextProvider context;

    private MessagesValidationSync sync;

    public ValidationModelWrapper(ContextProvider context, MessagesValidationSync sync) {
        this.context = context;
        this.sync = sync;
        validationGui = new ValidationGui(this);
    }

    public MessagesValidationSync getSync(){
        return sync;
    }

    @Override
    public ContextProvider getContext() {
        return context;
    }

    @Override
    public String getName() {
        return Messages.Scenario.VALIDATION;
    }

    @Override
    public void commit(String message) {
        new CommitAction(context).actionPerformed(new ActionEvent(this,1,message));
    }

    @Override
    public JPanel getGui() {
        return validationGui;
    }

    @Override
    public void updateGui() {
        validationGui.updateGui();
    }

    @Override
    public void updateModel(Object componentModel) {
        Scenario.Validation tempModel = (Scenario.Validation) componentModel;
        if (validationModel == null){
            validationModel = tempModel;
        } else {
            validationModel.setEnabled(tempModel.isEnabled());
            validationModel.setFastForward(tempModel.isFastForward());
            validationModel.getValidator().clear();
            validationModel.getValidator().addAll(tempModel.getValidator());
        }

        sync.syncValidatorRef();
    }

    @Override
    public Object retrieveModel() {
        return validationModel;
    }

    public void addValidator(Scenario.Validation.Validator validator){
        if (validator == null) {
            throw new NullPointerException(Messages.Exception.ADD_NULL_VALIDATOR);
        }
        validationModel.getValidator().add(validator);
    }

    @Override
    public List<AccessibleModel> getChildrenModels() {
        List<AccessibleModel> childrenModelList = new ArrayList<>();
        for (Scenario.Validation.Validator validator : validationModel.getValidator()){
            AccessibleModel validatorModelWrapper = new ValidatorModelWrapper(this);
            validatorModelWrapper.updateModel(validator);
            validatorModelWrapper.updateGui();
            childrenModelList.add(validatorModelWrapper);
        }
        return childrenModelList;
    }

    @Override
    public void setChildrenFromModels(List<AccessibleModel> childrenModels) {
        validationModel.getValidator().clear();
        for (AccessibleModel childModel : childrenModels){
            validationModel.getValidator().add((Scenario.Validation.Validator) childModel.retrieveModel());
        }
    }

    @Override
    public void deleteChild(AccessibleModel childModelWrapper) {
        Scenario.Validation.Validator validatorToDel = (Scenario.Validation.Validator) childModelWrapper.retrieveModel();
        validationModel.getValidator().remove(validatorToDel);
        sync.syncValidatorRef();
    }

    @Override
    public void setToggle(boolean toggle) {
        validationModel.setEnabled(toggle);
    }
}
