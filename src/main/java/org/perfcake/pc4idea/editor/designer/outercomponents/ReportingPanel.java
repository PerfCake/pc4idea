package org.perfcake.pc4idea.editor.designer.outercomponents;

import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.PerfCakeEditorGUI;
import org.perfcake.pc4idea.editor.designer.common.ComponentDragListener;
import org.perfcake.pc4idea.editor.designer.common.ScenarioDialogEditor;
import org.perfcake.pc4idea.editor.designer.editors.AbstractEditor;
import org.perfcake.pc4idea.editor.designer.editors.PropertyEditor;
import org.perfcake.pc4idea.editor.designer.editors.ReporterEditor;
import org.perfcake.pc4idea.editor.designer.editors.ReportingEditor;
import org.perfcake.pc4idea.editor.designer.innercomponents.ReporterComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 28.9.2014
 */
public class ReportingPanel extends AbstractPanel {
    private Color reportingColor = Color.getHSBColor(0 / 360f, 0.75f, 0.75f);

    private ReportingEditor reportingEditor;
    private Scenario.Reporting reporting;
    private PerfCakeEditorGUI.ScenarioEvent scenarioEvent;

    private JLabel labelReporting;
    private PanelReporters panelReporters;

    private int labelReportingWidth;

    public ReportingPanel(PerfCakeEditorGUI.ScenarioEvent scenarioEvent){
        this.scenarioEvent = scenarioEvent;
        labelReportingWidth = 0;

        initComponents();
    }

    private void initComponents(){
        labelReporting = new JLabel("Reporting");
        labelReporting.setFont(new Font(labelReporting.getFont().getName(),0,15));
        labelReporting.setForeground(reportingColor);
        FontMetrics fontMetrics = labelReporting.getFontMetrics(labelReporting.getFont());
        labelReportingWidth = fontMetrics.stringWidth(labelReporting.getText());

        panelReporters = new PanelReporters();

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
                e.getComponent().revalidate();
                e.getComponent().repaint();
            }
        });
    }

    @Override
    protected List<JMenuItem> getPopupMenuItems(){
        List<JMenuItem> menuItems = new ArrayList<>();

        JMenuItem itemOpenEditor = new JMenuItem("Open Editor");
        itemOpenEditor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ScenarioDialogEditor editor = new ScenarioDialogEditor(getEditorPanel());
                editor.show();
                if (editor.getExitCode() == 0) {
                    applyChanges();
                }
            }
        });
        menuItems.add(itemOpenEditor);

        JMenuItem itemAddReporter = new JMenuItem("Add Reporter");
        itemAddReporter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ReporterEditor reporterEditor = new ReporterEditor();
                ScenarioDialogEditor dialog = new ScenarioDialogEditor(reporterEditor);
                dialog.show();
                if (dialog.getExitCode() == 0) {
                    Scenario.Reporting.Reporter reporter = reporterEditor.getReporter();
                    reporting.getReporter().add(reporter);
                    ReportingPanel.this.setComponentModel(reporting);
                    scenarioEvent.saveReporting();
                }
            }
        });
        menuItems.add(itemAddReporter);

        JMenuItem itemAddProperty = new JMenuItem("Add Property");
        itemAddProperty.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PropertyEditor propertyEditor = new PropertyEditor();
                ScenarioDialogEditor dialog = new ScenarioDialogEditor(propertyEditor);
                dialog.show();
                if (dialog.getExitCode() == 0) {
                    Property property = propertyEditor.getProperty();
                    reporting.getProperty().add(property);
                    ReportingPanel.this.setComponentModel(reporting);
                    scenarioEvent.saveReporting();
                }
            }
        });
        menuItems.add(itemAddProperty);

        return menuItems;
    }

    @Override
    protected void performImport(String transferredData){  /*TODO bez dialogu -> just create*/
        if (transferredData.contains("Reporter")) {
            Scenario.Reporting.Reporter reporterClass = new Scenario.Reporting.Reporter();
            reporterClass.setClazz(transferredData);

            ReporterEditor reporterEditor = new ReporterEditor();
            reporterEditor.setReporter(reporterClass);
            ScenarioDialogEditor dialog = new ScenarioDialogEditor(reporterEditor);
            dialog.show();
            if (dialog.getExitCode() == 0) {
                reporting.getReporter().add(reporterEditor.getReporter());
                setComponentModel(reporting);
                scenarioEvent.saveReporting();
            }
        }
    }

    @Override
    protected Color getColor() {
        return reportingColor;
    }

    @Override
    protected AbstractEditor getEditorPanel() {
        reportingEditor = new ReportingEditor();
        reportingEditor.setReporting(reporting);
        return reportingEditor;
    }

    @Override
    protected void applyChanges() {
        this.setComponentModel(reportingEditor.getReporting());
        scenarioEvent.saveReporting();
    }

    @Override
    public void setComponentModel(Object componentModel) {
        if (componentModel != null) {
            reporting = (Scenario.Reporting) componentModel;
            panelReporters.setReporters(reporting.getReporter());
        } else {
            reporting = new Scenario.Reporting();
            panelReporters.setReporters(new ArrayList<Scenario.Reporting.Reporter>());
        }
        this.revalidate();
    }

    @Override
    public Object getComponentModel() {
        return (reporting.getReporter().isEmpty()) ? null : reporting;   /*TODO if (!reporting.getProperty().isEmpty) message warning properties removed*/
    }

    @Override
    public Dimension getMinimumSize(){
        Dimension dimension = new Dimension();
        int minimumWidth = panelReporters.getWidestReporterMinimumWidth();
        dimension.width = (minimumWidth+20 > labelReportingWidth+30) ? minimumWidth+20 : labelReportingWidth+30;
        dimension.height = panelReporters.getReportersRowHeight() + 50;
        return dimension;
    }
