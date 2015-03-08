package org.perfcake.pc4idea.editor.gui;

import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.ComponentsPanel;
import org.perfcake.pc4idea.editor.colors.ColorComponents;
import org.perfcake.pc4idea.editor.colors.ColorType;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 3.3.2015
 */
public class SenderGUI extends AbstractComponentGUI {
    private Scenario.Sender sender;

    private JLabel labelSenderClass;
    private ComponentsPanel<PropertyGUI> panelProperties;

    private int labelSenderClassWidth = 0;

    public SenderGUI(ActionMap actionMap){
        super(actionMap);
        initComponents();
        updateColors();
    }

    private void initComponents(){
        labelSenderClass = new JLabel("---");
        labelSenderClass.setFont(new Font(labelSenderClass.getFont().getName(), 0, 15));

        panelProperties = new ComponentsPanel<>(this);

        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        this.add(labelSenderClass);
        this.add(panelProperties);

        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, labelSenderClass,
                0,
                SpringLayout.HORIZONTAL_CENTER, this);
        layout.putConstraint(SpringLayout.NORTH, labelSenderClass,
                10,
                SpringLayout.NORTH, this);

        layout.putConstraint(SpringLayout.WEST, panelProperties,
                10,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, panelProperties,8,SpringLayout.SOUTH, labelSenderClass);


        /*TODO componentAdapter componentResized????*/
    }



    @Override
    List<JMenuItem> getMenuItems() {
        /*TODO*/
        return null;
    }

    @Override
    void performImport(String transferredData) {
        /*TODO*/
    }

    @Override
    void openEditor() {
        /*TODO*/
    }

    @Override
    public void setComponentModel(Object componentModel) {
        sender = (Scenario.Sender) componentModel;
        labelSenderClass.setText(sender.getClazz());
        FontMetrics fontMetrics = labelSenderClass.getFontMetrics(labelSenderClass.getFont());
        labelSenderClassWidth = fontMetrics.stringWidth(labelSenderClass.getText());

        List<PropertyGUI> propertyGUIList = new ArrayList<>();
        for (Property p : sender.getProperty()){
            PropertyGUI propertyGUI = new PropertyGUI(this.getActionMap());
            propertyGUI.setComponentModel(p);
            propertyGUIList.add(propertyGUI);
        }
        panelProperties.setComponentList(propertyGUIList);
    }

    @Override
    public Object getComponentModel() {
        return sender;
    }

    @Override
    public void updateColors() {
        setBackground(ColorComponents.getColor(ColorType.SENDER_BACKGROUND));
        Color color = ColorComponents.getColor(ColorType.SENDER_FOREGROUND);
        setForeground(color);
        labelSenderClass.setForeground(color);
    }
}
