package org.perfcake.pc4idea.impl.editor.modelwrapper.component;

import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.editor.ContextProvider;
import org.perfcake.pc4idea.api.editor.modelwrapper.component.CanAddProperty;
import org.perfcake.pc4idea.api.editor.modelwrapper.component.ComponentModelWrapper;
import org.perfcake.pc4idea.api.editor.modelwrapper.component.HasGUIChildren;
import org.perfcake.pc4idea.api.util.Messages;
import org.perfcake.pc4idea.impl.editor.actions.CommitAction;
import org.perfcake.pc4idea.impl.editor.gui.component.PropertiesGui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stanislav Kaleta on 3/18/15.
 */
public class PropertiesModelWrapper implements ComponentModelWrapper, HasGUIChildren, CanAddProperty {
    private Scenario.Properties propertiesModel;

    private PropertiesGui propertiesGui;

    private ContextProvider context;

    public PropertiesModelWrapper(ContextProvider context) {
        this.context = context;
        propertiesGui = new PropertiesGui(this);
    }

    @Override
    public ContextProvider getContext() {
        return context;
    }

    @Override
    public String getName() {
        return Messages.Scenario.PROPERTIES;
    }

    @Override
    public void commit(String message) {
        new CommitAction(context).actionPerformed(new ActionEvent(this,1,message));
    }

    @Override
    public JPanel getGui() {
        return propertiesGui;
    }

    @Override
    public void updateGui() {
        propertiesGui.updateGui();
    }



    @Override
    public void updateModel(Object componentModel) {
        propertiesModel = (componentModel != null) ? (Scenario.Properties) componentModel : new Scenario.Properties();
    }

    @Override
    public Object retrieveModel() {
        return (propertiesModel.getProperty().isEmpty()) ? null : propertiesModel;
    }

    @Override
    public void addProperty(Property property) {
        propertiesModel.getProperty().add(property);
    }

    @Override
    public List<ComponentModelWrapper> getChildrenModels() {
        List<ComponentModelWrapper> childrenModelList = new ArrayList<>();
        for (Property property : propertiesModel.getProperty()){
            ComponentModelWrapper propertyModelWrapper = new PropertyModelWrapper(this);
            propertyModelWrapper.updateModel(property);
            propertyModelWrapper.updateGui();
            childrenModelList.add(propertyModelWrapper);
        }
        return childrenModelList;
    }

    @Override
    public void setChildrenFromModels(List<ComponentModelWrapper> childrenModels) {
        propertiesModel.getProperty().clear();
        for (ComponentModelWrapper childModel : childrenModels){
            propertiesModel.getProperty().add((Property) childModel.retrieveModel());
        }
    }

    @Override
    public void deleteChild(ComponentModelWrapper childModelWrapper) {
        Property propertyToDel = (Property) childModelWrapper.retrieveModel();
        propertiesModel.getProperty().remove(propertyToDel);
    }
}
