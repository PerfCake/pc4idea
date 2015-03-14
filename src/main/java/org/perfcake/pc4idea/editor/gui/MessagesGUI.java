package org.perfcake.pc4idea.editor.gui;

import org.perfcake.model.Scenario;

import javax.swing.*;
import java.util.List;

/**
 * Created by Stanislav Kaleta on 3/7/15.
 */
public class MessagesGUI extends AbstractComponentGUI  {
    private Scenario.Messages messages;


    public MessagesGUI(ActionMap actionMap){
        super(actionMap);

    }
    @Override
    List<JMenuItem> getMenuItems() {
        return null;
    }

    @Override
    void performImport(String transferredData) {

    }

    @Override
    void openEditor() {
        /*TODO*/
    }

    @Override
    public void setComponentModel(Object componentModel) {

    }

    @Override
    public Object getComponentModel() {
        return null;
    }

    @Override
    public void updateColors() {

    }
}
