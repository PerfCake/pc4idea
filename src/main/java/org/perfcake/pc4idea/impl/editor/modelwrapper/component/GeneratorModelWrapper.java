package org.perfcake.pc4idea.impl.editor.modelwrapper.component;

import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.editor.ContextProvider;
import org.perfcake.pc4idea.api.editor.modelwrapper.component.AccessibleModel;
import org.perfcake.pc4idea.api.editor.modelwrapper.component.CanAddProperty;
import org.perfcake.pc4idea.api.util.Messages;
import org.perfcake.pc4idea.impl.editor.actions.CommitAction;
import org.perfcake.pc4idea.impl.editor.gui.component.GeneratorGui;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Stanislav Kaleta on 3/16/15.
 */
public class GeneratorModelWrapper implements AccessibleModel, CanAddProperty {
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
        new CommitAction(context).actionPerformed(new ActionEvent(this,1,message));
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
        Scenario.Generator tempModel = (Scenario.Generator) componentModel;
        if (generatorModel == null){
            generatorModel = tempModel;
        } else {
            generatorModel.setThreads(tempModel.getThreads());
            generatorModel.setClazz(tempModel.getClazz());
            generatorModel.getProperty().clear();
            generatorModel.getProperty().addAll(tempModel.getProperty());
        }
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
