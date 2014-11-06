package org.perfcake.pc4idea.editor.gui;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.PerfCakeEditorGUI;
import org.perfcake.pc4idea.editor.components.ComponentEditor;
import org.perfcake.pc4idea.editor.components.ReporterComponent;
import org.perfcake.pc4idea.editor.wizard.ReporterEditor;
import org.perfcake.pc4idea.editor.wizard.ReportingEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 28.9.2014
 */
public class ReportingPanel extends AbstractPanel {
    private final String TITLE ="Reporting Editor";
    private Color reportingColor = Color.getHSBColor(0/360f,1f,0.75f);

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

        this.setTransferHandler(new TransferHandler(){
            @Override
            public boolean canImport(TransferHandler.TransferSupport support){
                support.setDropAction(COPY);
                return support.isDataFlavorSupported(DataFlavor.stringFlavor);
            }
            @Override
            public boolean importData(TransferHandler.TransferSupport support){
                if (!canImport(support)) {
                    return false;
                }
                Transferable t = support.getTransferable();
                String transferredData = "";
                try {
                    transferredData = (String)t.getTransferData(DataFlavor.stringFlavor);
                } catch (UnsupportedFlavorException e) {
                    e.printStackTrace();   /*TODO log*/
                } catch (IOException e) {
                    e.printStackTrace();   /*TODO log*/
                }
                if (transferredData.contains("Reporter")) {
                    ReporterEditor reporterEditor = new ReporterEditor();
                    Scenario.Reporting.Reporter reporterClass = new Scenario.Reporting.Reporter();
                    reporterClass.setClazz(transferredData);
                    ComponentEditor editor = new ComponentEditor("Reporter Editor", reporterEditor);
                    editor.show();
                    if (editor.getExitCode() == 0) {
                        reporting.getReporter().add(reporterEditor.getReporter());
                        setComponentModel(reporting);
                        scenarioEvent.saveReporting();
                    }
                }
                return true;
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
        this.setComponentModel(reportingEditor.getReporting());
        scenarioEvent.saveReporting();
    }

    @Override
    public void setComponentModel(Object componentModel) {
        reporting = (Scenario.Reporting) componentModel;

        panelReporters.removeAll();
        panelReporters.repaint();

        panelReporters.setReporters(reporting.getReporter());

        this.revalidate();
    }

    @Override
    public Object getComponentModel() {
        return reporting;
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
            //this.addMouseListener(new DragListener());
            this.setBackground(Color.orange);//this.setOpaque(false);
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
            /*TODO save Property Maybe,after right click addProp.*/
        }

//        private class DragListener extends MouseInputAdapter {
//            private boolean mousePressed;
//            private int selectedComponent;
//            private int expectedReleaseComponent;
//
//            private DragListener(){
//                mousePressed = false;
//            }
//
//            @Override
//            public void mousePressed(MouseEvent e){
//                if (e.getComponentModel() instanceof ValidatorComponent){
//                    for (int i = 0;i< validatorComponentList.size();i++){
//                        if (e.getComponentModel().equals(validatorComponentList.get(i))){
//                            selectedComponent = i;
//                            expectedReleaseComponent = i;
//                            mousePressed = true;
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void mouseEntered(MouseEvent e) {
//                if (e.getComponentModel() instanceof ValidatorComponent){
//                    for (int i = 0;i< validatorComponentList.size();i++){
//                        if (e.getComponentModel().equals(validatorComponentList.get(i))){
//                            expectedReleaseComponent = i;
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void mouseReleased(MouseEvent e){
//                if(mousePressed) {
//                    if (selectedComponent == expectedReleaseComponent) {
//                        // do nothing
//                    } else {
//                        if (selectedComponent < expectedReleaseComponent) {
//                            for (int i = 0; i < validatorList.size(); i++) {
//                                if (i < selectedComponent) {
//                                    // do nothing
//                                } else {
//                                    if (i < expectedReleaseComponent) {
//                                        Collections.swap(validatorList, i, i + 1);
//                                    }
//                                }
//                            }
//                        }
//                        if (selectedComponent > expectedReleaseComponent) {
//                            for (int i = validatorList.size() - 1; 0 <= i; i--) {
//                                if (i < selectedComponent) {
//                                    if (i >= expectedReleaseComponent) {
//                                        Collections.swap(validatorList, i, i + 1);
//                                    }
//                                }
//                            }
//                        }
//                        validation.getValidator().clear();
//                        validation.getValidator().addAll(validatorList);
//                        ValidationPanel.this.setComponentModel(validation);
//                        scenarioEvent.saveValidation();
//                    }
//                    mousePressed = false;
//                }
//            }
//
//            @Override
//            public void mouseClicked(MouseEvent e){
//                MouseEvent wrappedEvent = new MouseEvent((Component)e.getSource(),e.getID(),e.getWhen(),e.getModifiers(),e.getX()+10,e.getY()+40,e.getClickCount(),e.isPopupTrigger(),e.getButton());
//                ((JPanel)e.getComponentModel().getAccessibleContext().getAccessibleParent()).dispatchEvent(wrappedEvent);
//            }
//        }
    }

}
