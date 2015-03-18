package org.perfcake.pc4idea.editor.modelwrapper;

import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.Messages;
import org.perfcake.pc4idea.editor.gui.AbstractComponentGUI;
import org.perfcake.pc4idea.editor.gui.GeneratorGUI;
import org.perfcake.pc4idea.editor.interfaces.CanAddProperty;
import org.perfcake.pc4idea.editor.interfaces.ModelWrapper;

import javax.swing.*;

/**
 * Created by Stanislav Kaleta on 3/16/15.
 */
public class GeneratorModel implements ModelWrapper, CanAddProperty {
    private Scenario.Generator generatorModel;

    private GeneratorGUI generatorGUI;

    public GeneratorModel(ActionMap baseActionMap){
        generatorGUI = new GeneratorGUI(this, baseActionMap);

    }


    @Override
    public AbstractComponentGUI getGUI() {
        return generatorGUI;
    }

    @Override
    public void updateModel(Object componentModel, boolean doCommit) {
        generatorModel = (Scenario.Generator) componentModel;
        generatorGUI.updateGUI();
        if (doCommit) {
            generatorGUI.commitChanges(Messages.BUNDLE.getString("EDIT") + " Generator");
        }
    }

    @Override
    public Object retrieveModel() {
        return generatorModel;
    }

    @Override
    public void addProperty(Property property) {
        generatorModel.getProperty().add(property);
        generatorGUI.updateGUI();
        generatorGUI.commitChanges(Messages.BUNDLE.getString("ADD")+" Property");
    }
}
