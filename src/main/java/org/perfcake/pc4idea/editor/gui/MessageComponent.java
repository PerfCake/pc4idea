package org.perfcake.pc4idea.editor.gui;

import com.intellij.openapi.project.Project;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.wizard.MessageEditor;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 28.10.2014
 */
public class MessageComponent extends AbstractPanel {
    private final String TITLE ="Message Editor";
    private final Color messageColor;

    private MessageEditor messageEditor;
    private Scenario.Messages.Message message;

    private JLabel messageAttr;
    private Dimension messageSize;

    public MessageComponent(Project project, Color messagesColor){
        super(project);
        this.messageColor = messagesColor;

        initComponents();
    }

    private void initComponents(){
        messageAttr = new JLabel("-");
        messageAttr.setFont(new Font(messageAttr.getFont().getName(), 0, 15));
        messageAttr.setForeground(messageColor);
        messageSize = new Dimension(40,40);

        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        this.add(messageAttr);

        layout.putConstraint(SpringLayout.NORTH, messageAttr,
                10,
                SpringLayout.NORTH, this);

        layout.putConstraint(SpringLayout.WEST, messageAttr,
                15,
                SpringLayout.WEST, this);
    }

    @Override
    protected Color getColor() {
        return messageColor;
    }

    @Override
    protected String getEditorTitle() {
        return TITLE;
    }

    @Override
    protected JPanel getEditorPanel() {
        messageEditor = new MessageEditor();
        messageEditor.setMessage(message);
        return messageEditor;
    }

    @Override
    protected void applyChanges() {
        this.setComponent(messageEditor.getMessage());
    }     /*TODO po edit. message. neuklada do messages!!!*/

    @Override
    public void setComponent(Object component) {
        message = (Scenario.Messages.Message) component;
        messageAttr.setText(message.getUri());
        FontMetrics fontMetrics = messageAttr.getFontMetrics(messageAttr.getFont());
        messageSize.width = fontMetrics.stringWidth(messageAttr.getText()) + 30;
    }

    @Override
    public Object getComponent() {
        return message;
    }

    @Override
    public Dimension getMinimumSize(){
        return messageSize;
    }

    @Override
    public Dimension getPreferredSize(){
        return messageSize;
    }

    @Override
    public Dimension getMaximumSize(){
        return messageSize;
    }
}
