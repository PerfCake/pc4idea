package org.perfcake.pc4idea.editor.components;

import com.intellij.openapi.project.Project;
import org.perfcake.model.Scenario;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 28.9.2014
 * To change this template use File | Settings | File Templates.
 */
public class GeneratorComponent extends AbstractComponent {
    private final String TITLE ="Generator Editor";
    private Color panelColor = Color.getHSBColor(120/360f,0.5f,0.75f);/* TODO default but able to change in settings*/

    private JPanel panelEditor;
    private Scenario.Generator generator;

    private JLabel generatorAttr;
    private JLabel generatorRunAttr;




    public GeneratorComponent(Project project){
        super(project);
        setMinimumSize(new Dimension(0,70));
        setMaximumSize(new Dimension(Integer.MAX_VALUE,70));


        initComponents();
        panelEditor = initPanelEditor();



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

    private JPanel initPanelEditor(){
        JPanel panel = new JPanel();
        /*TODO same as wizard gen.e.*/
//        panel.add(new JLabel("aaaaa"),0);
//        panel.add(new JLabel("bbbbb"),1);

        return panel;
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
    protected JPanel getPanelEditor() {
        return panelEditor;
    }

    @Override
    protected void applyChanges() {
        /*TODO ...*/
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
