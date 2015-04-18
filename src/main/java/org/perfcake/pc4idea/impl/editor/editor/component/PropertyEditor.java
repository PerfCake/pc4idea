package org.perfcake.pc4idea.impl.editor.editor.component;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.ValidationInfo;
import org.perfcake.model.Property;
import org.perfcake.pc4idea.api.editor.editor.component.AbstractEditor;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 19.10.2014
 */
public class PropertyEditor extends AbstractEditor {
    private JComboBox comboBoxName;
    private JTextField textFieldValue;

    public PropertyEditor(){
        comboBoxName = new ComboBox(new DefaultComboBoxModel<>());
        initComponents();
    }

    public PropertyEditor(String[] hints) {
        comboBoxName = new ComboBox(new DefaultComboBoxModel<>(hints));
        initComponents();
    }

    private void initComponents(){
        JLabel labelName = new JLabel("Name:");
        JLabel labelValue = new JLabel("Value:");

        comboBoxName.setEditable(true);
        comboBoxName.setSelectedIndex(-1);

        textFieldValue = new JTextField();

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup()
            .addGroup(layout.createSequentialGroup()
                .addComponent(labelName,GroupLayout.PREFERRED_SIZE,50,GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBoxName))
            .addGroup(layout.createSequentialGroup()
                .addComponent(labelValue,GroupLayout.PREFERRED_SIZE,50,GroupLayout.PREFERRED_SIZE)
                .addComponent(textFieldValue)));
        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup()
                    .addComponent(labelName, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboBoxName, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
            .addGap(10)
            .addGroup(layout.createParallelGroup()
                    .addComponent(labelValue, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                    .addComponent(textFieldValue, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)));
    }

    public void setProperty(Property property){
        comboBoxName.setSelectedItem(property.getName());
        textFieldValue.setText(property.getValue());
    }

    public Property getProperty(){
        Property newProperty = new Property();
        newProperty.setName((String) comboBoxName.getSelectedItem());
        newProperty.setValue(textFieldValue.getText());
        return newProperty;
    }

    @Override
    public String getTitle(){
        return "Property Editor";
    }

    @Override
    public ValidationInfo areInsertedValuesValid() {
        String name = (String) comboBoxName.getSelectedItem();
        ValidationInfo info = null;
        if (name.trim().isEmpty()) {
            info = new ValidationInfo("Name can't be empty");
        }
        if (textFieldValue.getText().trim().isEmpty()) {
            info = new ValidationInfo("Value can't be empty");
        }
        return info;
    }
}
