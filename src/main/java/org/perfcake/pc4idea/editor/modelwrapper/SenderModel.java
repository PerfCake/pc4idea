package org.perfcake.pc4idea.editor.modelwrapper;

import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.Messages;
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
public class SenderModel implements ModelWrapper, HasGUIChildren, CanAddProperty {
    private Scenario.Sender senderModel;

    private SenderGUI senderGUI;

    public SenderModel(ActionMap baseActionMap){
        senderGUI = new SenderGUI(this, baseActionMap);
    }

    @Override
    public AbstractComponentGUI getGUI() {
        return senderGUI;
    }

    @Override
    public void updateModel(Object componentModel, boolean doCommit) {
        senderModel = (Scenario.Sender) componentModel;
        senderGUI.updateGUI();
        if (doCommit){
            senderGUI.commitChanges(Messages.BUNDLE.getString("EDIT")+" Sender");
        }
    }

    @Override
    public Object retrieveModel() {
        return senderModel;
    }

    @Override
    public void addProperty(Property property) {
        senderModel.getProperty().add(property);
        senderGUI.updateGUI();
        senderGUI.commitChanges(Messages.BUNDLE.getString("ADD") + " Property");
    }

    @Override
    public List<ModelWrapper> getChildrenModels() {
        List<ModelWrapper> childrenModelList = new ArrayList<>();
        for (Property property : senderModel.getProperty()){
            ModelWrapper propertyModelWrapper = new PropertyModel(this, senderGUI.getBaseActionMap());
            propertyModelWrapper.updateModel(property, false);
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
        senderGUI.updateGUI();
        senderGUI.commitChanges("Sender: Properties "+Messages.BUNDLE.getString("REORDER"));
    }

//    @Override
//    public void editChild(ModelWrapper childModelWrapper) {
//
//    }

    @Override
    public void deleteChild(ModelWrapper childModelWrapper) {
        Property propertyToDel = (Property) childModelWrapper.retrieveModel();
        senderModel.getProperty().remove(propertyToDel);
        senderGUI.updateGUI();
        senderGUI.commitChanges("Sender: "+Messages.BUNDLE.getString("DEL")+" Property");
    }
}
