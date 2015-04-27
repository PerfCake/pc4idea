package org.perfcake.pc4idea.impl.editor.modelwrapper.component;

import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.editor.ContextProvider;
import org.perfcake.pc4idea.api.editor.modelwrapper.component.AccessibleModel;
import org.perfcake.pc4idea.api.editor.modelwrapper.component.CanAddProperty;
import org.perfcake.pc4idea.api.editor.modelwrapper.component.HasGUIChildren;
import org.perfcake.pc4idea.api.util.Messages;
import org.perfcake.pc4idea.impl.editor.actions.CommitAction;
import org.perfcake.pc4idea.impl.editor.gui.component.SenderGui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stanislav Kaleta on 3/17/15.
 */
public class SenderModelWrapper implements AccessibleModel, HasGUIChildren, CanAddProperty {
    private Scenario.Sender senderModel;

    private SenderGui senderGui;

    private ContextProvider context;

    public SenderModelWrapper(ContextProvider context) {
        this.context = context;
        senderGui = new SenderGui(this);
    }

    @Override
    public ContextProvider getContext() {
        return context;
    }

    @Override
    public String getName() {
        return Messages.Scenario.SENDER;
    }

    @Override
    public void commit(String message) {
        new CommitAction(context).actionPerformed(new ActionEvent(this,1,message));
    }

    @Override
    public JPanel getGui() {
        return senderGui;
    }

    @Override
    public void updateGui() {
        senderGui.updateGui();
    }


    @Override
    public void updateModel(Object componentModel) {
        Scenario.Sender tempModel = (Scenario.Sender) componentModel;
        if (senderModel == null){
            senderModel =  tempModel;
        } else {
            senderModel.setClazz(tempModel.getClazz());
            senderModel.getProperty().clear();
            senderModel.getProperty().addAll(tempModel.getProperty());
        }
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
    public List<AccessibleModel> getChildrenModels() {
        List<AccessibleModel> childrenModelList = new ArrayList<>();
        for (Property property : senderModel.getProperty()){
            AccessibleModel propertyModelWrapper = new PropertyModelWrapper(this);
            propertyModelWrapper.updateModel(property);
            propertyModelWrapper.updateGui();
            childrenModelList.add(propertyModelWrapper);
        }
        return childrenModelList;
    }

    @Override
    public void setChildrenFromModels(List<AccessibleModel> childrenModels) {
        senderModel.getProperty().clear();
        for (AccessibleModel childModel : childrenModels){
            senderModel.getProperty().add((Property) childModel.retrieveModel());
        }
    }

    @Override
    public void deleteChild(AccessibleModel childModelWrapper) {
        Property propertyToDel = (Property) childModelWrapper.retrieveModel();
        senderModel.getProperty().remove(propertyToDel);
    }
}
