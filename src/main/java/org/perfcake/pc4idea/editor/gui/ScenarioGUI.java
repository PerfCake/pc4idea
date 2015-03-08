package org.perfcake.pc4idea.editor.gui;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.colors.ColorAdjustable;
import org.perfcake.pc4idea.editor.colors.ColorComponents;
import org.perfcake.pc4idea.editor.colors.ColorType;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 6.3.2015
 */
public class ScenarioGUI extends JPanel implements ColorAdjustable {
    private AbstractComponentGUI generatorGUI;
    private AbstractComponentGUI senderGUI;
    private AbstractComponentGUI messagesGUI;
    private AbstractComponentGUI validationGUI;
    private AbstractComponentGUI reportingGUI;
    private AbstractComponentGUI propertiesGUI;

    public ScenarioGUI(ActionMap actionMap){
        this.setActionMap(actionMap);

        initComponents();/*TODO LAYERS!!!!!*/
        updateColors();
    }

    private void initComponents(){
        generatorGUI = new GeneratorGUI(this.getActionMap());
        senderGUI = new SenderGUI(this.getActionMap());
        messagesGUI = new MessagesGUI(this.getActionMap());
        validationGUI = new ValidationGUI(this.getActionMap());
        reportingGUI = new ReportingGUI(this.getActionMap());
        propertiesGUI = new PropertiesGUI(this.getActionMap());


        GroupLayout scenarioLayout = new GroupLayout(this);
        this.setLayout(scenarioLayout);
        scenarioLayout.setHorizontalGroup(
                scenarioLayout.createParallelGroup()
                        .addComponent(generatorGUI)
                        .addComponent(senderGUI)
                        .addGroup(scenarioLayout.createSequentialGroup()
                                .addGroup(scenarioLayout.createParallelGroup()
                                        .addComponent(messagesGUI)
                                        .addComponent(validationGUI))
                                .addComponent(reportingGUI))
                        .addComponent(propertiesGUI)
        );
        scenarioLayout.setVerticalGroup(
                scenarioLayout.createSequentialGroup()
                        .addComponent(generatorGUI)
                        .addComponent(senderGUI)
                        .addGroup(scenarioLayout.createParallelGroup()
                                .addGroup(scenarioLayout.createSequentialGroup()
                                        .addComponent(messagesGUI)
                                        .addComponent(validationGUI))
                                .addComponent(reportingGUI))
                        .addComponent(propertiesGUI)
                        .addContainerGap(0, Short.MAX_VALUE)
        );

    }

    public Scenario getScenarioModel() {
        Scenario scenarioModel = new Scenario();
        scenarioModel.setGenerator((Scenario.Generator) generatorGUI.getComponentModel());
        scenarioModel.setSender((Scenario.Sender) senderGUI.getComponentModel());
        scenarioModel.setMessages((Scenario.Messages) messagesGUI.getComponentModel());
        scenarioModel.setValidation((Scenario.Validation) validationGUI.getComponentModel());
        scenarioModel.setReporting((Scenario.Reporting) reportingGUI.getComponentModel());
        scenarioModel.setProperties((Scenario.Properties) propertiesGUI.getComponentModel());
        return scenarioModel;
    }

    public void setScenarioModel(Scenario scenarioModel) {
        if (scenarioModel != null) {
            generatorGUI.setComponentModel(scenarioModel.getGenerator());
            senderGUI.setComponentModel(scenarioModel.getSender());
            messagesGUI.setComponentModel(scenarioModel.getMessages());
            validationGUI.setComponentModel(scenarioModel.getValidation());
            reportingGUI.setComponentModel(scenarioModel.getReporting());
            propertiesGUI.setComponentModel(scenarioModel.getProperties());
        } else {
            if (getScenarioModel() != null){
             /*TODO reload if possible = fireSC.INv.*/
            }
        }
    }

    @Override
    public void updateColors() {
        setBackground(ColorComponents.getColor(ColorType.SCENARIO_BACKGROUND));
    }
}
