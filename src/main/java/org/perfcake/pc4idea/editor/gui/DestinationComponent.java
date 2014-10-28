package org.perfcake.pc4idea.editor.gui;

import com.intellij.openapi.project.Project;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.components.EnabledIndicator;
import org.perfcake.pc4idea.editor.wizard.DestinationEditor;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 15.10.2014
 */
public class DestinationComponent extends AbstractPanel {
    private final String TITLE ="Destination Editor";
    private final Color destinationColor;

    private DestinationEditor destinationEditor;
    private Scenario.Reporting.Reporter.Destination destination;

    private JLabel destinationAttr;
    private EnabledIndicator destinationEnabled;

    public DestinationComponent(Project project, Color reporterColor){
        super(project);
        this.destinationColor = reporterColor;

        initComponents();
    }

    private void initComponents(){
        destinationAttr = new JLabel("DestinationClass");
        destinationAttr.setFont(new Font(destinationAttr.getFont().getName(), 0, 15));
        destinationAttr.setForeground(destinationColor);
        destinationEnabled = new EnabledIndicator(destinationColor);

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
        FontMetrics fontMetrics = destinationAttr.getFontMetrics(destinationAttr.getFont());
        int width = fontMetrics.stringWidth(destinationAttr.getText()) + 25 + 20;

        this.setMinimumSize(new Dimension(width,40));
        this.setPreferredSize(new Dimension(width,40));
        this.setMaximumSize(new Dimension(width,40));
    }

    @Override
    protected Color getColor() {
        return destinationColor;
    }

    @Override
    protected String getEditorTitle() {
        return TITLE;
    }

    @Override
    protected JPanel getEditorPanel() {
        destinationEditor = new DestinationEditor();
        destinationEditor.setDestination(destination);
        return destinationEditor;
    }

    @Override
    protected void applyChanges(){
        this.setComponent(destinationEditor.getDestination());
    }

    @Override
    public void setComponent(Object component) {
        destination = (Scenario.Reporting.Reporter.Destination)component;
        destinationAttr.setText(destination.getClazz());
        destinationEnabled.setState(destination.isEnabled());

        setComponentSize();
    }

    @Override
    public Object getComponent() {
        /*TODO maybe not needed*/
        return destination;
    }
}
