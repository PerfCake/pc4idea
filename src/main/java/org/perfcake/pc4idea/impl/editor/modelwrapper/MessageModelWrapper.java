package org.perfcake.pc4idea.impl.editor.modelwrapper;

import org.perfcake.model.Header;
import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.gui.component.AbstractComponentGUI;
import org.perfcake.pc4idea.api.editor.modelwrapper.CanAddProperty;
import org.perfcake.pc4idea.api.editor.modelwrapper.ModelWrapper;
import org.perfcake.pc4idea.impl.editor.gui.component.MessageGUI;

/**
 * Created by Stanislav Kaleta on 3/27/15.
 */
public class MessageModelWrapper implements ModelWrapper, CanAddProperty {
    private Scenario.Messages.Message messageModel;
    private MessagesModelWrapper parent;

    private MessageGUI messageGUI;

    public MessageModelWrapper(MessagesModelWrapper parent) {
        this.parent = parent;
        messageGUI = new MessageGUI(this, parent);
    }

    @Override
    public AbstractComponentGUI getGUI() {
        return messageGUI;
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
            throw new NullPointerException("validator ref to add is null");
        }
        if (!parent.getSync().getValidatorIds().contains(ref.getId())) {
            throw new IllegalArgumentException("validator with id " + ref.getId() + " does not exists");
        }
        messageModel.getValidatorRef().add(ref);
    }

}
