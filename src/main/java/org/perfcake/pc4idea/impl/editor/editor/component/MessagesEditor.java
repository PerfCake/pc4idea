package org.perfcake.pc4idea.impl.editor.editor.component;

import com.intellij.openapi.ui.ValidationInfo;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.editor.component.AbstractEditor;
import org.perfcake.pc4idea.api.editor.swing.EditorTablePanel;
import org.perfcake.pc4idea.api.util.MessagesValidationSync;
import org.perfcake.pc4idea.impl.editor.editor.tablemodel.MessagesTableModel;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 28.10.2014
 */
public class MessagesEditor extends AbstractEditor {
    private EditorTablePanel tablePanelMessages;

    //private Module module;
    private MessagesValidationSync sync;

    public MessagesEditor(/*Module module, */MessagesValidationSync sync) {
        //this.module = module;
        this.sync = sync;
        initComponents();
    }

    private void initComponents(){
        tablePanelMessages = new EditorTablePanel(new MessagesTableModel(new ArrayList<>(), sync));

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addComponent(tablePanelMessages));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(tablePanelMessages));
    }

    public void setMessages(Scenario.Messages messages) {
        tablePanelMessages.setTableModel(new MessagesTableModel(messages.getMessage(), sync));

    }

    public Scenario.Messages getMessages(){
        Scenario.Messages newMessages = new Scenario.Messages();
        newMessages.getMessage().addAll(((MessagesTableModel) tablePanelMessages.getTableModel()).getMessageList());
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
}
