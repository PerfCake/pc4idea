package org.perfcake.pc4idea.editor.gui;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.ScenarioDialogEditor;
import org.perfcake.pc4idea.editor.colors.ColorComponents;
import org.perfcake.pc4idea.editor.colors.ColorType;
import org.perfcake.pc4idea.editor.editors.GeneratorEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Created by Stanislav Kaleta on 3/7/15.
 */
public class GeneratorGUI extends AbstractComponentGUI {
    private Scenario.Generator generator;

    private JLabel generatorAttr;
    private JLabel generatorRunAttr;

    private int minimumWidth = 0;

    public GeneratorGUI(ActionMap actionMap){
        super(actionMap);
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
    }

    @Override
    List<JMenuItem> getMenuItems() {
        /*TODO*/
        return null;
    }

    @Override
    void performImport(String transferredData) {
        // not supported
    }

    @Override
    void openEditor() {
        GeneratorEditor editor = new GeneratorEditor();
        editor.setGenerator(generator);
        ScenarioDialogEditor dialog = new ScenarioDialogEditor(editor);
        dialog.show();
        if (dialog.getExitCode() == 0) {
            this.setComponentModel(editor.getGenerator());
            this.getActionMap().get("COMMIT").actionPerformed(new ActionEvent(this,1,"Edit Generator"));
        }
    }

    @Override
    public void setComponentModel(Object componentModel) {
        generator = (Scenario.Generator) componentModel;
        generatorAttr.setText(generator.getClazz()+" ("+generator.getThreads()+")");
        generatorRunAttr.setText(generator.getRun().getType()+" : "+generator.getRun().getValue());

        FontMetrics fontMetrics = generatorAttr.getFontMetrics(generatorAttr.getFont());
        int gAttrWidth = fontMetrics.stringWidth(generatorAttr.getText()) + 30;
        int gRunAttrWidth = fontMetrics.stringWidth(generatorRunAttr.getText()) + 30;
        minimumWidth = (gAttrWidth > gRunAttrWidth) ? gAttrWidth : gRunAttrWidth;
    }

    @Override
    public Object getComponentModel() {
        return generator;
    }

    @Override
    public void updateColors() {
        setBackground(ColorComponents.getColor(ColorType.GENERATOR_BACKGROUND));
        Color color = ColorComponents.getColor(ColorType.GENERATOR_FOREGROUND);
        setForeground(color);
        generatorAttr.setForeground(color);
        generatorRunAttr.setForeground(color);
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
