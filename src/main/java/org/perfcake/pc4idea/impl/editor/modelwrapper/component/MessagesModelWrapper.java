package org.perfcake.pc4idea.impl.editor.modelwrapper.component;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.editor.ContextProvider;
import org.perfcake.pc4idea.api.editor.modelwrapper.component.AccessibleModel;
import org.perfcake.pc4idea.api.editor.modelwrapper.component.HasGUIChildren;
import org.perfcake.pc4idea.api.util.Messages;
import org.perfcake.pc4idea.api.util.MessagesValidationSync;
import org.perfcake.pc4idea.impl.editor.actions.CommitAction;
import org.perfcake.pc4idea.impl.editor.gui.component.MessagesGui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stanislav Kaleta on 3/18/15.
 */
public class MessagesModelWrapper implements AccessibleModel, HasGUIChildren {
    private Scenario.Messages messagesModel;

    private MessagesGui messagesGui;

    private ContextProvider context;

    private MessagesValidationSync sync;

    public MessagesModelWrapper(ContextProvider context, MessagesValidationSync sync) {
        this.context = context;
        this.sync = sync;
        messagesGui = new MessagesGui(this);
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
        return Messages.Scenario.MESSAGES;
    }

    @Override
    public void commit(String message) {
        new CommitAction(context).actionPerformed(new ActionEvent(this,1,message));
    }

    @Override
    public JPanel getGui() {
        return messagesGui;
    }

    @Override
    public void updateGui() {
        messagesGui.updateGui();
    }


    @Override
    public void updateModel(Object componentModel) {
        Scenario.Messages tempModel = (Scenario.Messages) componentModel;
        if (messagesModel == null){
            messagesModel = tempModel;
        } else {
            messagesModel.getMessage().clear();
            messagesModel.getMessage().addAll(tempModel.getMessage());
        }
    }

    @Override
    public Object retrieveModel() {
        return messagesModel;
    }

    public void addMessage(Scenario.Messages.Message message){
        if (message == null) {
            throw new NullPointerException(Messages.Exception.ADD_NULL_MESSAGE);
        }
        messagesModel.getMessage().add(message);
    }

    @Override
    public List<AccessibleModel> getChildrenModels() {
        List<AccessibleModel> childrenModelList = new ArrayList<>();
        for (Scenario.Messages.Message message : messagesModel.getMessage()){
            AccessibleModel messageModelWrapper = new MessageModelWrapper(this);
            messageModelWrapper.updateModel(message);
            messageModelWrapper.updateGui();
            childrenModelList.add(messageModelWrapper);
        }
        return childrenModelList;
    }

    @Override
    public void setChildrenFromModels(List<AccessibleModel> childrenModels) {
        messagesModel.getMessage().clear();
        for (AccessibleModel childModel : childrenModels){
            messagesModel.getMessage().add((Scenario.Messages.Message) childModel.retrieveModel());
        }
    }

    @Override
    public void deleteChild(AccessibleModel childModelWrapper) {
        Scenario.Messages.Message messageToDel = (Scenario.Messages.Message) childModelWrapper.retrieveModel();
        messagesModel.getMessage().remove(messageToDel);
    }
}
