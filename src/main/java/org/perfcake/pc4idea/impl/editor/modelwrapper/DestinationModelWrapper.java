package org.perfcake.pc4idea.impl.editor.modelwrapper;

import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.gui.component.AbstractComponentGUI;
import org.perfcake.pc4idea.api.editor.modelwrapper.CanAddProperty;
import org.perfcake.pc4idea.api.editor.modelwrapper.ModelWrapper;
import org.perfcake.pc4idea.api.editor.modelwrapper.Togglable;
import org.perfcake.pc4idea.impl.editor.gui.component.DestinationGUI;

/**
 * Created by Stanislav Kaleta on 4/18/15.
 */
public class DestinationModelWrapper implements ModelWrapper, CanAddProperty, Togglable {
    private Scenario.Reporting.Reporter.Destination destinationModel;
    private ReporterModelWrapper parent;

    private DestinationGUI destinationGUI;

    public DestinationModelWrapper(ReporterModelWrapper parent) {
        this.parent = parent;
        destinationGUI = new DestinationGUI(this, parent);
    }

    @Override
    public AbstractComponentGUI getGUI() {
        return destinationGUI;
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
