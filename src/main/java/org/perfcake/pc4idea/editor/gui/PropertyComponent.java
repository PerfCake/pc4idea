package org.perfcake.pc4idea.editor.gui;

import com.intellij.openapi.project.Project;
import org.perfcake.model.Property;
import org.perfcake.pc4idea.editor.wizard.PropertyEditor;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 12.10.2014
 */
public class PropertyComponent extends AbstractPanel {
    private final String TITLE ="Property Editor";
    private final Color componentColor;

    private PropertyEditor panelEditor;
    private Property property;

    private JLabel propertyAttr;

    public PropertyComponent(Project project, Color componentColor){
        super(project);
        this.componentColor = componentColor;

        initComponents();
    }

    private void initComponents(){
        propertyAttr = new JLabel("name : value");
        propertyAttr.setFont(new Font(propertyAttr.getFont().getName(), 0, 15));
        propertyAttr.setForeground(componentColor);

        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        this.add(propertyAttr);

        layout.putConstraint(SpringLayout.NORTH, propertyAttr,
                10,
                SpringLayout.NORTH, this);

        layout.putConstraint(SpringLayout.WEST, propertyAttr,
                15,
                SpringLayout.WEST, this);
    }
    private void setComponentSize() {
        FontMetrics fontMetrics = propertyAttr.getFontMetrics(propertyAttr.getFont());
        int width = fontMetrics.stringWidth(propertyAttr.getText()) + 30;

        this.setMinimumSize(new Dimension(width, 40));
        this.setPreferredSize(new Dimension(width, 40));
        this.setMaximumSize(new Dimension(width, 40));
    }

    @Override
    protected Color getColor() {
        return componentColor;
    }

    @Override
    protected String getEditorTitle() {
        return TITLE;
    }

    @Override
    protected JPanel getEditorPanel() {
        panelEditor = new PropertyEditor();
        panelEditor.setProperty(property);
        return panelEditor;
    }

    @Override
    protected void applyChanges(){
        this.setComponent(panelEditor.getProperty());
    }

    @Override
    public void setComponent(Object component) {
        property = (Property) component;
        propertyAttr.setText(property.getName()+" : "+property.getValue());

        setComponentSize();
    }

    @Override
    public Object getComponent() {
        /*TODO maybe not needed*/
        return property;
    }
}
