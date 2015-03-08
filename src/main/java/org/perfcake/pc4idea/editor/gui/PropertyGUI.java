package org.perfcake.pc4idea.editor.gui;

import org.perfcake.model.Property;
import org.perfcake.pc4idea.editor.colors.ColorComponents;
import org.perfcake.pc4idea.editor.colors.ColorType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Created by Stanislav Kaleta on 3/7/15.
 */
public class PropertyGUI extends AbstractComponentGUI {
    private Property property;

    private JLabel propertyAttr;

    private Dimension propertySize = new Dimension(40, 40);

    public PropertyGUI(ActionMap actionMap){
        super(actionMap);
        initComponents();
        updateColors();
    }

    private void initComponents() {
        propertyAttr = new JLabel("-");
        propertyAttr.setFont(new Font(propertyAttr.getFont().getName(), 0, 15));

        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        this.add(propertyAttr);

        layout.putConstraint(SpringLayout.NORTH, propertyAttr,
                10,
                SpringLayout.NORTH, this);

        layout.putConstraint(SpringLayout.WEST, propertyAttr,
                15,
                SpringLayout.WEST, this);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ((JPanel)e.getComponent().getAccessibleContext().getAccessibleParent()).dispatchEvent(e);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                ((JPanel)e.getComponent().getAccessibleContext().getAccessibleParent()).dispatchEvent(e);
            }
            @Override
            public void mouseReleased(MouseEvent e){
                ((JPanel)e.getComponent().getAccessibleContext().getAccessibleParent()).dispatchEvent(e);
            }
        });
    }

    @Override
    List<JMenuItem> getMenuItems() {
        return null;
    }

    @Override
    void performImport(String transferredData) {
        // not used
    }

    @Override
    void openEditor() {
        /*TODO*/
    }

    @Override
    public void setComponentModel(Object componentModel) {
        property = (Property) componentModel;
        propertyAttr.setText(property.getName()+" : "+property.getValue());
        FontMetrics fontMetrics = propertyAttr.getFontMetrics(propertyAttr.getFont());
        propertySize.width = fontMetrics.stringWidth(propertyAttr.getText()) + 30;
    }

    @Override
    public Object getComponentModel() {
        return property;
    }

    @Override
    public void updateColors() {
        setBackground(ColorComponents.getColor(ColorType.PROPERTIES_BACKGROUND));
        Color color = ColorComponents.getColor(ColorType.PROPERTIES_FOREGROUND);
        setForeground(color);
        propertyAttr.setForeground(color);
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