/*TODO Reporting able to fit Validation+Messages*/
//    @Override
//    public Dimension getPreferredSize(){
//        Dimension dimension = new Dimension();
//        dimension.width = super.getPreferredSize().width;   /*TODO ak presne polku potom parent getsize.width/2*/
//        dimension.height = panelValidators.getValidatorsRowCount()*40 + 50;   /*TODO potom aj super.get width*/
//        return dimension;
//    }
//
//    @Override
//    public Dimension getMaximumSize(){
//        Dimension dimension = new Dimension();
//        dimension.width = super.getMaximumSize().width;
//        dimension.height = panelValidators.getValidatorsRowCount()*40 + 50;  /*TODO -//-*/
//        return dimension;
//    }

    public class PanelReporters extends JPanel {
        private List<ReporterComponent> reporterComponentList;
        private List<Scenario.Reporting.Reporter> reporterList;

        private int widestReporterMinimumWidth;
        private int reportersRowHeight;

        private PanelReporters(){
            reporterComponentList = new ArrayList<>();
            reporterList = new ArrayList<>();

            widestReporterMinimumWidth = 0;
            reportersRowHeight = 0;
            this.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
            this.setOpaque(false);
            this.addMouseListener(new ComponentDragListener() {
                @Override
                public int mousePressedActionPerformed(MouseEvent e) {
                    int pressedComponent = -1;
                    if (e.getComponent() instanceof ReporterComponent){
                        for (int i = 0;i< reporterComponentList.size();i++){
                            if (e.getComponent().equals(reporterComponentList.get(i))){
                                pressedComponent = i;
                            }
                        }
                    }
                    return pressedComponent;
                }

                @Override
                public int mouseEnteredActionPerformed(MouseEvent e) {
                    int enteredComponent = -1;
                    if (e.getComponent() instanceof ReporterComponent) {
                        for (int i = 0; i < reporterComponentList.size(); i++) {
                            if (e.getComponent().equals(reporterComponentList.get(i))) {
                                enteredComponent = i;
                            }
                        }
                    }
                    return enteredComponent;
                }

                @Override
                public void mouseReleasedActionPerformed(int selectedComponent, int releasedComponent) {
                    if (selectedComponent < releasedComponent) {
                        for (int i = 0; i < reporterList.size(); i++) {
                            if (i >= selectedComponent) {
                                if (i < releasedComponent) {
                                    Collections.swap(reporterList, i, i + 1);
                                }
                            }
                        }
                    }
                    if (selectedComponent > releasedComponent) {
                        for (int i = reporterList.size() - 1; 0 <= i; i--) {
                            if (i < selectedComponent) {
                                if (i >= releasedComponent) {
                                    Collections.swap(reporterList, i, i + 1);
                                }
                            }
                        }
                    }
                    reporting.getReporter().clear();
                    reporting.getReporter().addAll(reporterList);
                    ReportingPanel.this.setComponentModel(reporting);
                    scenarioEvent.saveReporting();
                }
            });
        }

        private void setReporters(List<Scenario.Reporting.Reporter> reporters){
            reporterList.clear();
            reporterList.addAll(reporters);
            reporterComponentList.clear();
            this.removeAll();
            this.repaint();

            widestReporterMinimumWidth = 0;
            int reporterId = 0;
            for (Scenario.Reporting.Reporter reporter : reporterList) {
                ReporterComponent reporterComponent = new ReporterComponent(reportingColor,reporterId,new ReportingEvent());
                reporterComponent.setReporter(reporter);
                reporterComponentList.add(reporterComponent);
                this.add(reporterComponent);
                if (reporterComponent.getMinimumSize().width > widestReporterMinimumWidth) {
                    widestReporterMinimumWidth = reporterComponent.getMinimumSize().width;
                }
                reporterId++;
            }
            countReportersRowHeight();

            this.revalidate();
        }

        private int getWidestReporterMinimumWidth(){
            return widestReporterMinimumWidth;
        }
        private int getReportersRowHeight(){
            return reportersRowHeight;
        }
        private void countReportersRowHeight(){
            int thisPanelWidth = ReportingPanel.this.getSize().width-20;
            thisPanelWidth = (thisPanelWidth < 0) ? Short.MAX_VALUE : thisPanelWidth;

            boolean delayCondition = true;
            for (ReporterComponent reporterComponent : reporterComponentList){
                reporterComponent.setRequiredWidth(thisPanelWidth);
                if (reporterComponent.getPreferredSize().width > thisPanelWidth){
                    delayCondition = false;
                }
            }

            if (widestReporterMinimumWidth <= thisPanelWidth && delayCondition) {
                int controlSum = 0;
                int expectedRowsHeight = 0;
                int nRowHeight = 0;
                for (int i = 0; i < reporterComponentList.size(); i++) {
                    controlSum += reporterComponentList.get(i).getPreferredSize().width;
                    if (controlSum > thisPanelWidth) {
                        i--;
                        controlSum = 0;

                        expectedRowsHeight += nRowHeight;
                        nRowHeight = 0;
                    } else {
                        int iReporterHeight = reporterComponentList.get(i).getPreferredSize().height;
                        nRowHeight = (iReporterHeight > nRowHeight) ? iReporterHeight : nRowHeight;
                    }
                }
                if (nRowHeight != 0){
                    expectedRowsHeight += nRowHeight;
                }
                reportersRowHeight = (expectedRowsHeight != reportersRowHeight) ? expectedRowsHeight : reportersRowHeight;
            }   /*TODO mozno este dokoncit vysku reportrov v riadku*/
        }

        @Override
        public Dimension getMinimumSize(){
            Dimension dimension = new Dimension();
            dimension.width = widestReporterMinimumWidth; /*TODO vo vsetkych vnut.P. ReportingPanel.this.getSize().width-20;*/
            dimension.height = reportersRowHeight;
            return dimension;
        }

        @Override
        public Dimension getPreferredSize(){
            countReportersRowHeight();

            Dimension dimension = new Dimension();
            dimension.width = ReportingPanel.this.getSize().width-20;
            dimension.height = reportersRowHeight;
            return dimension;
        }

        @Override
        public Dimension getMaximumSize(){
            Dimension dimension = new Dimension();
            dimension.width = ReportingPanel.this.getSize().width-20;
            dimension.height = reportersRowHeight;
            return dimension;
        }

        public final class ReportingEvent {
            public void saveReporter(int reporterId){
                for (int i = 0; i<reporterComponentList.size();i++){
                    if (reporterComponentList.get(i).getId() == reporterId){
                        reporterList.set(i, reporterComponentList.get(i).getReporter());

                        reporting.getReporter().clear();
                        reporting.getReporter().addAll(reporterList);
                        ReportingPanel.this.setComponentModel(reporting);
                        scenarioEvent.saveReporting();
                    }
                }

            }
            public void deleteReporter(int reporterId){
                for (int i = 0; i<reporterComponentList.size();i++){
                    if (reporterComponentList.get(i).getId() == reporterId){
                        reporterList.remove(i);

                        reporting.getReporter().clear();
                        reporting.getReporter().addAll(reporterList);
                        ReportingPanel.this.setComponentModel(reporting);
                        scenarioEvent.saveReporting();
                    }
                }
            }
        }
    }
}
