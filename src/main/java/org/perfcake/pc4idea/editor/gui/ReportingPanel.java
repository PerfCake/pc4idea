package org.perfcake.pc4idea.editor.gui;

import com.intellij.openapi.project.Project;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.wizard.ReportingEditor;

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
public class ReportingPanel extends AbstractPanel {
    private final String TITLE ="Reporting Editor";
    private Color reportingColor = Color.getHSBColor(0/360f,1f,0.75f);
    private final Project project;

    private ReportingEditor reportingEditor;
    private Scenario.Reporting reporting;

    private JLabel labelReporting;
    private JPanel panelReporters;

    private int reportersHeight;
    private int labelReportingWidth;
    private int widestReporterPreferredWidth;
    private int widestReporterMinimumWidth;

    public ReportingPanel(Project project){
        super(project);
        this.project = project;
        reportersHeight = 0;
        labelReportingWidth = 0;
        widestReporterPreferredWidth = 0;
        widestReporterMinimumWidth = 0;

        initComponents();
    }

    private void initComponents(){
        labelReporting = new JLabel("Reporting");
        labelReporting.setFont(new Font(labelReporting.getFont().getName(),0,15));
        labelReporting.setForeground(reportingColor);
        FontMetrics fontMetrics = labelReporting.getFontMetrics(labelReporting.getFont());
        labelReportingWidth = fontMetrics.stringWidth(labelReporting.getText());

        panelReporters = new JPanel();
        panelReporters.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
        /*panelReporters.setBackground(Color.cyan);*/panelReporters.setOpaque(false);

        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        this.add(labelReporting);
        this.add(panelReporters);

        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, labelReporting,
                0,
                SpringLayout.HORIZONTAL_CENTER, this);
        layout.putConstraint(SpringLayout.NORTH, labelReporting,
                10,
                SpringLayout.NORTH, this);

        layout.putConstraint(SpringLayout.WEST, panelReporters,
                10,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, panelReporters,8,SpringLayout.SOUTH, labelReporting);

        this.addComponentListener( new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (widestReporterMinimumWidth < e.getComponent().getSize().width - 20) {
                    AccessibleContext aReporters = panelReporters.getAccessibleContext();
                    for (int i = 0; i < aReporters.getAccessibleChildrenCount(); i++) {
                        ((ReporterComponent) aReporters.getAccessibleChild(i)).setRequiredMaximumWidth(e.getComponent().getSize().width - 20);
                    }
                    panelReporters.setMinimumSize(new Dimension(e.getComponent().getSize().width - 20, panelReporters.getMinimumSize().height));
                    panelReporters.setPreferredSize(new Dimension(e.getComponent().getSize().width - 20, panelReporters.getPreferredSize().height));
                    panelReporters.setMaximumSize(new Dimension(e.getComponent().getSize().width - 20, panelReporters.getMaximumSize().height));
                    panelReporters.revalidate();
                } else {
                    panelReporters.setMinimumSize(new Dimension(widestReporterMinimumWidth, panelReporters.getMinimumSize().height));
                    panelReporters.setPreferredSize(new Dimension(widestReporterMinimumWidth, panelReporters.getPreferredSize().height));
                    panelReporters.setMaximumSize(new Dimension(widestReporterMinimumWidth, panelReporters.getMaximumSize().height));
                    panelReporters.revalidate();

                }

            }
        });
        panelReporters.addComponentListener( new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (widestReporterMinimumWidth < e.getComponent().getSize().width) {
                    AccessibleContext aReporters = e.getComponent().getAccessibleContext();
                    int controlSum = 0;
                    int expectedHeight = 0;
                    int rowHeight = 0;
                    int rowLeader = 0;
                    for (int i = 0; i < aReporters.getAccessibleChildrenCount(); i++) {
                        ((ReporterComponent) aReporters.getAccessibleChild(i)).setRequiredMinimumHeight(0);
                        ReporterComponent iReporter = (ReporterComponent) aReporters.getAccessibleChild(i);
                        controlSum += iReporter.getPreferredSize().width;
                        if (controlSum > panelReporters.getPreferredSize().width) {
                            for (int j = rowLeader; j < i; j++) {
                                ((ReporterComponent) aReporters.getAccessibleChild(j)).setRequiredMinimumHeight(rowHeight);
                            }
                            rowLeader = i;
                            i--;
                            controlSum = 0;
                            expectedHeight += rowHeight;
                            rowHeight = 0;
                        } else {
                            rowHeight = (rowHeight > iReporter.getPreferredSize().height) ? rowHeight : iReporter.getPreferredSize().height;
                        }


                    }
                    if (rowHeight != 0) {
                        expectedHeight += rowHeight;
                        for (int j = rowLeader; j < aReporters.getAccessibleChildrenCount(); j++) {
                            ((ReporterComponent) aReporters.getAccessibleChild(j)).setRequiredMinimumHeight(rowHeight);
                        }
                    }
                    if (expectedHeight != reportersHeight) {
                        reportersHeight = expectedHeight;
                        panelReporters.setMinimumSize(new Dimension(panelReporters.getMinimumSize().width, reportersHeight));
                        panelReporters.setPreferredSize(new Dimension(panelReporters.getPreferredSize().width, reportersHeight));
                        panelReporters.setMaximumSize(new Dimension(panelReporters.getMaximumSize().width, reportersHeight));
                        panelReporters.revalidate();
                    }
                }
            }
        });

    }

    @Override
    protected Color getColor() {
        return reportingColor;
    }

    @Override
    protected String getEditorTitle() {
        return TITLE;
    }

    @Override
    protected JPanel getEditorPanel() {
        reportingEditor = new ReportingEditor();
        reportingEditor.setReporting(reporting);
        return reportingEditor;
    }

    @Override
    protected void applyChanges() {
        this.setComponent(reportingEditor.getReporting());
    }

    @Override
    public void setComponent(Object component) {
        reporting = (Scenario.Reporting) component;

        panelReporters.removeAll();
        panelReporters.repaint();

        widestReporterPreferredWidth = 0;
        widestReporterMinimumWidth = 0;
        for (Scenario.Reporting.Reporter reporter : reporting.getReporter()) {
            ReporterComponent reporterComponent = new ReporterComponent(project, reportingColor);
            reporterComponent.setComponent(reporter);
            panelReporters.add(reporterComponent);
            if (reporterComponent.getPreferredSize().width > widestReporterPreferredWidth){
                widestReporterPreferredWidth = reporterComponent.getPreferredSize().width;
            }
            if (reporterComponent.getMinimumWidth() > widestReporterMinimumWidth){
                widestReporterMinimumWidth = reporterComponent.getMinimumWidth();
            }
        }

        panelReporters.getComponentListeners()[0].componentResized(new ComponentEvent(panelReporters,0));
        this.revalidate();

    }

    @Override
    public Object getComponent() {
        return reporting;
    }

    @Override
    public Dimension getMinimumSize(){
        Dimension dimension = new Dimension();
        dimension.width = (widestReporterMinimumWidth +20 > labelReportingWidth+30) ? widestReporterMinimumWidth +20 : labelReportingWidth+30;
        dimension.height = reportersHeight + 50;
        return dimension;
    }
}
