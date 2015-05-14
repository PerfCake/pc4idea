package org.perfcake.pc4idea.impl.editor.editor.component;

import com.intellij.icons.AllIcons;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.table.JBTable;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.editor.component.AbstractEditor;
import org.perfcake.pc4idea.api.editor.openapi.ui.EditorDialog;
import org.perfcake.pc4idea.api.editor.swing.EditorTablePanel;
import org.perfcake.pc4idea.api.util.MessagesValidationSync;
import org.perfcake.pc4idea.impl.editor.editor.tablemodel.AttachedValidatorsTableModel;
import org.perfcake.pc4idea.impl.editor.editor.tablemodel.HeadersTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private JTextField textFieldMessageURI;
    private JTextField textFieldMultiplicity;
    private JTextField textFieldContent;
    private EditorTablePanel tablePanelHeaders;
    private PropertiesEditor panelProperties;
    private AttachedValidatorsEditor panelAttachedValidators;

    public MessageEditor(MessagesValidationSync sync) {
        initComponents();
        panelAttachedValidators.setUsedValidatorIDSet(sync.getValidatorIds());
    }

    private void initComponents(){
        JLabel labelMessageURI = new JLabel("URI:");
        JLabel labelMultiplicity = new JLabel("Multiplicity:");
        JLabel labelMessageContent = new JLabel("Content:");
        textFieldMessageURI = new JTextField(null);
        textFieldMultiplicity = new JTextField(null);
        textFieldContent = new JTextField(null);

        tablePanelHeaders = new EditorTablePanel(new HeadersTableModel(new ArrayList<>()));

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
        tablePanelHeaders.setTableModel(new HeadersTableModel(message.getHeader()));
        panelAttachedValidators.setValidatorRefs(message.getValidatorRef());
        panelProperties.setListProperties(message.getProperty());
    }

    public Scenario.Messages.Message getMessage(){
        Scenario.Messages.Message newMessage = new Scenario.Messages.Message();
        boolean uriIsEmpty = (textFieldMessageURI.getText().isEmpty() || textFieldMessageURI.getText().trim().isEmpty());
        newMessage.setUri(uriIsEmpty ? null : textFieldMessageURI.getText());

        boolean multiplicityIsEmpty = (textFieldMultiplicity.getText().isEmpty() || textFieldMultiplicity.getText().trim().isEmpty());
        if (multiplicityIsEmpty){
            newMessage.setMultiplicity("1");
            Notifications.Bus.notify(new Notification("PerfCake Plugin", "Multiplicity field is empty",
                    "Multiplicity will be set to default value \"1\".",
                    NotificationType.INFORMATION), null);
        } else {
            newMessage.setMultiplicity(textFieldMultiplicity.getText());
        }

        boolean contentIsEmpty = (textFieldContent.getText().isEmpty() || textFieldContent.getText().trim().isEmpty());
        if (contentIsEmpty){
            if (uriIsEmpty) {
                newMessage.setContent("");
                Notifications.Bus.notify(new Notification("PerfCake Plugin", "Both URI and Content fields are empty",
                        "This will lead to message specified by content with value \"\" (empty string).",
                        NotificationType.INFORMATION), null);
            } else {
                newMessage.setContent(null);
//                Notifications.Bus.notify(new Notification("PerfCake Plugin", "Ambiguous Input",
//                        "URI is set and Content is empty. While PerfCake prioritizes Content and " +
//                                "Content with empty string value is valid, this configuration is ambiguous. " +
//                                "For now, Content will be set to null and URI will be used. " +
//                                "If you want to use Content with empty string value, please remove URI value.",
//                        NotificationType.INFORMATION), null);
            }
        } else {
            newMessage.setContent(textFieldContent.getText());
            if (!uriIsEmpty){
                Notifications.Bus.notify(new Notification("PerfCake Plugin", "Both URI and Content fields are filled",
                        "While PerfCake prioritizes Content, this will lead to message specified by content and URI will be ignored.",
                        NotificationType.INFORMATION), null);
            }
        }

        newMessage.getHeader().addAll(((HeadersTableModel) tablePanelHeaders.getTableModel()).getHeaderList());
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
        // always valid
        return null;
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

            buttonAttach = new JButton("Attach", AllIcons.General.Add);
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
                    EditorDialog dialog = new EditorDialog(attachValidatorEditor);
                    dialog.show();
                    if (dialog.getExitCode() == 0) {
                        Scenario.Messages.Message.ValidatorRef validatorRef = attachValidatorEditor.getAttachedValidatorRef();
                        ((AttachedValidatorsTableModel)tableAttachedValidators.getModel()).getValidatorRefList().add(validatorRef);
                        tableAttachedValidators.repaint();
                        tableAttachedValidators.revalidate();
                    }
                }
            });

            buttonDetach = new JButton("Detach", AllIcons.Actions.Delete);
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
                            .addComponent(buttonAttach, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                            .addComponent(buttonDetach, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)));
            layout.setVerticalGroup(layout.createParallelGroup()
                    .addComponent(scrollPaneTableAttachedValidators, 0, GroupLayout.PREFERRED_SIZE, 100)
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
    }
}
