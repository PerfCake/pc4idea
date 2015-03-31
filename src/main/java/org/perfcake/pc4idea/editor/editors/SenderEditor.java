package org.perfcake.pc4idea.editor.editors;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.ValidationInfo;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.PerfCakeReflectUtil;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 23.10.2014
 */
public class SenderEditor extends AbstractEditor {
    private JComboBox comboBoxSenderType;
    private PropertiesEditor panelProperties;
    private JTextArea tempLabelProperties;

    public SenderEditor(){
        initComponents();
    }

    private void initComponents(){
        JLabel labelSenderType = new JLabel("Sender type:");

        String[] senders = new PerfCakeReflectUtil().findSenderClassNames();
        comboBoxSenderType = new ComboBox(new DefaultComboBoxModel<>(senders));
        comboBoxSenderType.setSelectedIndex(-1);
        comboBoxSenderType.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e) {
                String newClass = (String) comboBoxSenderType.getSelectedItem();
                tempLabelProperties.setText(new PerfCakeReflectUtil().findSenderProperties(newClass));
            }
        });

        panelProperties = new PropertiesEditor();
        tempLabelProperties = new JTextArea("-init-");

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup()
            .addGroup(layout.createSequentialGroup()
                .addComponent(labelSenderType,GroupLayout.PREFERRED_SIZE,70,GroupLayout.PREFERRED_SIZE)
                .addComponent(comboBoxSenderType))
            .addComponent(tempLabelProperties));
        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup()
                .addComponent(labelSenderType, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                .addComponent(comboBoxSenderType, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
            .addGap(10)
            .addComponent(tempLabelProperties));
    }

    public void setSender(Scenario.Sender sender){
        comboBoxSenderType.setSelectedItem(sender.getClazz());
        panelProperties.setListProperties(sender.getProperty());

        tempLabelProperties.setText(new PerfCakeReflectUtil().findSenderProperties(sender.getClazz()));
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
