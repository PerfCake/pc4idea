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
 * Date: 23.10.2014
 */
public class SenderEditor extends AbstractEditor {
    private JComboBox comboBoxSenderType;
    private PropertiesEditor panelProperties;

    private Module module;

    public SenderEditor(Module module) {
        this.module = module;
        initComponents();
    }

    private void initComponents() {
        JLabel labelSenderType = new JLabel("Sender type:");

        String[] senders = new PerfCakeReflectUtil(module).findComponentClassNames(PerfCakeReflectUtil.SENDER);
        comboBoxSenderType = new ComboBox(new DefaultComboBoxModel<>(senders));
        comboBoxSenderType.setSelectedIndex(-1);
        comboBoxSenderType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String className = (String) comboBoxSenderType.getSelectedItem();
                PerfCakeReflectUtil reflectUtil = new PerfCakeReflectUtil(module);
                List<Property> structureProp = reflectUtil.findComponentProperties(PerfCakeReflectUtil.SENDER, className);
                panelProperties.setStructureProperties(structureProp);
            }
        });

        panelProperties = new PropertiesEditor();

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                        .addComponent(labelSenderType, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                        .addComponent(comboBoxSenderType))
                .addComponent(panelProperties));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelSenderType, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                        .addComponent(comboBoxSenderType, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                .addGap(10)
                .addComponent(panelProperties));
    }

    public void setSender(Scenario.Sender sender){
        comboBoxSenderType.setSelectedItem(sender.getClazz());
        panelProperties.setListProperties(sender.getProperty());

        PerfCakeReflectUtil reflectUtil = new PerfCakeReflectUtil(module);
        List<Property> structureProp = reflectUtil.findComponentProperties(PerfCakeReflectUtil.SENDER, sender.getClazz());
        panelProperties.setStructureProperties(structureProp);
    }

    public Scenario.Sender getSender(){
        Scenario.Sender newSender = new Scenario.Sender();
        newSender.setClazz((String)comboBoxSenderType.getSelectedItem());
        newSender.getProperty().addAll(panelProperties.getListProperties());
        return newSender;
    }

    @Override
    public String getTitle(){
        return "Sender Editor";
    }

    @Override
    public ValidationInfo areInsertedValuesValid() {
        return (comboBoxSenderType.getSelectedIndex() == -1) ? new ValidationInfo("Sender type isn't selected") : null;
    }
}
