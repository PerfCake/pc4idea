package org.perfcake.pc4idea.impl.editor.modelwrapper;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.gui.component.AbstractComponentGUI;
import org.perfcake.pc4idea.api.editor.modelwrapper.HasGUIChildren;
import org.perfcake.pc4idea.api.editor.modelwrapper.ModelWrapper;
import org.perfcake.pc4idea.api.util.MessagesValidationSync;
import org.perfcake.pc4idea.api.util.PerfCakeEditorUtil;
import org.perfcake.pc4idea.impl.editor.gui.component.MessagesGUI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stanislav Kaleta on 3/18/15.
 */
public class MessagesModelWrapper implements ModelWrapper, HasGUIChildren {
    private Scenario.Messages messagesModel;

    private MessagesGUI messagesGUI;

    private MessagesValidationSync sync;

    public MessagesModelWrapper(PerfCakeEditorUtil util, MessagesValidationSync sync) {
        messagesGUI = new MessagesGUI(this, util);
        this.sync = sync;
    }

    @Override
    public AbstractComponentGUI getGUI() {
        return messagesGUI;
    }

    @Override
    public void updateModel(Object componentModel) {
        messagesModel = (componentModel != null) ? (Scenario.Messages) componentModel : new Scenario.Messages();
    }

    @Override
    public Object retrieveModel() {
        return (messagesModel.getMessage().isEmpty()) ? null : messagesModel;
    }

    public void addMessage(Scenario.Messages.Message message){
        if (message == null) {
            throw new NullPointerException("message to add is null");
        }
        messagesModel.getMessage().add(message);
    }

    @Override
    public List<ModelWrapper> getChildrenModels() {
        List<ModelWrapper> childrenModelList = new ArrayList<>();
        for (Scenario.Messages.Message message : messagesModel.getMessage()){
            ModelWrapper messageModelWrapper = new MessageModelWrapper(this);
            messageModelWrapper.updateModel(message);
            messageModelWrapper.getGUI().updateGUI();
            childrenModelList.add(messageModelWrapper);
        }
        return childrenModelList;
    }

    @Override
    public void setChildrenFromModels(List<ModelWrapper> childrenModels) {
        messagesModel.getMessage().clear();
        for (ModelWrapper childModel : childrenModels){
            messagesModel.getMessage().add((Scenario.Messages.Message) childModel.retrieveModel());
        }
    }

    @Override
    public void deleteChild(ModelWrapper childModelWrapper) {
        Scenario.Messages.Message messageToDel = (Scenario.Messages.Message) childModelWrapper.retrieveModel();
        messagesModel.getMessage().remove(messageToDel);
    }

    public MessagesValidationSync getSync(){
        return sync;
    }
}
