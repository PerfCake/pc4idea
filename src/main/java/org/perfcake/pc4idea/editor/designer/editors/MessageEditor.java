package org.perfcake.pc4idea.editor.designer.editors;

import com.intellij.openapi.ui.ValidationInfo;
import org.perfcake.model.Header;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.designer.common.EditorTablePanel;
import org.perfcake.pc4idea.editor.designer.common.ScenarioDialogEditor;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 28.10.2014
 */
public class MessageEditor extends AbstractEditor {
    private JLabel labelMessageURI;
    private JLabel labelMessageMultiplicity;
    private JLabel labelMessageContent;
    private JTextField textFieldMessageURI;
    private JTextField textFieldMessageMultiplicity;
    private JTextField textFieldContent;
    private EditorTablePanel tablePanelHeaders;
    private PropertiesEditor panelProperties;
    private AttachedValidatorsEditor panelAttachedValidators;

    public MessageEditor(){
        initComponents();
    }

    private void initComponents(){
        labelMessageURI = new JLabel("URI:");
        labelMessageMultiplicity = new JLabel("Multiplicity:");
        labelMessageContent = new JLabel("Content:");
        textFieldMessageURI = new JTextField();
        textFieldMessageMultiplicity = new JTextField();
        textFieldContent = new JTextField();

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

        panelAttachedValidators = new AttachedValidatorsEditor();/*TODO*/

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                        .addComponent(labelMessageURI, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                        .addComponent(textFieldMessageURI))
                .addGroup(layout.createSequentialGroup()
                        .addComponent(labelMessageMultiplicity, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                        .addComponent(textFieldMessageMultiplicity))
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
                        .addComponent(labelMessageMultiplicity, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                        .addComponent(textFieldMessageMultiplicity, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
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
        textFieldMessageMultiplicity.setText(message.getMultiplicity());
        textFieldContent.setText(message.getContent());
       tablePanelHeaders.getTable().setModel(new HeadersTableModel(message.getHeader()));
        panelProperties.setListProperties(message.getProperty());
        //panelAttachedValidators message.getValidatorRef()
    }

    public Scenario.Messages.Message getMessage(){
        Scenario.Messages.Message newMessage = new Scenario.Messages.Message();
        newMessage.setUri(textFieldMessageURI.getText());
        newMessage.setMultiplicity(textFieldMessageMultiplicity.getText());
        newMessage.setContent(textFieldContent.getText());
        newMessage.getHeader().addAll(((HeadersTableModel)tablePanelHeaders.getTable().getModel()).getHeaderList());
        newMessage.getProperty().addAll(panelProperties.getListProperties());
        //newMessage.getValidatorRef().addAll(panelAttachedValidators getatt...)
        return newMessage;
    }

    @Override
    public String getTitle(){
        return "Message Editor";
    }

    @Override
    public ValidationInfo areInsertedValuesValid() {
//        return (textFieldMessageURI.getText().isEmpty() || textFieldMessageMultiplicity.getText().isEmpty()
//                || textFieldContent.getText().isEmpty()) ?
//                new ValidationInfo("Text fields can't be empty") : null;
        return null; /*TODO maybe not need to be filled*/
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

    private class HeaderEditor extends AbstractEditor {
        private JLabel labelHeaderName;
        private JLabel labelHeaderValue;
        private JTextField textFieldHeaderName;
        private JTextField textFieldHeaderValue;

        private HeaderEditor(){
            initComponents();
        }

        private void initComponents(){
            labelHeaderName  = new JLabel("Name:");
            labelHeaderValue = new JLabel("Value:");
            textFieldHeaderName = new JTextField();
            textFieldHeaderValue = new JTextField();

            GroupLayout layout = new GroupLayout(this);
            this.setLayout(layout);
            layout.setHorizontalGroup(layout.createParallelGroup()
                    .addGroup(layout.createSequentialGroup()
                            .addComponent(labelHeaderName,GroupLayout.PREFERRED_SIZE,50,GroupLayout.PREFERRED_SIZE)
                            .addComponent(textFieldHeaderName))
                    .addGroup(layout.createSequentialGroup()
                            .addComponent(labelHeaderValue,GroupLayout.PREFERRED_SIZE,50,GroupLayout.PREFERRED_SIZE)
                            .addComponent(textFieldHeaderValue)));
            layout.setVerticalGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup()
                            .addComponent(labelHeaderName, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                            .addComponent(textFieldHeaderName, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                    .addGap(10)
                    .addGroup(layout.createParallelGroup()
                            .addComponent(labelHeaderValue, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                            .addComponent(textFieldHeaderValue, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)));
        }

        private void setHeader(Header header){
            textFieldHeaderName.setText(header.getName());
            textFieldHeaderValue.setText(header.getValue());
        }

        private Header getHeader(){
            Header newHeader = new Header();
            newHeader.setName(textFieldHeaderName.getText());
            newHeader.setValue(textFieldHeaderValue.getText());
            return newHeader;
        }

        @Override
        public String getTitle(){
            return "Header Editor";
        }

        @Override
        public ValidationInfo areInsertedValuesValid() {
            return (textFieldHeaderName.getText().isEmpty() || textFieldHeaderValue.getText().isEmpty()) ?
                    new ValidationInfo("Text fields can't be empty") : null;
        }
    }

    private class AttachedValidatorsEditor extends JPanel {
        /*TODO*/
    }
}
