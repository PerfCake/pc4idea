package org.perfcake.pc4idea.editor.wizard;

import org.perfcake.model.Property;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 19.10.2014
 */
public class PropertyEditor extends JPanel {
    private JLabel labelName;
    private JLabel labelValue;
    private JTextField textFieldName;
    private JTextField textFieldValue;

    public PropertyEditor(){
        initComponents();
    }

    private void initComponents(){
        labelName = new JLabel("Name:");
        labelValue = new JLabel("Value:");
        textFieldName = new JTextField();
        textFieldValue = new JTextField();

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup()
            .addGroup(layout.createSequentialGroup()
                .addComponent(labelName,GroupLayout.PREFERRED_SIZE,50,GroupLayout.PREFERRED_SIZE)
                .addComponent(textFieldName))
            .addGroup(layout.createSequentialGroup()
                .addComponent(labelValue,GroupLayout.PREFERRED_SIZE,50,GroupLayout.PREFERRED_SIZE)
                .addComponent(textFieldValue)));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelName, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                        .addComponent(textFieldName, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                .addGap(10)
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelValue, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                        .addComponent(textFieldValue, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)));
    }

    public void setProperty(Property property){
        textFieldName.setText(property.getName());
        textFieldValue.setText(property.getValue());
    }

    public Property getProperty(){
        Property newPropery = new Property();
        newPropery.setName(textFieldName.getText());
        newPropery.setValue(textFieldValue.getText());
        return newPropery;
    }



}
