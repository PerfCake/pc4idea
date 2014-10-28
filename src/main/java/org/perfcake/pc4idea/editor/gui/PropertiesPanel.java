package org.perfcake.pc4idea.editor.gui;

import com.intellij.openapi.project.Project;
import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.wizard.PropertiesEditor;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 28.9.2014
 */
public class PropertiesPanel extends AbstractPanel {
    private final String TITLE ="Properties Editor";
    private Color propertiesColor = Color.getHSBColor(0/360f,0.2f,0.5f);
    private final Project project;

    private PropertiesEditor propertiesEditor;
    private Scenario.Properties properties;

    private JLabel labelProperties;
    private JPanel panelProperties;

    public PropertiesPanel(Project project){
        super(project);
        this.project = project;

        initComponents();
    }

    private void initComponents(){
        labelProperties = new JLabel("Scenario Properties");
        labelProperties.setFont(new Font(labelProperties.getFont().getName(),0,15));
        labelProperties.setForeground(propertiesColor);
//        FontMetrics fontMetrics = labelProperties.getFontMetrics(labelProperties.getFont());
//        labelPropertiesWidth = fontMetrics.stringWidth(labelProperties.getText());

        panelProperties = new JPanel();
        panelProperties.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
        /*panelProperties.setBackground(Color.cyan);*/panelProperties.setOpaque(false);

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

    }

    @Override
    protected Color getColor() {
        return propertiesColor;
    }

    @Override
    protected String getEditorTitle() {
        return TITLE;
    }

    @Override
    protected JPanel getEditorPanel() {
        propertiesEditor = new PropertiesEditor();
        propertiesEditor.setObjProperties(properties);
        return propertiesEditor;
    }

    @Override
    protected void applyChanges() {
        this.setComponent(propertiesEditor.getObjProperties());
    }

    @Override
    public void setComponent(Object component) {
        properties = (Scenario.Properties) component;

        panelProperties.removeAll();
        panelProperties.repaint();

        for (Property property : properties.getProperty()){
            PropertyComponent propertyComponent = new PropertyComponent(project,propertiesColor);
            propertyComponent.setComponent(property);
            panelProperties.add(propertyComponent);
        }

        panelProperties.revalidate();
        this.revalidate();

        this.setPreferredSize(new Dimension(super.getPreferredSize().width,90));
    }

    @Override
    public Object getComponent() {
        return properties;
    }


}
