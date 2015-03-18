package org.perfcake.pc4idea.editor.modelwrapper;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.gui.AbstractComponentGUI;
import org.perfcake.pc4idea.editor.gui.MessagesGUI;
import org.perfcake.pc4idea.editor.interfaces.ModelWrapper;

import javax.swing.*;

/**
 * Created by Stanislav Kaleta on 3/18/15.
 */
public class MessagesModel implements ModelWrapper {
    private Scenario.Messages messagesModel;

    private MessagesGUI messagesGUI;

    public MessagesModel(ActionMap baseActionMap) {
        messagesGUI = new MessagesGUI(this, baseActionMap);
    }

    @Override
    public AbstractComponentGUI getGUI() {
        return messagesGUI;
    }

    @Override
    public void updateModel(Object componentModel, boolean doCommit) {

    }

    @Override
    public Object retrieveModel() {
        return null;
    }
}
