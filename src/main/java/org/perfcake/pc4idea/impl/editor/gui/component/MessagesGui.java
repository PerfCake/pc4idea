package org.perfcake.pc4idea.impl.editor.gui.component;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.actions.ActionType;
import org.perfcake.pc4idea.api.editor.color.ColorType;
import org.perfcake.pc4idea.api.editor.gui.ComponentGui;
import org.perfcake.pc4idea.api.editor.openapi.ui.EditorDialog;
import org.perfcake.pc4idea.api.editor.swing.ComponentsPanel;
import org.perfcake.pc4idea.api.util.Messages;
import org.perfcake.pc4idea.impl.editor.actions.AddMessageAction;
import org.perfcake.pc4idea.impl.editor.actions.EditAction;
import org.perfcake.pc4idea.impl.editor.actions.ReorderAction;
import org.perfcake.pc4idea.impl.editor.editor.component.MessagesEditor;
import org.perfcake.pc4idea.impl.editor.modelwrapper.component.MessagesModelWrapper;
import org.perfcake.pc4idea.impl.settings.ColorComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * Created by Stanislav Kaleta on 3/7/15.
 */
public class MessagesGui extends ComponentGui {
    private MessagesModelWrapper modelWrapper;

    private JLabel labelMessages;
    private ComponentsPanel panelMessages;

    private int labelMessagesWidth = 0;

    public MessagesGui(MessagesModelWrapper modelWrapper) {
        super(modelWrapper.getContext());
        this.modelWrapper = modelWrapper;
        initComponents();
        updateColors();
    }

    private void initComponents(){
        labelMessages = new JLabel(Messages.Scenario.MESSAGES);
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

        getActionMap().put(ActionType.ADDM, new AddMessageAction(modelWrapper));
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.SHIFT_MASK), ActionType.ADDM);

        getActionMap().put(ActionType.EDIT, new EditAction(modelWrapper));
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.SHIFT_MASK), ActionType.EDIT);

        getActionMap().put(ActionType.REORDER, new ReorderAction(modelWrapper, Messages.Scenario.MESSAGE));
    }

    public Point getMessageAnchorPoint(Scenario.Messages.Message message){
        Point anchorPoint = panelMessages.getComponentAnchorPoint(message, true);
        anchorPoint.setLocation(anchorPoint.getX()+this.getX(),anchorPoint.getY()+this.getY());
        return anchorPoint;
    }

    @Override
    public void performImport(String transferredData) {
        if (transferredData.equals("Message")){
            getActionMap().get(ActionType.ADDM).actionPerformed(null);
        }
    }

    @Override
    public Object openEditorDialogAndGetResult() {
        MessagesEditor editor = new MessagesEditor(modelWrapper.getSync(), modelWrapper.getContext().getModule());
        editor.setMessages((Scenario.Messages) modelWrapper.retrieveModel());
        EditorDialog dialog = new EditorDialog(editor);
        dialog.show();
        if (dialog.getExitCode() == 0) {
            return editor.getMessages();
        }
        return null;
    }

    @Override
    public void updateGui() {
        panelMessages.updateComponents();
        modelWrapper.getSync().syncValidatorRef();
        modelWrapper.getSync().repaintDependencies();
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
