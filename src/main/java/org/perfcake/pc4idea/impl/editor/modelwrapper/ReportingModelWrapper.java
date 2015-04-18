package org.perfcake.pc4idea.impl.editor.modelwrapper;

import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.gui.component.AbstractComponentGUI;
import org.perfcake.pc4idea.api.editor.modelwrapper.CanAddProperty;
import org.perfcake.pc4idea.api.editor.modelwrapper.HasGUIChildren;
import org.perfcake.pc4idea.api.editor.modelwrapper.ModelWrapper;
import org.perfcake.pc4idea.api.util.PerfCakeEditorUtil;
import org.perfcake.pc4idea.impl.editor.gui.component.ReportingGUI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stanislav Kaleta on 3/18/15.
 */
public class ReportingModelWrapper implements ModelWrapper, CanAddProperty, HasGUIChildren {
    private Scenario.Reporting reportingModel;

    private ReportingGUI reportingGUI;

    public ReportingModelWrapper(PerfCakeEditorUtil util) {
        reportingGUI = new ReportingGUI(this, util);
    }

    @Override
    public AbstractComponentGUI getGUI() {
        return reportingGUI;
    }

    @Override
    public void updateModel(Object componentModel) {
        reportingModel = (componentModel != null) ? (Scenario.Reporting) componentModel : new Scenario.Reporting();
    }

    @Override
    public Object retrieveModel() {
        return (reportingModel.getReporter().isEmpty()) ? null : reportingModel;
    }

    public void addReporter(Scenario.Reporting.Reporter reporter) {
        if (reporter == null) {
            throw new NullPointerException("reporter to add is null");
        }
        reportingModel.getReporter().add(reporter);
    }

    @Override
    public void addProperty(Property property) {
        reportingModel.getProperty().add(property);
    }

    @Override
    public List<ModelWrapper> getChildrenModels() {
        List<ModelWrapper> childrenModelList = new ArrayList<>();
        for (Scenario.Reporting.Reporter reporter : reportingModel.getReporter()) {
            ModelWrapper reporterModelWrapper = new ReporterModelWrapper(this);
            reporterModelWrapper.updateModel(reporter);
            reporterModelWrapper.getGUI().updateGUI();
            childrenModelList.add(reporterModelWrapper);
        }
        return childrenModelList;
    }

    @Override
    public void setChildrenFromModels(List<ModelWrapper> childrenModels) {
        reportingModel.getReporter().clear();
        for (ModelWrapper childModel : childrenModels) {
            reportingModel.getReporter().add((Scenario.Reporting.Reporter) childModel.retrieveModel());
        }
    }

    @Override
    public void deleteChild(ModelWrapper childModelWrapper) {
        Scenario.Reporting.Reporter reporterToDel = (Scenario.Reporting.Reporter) childModelWrapper.retrieveModel();
        reportingModel.getReporter().remove(reporterToDel);
    }


}
