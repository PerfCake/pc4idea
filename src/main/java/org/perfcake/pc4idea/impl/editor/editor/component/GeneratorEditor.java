package org.perfcake.pc4idea.impl.editor.editor.component;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.ValidationInfo;
import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.editor.component.AbstractEditor;
import org.perfcake.pc4idea.api.util.PerfCakeReflectUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 19.10.2014
 */
public class GeneratorEditor extends AbstractEditor {
    private JComboBox comboBoxGeneratorType;
    private JComboBox comboBoxRunType;
    private JTextField textFieldRunValue;
    private JTextField textFieldNumOfThreads;
    private PropertiesEditor panelProperties;

    private Module module;

    public GeneratorEditor(Module module) {
        this.module = module;
        initComponents();
    }

    private void initComponents(){
        JLabel labelGeneratorType = new JLabel("Generator type:");
        JLabel labelRunType = new JLabel("Run type:");
        JLabel labelRunValue = new JLabel("Duration:");
        JLabel labelNumOfThreads = new JLabel("Number of threads:");

        String[] generators = new PerfCakeReflectUtil(module).findComponentClassNames(PerfCakeReflectUtil.GENERATOR);
        comboBoxGeneratorType = new ComboBox(new DefaultComboBoxModel<>(generators));
        comboBoxGeneratorType.setSelectedIndex(-1);
        comboBoxGeneratorType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String className = (String) comboBoxGeneratorType.getSelectedItem();
                PerfCakeReflectUtil reflectUtil = new PerfCakeReflectUtil(module);
                List<Property> structureProp = reflectUtil.findComponentProperties(PerfCakeReflectUtil.GENERATOR, className);
                panelProperties.setStructureProperties(structureProp);
            }
        });

        String[] runs = new String[]{"iteration", "time", "percentage"};
        comboBoxRunType = new ComboBox(new DefaultComboBoxModel<>(generators));
        comboBoxRunType.setSelectedIndex(-1);
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
                        .addComponent(labelNumOfThreads, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
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
        panelProperties.setListProperties(generator.getProperty());

        PerfCakeReflectUtil reflectUtil = new PerfCakeReflectUtil(module);
        List<Property> structureProp = reflectUtil.findComponentProperties(PerfCakeReflectUtil.GENERATOR, generator.getClazz());
        panelProperties.setStructureProperties(structureProp);
    }

    public Scenario.Generator getGenerator(){
        Scenario.Generator newGenerator = new Scenario.Generator();
        newGenerator.setClazz((String)comboBoxGeneratorType.getSelectedItem());
        newGenerator.setThreads(textFieldNumOfThreads.getText());
        Scenario.Generator.Run newRun = new Scenario.Generator.Run();
        newRun.setType((String)comboBoxRunType.getSelectedItem());
        newRun.setValue(textFieldRunValue.getText());
        newGenerator.setRun(newRun);
        newGenerator.getProperty().addAll(panelProperties.getListProperties());
        return newGenerator;
    }

    @Override
    public String getTitle(){
        return "Generator Editor";
    }

    @Override
    public ValidationInfo areInsertedValuesValid() {
        ValidationInfo info = null;
        if (textFieldRunValue.getText().isEmpty()) {
            info = new ValidationInfo("Run value can't be empty");
        }
        if (textFieldNumOfThreads.getText().isEmpty()) {
            info = new ValidationInfo("Number of threads can't be empty");
        }
        if (comboBoxRunType.getSelectedIndex() == -1) {
            info = new ValidationInfo("Run type isn't selected");
        }
        if (comboBoxGeneratorType.getSelectedIndex() == -1){
            info = new ValidationInfo("Generator type isn't selected");
        }
        return info;
    }
}
