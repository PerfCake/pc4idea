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

    private SenderEditor panelEditor;
    private Scenario.Sender sender;

    private JLabel senderAttr;
    private JPanel panelProperties;

    private int propertiesRowCount;
    private int widestPropertyWidth;

    public SenderPanel(Project project){
        super(project);
        this.project = project;
        propertiesRowCount = 0;
        widestPropertyWidth = 0;

        initComponents();
    }

    private void initComponents() {
        senderAttr = new JLabel("SenderClass");
        senderAttr.setFont(new Font(senderAttr.getFont().getName(), 0, 15));
        senderAttr.setForeground(senderColor);
        panelProperties = new JPanel();
        panelProperties.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
        panelProperties.setOpaque(false);

        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        this.add(senderAttr);
        this.add(panelProperties);

        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, senderAttr,
                0,
                SpringLayout.HORIZONTAL_CENTER, this);
        layout.putConstraint(SpringLayout.NORTH, senderAttr,
                10,
                SpringLayout.NORTH, this);

        layout.putConstraint(SpringLayout.WEST, panelProperties,
                10,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, panelProperties,8,SpringLayout.SOUTH, senderAttr);

        this.addComponentListener( new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (widestPropertyWidth <  e.getComponent().getSize().width - 20) {
                    panelProperties.setMinimumSize(new Dimension(e.getComponent().getSize().width - 20, panelProperties.getMinimumSize().height));
                    panelProperties.setPreferredSize(new Dimension(e.getComponent().getSize().width - 20, panelProperties.getPreferredSize().height));
                    panelProperties.setMaximumSize(new Dimension(e.getComponent().getSize().width - 20, panelProperties.getMaximumSize().height));
                    panelProperties.revalidate();
                }
            }
        });
        panelProperties.addComponentListener( new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (propertiesRowCount > 0) {
                    if (widestPropertyWidth <  panelProperties.getSize().width) {
                        AccessibleContext ac = e.getComponent().getAccessibleContext();
                        int controlSum = 0;
                        int expectedRows = 1;
                        for (int i = 0; i < ac.getAccessibleChildrenCount(); i++) {
                            controlSum += ((PropertyComponent) ac.getAccessibleChild(i)).getSize().width;
                            if (controlSum > SenderPanel.this.getSize().width - 20) {
                                if (ac.getAccessibleChildrenCount() > expectedRows) {
                                    i--;
                                    controlSum = 0;
                                    expectedRows++;
                                }
                            }
                        }
                        if (expectedRows != propertiesRowCount) {
                            propertiesRowCount = expectedRows;
                            panelProperties.setMinimumSize(new Dimension(panelProperties.getMinimumSize().width, panelProperties.getMinimumSize().height + 40));
                            panelProperties.setPreferredSize(new Dimension(panelProperties.getPreferredSize().width, panelProperties.getPreferredSize().height + 40));
                            panelProperties.setMaximumSize(new Dimension(panelProperties.getMaximumSize().width, panelProperties.getMaximumSize().height + 40));
                            panelProperties.revalidate();
                        }
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
        panelEditor = new SenderEditor();
        panelEditor.setSender(sender);
        return panelEditor;
    }

    @Override
    protected void applyChanges() {
        this.setComponent(panelEditor.getSender());
    }

    @Override
    public void setComponent(Object component) {
        sender = (Scenario.Sender) component;
        senderAttr.setText(sender.getClazz());

        panelProperties.removeAll();
        panelProperties.repaint();

        widestPropertyWidth = 0;
        propertiesRowCount = 0;
        for (Property property : sender.getProperty()){
            PropertyComponent propertyComponent = new PropertyComponent(project, senderColor);
            propertyComponent.setComponent(property);
            panelProperties.add(propertyComponent);
            if (propertyComponent.getPreferredSize().width > widestPropertyWidth){
                widestPropertyWidth = propertyComponent.getPreferredSize().width;
            }
        }
        if (sender.getProperty().size() > 0) {
            propertiesRowCount = 1;
        } else {
            propertiesRowCount = 0;
        }
        panelProperties.revalidate();
        this.revalidate();
    }

    @Override
    public Object getComponent() {
        return sender;
    }

    @Override
    public Dimension getMinimumSize(){
        Dimension dimension = new Dimension();
        dimension.width = widestPropertyWidth+20;
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
