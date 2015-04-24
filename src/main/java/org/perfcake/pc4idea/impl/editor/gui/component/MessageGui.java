package org.perfcake.pc4idea.impl.editor.gui.component;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.actions.ActionType;
import org.perfcake.pc4idea.api.editor.color.ColorType;
import org.perfcake.pc4idea.api.editor.gui.component.ComponentGui;
import org.perfcake.pc4idea.api.editor.openapi.ui.EditorDialog;
import org.perfcake.pc4idea.impl.editor.actions.*;
import org.perfcake.pc4idea.impl.editor.editor.component.MessageEditor;
import org.perfcake.pc4idea.impl.editor.modelwrapper.component.MessageModelWrapper;
import org.perfcake.pc4idea.impl.editor.modelwrapper.component.MessagesModelWrapper;
import org.perfcake.pc4idea.todo.settings.ColorComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Stanislav Kaleta on 3/27/15.
 */
public class MessageGui extends ComponentGui {
    private MessageModelWrapper modelWrapper;
    private MessagesModelWrapper parentModelWrapper;

    private JLabel messageAttr;

    private Dimension messageSize = new Dimension(40, 40);

    public MessageGui(MessageModelWrapper modelWrapper, MessagesModelWrapper parentModelWrapper) {
        super(modelWrapper.getContext());
        this.modelWrapper = modelWrapper;
        this.parentModelWrapper = parentModelWrapper;
        initComponents();
        updateColors();
    }

    private void initComponents() {
        messageAttr = new JLabel("-");
        messageAttr.setFont(new Font(messageAttr.getFont().getName(), 0, 15));

        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        this.add(messageAttr);

        layout.putConstraint(SpringLayout.NORTH, messageAttr,
                10,
                SpringLayout.NORTH, this);

        layout.putConstraint(SpringLayout.WEST, messageAttr,
                15,
                SpringLayout.WEST, this);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ((JPanel)e.getComponent().getAccessibleContext().getAccessibleParent()).dispatchEvent(e);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                ((JPanel)e.getComponent().getAccessibleContext().getAccessibleParent()).dispatchEvent(e);
            }
            @Override
            public void mouseReleased(MouseEvent e){
                ((JPanel)e.getComponent().getAccessibleContext().getAccessibleParent()).dispatchEvent(e);
            }
        });

        getActionMap().put(ActionType.EDIT, new EditAction(modelWrapper));
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.SHIFT_MASK), ActionType.EDIT);

        getActionMap().put(ActionType.DEL, new DeleteAction(parentModelWrapper, modelWrapper));
        getInputMap().put(KeyStroke.getKeyStroke("DELETE"), ActionType.DEL);

        getActionMap().put(ActionType.ADDH, new AddHeaderAction(modelWrapper));
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.SHIFT_MASK), ActionType.ADDH);

        getActionMap().put(ActionType.ADDP, new AddPropertyAction(modelWrapper));
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.SHIFT_MASK), ActionType.ADDP);

        getActionMap().put(ActionType.ATTV, new AttachValidatorAction(modelWrapper, parentModelWrapper.getSync()));
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.SHIFT_MASK), ActionType.ATTV);
    }

    @Override
    public void performImport(String transferredData) {
        if (transferredData.equals("Attach validator")){
            getActionMap().get(ActionType.ATTV).actionPerformed(null);
        }
    }

    @Override
    public Object openEditorDialogAndGetResult() {
        MessageEditor editor = new MessageEditor(parentModelWrapper.getSync());
        editor.setMessage((Scenario.Messages.Message) modelWrapper.retrieveModel());
        EditorDialog dialog = new EditorDialog(editor);
        dialog.show();
        if (dialog.getExitCode() == 0) {
            return editor.getMessage();
        }
        return null;
    }

    @Override
    public void updateGui() {
        Scenario.Messages.Message message = (Scenario.Messages.Message) modelWrapper.retrieveModel();
        String uri = message.getUri();
        String content = message.getContent();
        if (uri == null){
            messageAttr.setText("*");
            messageSize.width = 40;
        } else {
            if (content == null) {
                FontMetrics fontMetrics = messageAttr.getFontMetrics(messageAttr.getFont());
                int uriLength = fontMetrics.stringWidth(uri) + 30;
                if (uriLength > 200) {
                    messageAttr.setText("...");
                    messageSize.width = 40;
                } else {
                    messageAttr.setText(uri);
                    messageSize.width = uriLength;
                }
            } else {
                messageAttr.setText("*");
                messageSize.width = 40;
            }
        }
        parentModelWrapper.getSync().syncValidatorRef();
        parentModelWrapper.getSync().repaintDependencies();
    }

    @Override
    public void updateColors() {
        setBackground(ColorComponents.getColor(ColorType.MESSAGE_BACKGROUND));
        Color foregroundColor = ColorComponents.getColor(ColorType.MESSAGE_FOREGROUND);
        setForeground(foregroundColor);
        messageAttr.setForeground(foregroundColor);
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
