package org.perfcake.pc4idea.editor.gui;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.MessagesValidationSync;
import org.perfcake.pc4idea.editor.colors.ColorAdjustable;
import org.perfcake.pc4idea.editor.colors.ColorComponents;
import org.perfcake.pc4idea.editor.colors.ColorType;
import org.perfcake.pc4idea.editor.interfaces.ModelWrapper;
import org.perfcake.pc4idea.editor.modelwrapper.*;
import org.perfcake.pc4idea.editor.swing.DependenciesPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 6.3.2015
 */
public class ScenarioGUI extends JLayeredPane implements ColorAdjustable {
    private JPanel layerBackground;
    private JPanel layerScenario;
    private DependenciesPanel layerDependencies;

    private MessagesValidationSync syncMV;

    private ModelWrapper generatorModel;
    private ModelWrapper senderModel;
    private ModelWrapper messagesModel;
    private ModelWrapper validationModel;
    private ModelWrapper reportingModel;
    private ModelWrapper propertiesModel;

    public ScenarioGUI(ActionMap actionMap){
        this.setActionMap(actionMap);

        initComponents();
        initScenarioPanel();
        updateColors();
    }

    private void initComponents(){
        layerBackground = new JPanel();
        layerScenario = new JPanel();
        layerDependencies = new DependenciesPanel();
        this.add(layerBackground, new Integer(0));
        this.add(layerScenario, new Integer(1));
        this.add(layerDependencies, new Integer(2));
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);

                layerBackground.setBounds(0, 0, ScenarioGUI.this.getSize().width, ScenarioGUI.this.getSize().height);
                layerScenario.setBounds(0, 0, ScenarioGUI.this.getSize().width, ScenarioGUI.this.getSize().height);
                layerDependencies.setBounds(0, 0, ScenarioGUI.this.getSize().width, ScenarioGUI.this.getSize().height);
                syncMV.repaintDependencies();
            }
        });

        JLabel labelError = new JLabel("Scenario is invalid!",SwingConstants.CENTER);
        labelError.setFont(new Font(labelError.getFont().getName(),labelError.getFont().getStyle(),15));
        JLabel labelHint = new JLabel("(Please continue to the Text Editor)",SwingConstants.CENTER);
        labelHint.setFont(new Font(labelHint.getFont().getName(),labelHint.getFont().getStyle(),15));
        SpringLayout layerBackgroundLayout = new SpringLayout();
        layerBackground.setLayout(layerBackgroundLayout);
        layerBackground.add(labelError);
        layerBackground.add(labelHint);
        layerBackgroundLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, labelError,
                0,
                SpringLayout.HORIZONTAL_CENTER, layerBackground);
        layerBackgroundLayout.putConstraint(SpringLayout.VERTICAL_CENTER, labelError,
                0,
                SpringLayout.VERTICAL_CENTER,layerBackground);
        layerBackgroundLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, labelHint,
                0,
                SpringLayout.HORIZONTAL_CENTER, layerBackground);
        layerBackgroundLayout.putConstraint(SpringLayout.NORTH, labelHint,
                0,
                SpringLayout.SOUTH,labelError);


        syncMV = new MessagesValidationSync(layerDependencies);
    }

    private void initScenarioPanel(){
        generatorModel = new GeneratorModelWrapper(this.getActionMap());
        senderModel = new SenderModelWrapper(this.getActionMap());
        messagesModel = new MessagesModelWrapper(this.getActionMap(), syncMV);
        validationModel = new ValidationModelWrapper(this.getActionMap(), syncMV);
        reportingModel = new ReportingModelWrapper(this.getActionMap());
        propertiesModel = new PropertiesModelWrapper(this.getActionMap());

        syncMV.setModels((MessagesModelWrapper) messagesModel, (ValidationModelWrapper) validationModel);

        GroupLayout scenarioLayout = new GroupLayout(layerScenario);
        layerScenario.setLayout(scenarioLayout);
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
            validationModel.updateModel(scenarioModel.getValidation());
            messagesModel.getGUI().updateGUI();
            validationModel.getGUI().updateGUI();

            reportingModel.updateModel(scenarioModel.getReporting());
            reportingModel.getGUI().updateGUI();
            propertiesModel.updateModel(scenarioModel.getProperties());
            propertiesModel.getGUI().updateGUI();
        } else {
            if (getScenarioModel() != null){
             /*TODO reload if possible = fireSC.INv.*/ /*TODO dont forget to hide layer dep.*/
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
        layerScenario.setBackground(ColorComponents.getColor(ColorType.SCENARIO_BACKGROUND));
    }
}
