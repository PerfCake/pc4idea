package org.perfcake.pc4idea.editor.modelwrapper;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.gui.AbstractComponentGUI;
import org.perfcake.pc4idea.editor.gui.PropertiesGUI;
import org.perfcake.pc4idea.editor.interfaces.ModelWrapper;

import javax.security.sasl.SaslClient;
import javax.swing.*;

/**
 * Created by Stanislav Kaleta on 3/18/15.
 */
public class PropertiesModelWrapper implements ModelWrapper {
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

    }

    @Override
    public Object retrieveModel() {
        return null;
    }
}
