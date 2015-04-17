package org.perfcake.pc4idea.impl.editor.editor.tablemodel;

import com.intellij.openapi.module.Module;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.editor.tablemodel.EditorTableModel;
import org.perfcake.pc4idea.api.editor.openapi.ui.EditorDialog;
import org.perfcake.pc4idea.api.util.MessagesValidationSync;
import org.perfcake.pc4idea.impl.editor.editor.component.ValidatorEditor;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Stanislav Kaleta on 4/17/15.
 */
public class ValidatorsTableModel extends AbstractTableModel implements EditorTableModel {
    private List<Scenario.Validation.Validator> validatorList = new ArrayList<>();
    private Module module;
    private MessagesValidationSync sync;

    public ValidatorsTableModel(List<Scenario.Validation.Validator> validators, Module module, MessagesValidationSync sync) {
        validatorList.addAll(validators);
        this.module = module;
        this.sync = sync;
    }

    public List<Scenario.Validation.Validator> getValidatorList() {
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
        switch (columnIndex) {
            case 0:
                return validator.getId();
            case 1:
                return validator.getClazz();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Validator Id";
            case 1:
                return "Validator Type";
            default:
                return "";
        }
    }

    @Override
    public void reorderRows(int fromIndex, int toIndex) {
        Scenario.Validation.Validator validator = validatorList.get(fromIndex);
        validatorList.remove(fromIndex);
        validatorList.add(toIndex, validator);
    }

    @Override
    public void addRow() {
        ValidatorEditor editor = new ValidatorEditor(module, sync);
        EditorDialog dialog = new EditorDialog(editor);
        dialog.show();
        if (dialog.getExitCode() == 0) {
            Scenario.Validation.Validator validator = editor.getValidator();
            validatorList.add(validator);
            fireTableDataChanged();
        }
    }

    @Override
    public void editRow(int row) {
        ValidatorEditor editor = new ValidatorEditor(module, sync);
        editor.setValidator(validatorList.get(row));
        EditorDialog dialog = new EditorDialog(editor);
        dialog.show();
        if (dialog.getExitCode() == 0) {
            Scenario.Validation.Validator validator = editor.getValidator();
            validatorList.set(row, validator);
            fireTableDataChanged();
        }
    }

    @Override
    public void deleteRow(int row) {
        validatorList.remove(row);
        fireTableDataChanged();
    }
}
