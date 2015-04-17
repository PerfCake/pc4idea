package org.perfcake.pc4idea.impl.editor.editor.tablemodel;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.editor.tablemodel.EditorTableModel;
import org.perfcake.pc4idea.api.editor.openapi.ui.EditorDialog;
import org.perfcake.pc4idea.api.util.MessagesValidationSync;
import org.perfcake.pc4idea.impl.editor.editor.component.MessageEditor;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Stanislav Kaleta on 4/17/15.
 */
public class MessagesTableModel extends AbstractTableModel implements EditorTableModel {
    private List<Scenario.Messages.Message> messageList = new ArrayList<>();
    private MessagesValidationSync sync;

    public MessagesTableModel(List<Scenario.Messages.Message> messages, MessagesValidationSync sync) {
        messageList.addAll(messages);
        this.sync = sync;
    }

    public List<Scenario.Messages.Message> getMessageList() {
        return messageList;
    }

    @Override
    public int getRowCount() {
        return messageList.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Scenario.Messages.Message message = messageList.get(rowIndex);

        StringBuilder sb = new StringBuilder();
        for (Scenario.Messages.Message.ValidatorRef validatorRef : message.getValidatorRef()) {
            if (!sb.toString().equals("")) {
                sb.append(", ");
            }
            sb.append(validatorRef.getId());
        }

        switch (columnIndex) {
            case 0:
                return message.getUri();
            case 1:
                return message.getMultiplicity();
            case 2:
                return sb.toString();
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "Message URI";
            case 1:
                return "Multiplicity";
            case 2:
                return "Attached validators";
            default:
                return "";
        }
    }

    @Override
    public void reorderRows(int fromIndex, int toIndex) {
        Scenario.Messages.Message message = messageList.get(fromIndex);
        messageList.remove(fromIndex);
        messageList.add(toIndex, message);
    }

    @Override
    public void addRow() {
        MessageEditor editor = new MessageEditor(sync);
        EditorDialog dialog = new EditorDialog(editor);
        dialog.show();
        if (dialog.getExitCode() == 0) {
            Scenario.Messages.Message message = editor.getMessage();
            messageList.add(message);
            fireTableDataChanged();
        }
    }

    @Override
    public void editRow(int row) {
        MessageEditor editor = new MessageEditor(sync);
        editor.setMessage(messageList.get(row));
        EditorDialog dialog = new EditorDialog(editor);
        dialog.show();
        if (dialog.getExitCode() == 0) {
            Scenario.Messages.Message message = editor.getMessage();
            messageList.set(row, message);
            fireTableDataChanged();
        }
    }

    @Override
    public void deleteRow(int row) {
        messageList.remove(row);
        fireTableDataChanged();
    }
}
