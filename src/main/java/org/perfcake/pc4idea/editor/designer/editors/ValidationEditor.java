package org.perfcake.pc4idea.editor.designer.editors;

import com.intellij.openapi.ui.ValidationInfo;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.designer.common.EditorTablePanel;
import org.perfcake.pc4idea.editor.designer.common.ScenarioDialogEditor;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 28.10.2014
 */
public class ValidationEditor extends AbstractEditor {
    private JLabel labelEnabled;
    private JLabel labelFastForward;
    private JCheckBox checkBoxEnabled;
    private JCheckBox checkBoxFastForward;
    private EditorTablePanel tablePanelValidators;

    public ValidationEditor(){
        initComponents();
        this.setPreferredSize(new Dimension(350,0));
    }

    private void initComponents(){
        labelEnabled = new JLabel("Enabled: ");
        labelFastForward = new JLabel("Fast Forward: ");
        checkBoxEnabled = new JCheckBox();
        checkBoxFastForward = new JCheckBox();
        tablePanelValidators = new EditorTablePanel(new ValidatorsTableModel(new ArrayList<Scenario.Validation.Validator>())) {
            @Override
            public void tableClickedActionPerformed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 || e.getButton() == MouseEvent.BUTTON3) {
                    if (e.getClickCount() == 2) {
                        int selectedRow = tablePanelValidators.getTable().getSelectedRow();
                        if (selectedRow >= 0) {
                            ValidatorEditor validatorEditor = new ValidatorEditor();
                            validatorEditor.setValidator(((ValidatorsTableModel) tablePanelValidators.getTable().getModel()).getValidatorList().get(selectedRow));
                            ScenarioDialogEditor dialog = new ScenarioDialogEditor(validatorEditor);
                            dialog.show();
                            if (dialog.getExitCode() == 0) {
                                ((ValidatorsTableModel) tablePanelValidators.getTable().getModel()).getValidatorList().set(selectedRow, validatorEditor.getValidator());
                                tablePanelValidators.getTable().repaint();
                            }
                        }
                    }
                }
            }

            @Override
            public void buttonAddActionPerformed(ActionEvent e) {
                ValidatorEditor validatorEditor = new ValidatorEditor();
                ScenarioDialogEditor dialog = new ScenarioDialogEditor(validatorEditor);
                dialog.show();
                if (dialog.getExitCode() == 0) {
                    Scenario.Validation.Validator validator = validatorEditor.getValidator();
                    ((ValidatorsTableModel)tablePanelValidators.getTable().getModel()).getValidatorList().add(validator);
                    tablePanelValidators.getTable().repaint();
                    tablePanelValidators.getTable().revalidate();
                }
            }

            @Override
            public void buttonEditActionPerformed(ActionEvent e) {
                int selectedRow = tablePanelValidators.getTable().getSelectedRow();
                if (selectedRow >= 0) {
                    ValidatorEditor validatorEditor = new ValidatorEditor();
                    validatorEditor.setValidator(((ValidatorsTableModel) tablePanelValidators.getTable().getModel()).getValidatorList().get(selectedRow));
                    ScenarioDialogEditor dialog = new ScenarioDialogEditor(validatorEditor);
                    dialog.show();
                    if (dialog.getExitCode() == 0) {
                        ((ValidatorsTableModel) tablePanelValidators.getTable().getModel()).getValidatorList().set(selectedRow, validatorEditor.getValidator());
                        tablePanelValidators.getTable().repaint();
                    }
                }
            }

            @Override
            public void buttonDeleteActionPerformed(ActionEvent e) {
                int selectedRow = tablePanelValidators.getTable().getSelectedRow();
                if (selectedRow >= 0) {
                    ((ValidatorsTableModel)tablePanelValidators.getTable().getModel()).getValidatorList().remove(selectedRow);
                    tablePanelValidators.getTable().repaint();
                    tablePanelValidators.getTable().revalidate();
                }
            }
        };

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
        tablePanelValidators.getTable().setModel(new ValidatorsTableModel(validation.getValidator()));
    }

    public Scenario.Validation getValidation(){
        Scenario.Validation newValidation = new Scenario.Validation();
        newValidation.setEnabled(checkBoxEnabled.isSelected());
        newValidation.setFastForward(checkBoxFastForward.isSelected());
        newValidation.getValidator().addAll(((ValidatorsTableModel)tablePanelValidators.getTable().getModel()).getValidatorList());
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

    private class ValidatorsTableModel extends AbstractTableModel {
        private List<Scenario.Validation.Validator> validatorList = new ArrayList<>();

        private ValidatorsTableModel(List<Scenario.Validation.Validator> validators){
            validatorList.addAll(validators);
        }

        public List<Scenario.Validation.Validator> getValidatorList(){
            return validatorList;
        }

        @Override
        public int getRowCount() {
            return validatorList.size();
        }
        @Override
        public int getColumnCount() {
            return 2;
        }
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Scenario.Validation.Validator validator = validatorList.get(rowIndex);
            switch (columnIndex){
                case 0: return validator.getId();
                case 1: return validator.getClazz();
                default: return null;
            }
        }
        @Override
        public String getColumnName(int columnIndex){
            switch (columnIndex){
                case 0: return "Validator Id";
                case 1: return "Validator Type";
                default: return "";
            }
        }
    }
}
