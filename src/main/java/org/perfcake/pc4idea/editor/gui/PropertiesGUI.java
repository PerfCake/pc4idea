package org.perfcake.pc4idea.editor.gui;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.Messages;
import org.perfcake.pc4idea.editor.ScenarioDialogEditor;
import org.perfcake.pc4idea.editor.actions.ActionType;
import org.perfcake.pc4idea.editor.actions.AddPropertyAction;
import org.perfcake.pc4idea.editor.actions.EditAction;
import org.perfcake.pc4idea.editor.actions.ReorderAction;
import org.perfcake.pc4idea.editor.colors.ColorComponents;
import org.perfcake.pc4idea.editor.colors.ColorType;
import org.perfcake.pc4idea.editor.editors.PropertiesEditor;
import org.perfcake.pc4idea.editor.modelwrapper.PropertiesModelWrapper;
import org.perfcake.pc4idea.editor.swing.ComponentsPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * Created by Stanislav Kaleta on 3/7/15.
 */
public class PropertiesGUI extends AbstractComponentGUI {
    private PropertiesModelWrapper modelWrapper;

    private JLabel labelProperties;
    private ComponentsPanel panelProperties;

    private int labelPropertiesWidth = 0;

    public PropertiesGUI(PropertiesModelWrapper modelWrapper, ActionMap baseActionMap){
        super(baseActionMap);
        this.modelWrapper = modelWrapper;
        initComponents();
        updateColors();
    }

    private void initComponents(){
        labelProperties = new JLabel("Scenario Properties");
        labelProperties.setFont(new Font(labelProperties.getFont().getName(),0,15));

        FontMetrics fontMetrics = labelProperties.getFontMetrics(labelProperties.getFont());
        labelPropertiesWidth = fontMetrics.stringWidth(labelProperties.getText());

        panelProperties = new ComponentsPanel(modelWrapper);

        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        this.add(labelProperties);
        this.add(panelProperties);

        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, labelProperties,
                0,
                SpringLayout.HORIZONTAL_CENTER, this);
        layout.putConstraint(SpringLayout.NORTH, labelProperties,
                10,
                SpringLayout.NORTH, this);

        layout.putConstraint(SpringLayout.WEST, panelProperties,
                10,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, panelProperties,8,SpringLayout.SOUTH, labelProperties);

        this.getActionMap().put(ActionType.ADDP, new AddPropertyAction(modelWrapper, Messages.BUNDLE.getString("ADD")+" Property"));
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.SHIFT_MASK), ActionType.ADDP);

        this.getActionMap().put(ActionType.EDIT, new EditAction(modelWrapper, Messages.BUNDLE.getString("EDIT")+" Properties"));
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.SHIFT_MASK), ActionType.EDIT);

        this.getActionMap().put(ActionType.REORDER, new ReorderAction(modelWrapper, "Properties: " + Messages.BUNDLE.getString("REORDER") + " Property"));
    }

    @Override
    void performImport(String transferredData) {
        if (transferredData.equals("Property")) {
            getActionMap().get(ActionType.ADDP).actionPerformed(null);
        }
    }

    @Override
    public Object openEditorDialogAndGetResult() {
        PropertiesEditor editor = new PropertiesEditor();
        Scenario.Properties model = (Scenario.Properties) modelWrapper.retrieveModel();
        editor.setObjProperties((model == null) ? new Scenario.Properties() : model);
        ScenarioDialogEditor dialog = new ScenarioDialogEditor(editor);
        dialog.show();
        if (dialog.getExitCode() == 0) {
            return editor.getObjProperties();
        }
        return null;
    }

    @Override
    public void updateGUI() {
        panelProperties.updateComponents();
    }

    @Override
    public void updateColors() {
        setBackground(ColorComponents.getColor(ColorType.PROPERTIES_BACKGROUND));
        Color foregroundColor = ColorComponents.getColor(ColorType.PROPERTIES_FOREGROUND);
        setForeground(foregroundColor);
        labelProperties.setForeground(foregroundColor);
    }

    @Override
    public Dimension getMinimumSize(){
        int panelMinWidth = panelProperties.getMinimumSize().width;
        int width = (panelMinWidth + 20 > labelPropertiesWidth + 30) ? panelMinWidth + 20 : labelPropertiesWidth + 30;
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
