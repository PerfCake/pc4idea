package org.perfcake.pc4idea.impl.editor.modelwrapper.component;

import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.editor.ContextProvider;
import org.perfcake.pc4idea.api.editor.modelwrapper.component.AccessibleModel;
import org.perfcake.pc4idea.api.editor.modelwrapper.component.CanAddProperty;
import org.perfcake.pc4idea.api.util.Messages;
import org.perfcake.pc4idea.api.util.MessagesValidationSync;
import org.perfcake.pc4idea.impl.editor.actions.CommitAction;
import org.perfcake.pc4idea.impl.editor.gui.component.ValidatorGui;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Stanislav Kaleta on 3/31/15.
 */
public class ValidatorModelWrapper implements AccessibleModel, CanAddProperty {
    private Scenario.Validation.Validator validatorModel;

    private ValidatorGui validatorGui;

    private ContextProvider context;

    private MessagesValidationSync sync;

    public ValidatorModelWrapper(ValidationModelWrapper parent) {
        context = parent.getContext();
        sync = parent.getSync();
        validatorGui = new ValidatorGui(this, parent);
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
        return Messages.Scenario.VALIDATOR;
    }

    @Override
    public void commit(String message) {
        new CommitAction(context).actionPerformed(new ActionEvent(this,1,message));
    }

    @Override
    public JPanel getGui() {
        return validatorGui;
    }

    @Override
    public void updateGui() {
        validatorGui.updateGui();
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
        sync.syncValidatorRef();
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
