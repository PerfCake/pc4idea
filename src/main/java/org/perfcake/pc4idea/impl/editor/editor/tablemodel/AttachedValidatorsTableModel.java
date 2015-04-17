package org.perfcake.pc4idea.impl.editor.editor.tablemodel;

import org.perfcake.model.Scenario;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stanislav Kaleta on 4/17/15.
 */
public class AttachedValidatorsTableModel extends AbstractTableModel {
    private List<Scenario.Messages.Message.ValidatorRef> validatorRefList = new ArrayList<>();

    public AttachedValidatorsTableModel(List<Scenario.Messages.Message.ValidatorRef> validatorRefs) {
        validatorRefList.addAll(validatorRefs);
    }

    public List<Scenario.Messages.Message.ValidatorRef> getValidatorRefList() {
        return validatorRefList;
    }

    @Override
    public int getRowCount() {
        return validatorRefList.size();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Scenario.Messages.Message.ValidatorRef validatorRef = validatorRefList.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return validatorRef.getId();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Validator ID";
            default:
                return "";
        }
    }
}
