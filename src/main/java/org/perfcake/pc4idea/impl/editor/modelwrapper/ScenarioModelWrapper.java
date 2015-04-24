package org.perfcake.pc4idea.impl.editor.modelwrapper;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.editor.ContextProvider;
import org.perfcake.pc4idea.api.editor.modelwrapper.component.ComponentModelWrapper;
import org.perfcake.pc4idea.api.util.MessagesValidationSync;
import org.perfcake.pc4idea.impl.editor.gui.ScenarioGui;
import org.perfcake.pc4idea.impl.editor.modelwrapper.component.*;

/**
 * Created by Stanislav Kaleta on 4/23/15.
 */
public class ScenarioModelWrapper {
    private Scenario scenarioModel;

    private ScenarioGui scenarioGui;

    private MessagesValidationSync syncMV;
    private ContextProvider context;

    public ScenarioModelWrapper(ContextProvider context){
        this.context = context;
        scenarioGui = new ScenarioGui(this);

        syncMV = new MessagesValidationSync(scenarioGui.getLayerDependencies());
    }

    public ScenarioGui getScenarioGui() {
        return scenarioGui;
    }

    public void setScenarioModel(Scenario scenarioModel) {
        this.scenarioModel = scenarioModel;
        scenarioGui.updateGui();
    }

    public Scenario getScenarioModel() {
        return scenarioModel;
    }

    public ComponentModelWrapper[] getScenarioComponents() {
        GeneratorModelWrapper generatorModelWrapper = new GeneratorModelWrapper(context);
        generatorModelWrapper.updateModel(scenarioModel.getGenerator());
        generatorModelWrapper.updateGui();

        SenderModelWrapper senderModelWrapper = new SenderModelWrapper(context);
        senderModelWrapper.updateModel(scenarioModel.getSender());
        senderModelWrapper.updateGui();

        MessagesModelWrapper messagesModelWrapper = new MessagesModelWrapper(context, syncMV);
        ValidationModelWrapper validationModelWrapper = new ValidationModelWrapper(context, syncMV);
        syncMV.setEditorMode(messagesModelWrapper, validationModelWrapper);

        messagesModelWrapper.updateModel(scenarioModel.getMessages());
        validationModelWrapper.updateModel(scenarioModel.getValidation());
        messagesModelWrapper.updateGui();
        validationModelWrapper.updateGui();

        ReportingModelWrapper reportingModelWrapper = new ReportingModelWrapper(context);
        reportingModelWrapper.updateModel(scenarioModel.getReporting());
        reportingModelWrapper.updateGui();

        PropertiesModelWrapper propertiesModelWrapper = new PropertiesModelWrapper(context);
        propertiesModelWrapper.updateModel(scenarioModel.getProperties());
        propertiesModelWrapper.updateGui();

        return new ComponentModelWrapper[]{generatorModelWrapper, senderModelWrapper,
                messagesModelWrapper, validationModelWrapper, reportingModelWrapper,
                propertiesModelWrapper};
    }
}
