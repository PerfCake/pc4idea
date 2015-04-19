package org.perfcake.pc4idea.impl.editor.modelwrapper;

import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.gui.component.AbstractComponentGUI;
import org.perfcake.pc4idea.api.editor.modelwrapper.CanAddProperty;
import org.perfcake.pc4idea.api.editor.modelwrapper.HasGUIChildren;
import org.perfcake.pc4idea.api.editor.modelwrapper.ModelWrapper;
import org.perfcake.pc4idea.api.editor.modelwrapper.Togglable;
import org.perfcake.pc4idea.impl.editor.gui.component.ReporterGUI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stanislav Kaleta on 4/18/15.
 */
public class ReporterModelWrapper implements ModelWrapper, CanAddProperty, HasGUIChildren, Togglable {
    private Scenario.Reporting.Reporter reporterModel;
    private ReportingModelWrapper parent;

    private ReporterGUI reporterGUI;

    public ReporterModelWrapper(ReportingModelWrapper parent) {
        this.parent = parent;
        reporterGUI = new ReporterGUI(this, parent);
    }

    @Override
    public AbstractComponentGUI getGUI() {
        return reporterGUI;
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
            throw new NullPointerException("destination to add is null");
        }
        reporterModel.getDestination().add(destination);
    }

    @Override
    public void addProperty(Property property) {
        reporterModel.getProperty().add(property);
    }

    @Override
    public List<ModelWrapper> getChildrenModels() {
        List<ModelWrapper> childrenModelList = new ArrayList<>();
        for (Scenario.Reporting.Reporter.Destination destination : reporterModel.getDestination()) {
            ModelWrapper destinationModelWrapper = new DestinationModelWrapper(this);
            destinationModelWrapper.updateModel(destination);
            destinationModelWrapper.getGUI().updateGUI();
            childrenModelList.add(destinationModelWrapper);
        }
        return childrenModelList;
    }

    @Override
    public void setChildrenFromModels(List<ModelWrapper> childrenModels) {
        reporterModel.getDestination().clear();
        for (ModelWrapper childModel : childrenModels) {
            reporterModel.getDestination().add((Scenario.Reporting.Reporter.Destination) childModel.retrieveModel());
        }
    }

    @Override
    public void deleteChild(ModelWrapper childModelWrapper) {
        Scenario.Reporting.Reporter.Destination destinationToDel = (Scenario.Reporting.Reporter.Destination) childModelWrapper.retrieveModel();
        reporterModel.getDestination().remove(destinationToDel);
    }

    @Override
    public void setToggle(boolean toggle) {
        reporterModel.setEnabled(toggle);
    }
}
