package org.perfcake.pc4idea.impl.editor.editor.component;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.ScrollPaneFactory;
import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.editor.component.AbstractEditor;
import org.perfcake.pc4idea.api.editor.swing.SenderPropertiesTable;
import org.perfcake.pc4idea.api.util.PerfCakeReflectUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 23.10.2014
 */
public class SenderEditor extends AbstractEditor {
    private JComboBox comboBoxSenderType;
    private SenderPropertiesTable senderPropertiesTable;
    private JScrollPane scrollPaneTable;

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
        comboBoxSenderType.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String className = (String) comboBoxSenderType.getSelectedItem();
                PerfCakeReflectUtil reflectUtil = new PerfCakeReflectUtil(module);
                List<Property> properties = reflectUtil.findComponentProperties(PerfCakeReflectUtil.SENDER, className);
                senderPropertiesTable.setStructureProperties(properties);
            }
        });

        senderPropertiesTable = new SenderPropertiesTable();
        scrollPaneTable = ScrollPaneFactory.createScrollPane(senderPropertiesTable);
        scrollPaneTable.setMinimumSize(new Dimension(scrollPaneTable.getMinimumSize().width, 100));

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                        .addComponent(labelSenderType, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                        .addComponent(comboBoxSenderType))
                .addComponent(scrollPaneTable));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelSenderType, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                        .addComponent(comboBoxSenderType, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                .addGap(10)
                .addComponent(scrollPaneTable));
    }

    public void setSender(Scenario.Sender sender){
        comboBoxSenderType.setSelectedItem(sender.getClazz());

        senderPropertiesTable.setModelProperties(sender.getProperty());

        PerfCakeReflectUtil reflectUtil = new PerfCakeReflectUtil(module);
        List<Property> properties = reflectUtil.findComponentProperties(PerfCakeReflectUtil.SENDER, sender.getClazz());
        senderPropertiesTable.setStructureProperties(properties);
    }

    public Scenario.Sender getSender(){
        Scenario.Sender newSender = new Scenario.Sender();
        newSender.setClazz((String)comboBoxSenderType.getSelectedItem());
        newSender.getProperty().addAll(senderPropertiesTable.getProperties());
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
