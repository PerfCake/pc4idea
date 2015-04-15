package org.perfcake.pc4idea.editor.editors;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.ScrollPaneFactory;
import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.PerfCakeEditorUtil;
import org.perfcake.pc4idea.editor.PerfCakeReflectUtil;
import org.perfcake.pc4idea.editor.swing.SenderPropertiesTable;

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
    private PropertiesEditor panelProperties;
    private SenderPropertiesTable senderPropertiesTable;
    private JScrollPane scrollPaneTable;

    private PerfCakeEditorUtil util;

    public SenderEditor(PerfCakeEditorUtil util) {
        this.util = util;
        initComponents();
    }

    private void initComponents() {
        JLabel labelSenderType = new JLabel("Sender type:");

        String[] senders = new PerfCakeReflectUtil(util).findSenderClassNames();
        comboBoxSenderType = new ComboBox(new DefaultComboBoxModel<>(senders));
        comboBoxSenderType.setSelectedIndex(-1);
        comboBoxSenderType.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String newClass = (String) comboBoxSenderType.getSelectedItem();
                List<Property> properties = new PerfCakeReflectUtil(util).findSenderProperties(newClass);
                if (properties == null) {
                    scrollPaneTable.setVisible(false);
                    panelProperties.setVisible(true);
                } else {
                    senderPropertiesTable.updateStructure(properties);
                    scrollPaneTable.setVisible(true);
                    panelProperties.setVisible(false);
                }

                /*TODO if null hide panelP. and show newPanelP. and vice versa*/
            }
        });

        panelProperties = new PropertiesEditor();
        senderPropertiesTable = new SenderPropertiesTable();
        scrollPaneTable = ScrollPaneFactory.createScrollPane(senderPropertiesTable);
        scrollPaneTable.setMinimumSize(new Dimension(scrollPaneTable.getMinimumSize().width, 100));

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                        .addComponent(labelSenderType, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                        .addComponent(comboBoxSenderType))
                .addComponent(panelProperties)
                .addComponent(scrollPaneTable));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelSenderType, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                        .addComponent(comboBoxSenderType, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                .addGap(10)
                .addComponent(panelProperties)
                .addComponent(scrollPaneTable));
    }

    public void setSender(Scenario.Sender sender){
        comboBoxSenderType.setSelectedItem(sender.getClazz());
        panelProperties.setListProperties(sender.getProperty());

        senderPropertiesTable.updateModelProperties(sender.getProperty());

        List<Property> structureProperties = new PerfCakeReflectUtil(util).findSenderProperties(sender.getClazz());
        if (structureProperties == null) {
            scrollPaneTable.setVisible(false);
            panelProperties.setVisible(true);
        } else {
            senderPropertiesTable.updateStructure(structureProperties);
            scrollPaneTable.setVisible(true);
            panelProperties.setVisible(false);
        }
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
