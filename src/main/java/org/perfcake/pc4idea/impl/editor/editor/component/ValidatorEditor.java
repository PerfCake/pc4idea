package org.perfcake.pc4idea.impl.editor.editor.component;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.ValidationInfo;
import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.editor.component.AbstractEditor;
import org.perfcake.pc4idea.api.util.MessagesValidationSync;
import org.perfcake.pc4idea.api.util.PerfCakeReflectUtil;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 28.10.2014
 */
public class ValidatorEditor extends AbstractEditor {
    private JComboBox comboBoxType;
    private JTextField textFieldId;
    private PropertiesEditor panelProperties;

    private Module module;
    private MessagesValidationSync sync;

    private String idBefore;

    public ValidatorEditor(Module module, MessagesValidationSync sync) {
        this.module = module;
        this.sync = sync;
        idBefore = null;
        initComponents();
    }

    private void initComponents(){
        JLabel labelType = new JLabel("Validator type:");
        JLabel labelId = new JLabel("Validator id:");
        String[] validators = new PerfCakeReflectUtil(module).findComponentClassNames(PerfCakeReflectUtil.VALIDATOR);
        comboBoxType = new ComboBox(new DefaultComboBoxModel<>(validators));
        comboBoxType.setSelectedIndex(-1);
        comboBoxType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String className = (String) comboBoxType.getSelectedItem();
                PerfCakeReflectUtil reflectUtil = new PerfCakeReflectUtil(module);
                List<Property> structureProp = reflectUtil.findComponentProperties(PerfCakeReflectUtil.VALIDATOR, className);
                panelProperties.setDefinedProperties(structureProp);
            }
        });

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

    public void setValidator(Scenario.Validation.Validator validator) {
        comboBoxType.setSelectedItem(validator.getClazz());
        textFieldId.setText(validator.getId());
        panelProperties.setListProperties(validator.getProperty());

        PerfCakeReflectUtil reflectUtil = new PerfCakeReflectUtil(module);
        List<Property> structureProp = reflectUtil.findComponentProperties(PerfCakeReflectUtil.VALIDATOR, validator.getClazz());
        panelProperties.setDefinedProperties(structureProp);

        idBefore = validator.getId();

        if (sync.isValidatorAttached(validator)) {
            textFieldId.getDocument().addDocumentListener(new DocumentListener() {
                private boolean warningShown = false;

                @Override
                public void insertUpdate(DocumentEvent e) {
                    if (!warningShown) {
                        showWarning();
                    }
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    if (!warningShown) {
                        showWarning();
                    }
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    if (!warningShown) {
                        showWarning();
                    }
                }

                private void showWarning() {
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
        if (sync.isIdUsed(textFieldId.getText()) && !textFieldId.getText().equals(idBefore)) {
            info = new ValidationInfo("ID must be unique! \"" + textFieldId.getText() + "\" is already used.");
        }
        if (textFieldId.getText().isEmpty()) {
            info = new ValidationInfo("Validator ID can't be empty");
        }
        if (comboBoxType.getSelectedIndex() == -1) {
            info = new ValidationInfo("Validator type isn't selected");
        }
        return info;
    }
}
