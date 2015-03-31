package org.perfcake.pc4idea.editor.modelwrapper;

import org.perfcake.model.Property;
import org.perfcake.pc4idea.editor.gui.AbstractComponentGUI;
import org.perfcake.pc4idea.editor.gui.PropertyGUI;
import org.perfcake.pc4idea.editor.interfaces.ModelWrapper;

import javax.swing.*;

/**
 * Created by Stanislav Kaleta on 3/17/15.
 */
public class PropertyModelWrapper implements ModelWrapper {
    private Property propertyModel;

    private PropertyGUI propertyGUI;

    public PropertyModelWrapper(ModelWrapper parent, ActionMap baseActionMap){
        propertyGUI = new PropertyGUI(this, parent, baseActionMap);
    }

    @Override
    public AbstractComponentGUI getGUI() {
        return propertyGUI;
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
