package org.perfcake.pc4idea.impl.editor.modelwrapper.component;

import org.perfcake.model.Header;
import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.editor.ContextProvider;
import org.perfcake.pc4idea.api.editor.modelwrapper.component.AccessibleModel;
import org.perfcake.pc4idea.api.editor.modelwrapper.component.CanAddProperty;
import org.perfcake.pc4idea.api.util.Messages;
import org.perfcake.pc4idea.api.util.MessagesValidationSync;
import org.perfcake.pc4idea.impl.editor.actions.CommitAction;
import org.perfcake.pc4idea.impl.editor.gui.component.MessageGui;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Stanislav Kaleta on 3/27/15.
 */
public class MessageModelWrapper implements AccessibleModel, CanAddProperty {
    private Scenario.Messages.Message messageModel;

    private MessageGui messageGui;

    private ContextProvider context;

    private MessagesValidationSync sync;

    public MessageModelWrapper(MessagesModelWrapper parent) {
        context = parent.getContext();
        sync = parent.getSync();
        messageGui = new MessageGui(this, parent);
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
        return Messages.Scenario.MESSAGE;
    }

    @Override
    public void commit(String message) {
        new CommitAction(context).actionPerformed(new ActionEvent(this,1,message));
    }

    @Override
    public JPanel getGui() {
        return messageGui;
    }

    @Override
    public void updateGui() {
        messageGui.updateGui();
    }

    @Override
    public void updateModel(Object componentModel) {
        Scenario.Messages.Message tempModel = (Scenario.Messages.Message) componentModel;
        if (messageModel == null){
            messageModel = tempModel;
        } else {
            messageModel.setContent(tempModel.getContent());
            messageModel.setMultiplicity(tempModel.getMultiplicity());
            messageModel.setUri(tempModel.getUri());
            messageModel.getHeader().clear();
            messageModel.getHeader().addAll(tempModel.getHeader());
            messageModel.getValidatorRef().clear();
            messageModel.getValidatorRef().addAll(tempModel.getValidatorRef());
            messageModel.getProperty().clear();
            messageModel.getProperty().addAll(tempModel.getProperty());
        }
    }

    @Override
    public Object retrieveModel() {
        return messageModel;
    }

    public void addHeader(Header header){
        messageModel.getHeader().add(header);
    }

    @Override
    public void addProperty(Property property) {
        messageModel.getProperty().add(property);
    }

    public void attachValidator(Scenario.Messages.Message.ValidatorRef ref){
        if (ref == null) {
            throw new NullPointerException(Messages.Exception.ADD_NULL_VALIDATOR_REF);
        }
        if (!sync.getValidatorIds().contains(ref.getId())) {
            String[] eMsg = Messages.Exception.INVALID_VALIDATOR_ID;
            throw new IllegalArgumentException(eMsg[0] + ref.getId() + eMsg[1]);
        }
        messageModel.getValidatorRef().add(ref);
    }
}
