package org.perfcake.pc4idea.impl.editor.modelwrapper.component;

import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.editor.ContextProvider;
import org.perfcake.pc4idea.api.editor.modelwrapper.component.AccessibleModel;
import org.perfcake.pc4idea.api.editor.modelwrapper.component.CanAddProperty;
import org.perfcake.pc4idea.api.editor.modelwrapper.component.HasGUIChildren;
import org.perfcake.pc4idea.api.util.Messages;
import org.perfcake.pc4idea.impl.editor.actions.CommitAction;
import org.perfcake.pc4idea.impl.editor.gui.component.ReportingGui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stanislav Kaleta on 3/18/15.
 */
public class ReportingModelWrapper implements AccessibleModel, CanAddProperty, HasGUIChildren {
    private Scenario.Reporting reportingModel;

    private ReportingGui reportingGui;

    private ContextProvider context;

    public ReportingModelWrapper(ContextProvider context) {
        this.context = context;
        reportingGui = new ReportingGui(this);
    }

    @Override
    public ContextProvider getContext() {
        return context;
    }

    @Override
    public String getName() {
        return Messages.Scenario.REPORTING;
    }

    @Override
    public void commit(String message) {
        new CommitAction(context).actionPerformed(new ActionEvent(this,1,message));
    }

    @Override
    public JPanel getGui() {
        return reportingGui;
    }

    @Override
    public void updateGui() {
        reportingGui.updateGui();
    }

    @Override
    public void updateModel(Object componentModel) {
        Scenario.Reporting tempModel = (Scenario.Reporting) componentModel;
        if (reportingModel == null){
            reportingModel = tempModel;
        } else {
            reportingModel.getProperty().clear();
            reportingModel.getProperty().addAll(tempModel.getProperty());
            reportingModel.getReporter().clear();
            reportingModel.getReporter().addAll(tempModel.getReporter());
        }
    }

    @Override
    public Object retrieveModel() {
        return reportingModel;
    }

    public void addReporter(Scenario.Reporting.Reporter reporter) {
        if (reporter == null) {
            throw new NullPointerException(Messages.Exception.ADD_NULL_REPORTER);
        }
        reportingModel.getReporter().add(reporter);
    }

    @Override
    public void addProperty(Property property) {
        reportingModel.getProperty().add(property);
    }

    @Override
    public List<AccessibleModel> getChildrenModels() {
        List<AccessibleModel> childrenModelList = new ArrayList<>();
        for (Scenario.Reporting.Reporter reporter : reportingModel.getReporter()) {
            AccessibleModel reporterModelWrapper = new ReporterModelWrapper(this);
            reporterModelWrapper.updateModel(reporter);
            reporterModelWrapper.updateGui();
            childrenModelList.add(reporterModelWrapper);
        }
        return childrenModelList;
    }

    @Override
    public void setChildrenFromModels(List<AccessibleModel> childrenModels) {
        reportingModel.getReporter().clear();
        for (AccessibleModel childModel : childrenModels) {
            reportingModel.getReporter().add((Scenario.Reporting.Reporter) childModel.retrieveModel());
        }
    }

    @Override
    public void deleteChild(AccessibleModel childModelWrapper) {
        Scenario.Reporting.Reporter reporterToDel =
                (Scenario.Reporting.Reporter) childModelWrapper.retrieveModel();
        reportingModel.getReporter().remove(reporterToDel);
    }
}
