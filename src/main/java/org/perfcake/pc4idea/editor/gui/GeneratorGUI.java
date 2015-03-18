package org.perfcake.pc4idea.editor.gui;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.Messages;
import org.perfcake.pc4idea.editor.ScenarioDialogEditor;
import org.perfcake.pc4idea.editor.actions.ActionType;
import org.perfcake.pc4idea.editor.actions.AddPropertyAction;
import org.perfcake.pc4idea.editor.actions.EditAction;
import org.perfcake.pc4idea.editor.colors.ColorComponents;
import org.perfcake.pc4idea.editor.colors.ColorType;
import org.perfcake.pc4idea.editor.editors.GeneratorEditor;
import org.perfcake.pc4idea.editor.interfaces.CanAddProperty;
import org.perfcake.pc4idea.editor.interfaces.ModelWrapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * Created by Stanislav Kaleta on 3/7/15.
 */
public class GeneratorGUI extends AbstractComponentGUI {
    private ModelWrapper generatorModelWrapper;

    private JLabel generatorAttr;
    private JLabel generatorRunAttr;

    private int minimumWidth = 0;

    public GeneratorGUI(ModelWrapper generatorModelWrapper, ActionMap baseActionMap){
        super(baseActionMap);
        this.generatorModelWrapper = generatorModelWrapper;
        initComponents();
        updateColors();
    }

    private void initComponents(){
        generatorAttr = new JLabel("-");
        generatorAttr.setFont(new Font(generatorAttr.getFont().getName(), 0, 15));
        generatorRunAttr = new JLabel("-");
        generatorRunAttr.setFont(new Font(generatorRunAttr.getFont().getName(), 0, 15));

        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        this.add(generatorAttr);
        this.add(generatorRunAttr);

        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, generatorAttr,
                0,
                SpringLayout.HORIZONTAL_CENTER, this);
        layout.putConstraint(SpringLayout.NORTH, generatorAttr,
                10,
                SpringLayout.NORTH, this);

        layout.putConstraint(SpringLayout.WEST, generatorRunAttr,
                15,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, generatorRunAttr,8,SpringLayout.SOUTH, generatorAttr);

        getActionMap().put(ActionType.ADDP, new AddPropertyAction((CanAddProperty) generatorModelWrapper, Messages.BUNDLE.getString("ADD")+" Property"));
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.SHIFT_MASK), ActionType.ADDP);

        getActionMap().put(ActionType.EDIT,new EditAction(generatorModelWrapper,Messages.BUNDLE.getString("EDIT")+" Generator"));
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.SHIFT_MASK), ActionType.EDIT);
    }

    @Override
    void performImport(String transferredData) {
        // not supported
    }

    @Override
    public Object openEditorDialogAndGetResult() {
        GeneratorEditor editor = new GeneratorEditor();
        editor.setGenerator((Scenario.Generator) generatorModelWrapper.retrieveModel());
        ScenarioDialogEditor dialog = new ScenarioDialogEditor(editor);
        dialog.show();
        if (dialog.getExitCode() == 0) {
            return editor.getGenerator();
        }
        return null;
    }

    @Override
    public void updateGUI() {
        Scenario.Generator generator = (Scenario.Generator) generatorModelWrapper.retrieveModel();
        generatorAttr.setText(generator.getClazz()+" ("+generator.getThreads()+")");
        generatorRunAttr.setText(generator.getRun().getType()+" : "+generator.getRun().getValue());

        FontMetrics fontMetrics = generatorAttr.getFontMetrics(generatorAttr.getFont());
        int gAttrWidth = fontMetrics.stringWidth(generatorAttr.getText()) + 30;
        int gRunAttrWidth = fontMetrics.stringWidth(generatorRunAttr.getText()) + 30;
        minimumWidth = (gAttrWidth > gRunAttrWidth) ? gAttrWidth : gRunAttrWidth;
    }

    @Override
    public void updateColors() {
        setBackground(ColorComponents.getColor(ColorType.GENERATOR_BACKGROUND));
        Color foregroundColor = ColorComponents.getColor(ColorType.GENERATOR_FOREGROUND);
        setForeground(foregroundColor);
        generatorAttr.setForeground(foregroundColor);
        generatorRunAttr.setForeground(foregroundColor);
    }

    @Override
    public Dimension getMinimumSize(){
        return new Dimension(minimumWidth,70);
    }

    @Override
    public Dimension getPreferredSize(){
        return new Dimension(super.getPreferredSize().width,70);
    }

    @Override
    public Dimension getMaximumSize(){
        return new Dimension(super.getMaximumSize().width,70);
    }
}
