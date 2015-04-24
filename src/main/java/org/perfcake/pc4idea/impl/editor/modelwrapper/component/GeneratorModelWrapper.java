package org.perfcake.pc4idea.impl.editor.modelwrapper.component;

import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.editor.ContextProvider;
import org.perfcake.pc4idea.api.editor.modelwrapper.component.CanAddProperty;
import org.perfcake.pc4idea.api.editor.modelwrapper.component.ComponentModelWrapper;
import org.perfcake.pc4idea.api.util.Messages;
import org.perfcake.pc4idea.impl.editor.gui.component.GeneratorGui;

import javax.swing.*;

/**
 * Created by Stanislav Kaleta on 3/16/15.
 */
public class GeneratorModelWrapper implements ComponentModelWrapper, CanAddProperty {
    private Scenario.Generator generatorModel;

    private GeneratorGui generatorGui;

    private ContextProvider context;

    public GeneratorModelWrapper(ContextProvider context) {
        this.context = context;
        generatorGui = new GeneratorGui(this);
    }

    @Override
    public ContextProvider getContext() {
        return context;
    }

    @Override
    public String getName() {
        return Messages.Scenario.GENERATOR;
    }

    @Override
    public void commit(String message) {
        /*TODO*/
    }

    @Override
    public JPanel getGui() {
        return generatorGui;
    }

    @Override
    public void updateGui() {
        generatorGui.updateGui();
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
