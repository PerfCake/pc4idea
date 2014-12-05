package org.perfcake.pc4idea.editor.designer.editors;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.ValidationInfo;
import org.perfcake.model.Scenario;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 23.10.2014
 */
public class SenderEditor extends AbstractEditor {
    private JComboBox comboBoxSenderType;
    private PropertiesEditor panelProperties;

    public SenderEditor(){
        initComponents();
    }

    private void initComponents(){
        JLabel labelSenderType = new JLabel("Sender type:");
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
        comboBoxSenderType.addItem("SslSocketSender");
        comboBoxSenderType.addItem("WebSocketSender");
        comboBoxSenderType.addItem("DummySender");
        comboBoxSenderType.addItem("CommandSender");
        comboBoxSenderType.addItem("JmsSender");
        comboBoxSenderType.setSelectedIndex(-1);
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
