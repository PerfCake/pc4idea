package org.perfcake.pc4idea.editor.modelwrapper;

import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.gui.AbstractComponentGUI;
import org.perfcake.pc4idea.editor.gui.SenderGUI;
import org.perfcake.pc4idea.editor.interfaces.CanAddProperty;
import org.perfcake.pc4idea.editor.interfaces.HasGUIChildren;
import org.perfcake.pc4idea.editor.interfaces.ModelWrapper;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stanislav Kaleta on 3/17/15.
 */
public class SenderModelWrapper implements ModelWrapper, HasGUIChildren, CanAddProperty {
    private Scenario.Sender senderModel;

    private SenderGUI senderGUI;

    public SenderModelWrapper(ActionMap baseActionMap){
        senderGUI = new SenderGUI(this, baseActionMap);
    }

    @Override
    public AbstractComponentGUI getGUI() {
        return senderGUI;
    }

    @Override
    public void updateModel(Object componentModel) {
        senderModel = (Scenario.Sender) componentModel;
    }

    @Override
    public Object retrieveModel() {
        return senderModel;
    }

    @Override
    public void addProperty(Property property) {
        senderModel.getProperty().add(property);
    }

    @Override
    public List<ModelWrapper> getChildrenModels() {
        List<ModelWrapper> childrenModelList = new ArrayList<>();
        for (Property property : senderModel.getProperty()){
            ModelWrapper propertyModelWrapper = new PropertyModelWrapper(this, senderGUI.getBaseActionMap());
            propertyModelWrapper.updateModel(property);
            propertyModelWrapper.getGUI().updateGUI();
            childrenModelList.add(propertyModelWrapper);
        }
        return childrenModelList;
    }

    @Override
    public void setChildrenFromModels(List<ModelWrapper> childrenModels) {
        senderModel.getProperty().clear();
        for (ModelWrapper childModel : childrenModels){
            senderModel.getProperty().add((Property) childModel.retrieveModel());
        }
    }

    @Override
    public void deleteChild(ModelWrapper childModelWrapper) {
        Property propertyToDel = (Property) childModelWrapper.retrieveModel();
        senderModel.getProperty().remove(propertyToDel);
    }
}
