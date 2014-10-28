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
    private final Color propertyColor;

    private PropertyEditor propertyEditor;
    private Property property;

    private JLabel propertyAttr;
    private Dimension propertySize;

    public PropertyComponent(Project project, Color ancestorColor){
        super(project);
        this.propertyColor = ancestorColor;

        initComponents();
    }

    private void initComponents(){
        propertyAttr = new JLabel("-");
        propertyAttr.setFont(new Font(propertyAttr.getFont().getName(), 0, 15));
        propertyAttr.setForeground(propertyColor);
        propertySize = new Dimension(40,40);

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

    @Override
    protected Color getColor() {
        return propertyColor;
    }

    @Override
    protected String getEditorTitle() {
        return TITLE;
    }

    @Override
    protected JPanel getEditorPanel() {
        propertyEditor = new PropertyEditor();
        propertyEditor.setProperty(property);
        return propertyEditor;
    }

    @Override
    protected void applyChanges(){
        this.setComponent(propertyEditor.getProperty());
    }      /*TODO po edit. sender prop. neuklada do sendra!!!*/

    @Override
    public void setComponent(Object component) {
        property = (Property) component;
        propertyAttr.setText(property.getName()+" : "+property.getValue());
        FontMetrics fontMetrics = propertyAttr.getFontMetrics(propertyAttr.getFont());
        propertySize.width = fontMetrics.stringWidth(propertyAttr.getText()) + 30;
    }

    @Override
    public Object getComponent() {
        return property;
    }

    @Override
    public Dimension getMinimumSize(){
        return propertySize;
    }

    @Override
    public Dimension getPreferredSize(){
        return propertySize;
    }

    @Override
    public Dimension getMaximumSize(){
        return propertySize;
    }
}
