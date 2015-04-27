package org.perfcake.pc4idea.impl.editor.modelwrapper.component;

import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.editor.ContextProvider;
import org.perfcake.pc4idea.api.editor.modelwrapper.component.AccessibleModel;
import org.perfcake.pc4idea.api.editor.modelwrapper.component.CanAddProperty;
import org.perfcake.pc4idea.api.editor.modelwrapper.component.HasGUIChildren;
import org.perfcake.pc4idea.api.editor.modelwrapper.component.Togglable;
import org.perfcake.pc4idea.api.util.Messages;
import org.perfcake.pc4idea.impl.editor.actions.CommitAction;
import org.perfcake.pc4idea.impl.editor.gui.component.ReporterGui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stanislav Kaleta on 4/18/15.
 */
public class ReporterModelWrapper implements AccessibleModel, CanAddProperty, HasGUIChildren, Togglable {
    private Scenario.Reporting.Reporter reporterModel;

    private ReporterGui reporterGui;

    private ContextProvider context;

    public ReporterModelWrapper(ReportingModelWrapper parent) {
        context = parent.getContext();
        reporterGui = new ReporterGui(this, parent);
    }

    @Override
    public ContextProvider getContext() {
        return context;
    }

    @Override
    public String getName() {
        return Messages.Scenario.REPORTER;
    }

    @Override
    public void commit(String message) {
        new CommitAction(context).actionPerformed(new ActionEvent(this,1,message));
    }

    @Override
    public JPanel getGui() {
        return reporterGui;
    }

    @Override
    public void updateGui() {
        reporterGui.updateGui();
    }

    @Override
    public void updateModel(Object componentModel) {
        Scenario.Reporting.Reporter tempModel = (Scenario.Reporting.Reporter) componentModel;
        if (reporterModel == null) {
            reporterModel = tempModel;
        } else {
            reporterModel.setClazz(tempModel.getClazz());
            reporterModel.setEnabled(tempModel.isEnabled());
            reporterModel.getProperty().clear();
            reporterModel.getProperty().addAll(tempModel.getProperty());
            reporterModel.getDestination().clear();
            reporterModel.getDestination().addAll(tempModel.getDestination());
        }
    }

    @Override
    public Object retrieveModel() {
        return reporterModel;
    }

    public void addDestination(Scenario.Reporting.Reporter.Destination destination) {
        if (destination == null) {
            throw new NullPointerException(Messages.Exception.ADD_NULL_DESTINATION);
        }
        reporterModel.getDestination().add(destination);
    }

    @Override
    public void addProperty(Property property) {
        reporterModel.getProperty().add(property);
    }

    @Override
    public List<AccessibleModel> getChildrenModels() {
        List<AccessibleModel> childrenModelList = new ArrayList<>();
        for (Scenario.Reporting.Reporter.Destination destination : reporterModel.getDestination()) {
            AccessibleModel destinationModelWrapper = new DestinationModelWrapper(this);
            destinationModelWrapper.updateModel(destination);
            destinationModelWrapper.updateGui();
            childrenModelList.add(destinationModelWrapper);
        }
        return childrenModelList;
    }

    @Override
    public void setChildrenFromModels(List<AccessibleModel> childrenModels) {
        reporterModel.getDestination().clear();
        for (AccessibleModel childModel : childrenModels) {
            reporterModel.getDestination().add((Scenario.Reporting.Reporter.Destination) childModel.retrieveModel());
        }
    }

    @Override
    public void deleteChild(AccessibleModel childModelWrapper) {
        Scenario.Reporting.Reporter.Destination destinationToDel =
                (Scenario.Reporting.Reporter.Destination) childModelWrapper.retrieveModel();
        reporterModel.getDestination().remove(destinationToDel);
    }

    @Override
    public void setToggle(boolean toggle) {
        reporterModel.setEnabled(toggle);
    }
}
