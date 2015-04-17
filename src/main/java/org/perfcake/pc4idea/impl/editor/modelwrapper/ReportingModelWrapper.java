package org.perfcake.pc4idea.impl.editor.modelwrapper;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.gui.component.AbstractComponentGUI;
import org.perfcake.pc4idea.api.editor.modelwrapper.ModelWrapper;
import org.perfcake.pc4idea.impl.editor.gui.component.ReportingGUI;

import javax.swing.*;

/**
 * Created by Stanislav Kaleta on 3/18/15.
 */
public class ReportingModelWrapper implements ModelWrapper {
    private Scenario.Reporting reportingModel;

    private ReportingGUI reportingGUI;

    public ReportingModelWrapper(ActionMap baseActionMap){
        reportingGUI = new ReportingGUI(this, baseActionMap);

    }

    @Override
    public AbstractComponentGUI getGUI() {
        return reportingGUI;
    }

    @Override
    public void updateModel(Object componentModel) {

    }

    @Override
    public Object retrieveModel() {
        return null;
    }
}
