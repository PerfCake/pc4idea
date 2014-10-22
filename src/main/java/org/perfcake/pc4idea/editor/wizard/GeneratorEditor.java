package org.perfcake.pc4idea.editor.wizard;

import com.intellij.openapi.ui.ComboBox;
import org.perfcake.model.Scenario;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 19.10.2014
 */
public class GeneratorEditor extends JPanel {
    private JLabel labelGeneratorType;
    private JLabel labelRunType;
    private JLabel labelRunValue;
    private JLabel labelNumOfThreads;
    private JComboBox comboBoxGeneratorType;
    private JComboBox comboBoxRunType;
    private JTextField textFieldRunValue;
    private JTextField textFieldNumOfThreads;
    private PropertiesEditor panelProperties;

    public GeneratorEditor(){
        initComponents();
        this.setPreferredSize(new Dimension(350,0));
    }

    private void initComponents(){
        labelGeneratorType = new JLabel("Generator type:");
        labelRunType = new JLabel("Run type:");
        labelRunValue = new JLabel("Duration:");
        labelNumOfThreads = new JLabel("Number of threads:");
        comboBoxGeneratorType = new ComboBox();
        comboBoxGeneratorType.addItem("DefaultMessageGenerator");
        comboBoxGeneratorType.addItem("RampUpDownGenerator");
        comboBoxRunType = new ComboBox();
        comboBoxRunType.addItem("iteration");
        comboBoxRunType.addItem("time");
        comboBoxRunType.addItem("percentage");
        textFieldRunValue = new JTextField();
        textFieldNumOfThreads = new JTextField();
        panelProperties = new PropertiesEditor();

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup()
                    .addGroup(layout.createSequentialGroup()
                            .addComponent(labelGeneratorType,GroupLayout.PREFERRED_SIZE,120,GroupLayout.PREFERRED_SIZE)
                            .addComponent(comboBoxGeneratorType))
                    .addGroup(layout.createSequentialGroup()
                            .addComponent(labelRunType,GroupLayout.PREFERRED_SIZE,120,GroupLayout.PREFERRED_SIZE)
                            .addComponent(comboBoxRunType))
                    .addGroup(layout.createSequentialGroup()
                            .addComponent(labelRunValue,GroupLayout.PREFERRED_SIZE,120,GroupLayout.PREFERRED_SIZE)
                            .addComponent(textFieldRunValue))
                    .addGroup(layout.createSequentialGroup()
                            .addComponent(labelNumOfThreads,GroupLayout.PREFERRED_SIZE,120,GroupLayout.PREFERRED_SIZE)
                            .addComponent(textFieldNumOfThreads))
                    .addComponent(panelProperties));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelGeneratorType, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                        .addComponent(comboBoxGeneratorType, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                .addGap(10)
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelRunType, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                        .addComponent(comboBoxRunType, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                .addGap(10)
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelRunValue, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                        .addComponent(textFieldRunValue, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                .addGap(10)
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelNumOfThreads, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                        .addComponent(textFieldNumOfThreads, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                .addGap(10)
                .addComponent(panelProperties));
    }

    public void setGenerator(Scenario.Generator generator){
        comboBoxGeneratorType.setSelectedItem(generator.getClazz());
        comboBoxRunType.setSelectedItem(generator.getRun().getType());
        textFieldRunValue.setText(generator.getRun().getValue());
        textFieldNumOfThreads.setText(generator.getThreads());
        panelProperties.setProperties(generator.getProperty());
    }

    public boolean areInsertedValuesValid() {
        //boolean areValid = false;
        /*TODO check validity of inserted data - if false OK button disabled*/
        return true;
    }

    public Scenario.Generator getGenerator(){
        Scenario.Generator newGenerator = new Scenario.Generator();
        newGenerator.setClazz((String)comboBoxGeneratorType.getSelectedItem());
        newGenerator.setThreads(textFieldNumOfThreads.getText());
        Scenario.Generator.Run newRun = new Scenario.Generator.Run();
        newRun.setType((String)comboBoxRunType.getSelectedItem());
        newRun.setValue(textFieldRunValue.getText());
        newGenerator.setRun(newRun);
        newGenerator.getProperty().addAll(panelProperties.getProperties());
        return newGenerator;
    }
}
