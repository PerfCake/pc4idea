package org.perfcake.pc4idea.impl.editor.modelwrapper.component;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.editor.ContextProvider;
import org.perfcake.pc4idea.api.editor.modelwrapper.component.ComponentModelWrapper;
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
public class ValidationModelWrapper implements ComponentModelWrapper, HasGUIChildren, Togglable {
    private Scenario.Validation validationModel;

    private ValidationGui validationGui;

    private ContextProvider context;
    private MessagesValidationSync sync;

    public ValidationModelWrapper(ContextProvider context, MessagesValidationSync sync) {
        this.context = context;
        this.sync = sync;
        validationGui = new ValidationGui(this);
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
    public List<ComponentModelWrapper> getChildrenModels() {
        List<ComponentModelWrapper> childrenModelList = new ArrayList<>();
        for (Scenario.Validation.Validator validator : validationModel.getValidator()){
            ComponentModelWrapper validatorModelWrapper = new ValidatorModelWrapper(this);
            validatorModelWrapper.updateModel(validator);
            validatorModelWrapper.updateGui();
            childrenModelList.add(validatorModelWrapper);
        }
        return childrenModelList;
    }

    @Override
    public void setChildrenFromModels(List<ComponentModelWrapper> childrenModels) {
        validationModel.getValidator().clear();
        for (ComponentModelWrapper childModel : childrenModels){
            validationModel.getValidator().add((Scenario.Validation.Validator) childModel.retrieveModel());
        }
    }

    @Override
    public void deleteChild(ComponentModelWrapper childModelWrapper) {
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
