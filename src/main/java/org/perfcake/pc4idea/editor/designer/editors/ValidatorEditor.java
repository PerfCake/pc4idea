package org.perfcake.pc4idea.editor.designer.editors;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.ValidationInfo;
import org.perfcake.model.Scenario;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 28.10.2014
 */
public class ValidatorEditor extends AbstractEditor {
    private JLabel labelType;
    private JLabel labelId;
    private JComboBox comboBoxType;
    private JTextField textFieldId;
    private PropertiesEditor panelProperties;

    public ValidatorEditor(){
        initComponents();
    }

    private void initComponents(){
        labelType = new JLabel("Validator type:");
        labelId = new JLabel("Validator id:");
        comboBoxType = new ComboBox();
        comboBoxType.addItem("ScriptValidator");        /*TODO load from classpath?*/
        comboBoxType.addItem("RulesValidator");
        comboBoxType.addItem("RegExpValidator");
        comboBoxType.addItem("DictionaryValidator");
        textFieldId = new JTextField();
        panelProperties = new PropertiesEditor();

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                        .addComponent(labelType,GroupLayout.PREFERRED_SIZE,100,GroupLayout.PREFERRED_SIZE)
                        .addComponent(comboBoxType))
                .addGroup(layout.createSequentialGroup()
                        .addComponent(labelId,GroupLayout.PREFERRED_SIZE,100,GroupLayout.PREFERRED_SIZE)
                        .addComponent(textFieldId))
                .addComponent(panelProperties));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelType, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                        .addComponent(comboBoxType, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                .addGap(10)
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelId, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                        .addComponent(textFieldId, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                .addGap(10)
                .addComponent(panelProperties));
    }
    public void setValidator(Scenario.Validation.Validator validator){
        comboBoxType.setSelectedItem(validator.getClazz());
        textFieldId.setText(validator.getId());
        panelProperties.setListProperties(validator.getProperty());
    }

    public Scenario.Validation.Validator getValidator(){
        Scenario.Validation.Validator newValidator = new Scenario.Validation.Validator();
        newValidator.setClazz((String)comboBoxType.getSelectedItem());
        newValidator.setId(textFieldId.getText());
        newValidator.getProperty().addAll(panelProperties.getListProperties());
        return newValidator;
    }

    @Override
    public String getTitle(){
        return "Validator Editor";
    }

    @Override
    public ValidationInfo areInsertedValuesValid() {
        /*TODO valid id must be uniqe*/
        return (textFieldId.getText().isEmpty()) ?
                new ValidationInfo("Text field can't be empty") : null;
    }
}
