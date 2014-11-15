package org.perfcake.pc4idea.editor.designer.outercomponents;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.PerfCakeEditorGUI;
import org.perfcake.pc4idea.editor.designer.editors.AbstractEditor;
import org.perfcake.pc4idea.editor.designer.editors.GeneratorEditor;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 28.9.2014
 */
public class GeneratorPanel extends AbstractPanel {
    private Color generatorColor = Color.getHSBColor(120/360f,0.75f,0.75f);/* TODO default but able to change in settings*/

    private GeneratorEditor panelEditor;
    private Scenario.Generator generator;
    private PerfCakeEditorGUI.ScenarioEvent scenarioEvent;

    private JLabel generatorAttr;
    private JLabel generatorRunAttr;
    private int minimumWidth = 0;

    public GeneratorPanel(PerfCakeEditorGUI.ScenarioEvent scenarioEvent){
        this.scenarioEvent = scenarioEvent;

        initComponents();
    }

    private void initComponents(){
        generatorAttr = new JLabel("-");
        generatorAttr.setFont(new Font(generatorAttr.getFont().getName(), 0, 15));
        generatorAttr.setForeground(generatorColor);
        generatorRunAttr = new JLabel("-");
        generatorRunAttr.setFont(new Font(generatorRunAttr.getFont().getName(), 0, 15));
        generatorRunAttr.setForeground(generatorColor);

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
    protected Color getColor() {
        return generatorColor;
    }

    @Override
    protected AbstractEditor getEditorPanel() {
        panelEditor = new GeneratorEditor();
        panelEditor.setGenerator(generator);
        return panelEditor;
    }

    @Override
    protected void applyChanges() {
        this.setComponentModel(panelEditor.getGenerator());
        scenarioEvent.saveGenerator();
    }

    @Override
    public void setComponentModel(Object componentModel){
        generator = (Scenario.Generator) componentModel;
        generatorAttr.setText(generator.getClazz()+" ("+generator.getThreads()+")");
        generatorRunAttr.setText(generator.getRun().getType()+" : "+generator.getRun().getValue());

        FontMetrics fontMetrics = generatorAttr.getFontMetrics(generatorAttr.getFont());
        int gAttrWidth = fontMetrics.stringWidth(generatorAttr.getText()) + 30;
        int gRunAttrWidth = fontMetrics.stringWidth(generatorRunAttr.getText()) + 30;
        minimumWidth = (gAttrWidth > gRunAttrWidth) ? gAttrWidth : gRunAttrWidth;
    }

    @Override
    public Object getComponentModel(){
        return generator;
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
