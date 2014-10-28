package org.perfcake.pc4idea.editor.gui;

import com.intellij.openapi.project.Project;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.components.EnabledIndicator;
import org.perfcake.pc4idea.editor.wizard.ReporterEditor;

import javax.accessibility.AccessibleContext;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 15.10.2014
 * To change this template use File | Settings | File Templates.
 */
public class ReporterComponent extends AbstractPanel {
    private final String TITLE ="Reporter Editor";
    private final Color reporterColor;
    private final Project project;

    private ReporterEditor reporterEditor;
    private Scenario.Reporting.Reporter reporter;

    private JLabel labelReporterClass;
    private EnabledIndicator reporterEnabled;
    private JPanel panelDestinations;

    private int destinationRowCount;
    private int minimumWidth;
    private int widestRowWidth;
    private int maximumWidth;
    private int requiredMaximumWidth;
    private int requiredMinimumHeight;


    public ReporterComponent(Project project, Color reportingColor){
        super(project);
        this.project = project;
        this.reporterColor = reportingColor;
        destinationRowCount = 0;
        minimumWidth = 0;
        widestRowWidth = 0;
        maximumWidth = 0;
        requiredMaximumWidth = 0;
        requiredMinimumHeight = 0;

        initComponents();
    }

    private void initComponents(){
        labelReporterClass = new JLabel("---");
        labelReporterClass.setFont(new Font(labelReporterClass.getFont().getName(), 0, 15));
        labelReporterClass.setForeground(reporterColor);
        reporterEnabled = new EnabledIndicator(reporterColor);

        panelDestinations = new JPanel();
        panelDestinations.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
        /*panelDestinations.setBackground(Color.ORANGE);*/panelDestinations.setOpaque(false);

        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        this.add(labelReporterClass);
        this.add(reporterEnabled);
        this.add(panelDestinations);

        layout.putConstraint(SpringLayout.NORTH, labelReporterClass,
                10,
                SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.NORTH, reporterEnabled,
                10,
                SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.WEST,reporterEnabled,
                10,SpringLayout.WEST,this);
        layout.putConstraint(SpringLayout.WEST, labelReporterClass,
                5,
                SpringLayout.EAST,reporterEnabled);

        layout.putConstraint(SpringLayout.WEST, panelDestinations,
                10,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, panelDestinations,8,SpringLayout.SOUTH, labelReporterClass);

        this.addComponentListener( new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (minimumWidth < e.getComponent().getSize().width) {
                    panelDestinations.setMinimumSize(new Dimension(e.getComponent().getSize().width-20, panelDestinations.getMinimumSize().height));
                    panelDestinations.setPreferredSize(new Dimension(e.getComponent().getSize().width-20, panelDestinations.getPreferredSize().height));
                    panelDestinations.setMaximumSize(new Dimension(e.getComponent().getSize().width-20, panelDestinations.getMaximumSize().height));
                    panelDestinations.revalidate();
                } else {
                    panelDestinations.setMinimumSize(new Dimension(minimumWidth-20, panelDestinations.getMinimumSize().height));
                    panelDestinations.setPreferredSize(new Dimension(minimumWidth - 20, panelDestinations.getPreferredSize().height));
                    panelDestinations.setMaximumSize(new Dimension(minimumWidth-20, panelDestinations.getMaximumSize().height));
                    panelDestinations.revalidate();
                }
            }
        });
        panelDestinations.addComponentListener( new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (minimumWidth-20 <= e.getComponent().getSize().width) {
                    AccessibleContext aDestinations = e.getComponent().getAccessibleContext();
                    int controlSum = 0;
                    int expectedRows = 0;
                    widestRowWidth = 0;
                    for (int i = 0; i < aDestinations.getAccessibleChildrenCount(); i++) {
                        if (i == 0) {
                            expectedRows = 1;
                        }
                        int iDestinationWidth = ((DestinationComponent) aDestinations.getAccessibleChild(i)).getPreferredSize().width;
                        controlSum += iDestinationWidth;
                        if (controlSum > panelDestinations.getPreferredSize().width) {

                            widestRowWidth  = (widestRowWidth < controlSum-iDestinationWidth) ? controlSum-iDestinationWidth : widestRowWidth;

                            i--;
                            controlSum = 0;
                            expectedRows++;
                        }
                    }
                    if (controlSum != 0) {
                        widestRowWidth  = (widestRowWidth < controlSum) ? controlSum : widestRowWidth;
                    }
                    if (expectedRows != destinationRowCount) {
                        destinationRowCount = expectedRows;
                        panelDestinations.setMinimumSize(new Dimension(panelDestinations.getMinimumSize().width, destinationRowCount * 40));
                        panelDestinations.setPreferredSize(new Dimension(panelDestinations.getPreferredSize().width, destinationRowCount * 40));
                        panelDestinations.setMaximumSize(new Dimension(panelDestinations.getMaximumSize().width, destinationRowCount * 40));
                        panelDestinations.revalidate();
                    }
                }
            }
        });
    }

    public int getMinimumWidth(){
        return minimumWidth;
    }

    public void setRequiredMinimumHeight(int requiredHeight){
        this.requiredMinimumHeight = requiredHeight;
        this.revalidate();
    }
    public void setRequiredMaximumWidth(int requiredMaximumWidth){
        this.requiredMaximumWidth = requiredMaximumWidth;
        this.revalidate();
    }

    @Override
    protected Color getColor() {
        return reporterColor;
    }

    @Override
    protected String getEditorTitle() {
        return TITLE;
    }

    @Override
    protected JPanel getEditorPanel() {
        reporterEditor = new ReporterEditor();
        reporterEditor.setReporter(reporter);
        return reporterEditor;
    }

    @Override
    protected void applyChanges() {
        this.setComponent(reporterEditor.getReporter());
    }

    @Override
    public void setComponent(Object component) {
        reporter = (Scenario.Reporting.Reporter) component;
        labelReporterClass.setText(reporter.getClazz());
        reporterEnabled.setState(reporter.isEnabled());

        panelDestinations.removeAll();
        panelDestinations.repaint();

        widestRowWidth = 0;
        int widestDestinationWidth = 0;
        for (Scenario.Reporting.Reporter.Destination destination : reporter.getDestination()){
            AbstractPanel destinationComponent = new DestinationComponent(project, reporterColor);
            destinationComponent.setComponent(destination);
            panelDestinations.add(destinationComponent);
            widestRowWidth += destinationComponent.getPreferredSize().width;
            if (destinationComponent.getPreferredSize().width > widestDestinationWidth) {
                widestDestinationWidth = destinationComponent.getPreferredSize().width;
           }
        }
        FontMetrics fontMetrics = labelReporterClass.getFontMetrics(labelReporterClass.getFont());
        int labelReporterClassWidth = fontMetrics.stringWidth(labelReporterClass.getText());

        minimumWidth = (widestDestinationWidth+20 > labelReporterClassWidth +25+20) ? widestDestinationWidth+20 : labelReporterClassWidth +25+20;
        maximumWidth = (widestRowWidth+20 > labelReporterClassWidth +25+20) ? widestRowWidth + 20 : labelReporterClassWidth +25+20;
        requiredMaximumWidth = maximumWidth;

        panelDestinations.getComponentListeners()[0].componentResized(new ComponentEvent(panelDestinations,0));
        this.revalidate();
    }
    @Override
    public Object getComponent() {
        return reporter;
    }

    @Override
    public Dimension getMinimumSize(){
        Dimension dimension = new Dimension();
        dimension.width = minimumWidth;
        dimension.height = (destinationRowCount*40 + 50 > requiredMinimumHeight) ? destinationRowCount*40 + 50 : requiredMinimumHeight;
        return dimension;
    }

    @Override
    public Dimension getPreferredSize(){
        Dimension dimension = new Dimension();

        int width = widestRowWidth+20;
        if (width < this.getMinimumSize().width){
            width = this.getMinimumSize().width;
        }
        if (width > this.getMaximumSize().width) {
            width = this.getMaximumSize().width;
        }

        dimension.width = width;
        dimension.height = (destinationRowCount*40 + 50 > requiredMinimumHeight) ? destinationRowCount*40 + 50 : requiredMinimumHeight;
        return dimension;
    }

    @Override
    public Dimension getMaximumSize(){
        Dimension dimension = new Dimension();
        dimension.width = (maximumWidth <= requiredMaximumWidth) ? maximumWidth : requiredMaximumWidth;
        dimension.height = (destinationRowCount*40 + 50 > requiredMinimumHeight) ? destinationRowCount*40 + 50 : requiredMinimumHeight;
        return dimension;
    }

}
