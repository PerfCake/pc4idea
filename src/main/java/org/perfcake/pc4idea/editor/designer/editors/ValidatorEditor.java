package org.perfcake.pc4idea.editor.designer.editors;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.ValidationInfo;
import org.perfcake.model.Scenario;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 28.10.2014
 */
public class ValidatorEditor extends AbstractEditor {
    private JComboBox comboBoxType;
    private JTextField textFieldId;
    private PropertiesEditor panelProperties;

    private Set<String> usedIDSet;

    public ValidatorEditor(Set<String> usedIDSet){
        this.usedIDSet = usedIDSet;
        initComponents();
    }

    private void initComponents(){
        JLabel labelType = new JLabel("Validator type:");
        JLabel labelId = new JLabel("Validator id:");
        comboBoxType = new ComboBox();
        comboBoxType.addItem("ScriptValidator");        /*TODO load from classpath?*/
        comboBoxType.addItem("RulesValidator");
        comboBoxType.addItem("RegExpValidator");
        comboBoxType.addItem("DictionaryValidator");
        comboBoxType.setSelectedIndex(-1);
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
    public void setValidator(Scenario.Validation.Validator validator, boolean isAttached){
        comboBoxType.setSelectedItem(validator.getClazz());
        textFieldId.setText(validator.getId());
        if (validator.getId() != null){
            usedIDSet.remove(validator.getId());
        }
        panelProperties.setListProperties(validator.getProperty());

        if (isAttached){
            textFieldId.getDocument().addDocumentListener(new DocumentListener() {
                private boolean warningShown = false;
                @Override
                public void insertUpdate(DocumentEvent e) {
                    if (!warningShown){
                        showWarning();
                    }
                }
                @Override
                public void removeUpdate(DocumentEvent e) {
                    if (!warningShown){
                        showWarning();
                    }
                }
                @Override
                public void changedUpdate(DocumentEvent e) {
                    if (!warningShown){
                        showWarning();
                    }
                }
                private void showWarning(){
                    warningShown = true;
                    Messages.showWarningDialog("This Validator is attached to some message!\n" +
                            "Any \"id\" changes will cause interruption of the attachment.", "Warning!");
                }
            });
        }

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
        ValidationInfo info = null;
        for (String id : usedIDSet){
            if (textFieldId.getText().equals(id)){
                info = new ValidationInfo("ID must be unique! \""+id+"\" is already used.");
            }
        }
        if (textFieldId.getText().isEmpty()){
            info = new ValidationInfo("Validator ID text field can't be empty");
        }
        if (comboBoxType.getSelectedIndex() == -1){
            info = new ValidationInfo("Validator type isn't selected");
        }
        return info;
    }
}
