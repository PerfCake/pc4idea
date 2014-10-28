package org.perfcake.pc4idea.editor.gui;

import com.intellij.openapi.project.Project;
import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.wizard.SenderEditor;

import javax.accessibility.AccessibleContext;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 28.9.2014
 */
public class SenderPanel extends AbstractPanel {
    private final String TITLE ="Sender Editor";
    private Color senderColor = Color.getHSBColor(220/360f,0.5f,0.75f);
    private final Project project;

    private SenderEditor senderEditor;
    private Scenario.Sender sender;

    private JLabel labelSenderClass;
    private JPanel panelProperties;

    private int propertiesRowCount;
    private int widestPropertyWidth;
    private int labelSenderClassWidth;

    public SenderPanel(Project project){
        super(project);
        this.project = project;
        propertiesRowCount = 0;
        widestPropertyWidth = 0;
        labelSenderClassWidth = 0;

        initComponents();
    }

    private void initComponents() {
        labelSenderClass = new JLabel("---");
        labelSenderClass.setFont(new Font(labelSenderClass.getFont().getName(), 0, 15));
        labelSenderClass.setForeground(senderColor);
        panelProperties = new JPanel();
        panelProperties.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
        /*panelProperties.setBackground(Color.cyan);*/panelProperties.setOpaque(false);

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

        this.addComponentListener( new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (widestPropertyWidth <  e.getComponent().getSize().width - 20) {
                    panelProperties.setMinimumSize(new Dimension(e.getComponent().getSize().width - 20, panelProperties.getMinimumSize().height));
                    panelProperties.setPreferredSize(new Dimension(e.getComponent().getSize().width - 20, panelProperties.getPreferredSize().height));
                    panelProperties.setMaximumSize(new Dimension(e.getComponent().getSize().width - 20, panelProperties.getMaximumSize().height));
                    panelProperties.revalidate();
                }  else {
                    panelProperties.setMinimumSize(new Dimension(widestPropertyWidth, panelProperties.getMinimumSize().height));
                    panelProperties.setPreferredSize(new Dimension(widestPropertyWidth, panelProperties.getPreferredSize().height));
                    panelProperties.setMaximumSize(new Dimension(widestPropertyWidth, panelProperties.getMaximumSize().height));
                    panelProperties.revalidate();
                }
            }
        });
        panelProperties.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (widestPropertyWidth < e.getComponent().getSize().width) {
                    AccessibleContext aProperties = e.getComponent().getAccessibleContext();
                    int controlSum = 0;
                    int expectedRows = 0;
                    for (int i = 0; i < aProperties.getAccessibleChildrenCount(); i++) {
                        if (i == 0) {
                            expectedRows = 1;
                        }
                        controlSum += ((PropertyComponent) aProperties.getAccessibleChild(i)).getPreferredSize().width;
                        if (controlSum > panelProperties.getPreferredSize().width) {
                            i--;
                            controlSum = 0;
                            expectedRows++;
                        }
                    }
                    if (expectedRows != propertiesRowCount) {
                        propertiesRowCount = expectedRows;
                        panelProperties.setMinimumSize(new Dimension(panelProperties.getMinimumSize().width, propertiesRowCount * 40));
                        panelProperties.setPreferredSize(new Dimension(panelProperties.getPreferredSize().width, propertiesRowCount * 40));
                        panelProperties.setMaximumSize(new Dimension(panelProperties.getMaximumSize().width, propertiesRowCount * 40));
                        panelProperties.revalidate();
                    }
                }
            }
        });
    }

    @Override
    protected Color getColor() {
        return senderColor;
    }

    @Override
    protected String getEditorTitle() {
        return TITLE;
    }

    @Override
    protected JPanel getEditorPanel() {
        senderEditor = new SenderEditor();
        senderEditor.setSender(sender);
        return senderEditor;
    }

    @Override
    protected void applyChanges() {
        this.setComponent(senderEditor.getSender());
    }

    @Override
    public void setComponent(Object component) {
        sender = (Scenario.Sender) component;
        labelSenderClass.setText(sender.getClazz());
        FontMetrics fontMetrics = labelSenderClass.getFontMetrics(labelSenderClass.getFont());
        labelSenderClassWidth = fontMetrics.stringWidth(labelSenderClass.getText());

        panelProperties.removeAll();
        panelProperties.repaint();

        widestPropertyWidth = 0;
        for (Property property : sender.getProperty()){
            PropertyComponent propertyComponent = new PropertyComponent(project, senderColor);
            propertyComponent.setComponent(property);
            panelProperties.add(propertyComponent);
            if (propertyComponent.getPreferredSize().width > widestPropertyWidth){
                widestPropertyWidth = propertyComponent.getPreferredSize().width;
            }
        }

        panelProperties.getComponentListeners()[0].componentResized(new ComponentEvent(panelProperties,0));
        this.revalidate();
    }

    @Override
    public Object getComponent() {
        return sender;
    }

    @Override
    public Dimension getMinimumSize(){
        Dimension dimension = new Dimension();
        dimension.width = (widestPropertyWidth+20 > labelSenderClassWidth+30) ? widestPropertyWidth+20 : labelSenderClassWidth+30;
        dimension.height = propertiesRowCount*40 + 50;
        return dimension;
    }

    @Override
    public Dimension getPreferredSize(){
        Dimension dimension = new Dimension();
        dimension.width = super.getPreferredSize().width;
        dimension.height = propertiesRowCount*40 + 50;
        return dimension;
    }

    @Override
    public Dimension getMaximumSize(){
        Dimension dimension = new Dimension();
        dimension.width = super.getMaximumSize().width;
        dimension.height = propertiesRowCount*40 + 50;
        return dimension;
    }

}
