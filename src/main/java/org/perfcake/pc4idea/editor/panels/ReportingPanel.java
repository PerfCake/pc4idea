package org.perfcake.pc4idea.editor.panels;

import com.intellij.openapi.project.Project;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.components.ReporterComponent;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 28.9.2014
 * To change this template use File | Settings | File Templates.
 */
public class ReportingPanel extends AbstractPanel {
    private final String TITLE ="Reporting Editor";
    private Color panelColor = Color.getHSBColor(0/360f,1f,0.75f);
    private final Project project;

    private JPanel panelEditor;
    private Scenario.Reporting reporting;

    private JLabel labelReporting;
    //private JScrollPane scrollPaneReporters;/*TODO*/
    private JPanel panelReporters;

    public ReportingPanel(Project project){
        super(project);
        this.project = project;

        setMinimumSize(new Dimension(0, 50));
        //setMaximumSize(new Dimension(Integer.MAX_VALUE, 50)); /*TODO y podla mess.+val. ,x polka z celkovej*/

        initComponents();

        panelEditor = initPanelEditor();

    }
    private void initComponents(){
        labelReporting = new JLabel("Reporting");
        labelReporting.setFont(new Font(labelReporting.getFont().getName(),0,15));
        labelReporting.setForeground(panelColor);

        panelReporters = new JPanel();
        panelReporters.setLayout(new BoxLayout(panelReporters,BoxLayout.X_AXIS));/*TODO layout*/
        panelReporters.setMinimumSize(new Dimension(170/*400/2-2*15*/,0));
        //panelReporters.setMaximumSize(new Dimension(Integer.MAX_VALUE, 0));
        panelReporters.setOpaque(false);

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
                15,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.EAST, this,
                15,
                SpringLayout.EAST, panelReporters);
        layout.putConstraint(SpringLayout.NORTH, panelReporters,8,SpringLayout.SOUTH, labelReporting);
        layout.putConstraint(SpringLayout.SOUTH, this,
                10,
                SpringLayout.SOUTH,panelReporters);
    }
    private JPanel initPanelEditor(){
        JPanel panel = new JPanel();
        /*TODO ...*/
        return panel;
    }

    @Override
    protected Color getColor() {
        return panelColor;
    }

    @Override
    protected String getEditorTitle() {
        return TITLE;
    }

    @Override
    protected JPanel getPanelEditor() {
        return panelEditor;
    }

    @Override
    protected void applyChanges() {
        /*TODO ...*/
    }

    @Override
    public void setComponent(Object component) {
        reporting = (Scenario.Reporting) component;

        /*TODO zarovnavat:akyLayout:podla sirky (min=200)*/
        for (Scenario.Reporting.Reporter reporter : reporting.getReporter()) {
            /*TODO ukladat zoznam rep.*/
            ReporterComponent reporterComponent = new ReporterComponent(project,panelColor);
            reporterComponent.setComponent(reporter);
            panelReporters.add(reporterComponent);
        }

        /*TODO*/ if (reporting.getReporter().size() > 0) { /* TODO temp n=1, m=1*/
            setMinimumSize(new Dimension(0, 50 + 50 + 40));
            //setMaximumSize(new Dimension(Integer.MAX_VALUE, 50 + 50 + 40)); /*TODO base=50 + 50*n +40*m n=number rows,m=number dest.rows of n*/

            panelReporters.setMinimumSize(new Dimension(170, 50 + 40));
            //panelReporters.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50 + 40));   /*TODO 0 or 40*n -//-*/
        }
        //panelProperties.validate();TODO if needed

    }

    @Override
    public Object getComponent() {
        return reporting;
    }


}
