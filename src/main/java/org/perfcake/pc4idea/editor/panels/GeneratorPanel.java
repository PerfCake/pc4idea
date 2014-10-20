package org.perfcake.pc4idea.editor.panels;

import com.intellij.openapi.project.Project;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.wizard.GeneratorEditor;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 28.9.2014
 * To change this template use File | Settings | File Templates.
 */
public class GeneratorPanel extends AbstractPanel {
    private final String TITLE ="Generator Editor";
    private Color panelColor = Color.getHSBColor(120/360f,0.5f,0.75f);/* TODO default but able to change in settings*/

    private GeneratorEditor panelEditor;
    private Scenario.Generator generator;

    private JLabel generatorAttr;
    private JLabel generatorRunAttr;


    public GeneratorPanel(Project project){
        super(project);
        setMinimumSize(new Dimension(0,70));
        setMaximumSize(new Dimension(Integer.MAX_VALUE,70));


        initComponents();


    }
    private void initComponents(){
        generatorAttr = new JLabel("{g.class} ({g.threads})");
        generatorAttr.setFont(new Font(generatorAttr.getFont().getName(), 0, 15));
        generatorAttr.setForeground(panelColor);
        generatorRunAttr = new JLabel("{g.run.type} : {g.run.value}");
        generatorRunAttr.setFont(new Font(generatorRunAttr.getFont().getName(), 0, 15));
        generatorRunAttr.setForeground(panelColor);

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
        return panelColor;
    }

    @Override
    protected String getEditorTitle() {
        return TITLE;
    }

    @Override
    protected JPanel getEditorPanel() {
        panelEditor = new GeneratorEditor();
        panelEditor.setGenerator(generator);
        return panelEditor;
    }

    @Override
    protected void applyChanges() {
        this.setComponent(panelEditor.getGenerator());
    }
    @Override
    public void setComponent(Object component){
        generator = (Scenario.Generator) component;
        generatorAttr.setText(generator.getClazz()+" ("+generator.getThreads()+")");
        generatorRunAttr.setText(generator.getRun().getType()+" : "+generator.getRun().getValue());
    }
    @Override
    public Object getComponent(){
        return generator;
    }



}
