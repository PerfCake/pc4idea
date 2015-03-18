package org.perfcake.pc4idea.editor.gui;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.colors.ColorAdjustable;
import org.perfcake.pc4idea.editor.colors.ColorComponents;
import org.perfcake.pc4idea.editor.colors.ColorType;
import org.perfcake.pc4idea.editor.interfaces.ModelWrapper;
import org.perfcake.pc4idea.editor.modelwrapper.*;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 6.3.2015
 */
public class ScenarioGUI extends JPanel implements ColorAdjustable {
    private ModelWrapper generatorModel;
    private ModelWrapper senderModel;
    private ModelWrapper messagesModel;
    private ModelWrapper validationModel;
    private ModelWrapper reportingModel;
    private ModelWrapper propertiesModel;

    public ScenarioGUI(ActionMap actionMap){
        this.setActionMap(actionMap);

        initComponents();/*TODO LAYERS!!!!!*/
        updateColors();
    }

    private void initComponents(){
        generatorModel = new GeneratorModelWrapper(this.getActionMap());
        senderModel = new SenderModelWrapper(this.getActionMap());
        messagesModel = new MessagesModelWrapper(this.getActionMap());
        validationModel = new ValidationModelWrapper(this.getActionMap());
        reportingModel = new ReportingModelWrapper(this.getActionMap());
        propertiesModel = new PropertiesModelWrapper(this.getActionMap());


        GroupLayout scenarioLayout = new GroupLayout(this);
        this.setLayout(scenarioLayout);
        scenarioLayout.setHorizontalGroup(
                scenarioLayout.createParallelGroup()
                        .addComponent(generatorModel.getGUI())
                        .addComponent(senderModel.getGUI())
                        .addGroup(scenarioLayout.createSequentialGroup()
                                .addGroup(scenarioLayout.createParallelGroup()
                                        .addComponent(messagesModel.getGUI())
                                        .addComponent(validationModel.getGUI()))
                                .addComponent(reportingModel.getGUI()))
                        .addComponent(propertiesModel.getGUI())
        );
        scenarioLayout.setVerticalGroup(
                scenarioLayout.createSequentialGroup()
                        .addComponent(generatorModel.getGUI())
                        .addComponent(senderModel.getGUI())
                        .addGroup(scenarioLayout.createParallelGroup()
                                .addGroup(scenarioLayout.createSequentialGroup()
                                        .addComponent(messagesModel.getGUI())
                                        .addComponent(validationModel.getGUI()))
                                .addComponent(reportingModel.getGUI()))
                        .addComponent(propertiesModel.getGUI())
                        .addContainerGap(0, Short.MAX_VALUE)
        );

    }

    public Scenario getScenarioModel() {
        Scenario scenarioModel = new Scenario();
        scenarioModel.setGenerator((Scenario.Generator) generatorModel.retrieveModel());
        scenarioModel.setSender((Scenario.Sender) senderModel.retrieveModel());
        scenarioModel.setMessages((Scenario.Messages) messagesModel.retrieveModel());
        scenarioModel.setValidation((Scenario.Validation) validationModel.retrieveModel());
        scenarioModel.setReporting((Scenario.Reporting) reportingModel.retrieveModel());
        scenarioModel.setProperties((Scenario.Properties) propertiesModel.retrieveModel());
        return scenarioModel;
    }

    public void setScenarioModel(Scenario scenarioModel) {
        if (scenarioModel != null) {
            generatorModel.updateModel(scenarioModel.getGenerator());
            generatorModel.getGUI().updateGUI();
            senderModel.updateModel(scenarioModel.getSender());
            senderModel.getGUI().updateGUI();
            messagesModel.updateModel(scenarioModel.getMessages());
            messagesModel.getGUI().updateGUI();
            validationModel.updateModel(scenarioModel.getValidation());
            validationModel.getGUI().updateGUI();
            reportingModel.updateModel(scenarioModel.getReporting());
            reportingModel.getGUI().updateGUI();
            propertiesModel.updateModel(scenarioModel.getProperties());
            propertiesModel.getGUI().updateGUI();
        } else {
            if (getScenarioModel() != null){
             /*TODO reload if possible = fireSC.INv.*/
            }
        }
    }

    public ModelWrapper getComponentModel(int modelNumber){
        switch (modelNumber){
            case 0:
                return generatorModel;
            case 1:
                return senderModel;
            case 2:
                return messagesModel;


            default:
                return null;
        }
    }

    @Override
    public void updateColors() {
        setBackground(ColorComponents.getColor(ColorType.SCENARIO_BACKGROUND));
    }
}
