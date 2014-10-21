package org.perfcake.pc4idea.editor.gui;

import com.intellij.openapi.project.Project;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.components.EnabledIndicator;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 15.10.2014
 */
public class DestinationComponent extends AbstractPanel {
    private final String TITLE ="Destination Editor";
    private final int HEIGHT = 40;
    private final Color componentColor;
    private final Project project;

    private JPanel panelEditor;
    private Scenario.Reporting.Reporter.Destination destination;

    private int width;
    private JLabel destinationAttr;
    private EnabledIndicator destinationEnabled;

    public DestinationComponent(Project project, Color componentColor){
        super(project);
        this.project = project;
        this.componentColor = componentColor;

        init();

        panelEditor = initPanelEditor();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(destinationAttr.getFont());
        width = g.getFontMetrics().stringWidth(destinationAttr.getText()) + 25 + 20;
        setComponentSize();

        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(componentColor);
        g2D.drawRoundRect(4, 4, width - 8, HEIGHT - 8, 20, 20);
    }

    private void init(){
        destinationAttr = new JLabel("DestinationClass");
        destinationAttr.setFont(new Font(destinationAttr.getFont().getName(), 0, 15));
        destinationAttr.setForeground(componentColor);
        destinationEnabled = new EnabledIndicator(componentColor);

        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        this.add(destinationAttr);
        this.add(destinationEnabled);

        layout.putConstraint(SpringLayout.NORTH, destinationEnabled,
                10,
                SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.WEST, destinationEnabled,
                10,
                SpringLayout.WEST, this);

        layout.putConstraint(SpringLayout.NORTH, destinationAttr,
                10,
                SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.WEST,destinationAttr,
                5,
                SpringLayout.EAST,destinationEnabled);


    }
    private void setComponentSize(){
        this.setMinimumSize(new Dimension(width,HEIGHT));
        this.setPreferredSize(new Dimension(width,HEIGHT));
        this.setMaximumSize(new Dimension(width,HEIGHT));
    }

    private JPanel initPanelEditor() {
        JPanel panel = new JPanel();
            /*TODO*/
        return panel;
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
        return panelEditor;
    }

    @Override
    protected void applyChanges(){
        /*TODO*/
    }

    @Override
    public void setComponent(Object component) {
        destination = (Scenario.Reporting.Reporter.Destination)component;
        destinationAttr.setText(destination.getClazz());
        destinationEnabled.setState(destination.isEnabled());
    }

    @Override
    public Object getComponent() {
        return null/*TODO*/;
    }

}
