package org.perfcake.pc4idea.impl.editor.modelwrapper;

import org.perfcake.model.Property;
import org.perfcake.pc4idea.api.editor.gui.component.AbstractComponentGUI;
import org.perfcake.pc4idea.api.editor.modelwrapper.ModelWrapper;
import org.perfcake.pc4idea.impl.editor.gui.component.PropertyGUI;

/**
 * Created by Stanislav Kaleta on 3/17/15.
 */
public class PropertyModelWrapper implements ModelWrapper {
    private Property propertyModel;

    private PropertyGUI propertyGUI;

    public PropertyModelWrapper(ModelWrapper parent) {
        propertyGUI = new PropertyGUI(this, parent);
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
