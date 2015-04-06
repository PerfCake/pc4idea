package org.perfcake.pc4idea.editor.modelwrapper;

import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.gui.AbstractComponentGUI;
import org.perfcake.pc4idea.editor.gui.PropertiesGUI;
import org.perfcake.pc4idea.editor.interfaces.CanAddProperty;
import org.perfcake.pc4idea.editor.interfaces.HasGUIChildren;
import org.perfcake.pc4idea.editor.interfaces.ModelWrapper;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stanislav Kaleta on 3/18/15.
 */
public class PropertiesModelWrapper implements ModelWrapper, HasGUIChildren, CanAddProperty {
    private Scenario.Properties propertiesModel;

    private PropertiesGUI propertiesGUI;

    public PropertiesModelWrapper(ActionMap baseActionMap){
        propertiesGUI = new PropertiesGUI(this, baseActionMap);
    }


    @Override
    public AbstractComponentGUI getGUI() {
        return propertiesGUI;
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
    public List<ModelWrapper> getChildrenModels() {
        List<ModelWrapper> childrenModelList = new ArrayList<>();
        for (Property property : propertiesModel.getProperty()){
            ModelWrapper propertyModelWrapper = new PropertyModelWrapper(this, propertiesGUI.getBaseActionMap());
            propertyModelWrapper.updateModel(property);
            propertyModelWrapper.getGUI().updateGUI();
            childrenModelList.add(propertyModelWrapper);
        }
        return childrenModelList;
    }

    @Override
    public void setChildrenFromModels(List<ModelWrapper> childrenModels) {
        propertiesModel.getProperty().clear();
        for (ModelWrapper childModel : childrenModels){
            propertiesModel.getProperty().add((Property) childModel.retrieveModel());
        }
    }

    @Override
    public void deleteChild(ModelWrapper childModelWrapper) {
        Property propertyToDel = (Property) childModelWrapper.retrieveModel();
        propertiesModel.getProperty().remove(propertyToDel);
    }



}
