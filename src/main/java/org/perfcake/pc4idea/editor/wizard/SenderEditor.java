package org.perfcake.pc4idea.editor.wizard;

import com.intellij.openapi.ui.ComboBox;
import org.perfcake.model.Scenario;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 23.10.2014
 */
public class SenderEditor extends JPanel {
    private JLabel labelSenderType;
    private JComboBox comboBoxSenderType;
    private PropertiesEditor panelProperties;

    public SenderEditor(){
        initComponents();
        this.setPreferredSize(new Dimension(350,0));
    }

    private void initComponents(){
        labelSenderType = new JLabel("Sender type:");
        comboBoxSenderType = new ComboBox();
        comboBoxSenderType.addItem("GroovySender");          /*TODO load from classpath*/
        comboBoxSenderType.addItem("RequestResponseJmsSender");
        comboBoxSenderType.addItem("LdapSender");
        comboBoxSenderType.addItem("HttpsSender");
        comboBoxSenderType.addItem("ChannelSender");
        comboBoxSenderType.addItem("PlainSockerSender");
        comboBoxSenderType.addItem("HttpSender");
        comboBoxSenderType.addItem("JdbcSender");
        comboBoxSenderType.addItem("SoapSender");
        /* etc. ...*/
        panelProperties = new PropertiesEditor();

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup()
            .addGroup(layout.createSequentialGroup()
                .addComponent(labelSenderType,GroupLayout.PREFERRED_SIZE,70,GroupLayout.PREFERRED_SIZE)
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
    }

    public boolean areInsertedValuesValid() {
        //boolean areValid = false;
        /*TODO check validity of inserted data - if false OK button disabled*/
        return true;
    }

    public Scenario.Sender getSender(){
        Scenario.Sender newSender = new Scenario.Sender();
        newSender.setClazz((String)comboBoxSenderType.getSelectedItem());
        newSender.getProperty().addAll(panelProperties.getListProperties());
        return newSender;
    }

}
