package org.perfcake.pc4idea.impl.editor.editor.component;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.ui.ValidationInfo;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.editor.component.AbstractEditor;
import org.perfcake.pc4idea.api.editor.swing.EditorTablePanel;
import org.perfcake.pc4idea.api.util.MessagesValidationSync;
import org.perfcake.pc4idea.impl.editor.editor.tablemodel.ValidatorsTableModel;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 28.10.2014
 */
public class ValidationEditor extends AbstractEditor {
    private JCheckBox checkBoxEnabled;
    private JCheckBox checkBoxFastForward;
    private EditorTablePanel tablePanelValidators;

    private Module module;
    private MessagesValidationSync sync;

    public ValidationEditor(Module module, MessagesValidationSync sync) {
        this.module = module;
        this.sync = sync;
        initComponents();
    }

    private void initComponents(){
        JLabel labelEnabled = new JLabel("Enabled:");
        JLabel labelFastForward = new JLabel("Fast Forward:");
        checkBoxEnabled = new JCheckBox();
        checkBoxFastForward = new JCheckBox();
        tablePanelValidators = new EditorTablePanel(new ValidatorsTableModel(new ArrayList<>(), module, sync));

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                        .addComponent(labelEnabled,GroupLayout.PREFERRED_SIZE,100,GroupLayout.PREFERRED_SIZE)
                        .addComponent(checkBoxEnabled))
                .addGroup(layout.createSequentialGroup()
                        .addComponent(labelFastForward,GroupLayout.PREFERRED_SIZE,100,GroupLayout.PREFERRED_SIZE)
                        .addComponent(checkBoxFastForward))
                .addComponent(tablePanelValidators));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelEnabled, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                        .addComponent(checkBoxEnabled, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                .addGap(10)
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelFastForward, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                        .addComponent(checkBoxFastForward, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                .addGap(10)
                .addComponent(tablePanelValidators));
    }

    public void setValidation(Scenario.Validation validation){
        checkBoxEnabled.setSelected(validation.isEnabled());
        checkBoxFastForward.setSelected(validation.isFastForward());
        tablePanelValidators.setTableModel(new ValidatorsTableModel(validation.getValidator(), module, sync));
    }

    public Scenario.Validation getValidation(){
        Scenario.Validation newValidation = new Scenario.Validation();
        newValidation.setEnabled(checkBoxEnabled.isSelected());
        newValidation.setFastForward(checkBoxFastForward.isSelected());
        newValidation.getValidator().addAll(((ValidatorsTableModel) tablePanelValidators.getTableModel()).getValidatorList());
        return newValidation;
    }

    @Override
    public String getTitle(){
        return "Validation Editor";
    }

    @Override
    public ValidationInfo areInsertedValuesValid() {
        // always valid
        return null;
    }
}
