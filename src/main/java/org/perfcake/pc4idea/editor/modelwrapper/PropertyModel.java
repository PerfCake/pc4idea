package org.perfcake.pc4idea.editor.modelwrapper;

import org.perfcake.model.Property;
import org.perfcake.pc4idea.editor.Messages;
import org.perfcake.pc4idea.editor.gui.AbstractComponentGUI;
import org.perfcake.pc4idea.editor.gui.PropertyGUI;
import org.perfcake.pc4idea.editor.interfaces.ModelWrapper;

import javax.swing.*;

/**
 * Created by Stanislav Kaleta on 3/17/15.
 */
public class PropertyModel implements ModelWrapper {
    private Property propertyModel;

    private PropertyGUI propertyGUI;

    public PropertyModel(ModelWrapper parent, ActionMap baseActionMap){
        propertyGUI = new PropertyGUI(this, parent, baseActionMap);
    }

    @Override
    public AbstractComponentGUI getGUI() {
        return propertyGUI;
    }

    @Override
    public void updateModel(Object componentModel, boolean doCommit) {
        if (propertyModel == null){
            propertyModel = (Property) componentModel;
        } else {
            propertyModel.setName(((Property) componentModel).getName());
            propertyModel.setValue(((Property) componentModel).getValue());
        }

        propertyGUI.updateGUI();

        if (doCommit){
            propertyGUI.commitChanges(Messages.BUNDLE.getString("EDIT")+" Property");
        }
    }

    @Override
    public Object retrieveModel() {
        return propertyModel;
    }
}
