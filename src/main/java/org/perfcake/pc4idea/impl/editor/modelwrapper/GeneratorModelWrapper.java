package org.perfcake.pc4idea.impl.editor.modelwrapper;

import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.gui.component.AbstractComponentGUI;
import org.perfcake.pc4idea.api.editor.modelwrapper.CanAddProperty;
import org.perfcake.pc4idea.api.editor.modelwrapper.ModelWrapper;
import org.perfcake.pc4idea.impl.editor.gui.component.GeneratorGUI;

import javax.swing.*;

/**
 * Created by Stanislav Kaleta on 3/16/15.
 */
public class GeneratorModelWrapper implements ModelWrapper, CanAddProperty {
    private Scenario.Generator generatorModel;

    private GeneratorGUI generatorGUI;

    public GeneratorModelWrapper(ActionMap baseActionMap){
        generatorGUI = new GeneratorGUI(this, baseActionMap);

    }

    @Override
    public AbstractComponentGUI getGUI() {
        return generatorGUI;
    }

    @Override
    public void updateModel(Object componentModel) {
        generatorModel = (Scenario.Generator) componentModel;
    }

    @Override
    public Object retrieveModel() {
        return generatorModel;
    }

    @Override
    public void addProperty(Property property) {
        generatorModel.getProperty().add(property);
    }
}
