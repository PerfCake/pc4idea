package org.perfcake.pc4idea.editor.gui;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.swing.ComponentsPanel;
import org.perfcake.pc4idea.editor.Messages;
import org.perfcake.pc4idea.editor.ScenarioDialogEditor;
import org.perfcake.pc4idea.editor.actions.ActionType;
import org.perfcake.pc4idea.editor.actions.AddMessageAction;
import org.perfcake.pc4idea.editor.actions.EditAction;
import org.perfcake.pc4idea.editor.colors.ColorComponents;
import org.perfcake.pc4idea.editor.colors.ColorType;
import org.perfcake.pc4idea.editor.editors.MessagesEditor;
import org.perfcake.pc4idea.editor.modelwrapper.MessagesModelWrapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * Created by Stanislav Kaleta on 3/7/15.
 */
public class MessagesGUI extends AbstractComponentGUI  {/*TODO attach validator*/
    private MessagesModelWrapper modelWrapper;

    private JLabel labelMessages;
    private ComponentsPanel panelMessages;

    private int labelMessagesWidth = 0;

    public MessagesGUI(MessagesModelWrapper modelWrapper, ActionMap baseActionMap){
        super(baseActionMap);
        this.modelWrapper = modelWrapper;
        initComponents();
        updateColors();
    }

    private void initComponents(){
        labelMessages = new JLabel("Messages");
        labelMessages.setFont(new Font(labelMessages.getFont().getName(),0,15));
        FontMetrics fontMetrics = labelMessages.getFontMetrics(labelMessages.getFont());
        labelMessagesWidth = fontMetrics.stringWidth(labelMessages.getText());

        panelMessages = new ComponentsPanel(modelWrapper);

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

        this.getActionMap().put(ActionType.ADDM, new AddMessageAction(modelWrapper, Messages.BUNDLE.getString("ADD")+" Message"));
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.SHIFT_MASK), ActionType.ADDM);

        this.getActionMap().put(ActionType.EDIT, new EditAction(modelWrapper, Messages.BUNDLE.getString("EDIT")+" Message"));
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.SHIFT_MASK), ActionType.EDIT);
    }

    public Point getMessageAnchorPoint(Scenario.Messages.Message message){
        Point anchorPoint = panelMessages.getComponentAnchorPoint(message, true);
        anchorPoint.setLocation(anchorPoint.getX()+this.getX(),anchorPoint.getY()+this.getY());
        return anchorPoint;
    }

    @Override
    void performImport(String transferredData) {
        if (transferredData.equals("Message")){
            getActionMap().get(ActionType.ADDM).actionPerformed(null);
        }
    }

    @Override
    public Object openEditorDialogAndGetResult() {
        MessagesEditor editor = new MessagesEditor();
        Scenario.Messages model = (Scenario.Messages) modelWrapper.retrieveModel();
        editor.setMessages((model == null) ? new Scenario.Messages() : model, modelWrapper.getSync().getValidatorIDs());
        ScenarioDialogEditor dialog = new ScenarioDialogEditor(editor);
        dialog.show();
        if (dialog.getExitCode() == 0) {
            return editor.getMessages();
        }
        return null;
    }

    @Override
    public void updateGUI() {
        panelMessages.updateComponents();
    }

    @Override
    public void updateColors() {
        setBackground(ColorComponents.getColor(ColorType.MESSAGES_BACKGROUND));
        Color foregroundColor = ColorComponents.getColor(ColorType.MESSAGES_FOREGROUND);
        setForeground(foregroundColor);
        labelMessages.setForeground(foregroundColor);
    }

    @Override
    public Dimension getMinimumSize(){
        int panelMinWidth = panelMessages.getMinimumSize().width;
        int width = (panelMinWidth + 20 > labelMessagesWidth + 30) ? panelMinWidth + 20 : labelMessagesWidth + 30;
        return new Dimension(width,panelMessages.getMinimumSize().height + 50);
    }

    @Override
    public Dimension getPreferredSize(){
        return new Dimension(super.getPreferredSize().width,panelMessages.getPreferredSize().height + 50);
    }

    @Override
    public Dimension getMaximumSize(){
        return new Dimension(super.getMaximumSize().width,panelMessages.getMaximumSize().height + 50);

    }
}
