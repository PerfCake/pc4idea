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
        scenarioGui = new ScenarioGui();
        syncMV = new MessagesValidationSync(scenarioGui.getLayerDependencies());
    }

    public ScenarioGui getScenarioGui() {
        return scenarioGui;
    }

    public void setScenarioModel(Scenario scenarioModel) {
        this.scenarioModel = scenarioModel;

        ComponentModelWrapper[] modelWrappers = null;
        if (scenarioModel != null) {
            modelWrappers = prepareModelWrappers();
        }
        scenarioGui.updateGui(modelWrappers);
    }

    public Scenario getScenarioModel() {
        Scenario tempModel = new Scenario();

        tempModel.setGenerator(scenarioModel.getGenerator());
        tempModel.setSender(scenarioModel.getSender());
        tempModel.setMessages((scenarioModel.getMessages().getMessage().isEmpty()) ?
                null : scenarioModel.getMessages());
        tempModel.setValidation((scenarioModel.getValidation().getValidator().isEmpty()) ?
                null : scenarioModel.getValidation());
        tempModel.setReporting((scenarioModel.getReporting().getReporter().isEmpty()) ?
                null : scenarioModel.getReporting());
        tempModel.setProperties((scenarioModel.getProperties().getProperty().isEmpty()) ?
                null : scenarioModel.getProperties());

        return tempModel;
    }

    public ComponentModelWrapper[] prepareModelWrappers() {
        GeneratorModelWrapper generatorModelWrapper = new GeneratorModelWrapper(context);
        generatorModelWrapper.updateModel(scenarioModel.getGenerator());
        generatorModelWrapper.updateGui();

        SenderModelWrapper senderModelWrapper = new SenderModelWrapper(context);
        senderModelWrapper.updateModel(scenarioModel.getSender());
        senderModelWrapper.updateGui();

        MessagesModelWrapper messagesModelWrapper = new MessagesModelWrapper(context, syncMV);
        ValidationModelWrapper validationModelWrapper = new ValidationModelWrapper(context, syncMV);
        syncMV.setEditorMode(messagesModelWrapper, validationModelWrapper);

        if ((scenarioModel.getMessages() == null)){
            scenarioModel.setMessages(new Scenario.Messages());
        }
        messagesModelWrapper.updateModel(scenarioModel.getMessages());
        if (scenarioModel.getValidation() == null){
            scenarioModel.setValidation(new Scenario.Validation());
        }
        validationModelWrapper.updateModel(scenarioModel.getValidation());
        messagesModelWrapper.updateGui();
        validationModelWrapper.updateGui();

        ReportingModelWrapper reportingModelWrapper = new ReportingModelWrapper(context);
        if (scenarioModel.getReporting() == null){
            scenarioModel.setReporting(new Scenario.Reporting());
        }
        reportingModelWrapper.updateModel(scenarioModel.getReporting());
        reportingModelWrapper.updateGui();

        PropertiesModelWrapper propertiesModelWrapper = new PropertiesModelWrapper(context);
        if (scenarioModel.getProperties() == null){
            scenarioModel.setProperties(new Scenario.Properties());
        }
        propertiesModelWrapper.updateModel(scenarioModel.getProperties());
        propertiesModelWrapper.updateGui();

        return new ComponentModelWrapper[]{generatorModelWrapper, senderModelWrapper,
                messagesModelWrapper, validationModelWrapper, reportingModelWrapper,
                propertiesModelWrapper};
    }

    public void repaintDependencies(){
        syncMV.repaintDependencies();
    }
}
