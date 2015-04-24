package org.perfcake.pc4idea.impl.editor.modelwrapper.component;

import org.perfcake.model.Property;
import org.perfcake.pc4idea.api.editor.editor.ContextProvider;
import org.perfcake.pc4idea.api.editor.modelwrapper.component.ComponentModelWrapper;
import org.perfcake.pc4idea.api.util.Messages;
import org.perfcake.pc4idea.impl.editor.actions.CommitAction;
import org.perfcake.pc4idea.impl.editor.gui.component.PropertyGui;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Stanislav Kaleta on 3/17/15.
 */
public class PropertyModelWrapper implements ComponentModelWrapper {
    private Property propertyModel;

    private PropertyGui propertyGui;

    private ContextProvider context;

    public PropertyModelWrapper(ComponentModelWrapper parent) {
        context = parent.getContext();
        propertyGui = new PropertyGui(this, parent);
    }

    @Override
    public ContextProvider getContext() {
        return context;
    }

    @Override
    public String getName() {
        return Messages.Scenario.PROPERTY;
    }

    @Override
    public void commit(String message) {
        new CommitAction(context).actionPerformed(new ActionEvent(this,1,message));
    }

    @Override
    public JPanel getGui() {
        return propertyGui;
    }

    @Override
    public void updateGui() {
        propertyGui.updateGui();
    }

    @Override
    public void updateModel(Object componentModel) {
        if (propertyModel == null){
            propertyModel = (Property) componentModel;
        } else {
            propertyModel.setName(((Property) componentModel).getName());
            propertyModel.setValue(((Property) componentModel).getValue());
        }
    }

    @Override
    public Object retrieveModel() {
        return propertyModel;
    }
}
