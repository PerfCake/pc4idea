package org.perfcake.pc4idea.editor.wizard;

import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.table.JBTable;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.components.ComponentEditor;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 28.10.2014
 */
public class ValidationEditor extends JPanel {
    private JLabel labelEnabled;
    private JLabel labelFastForward;
    private JCheckBox checkBoxEnabled;
    private JCheckBox checkBoxFastForward;
    private ValidatorsEditor panelValidators;

    public ValidationEditor(){
        initComponents();
        this.setPreferredSize(new Dimension(350,0));
    }

    private void initComponents(){
        labelEnabled = new JLabel("Enabled: ");
        labelFastForward = new JLabel("Fast Forward: ");
        checkBoxEnabled = new JCheckBox();
        checkBoxFastForward = new JCheckBox();
        panelValidators = new ValidatorsEditor();

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                        .addComponent(labelEnabled,GroupLayout.PREFERRED_SIZE,100,GroupLayout.PREFERRED_SIZE)
                        .addComponent(checkBoxEnabled))
                .addGroup(layout.createSequentialGroup()
                        .addComponent(labelFastForward,GroupLayout.PREFERRED_SIZE,100,GroupLayout.PREFERRED_SIZE)
                        .addComponent(checkBoxFastForward))
                .addComponent(panelValidators));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelEnabled, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                        .addComponent(checkBoxEnabled, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                .addGap(10)
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelFastForward, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                        .addComponent(checkBoxFastForward, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                .addGap(10)
                .addComponent(panelValidators));
    }





    public void setValidation(Scenario.Validation validation){
        checkBoxEnabled.setSelected(validation.isEnabled());
        checkBoxFastForward.setSelected(validation.isFastForward());
        panelValidators.setValidators(validation.getValidator());
    }

    public boolean areInsertedValuesValid() {
        //boolean areValid = false;
        /*TODO check validity of inserted data - if false OK button disabled*/
        return true;
    }

    public Scenario.Validation getValidation(){
        Scenario.Validation newValidation = new Scenario.Validation();
        newValidation.setEnabled(checkBoxEnabled.isSelected());
        newValidation.setFastForward(checkBoxFastForward.isSelected());
        newValidation.getValidator().addAll(panelValidators.getValidators());
        return newValidation;
    }

    private class ValidatorsEditor extends JPanel{
        private JTable tableValidators;
        private JScrollPane scrollPaneTableValidators;
        private JButton buttonAddValidator;
        private JButton buttonEditValidator;
        private JButton buttonDeleteValidator;

        private ValidatorsEditor(){
            initComponents();
            this.setPreferredSize(new Dimension(350,0));
        }

        private void initComponents(){
            tableValidators = new JBTable();
            scrollPaneTableValidators = ScrollPaneFactory.createScrollPane(tableValidators);
            tableValidators.setModel(new ValidatorsTableModel(Collections.<Scenario.Validation.Validator>emptyList()));
            tableValidators.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON1 || e.getButton() == MouseEvent.BUTTON3) {
                        if (e.getClickCount() == 2) {
                            ValidatorEditor validatorEditor = new ValidatorEditor();
                            int selectedRow = tableValidators.getSelectedRow();
                            validatorEditor.setValidator(((ValidatorsTableModel) tableValidators.getModel()).getValidators().get(selectedRow));
                            ComponentEditor editor = new ComponentEditor("Validator Editor",validatorEditor);
                            editor.show();
                            if (editor.getExitCode() == 0) {
                                ((ValidatorsTableModel)tableValidators.getModel()).getValidators().set(selectedRow, validatorEditor.getValidator());
                                tableValidators.repaint();
                            }
                        }
                    }
                }
            });
            tableValidators.getTableHeader().setReorderingAllowed(false);
            ((DefaultTableCellRenderer)tableValidators.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);

            buttonAddValidator = new JButton("Add");
            buttonAddValidator.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ValidatorEditor validatorEditor = new ValidatorEditor();
                    ComponentEditor editor = new ComponentEditor("Validator Editor",validatorEditor);
                    editor.show();
                    if (editor.getExitCode() == 0) {
                        Scenario.Validation.Validator validator = validatorEditor.getValidator();
                        ((ValidatorsTableModel)tableValidators.getModel()).getValidators().add(validator);
                        tableValidators.repaint();
                        scrollPaneTableValidators.revalidate();
                    }
                }
            });
            buttonEditValidator = new JButton("Edit");
            buttonEditValidator.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = tableValidators.getSelectedRow();
                    if (selectedRow >= 0) {
                        ValidatorEditor validatorEditor = new ValidatorEditor();
                        validatorEditor.setValidator(((ValidatorsTableModel) tableValidators.getModel()).getValidators().get(selectedRow));
                        ComponentEditor editor = new ComponentEditor("Validator Editor",validatorEditor);
                        editor.show();
                        if (editor.getExitCode() == 0) {
                            ((ValidatorsTableModel) tableValidators.getModel()).getValidators().set(selectedRow, validatorEditor.getValidator());
                            tableValidators.repaint();
                        }
                    }
                }
            });
            buttonDeleteValidator = new JButton("Delete");
            buttonDeleteValidator.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = tableValidators.getSelectedRow();
                    if (selectedRow >= 0) {
                        ((ValidatorsTableModel)tableValidators.getModel()).getValidators().remove(selectedRow);
                        tableValidators.repaint();
                        scrollPaneTableValidators.revalidate();
                    }
                }
            });

            GroupLayout layout = new GroupLayout(this);
            this.setLayout(layout);
            layout.setHorizontalGroup(layout.createSequentialGroup()
                    .addComponent(scrollPaneTableValidators)
                    .addGap(5)
                    .addGroup(layout.createParallelGroup()
                            .addComponent(buttonAddValidator, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonEditValidator, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonDeleteValidator, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)));
            layout.setVerticalGroup(layout.createParallelGroup()
                    .addComponent(scrollPaneTableValidators)
                    .addGroup(layout.createSequentialGroup()
                            .addComponent(buttonAddValidator)
                            .addComponent(buttonEditValidator)
                            .addComponent(buttonDeleteValidator)));
        }

        public void setValidators(List<Scenario.Validation.Validator> validators){
            ValidatorsTableModel model = new ValidatorsTableModel(validators);
            tableValidators.setModel(model);
        }

        private List<Scenario.Validation.Validator> getValidators(){
            return ((ValidatorsTableModel)tableValidators.getModel()).getValidators();
        }

        private class ValidatorsTableModel extends AbstractTableModel {
            private List<Scenario.Validation.Validator> validatorList = new ArrayList<>();

            private ValidatorsTableModel(List<Scenario.Validation.Validator> validators){
                validatorList.addAll(validators);
            }

            public List<Scenario.Validation.Validator> getValidators(){
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
}
