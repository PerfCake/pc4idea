package org.perfcake.pc4idea.editor.gui;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.swing.ComponentsPanel;
import org.perfcake.pc4idea.editor.Messages;
import org.perfcake.pc4idea.editor.ScenarioDialogEditor;
import org.perfcake.pc4idea.editor.actions.ActionType;
import org.perfcake.pc4idea.editor.actions.AddPropertyAction;
import org.perfcake.pc4idea.editor.actions.EditAction;
import org.perfcake.pc4idea.editor.colors.ColorComponents;
import org.perfcake.pc4idea.editor.colors.ColorType;
import org.perfcake.pc4idea.editor.editors.SenderEditor;
import org.perfcake.pc4idea.editor.modelwrapper.SenderModelWrapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;


/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 3.3.2015
 */
public class SenderGUI extends AbstractComponentGUI {
    private SenderModelWrapper modelWrapper;

    private JLabel labelSenderClass;
    private ComponentsPanel panelProperties;

    private int labelSenderClassWidth = 0;

    public SenderGUI(SenderModelWrapper modelWrapper, ActionMap baseActionMap){
        super(baseActionMap);
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

        this.getActionMap().put(ActionType.ADDP, new AddPropertyAction(modelWrapper, Messages.BUNDLE.getString("ADD")+" Property"));
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.SHIFT_MASK), ActionType.ADDP);

        this.getActionMap().put(ActionType.EDIT, new EditAction(modelWrapper, Messages.BUNDLE.getString("EDIT")+" Sender"));
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.SHIFT_MASK), ActionType.EDIT);
    }

    @Override
    protected void performImport(String transferredData) {
        if (transferredData.equals("Property")) {
            getActionMap().get(ActionType.ADDP).actionPerformed(null);
        }
    }

    @Override
    public Object openEditorDialogAndGetResult() {
        SenderEditor editor = new SenderEditor();
        editor.setSender((Scenario.Sender) modelWrapper.retrieveModel());
        ScenarioDialogEditor dialog = new ScenarioDialogEditor(editor);
        dialog.show();
        if (dialog.getExitCode() == 0) {
            return editor.getSender();
        }
        return null;
    }

    @Override
    public void updateGUI() {
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
        int width = (panelMinWidth + 20 > labelSenderClassWidth + 30) ? panelMinWidth + 20 : labelSenderClassWidth + 30;
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
