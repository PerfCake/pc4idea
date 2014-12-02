package org.perfcake.pc4idea.editor.designer.editors;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.table.JBTable;
import org.perfcake.model.Header;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.designer.common.EditorTablePanel;
import org.perfcake.pc4idea.editor.designer.common.ScenarioDialogEditor;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
public class MessageEditor extends AbstractEditor {
    private JLabel labelMessageURI;
    private JLabel labelMultiplicity;
    private JLabel labelMessageContent;
    private JTextField textFieldMessageURI;
    private JTextField textFieldMultiplicity;
    private JTextField textFieldContent;
    private EditorTablePanel tablePanelHeaders;
    private PropertiesEditor panelProperties;
    private AttachedValidatorsEditor panelAttachedValidators;

    private boolean nullTextFieldsWarningShowed;
    private boolean ambiguousWarningShowed;

    public MessageEditor(Set<String> usedValidatorIDSet){
        nullTextFieldsWarningShowed = false;
        ambiguousWarningShowed  = false;
        initComponents();
        panelAttachedValidators.setUsedValidatorIDSet(usedValidatorIDSet);
    }

    private void initComponents(){
        labelMessageURI = new JLabel("URI:");
        labelMultiplicity = new JLabel("Multiplicity:");
        labelMessageContent = new JLabel("Content:");
        textFieldMessageURI = new JTextField(null);
        textFieldMultiplicity = new JTextField(null);
        textFieldContent = new JTextField(null);

        tablePanelHeaders = new EditorTablePanel(new HeadersTableModel(new ArrayList<Header>())) {
            @Override
            public void tableClickedActionPerformed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 || e.getButton() == MouseEvent.BUTTON3) {
                    if (e.getClickCount() == 2) {
                        int selectedRow = tablePanelHeaders.getTable().getSelectedRow();
                        if (selectedRow >= 0) {
                            HeaderEditor headerEditor = new HeaderEditor();
                            headerEditor.setHeader(((HeadersTableModel) tablePanelHeaders.getTable().getModel()).getHeaderList().get(selectedRow));
                            ScenarioDialogEditor dialog = new ScenarioDialogEditor(headerEditor);
                            dialog.show();
                            if (dialog.getExitCode() == 0) {
                                ((HeadersTableModel) tablePanelHeaders.getTable().getModel()).getHeaderList().set(selectedRow, headerEditor.getHeader());
                                tablePanelHeaders.getTable().repaint();
                            }
                        }
                    }
                }
            }

            @Override
            public void buttonAddActionPerformed(ActionEvent e) {
                HeaderEditor headerEditor = new HeaderEditor();
                ScenarioDialogEditor dialog = new ScenarioDialogEditor(headerEditor);
                dialog.show();
                if (dialog.getExitCode() == 0) {
                    Header header = headerEditor.getHeader();
                    ((HeadersTableModel)tablePanelHeaders.getTable().getModel()).getHeaderList().add(header);
                    tablePanelHeaders.getTable().repaint();
                    tablePanelHeaders.getTable().revalidate();
                }
            }

            @Override
            public void buttonEditActionPerformed(ActionEvent e) {
                int selectedRow = tablePanelHeaders.getTable().getSelectedRow();
                if (selectedRow >= 0) {
                    HeaderEditor headerEditor = new HeaderEditor();
                    headerEditor.setHeader(((HeadersTableModel) tablePanelHeaders.getTable().getModel()).getHeaderList().get(selectedRow));
                    ScenarioDialogEditor dialog = new ScenarioDialogEditor(headerEditor);
                    dialog.show();
                    if (dialog.getExitCode() == 0) {
                        ((HeadersTableModel) tablePanelHeaders.getTable().getModel()).getHeaderList().set(selectedRow, headerEditor.getHeader());
                        tablePanelHeaders.getTable().repaint();
                    }
                }
            }

