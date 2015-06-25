package org.perfcake.pc4idea.impl.editor.gui.component;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.actions.ActionType;
import org.perfcake.pc4idea.api.editor.color.ColorType;
import org.perfcake.pc4idea.api.editor.gui.ComponentGui;
import org.perfcake.pc4idea.api.editor.openapi.ui.EditorDialog;
import org.perfcake.pc4idea.api.editor.swing.ComponentsPanel;
import org.perfcake.pc4idea.api.util.Messages;
import org.perfcake.pc4idea.impl.editor.actions.AddPropertyAction;
import org.perfcake.pc4idea.impl.editor.actions.EditAction;
import org.perfcake.pc4idea.impl.editor.actions.ReorderAction;
import org.perfcake.pc4idea.impl.editor.editor.component.SenderEditor;
import org.perfcake.pc4idea.impl.editor.modelwrapper.component.SenderModelWrapper;
import org.perfcake.pc4idea.impl.settings.ColorComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;


/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 3.3.2015
 */
public class SenderGui extends ComponentGui {
    private SenderModelWrapper modelWrapper;

    private JLabel labelSenderClass;
    private ComponentsPanel panelProperties;

    private int labelSenderClassWidth = 0;

    public SenderGui(SenderModelWrapper modelWrapper) {
        super(modelWrapper.getContext());
        this.modelWrapper = modelWrapper;
        initComponents();
        updateColors();
    }

    private void initComponents(){
        labelSenderClass = new JLabel("---");
        labelSenderClass.setFont(new Font(labelSenderClass.getFont().getName(), 0, 15));

        panelProperties = new ComponentsPanel(modelWrapper);

        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        this.add(labelSenderClass);
        this.add(panelProperties);

        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, labelSenderClass,
                0,
                SpringLayout.HORIZONTAL_CENTER, this);
        layout.putConstraint(SpringLayout.NORTH, labelSenderClass,
                10,
                SpringLayout.NORTH, this);

        layout.putConstraint(SpringLayout.WEST, panelProperties,
                10,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, panelProperties,8,SpringLayout.SOUTH, labelSenderClass);

        getActionMap().put(ActionType.ADDP, new AddPropertyAction(modelWrapper));
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.SHIFT_MASK), ActionType.ADDP);

        getActionMap().put(ActionType.EDIT, new EditAction(modelWrapper));
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.SHIFT_MASK), ActionType.EDIT);

        getActionMap().put(ActionType.REORDER, new ReorderAction(modelWrapper, Messages.Scenario.PROPERTY));
    }

    @Override
    public void performImport(String transferredData) {
        if (transferredData.equals("Property")) {
            getActionMap().get(ActionType.ADDP).actionPerformed(null);
        }
    }

    @Override
    public Object openEditorDialogAndGetResult() {
        SenderEditor editor = new SenderEditor(modelWrapper.getContext().getModule());
        editor.setSender((Scenario.Sender) modelWrapper.retrieveModel());
        EditorDialog dialog = new EditorDialog(editor);
        dialog.show();
        if (dialog.getExitCode() == 0) {
            return editor.getSender();
        }
        return null;
    }

    @Override
    public void updateGui() {
        Scenario.Sender sender = (Scenario.Sender) modelWrapper.retrieveModel();
        labelSenderClass.setText(sender.getClazz());
        FontMetrics fontMetrics = labelSenderClass.getFontMetrics(labelSenderClass.getFont());
        labelSenderClassWidth = fontMetrics.stringWidth(labelSenderClass.getText());
        panelProperties.updateComponents();
    }

    @Override
    public void updateColors() {
        setBackground(ColorComponents.getColor(ColorType.SENDER_BACKGROUND));
        Color foregroundColor = ColorComponents.getColor(ColorType.SENDER_FOREGROUND);
        setForeground(foregroundColor);
        labelSenderClass.setForeground(foregroundColor);
    }

    @Override
    public Dimension getMinimumSize(){
        int panelMinWidth = panelProperties.getMinimumSize().width;
        int width = (panelMinWidth + 20 > labelSenderClassWidth + 30) ?
                panelMinWidth + 20 : labelSenderClassWidth + 30;
        return new Dimension(width,panelProperties.getMinimumSize().height + 50);
    }

    @Override
    public Dimension getPreferredSize(){
        return new Dimension(super.getPreferredSize().width,panelProperties.getPreferredSize().height + 50);
    }

    @Override
    public Dimension getMaximumSize(){
        return new Dimension(super.getMaximumSize().width,panelProperties.getMaximumSize().height + 50);
    }
}
