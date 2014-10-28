package org.perfcake.pc4idea.editor.gui;

import com.intellij.openapi.project.Project;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.wizard.MessagesEditor;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 28.9.2014
 */
public class MessagesPanel extends AbstractPanel {
    private final String TITLE ="Messages Editor";
    private Color messagesColor = Color.getHSBColor(40/360f,1f,0.75f);
    private final Project project;

    private MessagesEditor messagesEditor;
    private Scenario.Messages messages;

    private JLabel labelMessages;
    private JPanel panelMessages;

    public MessagesPanel(Project project){
        super(project);
        this.project = project;

        initComponents();
    }

    private void initComponents(){
        labelMessages = new JLabel("Messages");
        labelMessages.setFont(new Font(labelMessages.getFont().getName(),0,15));
        labelMessages.setForeground(messagesColor);
//        FontMetrics fontMetrics = labelMessages.getFontMetrics(labelMessages.getFont());
//        labelMessagesWidth = fontMetrics.stringWidth(labelMessages.getText());

        panelMessages = new JPanel();
        panelMessages.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
        /*panelMessages.setBackground(Color.cyan);*/panelMessages.setOpaque(false);

        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        this.add(labelMessages);
        this.add(panelMessages);

        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, labelMessages,
                0,
                SpringLayout.HORIZONTAL_CENTER, this);
        layout.putConstraint(SpringLayout.NORTH, labelMessages,
                10,
                SpringLayout.NORTH, this);

        layout.putConstraint(SpringLayout.WEST, panelMessages,
                10,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, panelMessages,8,SpringLayout.SOUTH, labelMessages);

    }

    @Override
   protected Color getColor() {
        return messagesColor;
    }

    @Override
    protected String getEditorTitle() {
        return TITLE;
    }

    @Override
    protected JPanel getEditorPanel() {
        messagesEditor = new MessagesEditor();
        messagesEditor.setMessages(messages);
        return messagesEditor;
    }

    @Override
    protected void applyChanges() {
        this.setComponent(messagesEditor.getMessages());
    }

    @Override
    public void setComponent(Object component) {
        messages = (Scenario.Messages) component;

        panelMessages.removeAll();
        panelMessages.repaint();

        for (Scenario.Messages.Message message : messages.getMessage()){
            MessageComponent messageComponent = new MessageComponent(project,messagesColor);
            messageComponent.setComponent(message);
            panelMessages.add(messageComponent);
        }

        panelMessages.revalidate();
        this.revalidate();
    }

    @Override
    public Object getComponent() {
        return messages;
    }


}
