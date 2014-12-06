package org.perfcake.pc4idea.editor.designer.editors;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.ui.ValidationInfo;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.designer.common.EditorTablePanel;
import org.perfcake.pc4idea.module.PCModuleUtil;
import org.perfcake.pc4idea.editor.designer.common.ScenarioDialogEditor;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 28.10.2014
 */
public class MessagesEditor extends AbstractEditor {
    private EditorTablePanel tablePanelMessages;

    private Set<String> usedValidatorIDSet;
    private Module module;

    public MessagesEditor(Module module){
        this.module = module;
        usedValidatorIDSet = new TreeSet<>();
        initComponents();
    }

    private void initComponents(){
        tablePanelMessages = new EditorTablePanel(new MessagesTableModel(new ArrayList<Scenario.Messages.Message>())) {
            @Override
            public void tableClickedActionPerformed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 || e.getButton() == MouseEvent.BUTTON3) {
                    if (e.getClickCount() == 2) {
                        int selectedRow = tablePanelMessages.getTable().getSelectedRow();
                        if (selectedRow >= 0) {
                            MessageEditor messageEditor = new MessageEditor(usedValidatorIDSet);
                            messageEditor.setMessage(((MessagesTableModel) tablePanelMessages.getTable().getModel()).getMessageList().get(selectedRow));
                            ScenarioDialogEditor dialog = new ScenarioDialogEditor(messageEditor);
                            dialog.show();
                            if (dialog.getExitCode() == 0) {
                                ((MessagesTableModel) tablePanelMessages.getTable().getModel()).getMessageList().set(selectedRow, messageEditor.getMessage());
                                tablePanelMessages.getTable().repaint();
                            }
                        }
                    }
                }
            }

            @Override
            public void buttonAddActionPerformed(ActionEvent e) {
                MessageEditor messageEditor = new MessageEditor(usedValidatorIDSet);
                ScenarioDialogEditor dialog = new ScenarioDialogEditor(messageEditor);
                dialog.show();
                if (dialog.getExitCode() == 0) {
                    Scenario.Messages.Message message = messageEditor.getMessage();
                    ((MessagesTableModel)tablePanelMessages.getTable().getModel()).getMessageList().add(message);
                    tablePanelMessages.getTable().repaint();
                    tablePanelMessages.getTable().revalidate();
                    PCModuleUtil.createMessageFile(message, module);

                }
            }

            @Override
            public void buttonEditActionPerformed(ActionEvent e) {
                int selectedRow = tablePanelMessages.getTable().getSelectedRow();
                if (selectedRow >= 0) {
                    MessageEditor messageEditor = new MessageEditor(usedValidatorIDSet);
                    messageEditor.setMessage(((MessagesTableModel) tablePanelMessages.getTable().getModel()).getMessageList().get(selectedRow));
                    ScenarioDialogEditor dialog = new ScenarioDialogEditor(messageEditor);
                    dialog.show();
                    if (dialog.getExitCode() == 0) {
                        ((MessagesTableModel) tablePanelMessages.getTable().getModel()).getMessageList().set(selectedRow, messageEditor.getMessage());
                        tablePanelMessages.getTable().repaint();
                    }
                }
            }

            @Override
            public void buttonDeleteActionPerformed(ActionEvent e) {
                int selectedRow = tablePanelMessages.getTable().getSelectedRow();
                if (selectedRow >= 0) {
                    ((MessagesTableModel)tablePanelMessages.getTable().getModel()).getMessageList().remove(selectedRow);
                    tablePanelMessages.getTable().repaint();
                    tablePanelMessages.getTable().revalidate();
                }
            }
        };

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addComponent(tablePanelMessages));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(tablePanelMessages));
    }
    public void setMessages(Scenario.Messages messages, Set<String> usedValidatorIDSet){
        tablePanelMessages.getTable().setModel(new MessagesTableModel(messages.getMessage()));

        this.usedValidatorIDSet = usedValidatorIDSet;
    }

    public Scenario.Messages getMessages(){
        Scenario.Messages newMessages = new Scenario.Messages();
        newMessages.getMessage().addAll(((MessagesTableModel)tablePanelMessages.getTable().getModel()).getMessageList());
        return newMessages;
    }

    @Override
    public String getTitle(){
        return "Messages Editor";
    }

    @Override
    public ValidationInfo areInsertedValuesValid() {
        // always valid
        return null;
    }

    private class MessagesTableModel extends AbstractTableModel {
        private List<Scenario.Messages.Message> messageList = new ArrayList<>();

        private MessagesTableModel(List<Scenario.Messages.Message> messages){
            messageList.addAll(messages);
        }

        public List<Scenario.Messages.Message> getMessageList(){
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
            for (Scenario.Messages.Message.ValidatorRef validatorRef : message.getValidatorRef()){
                if (!sb.toString().equals("")){
                    sb.append(", ");
                }
                sb.append(validatorRef.getId());
            }

            switch (columnIndex){
                case 0: return message.getUri();
                case 1: return message.getMultiplicity();
                case 2: return sb.toString();
                default: return null;
            }
        }
        @Override
        public String getColumnName(int columnIndex){
            switch (columnIndex){
                case 0: return "Message URI";
                case 1: return "Multiplicity";
                case 2: return "Attached validators";
                default: return "";
            }
        }
    }
}
