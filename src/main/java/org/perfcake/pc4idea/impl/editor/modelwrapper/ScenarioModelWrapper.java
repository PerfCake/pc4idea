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
        Scenario tempModel = scenarioModel;
        if (tempModel.getMessages().getMessage().isEmpty()){
            tempModel.setMessages(null);
        }
        if (tempModel.getValidation().getValidator().isEmpty()){
            tempModel.setValidation(null);
        }
        if (tempModel.getReporting().getReporter().isEmpty()){
            tempModel.setReporting(null);
        }
//        if (tempModel.getProperties().getProperty().isEmpty()){
//            tempModel.setProperties(null);
//        }
        if (tempModel.getProperties() == null){
            System.out.println("FFFFFFFU");/*TODO*/
        }
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


        messagesModelWrapper.updateModel((scenarioModel.getMessages() == null) ?
                new Scenario.Messages() : scenarioModel.getMessages());
        validationModelWrapper.updateModel((scenarioModel.getValidation() == null) ?
                new Scenario.Validation() : scenarioModel.getValidation());
        messagesModelWrapper.updateGui();
        validationModelWrapper.updateGui();

        ReportingModelWrapper reportingModelWrapper = new ReportingModelWrapper(context);
        reportingModelWrapper.updateModel((scenarioModel.getReporting() == null) ?
                new Scenario.Reporting() : scenarioModel.getReporting());
        reportingModelWrapper.updateGui();

        PropertiesModelWrapper propertiesModelWrapper = new PropertiesModelWrapper(context);
        propertiesModelWrapper.updateModel((scenarioModel.getProperties() == null) ?
                new Scenario.Properties() : scenarioModel.getProperties());
        propertiesModelWrapper.updateGui();

        return new ComponentModelWrapper[]{generatorModelWrapper, senderModelWrapper,
                messagesModelWrapper, validationModelWrapper, reportingModelWrapper,
                propertiesModelWrapper};
    }

    public void repaintDependencies(){
        syncMV.repaintDependencies();
    }
}
