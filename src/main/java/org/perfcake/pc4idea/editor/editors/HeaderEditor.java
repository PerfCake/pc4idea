package org.perfcake.pc4idea.editor.editors;

import com.intellij.openapi.ui.ValidationInfo;
import org.perfcake.model.Header;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 2.12.2014
 */
public class HeaderEditor extends AbstractEditor {
    private JLabel labelHeaderName;
    private JLabel labelHeaderValue;
    private JTextField textFieldHeaderName;
    private JTextField textFieldHeaderValue;

    public HeaderEditor(){
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

    public void setHeader(Header header){
        textFieldHeaderName.setText(header.getName());
        textFieldHeaderValue.setText(header.getValue());
    }

    public Header getHeader(){
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