            @Override
            public void buttonDeleteActionPerformed(ActionEvent e) {
                int selectedRow = tablePanelHeaders.getTable().getSelectedRow();
                if (selectedRow >= 0) {
                    ((HeadersTableModel)tablePanelHeaders.getTable().getModel()).getHeaderList().remove(selectedRow);
                    tablePanelHeaders.getTable().repaint();
                    tablePanelHeaders.getTable().revalidate();
                }
            }
        };

        panelProperties = new PropertiesEditor();

        panelAttachedValidators = new AttachedValidatorsEditor();

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                        .addComponent(labelMessageURI, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                        .addComponent(textFieldMessageURI))
                .addGroup(layout.createSequentialGroup()
                        .addComponent(labelMultiplicity, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                        .addComponent(textFieldMultiplicity))
                .addGroup(layout.createSequentialGroup()
                        .addComponent(labelMessageContent,GroupLayout.PREFERRED_SIZE,100,GroupLayout.PREFERRED_SIZE)
                        .addComponent(textFieldContent))
                .addComponent(tablePanelHeaders)
                .addComponent(panelProperties)
                .addComponent(panelAttachedValidators));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelMessageURI, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                        .addComponent(textFieldMessageURI, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                .addGap(10)
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelMultiplicity, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                        .addComponent(textFieldMultiplicity, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                .addGap(10)
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelMessageContent, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                        .addComponent(textFieldContent, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                .addGap(10)
                .addComponent(tablePanelHeaders)
                .addGap(10)
                .addComponent(panelProperties)
                .addGap(10)
                .addComponent(panelAttachedValidators));
    }


    public void setMessage(Scenario.Messages.Message message){
        textFieldMessageURI.setText(message.getUri());
        textFieldMultiplicity.setText(message.getMultiplicity());
        textFieldContent.setText(message.getContent());
        tablePanelHeaders.getTable().setModel(new HeadersTableModel(message.getHeader()));
        panelProperties.setListProperties(message.getProperty());
        panelAttachedValidators.setValidatorRefs(message.getValidatorRef());
    }

    public Scenario.Messages.Message getMessage(){
        Scenario.Messages.Message newMessage = new Scenario.Messages.Message();
        boolean uriIsEmpty = (textFieldMessageURI.getText().isEmpty() || textFieldMessageURI.getText().trim().isEmpty());
        newMessage.setUri(uriIsEmpty ? null : textFieldMessageURI.getText());
        boolean multiplicityIsEmpty = (textFieldMultiplicity.getText().isEmpty() || textFieldMultiplicity.getText().trim().isEmpty());
        newMessage.setMultiplicity(multiplicityIsEmpty ? "1" : textFieldMultiplicity.getText());

        boolean contentIsEmpty = (textFieldContent.getText().isEmpty() || textFieldContent.getText().trim().isEmpty());
        if (contentIsEmpty){
            if (uriIsEmpty){
                newMessage.setContent("");
            } else {
                newMessage.setContent(null);
            }
        } else {
            newMessage.setContent(textFieldContent.getText());
        }

        newMessage.getHeader().addAll(((HeadersTableModel)tablePanelHeaders.getTable().getModel()).getHeaderList());
        newMessage.getProperty().addAll(panelProperties.getListProperties());
        newMessage.getValidatorRef().addAll(panelAttachedValidators.getValidatorRefs());
        return newMessage;
    }

    @Override
    public String getTitle(){
        return "Message Editor";
    }

    @Override
    public ValidationInfo areInsertedValuesValid() {
        ValidationInfo info = null;
        boolean uriIsEmpty = (textFieldMessageURI.getText().isEmpty() || textFieldMessageURI.getText().trim().isEmpty());      /*TODO white space?*/
        boolean contentIsEmpty = (textFieldContent.getText().isEmpty() || textFieldContent.getText().trim().isEmpty());

        if (uriIsEmpty && contentIsEmpty){
            if (!nullTextFieldsWarningShowed) {
                int result = Messages.showYesNoDialog("Both URI and Content are empty!\n" +
                        "This will lead to message specified\n" +
                        "by content with value \"\" (empty string).\n\n" +
                        "Would you like to continue?", "Empty Text Fields!", AllIcons.General.QuestionDialog);
                nullTextFieldsWarningShowed = true;
                if (result != 0) {
                    info = new ValidationInfo("OK Interrupted...");
                }
            }
        }

        if (!uriIsEmpty && contentIsEmpty){
            if (!ambiguousWarningShowed){
                int result = Messages.showYesNoDialog("URI is set and Content is empty.\n" +
                        "While PerfCake prioritizes Content and\n" +
                        "Content with empty string value is valid,\n" +
                        "this configuration is ambiguous.\n" +
                        "If you continue, Content will be set to null\n " +
                        "and URI will be used.\n\n" +
                        "Would you like to continue?\n\n" +
                        "Hint: If you want to use Content with empty string value,\n" +
                        "please remove URI value.", "Input Ambiguous", AllIcons.General.QuestionDialog);
                ambiguousWarningShowed = true;
                if (result != 0) {
                    info = new ValidationInfo("OK Interrupted...");
                }
            }
        }
        return info;
    }

    private class HeadersTableModel extends AbstractTableModel {
        private List<Header> headerList = new ArrayList<>();

        private HeadersTableModel(List<Header> headers){
            headerList.addAll(headers);
        }

        public List<Header> getHeaderList(){
            return headerList;
        }

        @Override
        public int getRowCount() {
            return headerList.size();
        }
        @Override
        public int getColumnCount() {
            return 2;
        }
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Header header = headerList.get(rowIndex);
            switch (columnIndex){
                case 0: return header.getName();
                case 1: return header.getValue();
                default: return null;
            }
        }
        @Override
        public String getColumnName(int columnIndex){
            switch (columnIndex){
                case 0: return "Header Name";
                case 1: return "Header Value";
                default: return "";
            }
        }
    }

    private class AttachedValidatorsEditor extends JPanel {
        Set<String> usedValidatorIDSet;

        private JTable tableAttachedValidators;
        private JScrollPane scrollPaneTableAttachedValidators;
        private JButton buttonAttach;
        private JButton buttonDetach;

        private AttachedValidatorsEditor(){
            usedValidatorIDSet = new TreeSet<>();
            initComponents();
        }

        private void initComponents(){
            tableAttachedValidators = new JBTable(new AttachedValidatorsTableModel(new ArrayList<Scenario.Messages.Message.ValidatorRef>()));
            scrollPaneTableAttachedValidators = ScrollPaneFactory.createScrollPane(tableAttachedValidators);
            ((DefaultTableCellRenderer)tableAttachedValidators.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);

            buttonAttach = new JButton("Attach");
            buttonAttach.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Set<String> notAttachedID = new TreeSet<>();
                    for (String id : usedValidatorIDSet){
                        boolean isRef = false;
                        for (Scenario.Messages.Message.ValidatorRef ref : AttachedValidatorsEditor.this.getValidatorRefs()){
                            if(id.equals(ref.getId())){
                                isRef = true;
                            }
                        }
                        if (!isRef){
                            notAttachedID.add(id);
                        }
                    }
                    AttachValidatorEditor attachValidatorEditor = new AttachValidatorEditor(notAttachedID);
                    ScenarioDialogEditor dialog = new ScenarioDialogEditor(attachValidatorEditor);
                    dialog.show();
                    if (dialog.getExitCode() == 0) {
                        Scenario.Messages.Message.ValidatorRef validatorRef = attachValidatorEditor.getAttachedValidatorRef();
                        ((AttachedValidatorsTableModel)tableAttachedValidators.getModel()).getValidatorRefList().add(validatorRef);
                        tableAttachedValidators.repaint();
                        tableAttachedValidators.revalidate();
                    }
                }
            });

            buttonDetach = new JButton("Detach");
            buttonDetach.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = tableAttachedValidators.getSelectedRow();
                    if (selectedRow >= 0) {
                        ((AttachedValidatorsTableModel)tableAttachedValidators.getModel()).getValidatorRefList().remove(selectedRow);
                        tableAttachedValidators.repaint();
                        tableAttachedValidators.revalidate();
                    }
                }
            });

            GroupLayout layout = new GroupLayout(this);
            this.setLayout(layout);
            layout.setHorizontalGroup(layout.createSequentialGroup()
                    .addComponent(scrollPaneTableAttachedValidators)
                    .addGap(5)
                    .addGroup(layout.createParallelGroup()
                            .addComponent(buttonAttach, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonDetach, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)));
            layout.setVerticalGroup(layout.createParallelGroup()
                    .addComponent(scrollPaneTableAttachedValidators)
                    .addGroup(layout.createSequentialGroup()
                            .addComponent(buttonAttach)
                            .addComponent(buttonDetach)));
        }

        private void setUsedValidatorIDSet(Set<String> usedValidatorIDSet){
            this.usedValidatorIDSet = usedValidatorIDSet;
        }

        private void setValidatorRefs(List<Scenario.Messages.Message.ValidatorRef> validatorRefs){
            tableAttachedValidators.setModel(new AttachedValidatorsTableModel(validatorRefs));
        }

        private List<Scenario.Messages.Message.ValidatorRef> getValidatorRefs(){
            return ((AttachedValidatorsTableModel)tableAttachedValidators.getModel()).getValidatorRefList();
        }

        private class AttachedValidatorsTableModel extends AbstractTableModel {
            private List<Scenario.Messages.Message.ValidatorRef> validatorRefList = new ArrayList<>();

            private AttachedValidatorsTableModel(List<Scenario.Messages.Message.ValidatorRef> validatorRefs){
                validatorRefList.addAll(validatorRefs);
            }

            public List<Scenario.Messages.Message.ValidatorRef> getValidatorRefList(){
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
                switch (columnIndex){
                    case 0: return validatorRef.getId();
                    default: return null;
                }
            }
            @Override
            public String getColumnName(int columnIndex){
                switch (columnIndex){
                    case 0: return "Validator ID";
                    default: return "";
                }
            }
        }
    }
}
