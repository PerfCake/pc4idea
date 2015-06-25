package org.perfcake.pc4idea.impl.editor.modelwrapper.component;

import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.editor.ContextProvider;
import org.perfcake.pc4idea.api.editor.modelwrapper.component.AccessibleModel;
import org.perfcake.pc4idea.api.editor.modelwrapper.component.CanAddProperty;
import org.perfcake.pc4idea.api.editor.modelwrapper.component.Togglable;
import org.perfcake.pc4idea.api.util.Messages;
import org.perfcake.pc4idea.impl.editor.actions.CommitAction;
import org.perfcake.pc4idea.impl.editor.gui.component.DestinationGui;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Stanislav Kaleta on 4/18/15.
 */
public class DestinationModelWrapper implements AccessibleModel, CanAddProperty, Togglable {
    private Scenario.Reporting.Reporter.Destination destinationModel;

    private DestinationGui destinationGui;

    private ContextProvider context;

    public DestinationModelWrapper(ReporterModelWrapper parent) {
        context = parent.getContext();
        destinationGui = new DestinationGui(this, parent);
    }

    @Override
    public ContextProvider getContext() {
        return context;
    }

    @Override
    public String getName() {
        return Messages.Scenario.DESTINATION;
    }

    @Override
    public void commit(String message) {
        new CommitAction(context).actionPerformed(new ActionEvent(this,1,message));
    }

    @Override
    public JPanel getGui() {
        return destinationGui;
    }

    @Override
    public void updateGui() {
        destinationGui.updateGui();
    }

    @Override
    public void updateModel(Object componentModel) {
        Scenario.Reporting.Reporter.Destination tempModel = (Scenario.Reporting.Reporter.Destination) componentModel;
        if (destinationModel == null) {
            destinationModel = tempModel;
        } else {
            destinationModel.setClazz(tempModel.getClazz());
            destinationModel.setEnabled(tempModel.isEnabled());
            destinationModel.getProperty().clear();
            destinationModel.getProperty().addAll(tempModel.getProperty());
            destinationModel.getPeriod().clear();
            destinationModel.getPeriod().addAll(tempModel.getPeriod());
        }
    }

    @Override
    public Object retrieveModel() {
        return destinationModel;
    }

    public void addPeriod(Scenario.Reporting.Reporter.Destination.Period period) {
        destinationModel.getPeriod().add(period);
    }

    @Override
    public void addProperty(Property property) {
        destinationModel.getProperty().add(property);
    }

    @Override
    public void setToggle(boolean toggle) {
        destinationModel.setEnabled(toggle);
    }
}
