package org.perfcake.pc4idea.editor.components;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.gui.ReportingPanel;
import org.perfcake.pc4idea.editor.wizard.DestinationEditor;
import org.perfcake.pc4idea.editor.wizard.ReporterEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 15.10.2014
 */
public class ReporterComponent extends JPanel {
    private final String TITLE ="Reporter Editor";
    private final Color reporterColor;
    private final int id;

    private ReporterEditor reporterEditor;
    private Scenario.Reporting.Reporter reporter;
    private ReportingPanel.PanelReporters.ReportingEvent reportingEvent;

    private JLabel labelReporterClass;
    private EnabledIndicator reporterEnabled;
    private PanelDestinations panelDestinations;
    private JPopupMenu popupMenu;
    private JMenuItem popupOpenEditor;
    private JMenuItem popupDelete;

    private int labelReporterClassWidth;
    private int requiredWidth;


    public ReporterComponent(Color reportingColor, int id, ReportingPanel.PanelReporters.ReportingEvent reportingEvent){
        this.reporterColor = reportingColor;
        this.id = id;
        this.reportingEvent = reportingEvent;

        labelReporterClassWidth = 0;
        requiredWidth = Short.MAX_VALUE;

        this.setOpaque(false);

        initComponents();
    }

    private void initComponents(){
        labelReporterClass = new JLabel("---");
        labelReporterClass.setFont(new Font(labelReporterClass.getFont().getName(), 0, 15));
        labelReporterClass.setForeground(reporterColor);

        reporterEnabled = new EnabledIndicator(reporterColor);

        panelDestinations = new PanelDestinations();

        popupOpenEditor = new JMenuItem("Open Editor");
        popupOpenEditor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reporterEditor = new ReporterEditor();
                reporterEditor.setReporter(reporter);
                ComponentEditor editor = new ComponentEditor(TITLE, reporterEditor);
                editor.show();
                if (editor.getExitCode() == 0) {
                    setReporter(reporterEditor.getReporter());
                    reportingEvent.saveReporter(id);
                }
            }
        });
        popupDelete = new JMenuItem("Delete");
        popupDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reportingEvent.deleteReporter(id);
            }
        });

        popupMenu = new JPopupMenu();
        popupMenu.add(popupOpenEditor);
        popupMenu.add(new JPopupMenu.Separator());
        popupMenu.add(popupDelete);
        /*TODO dalsie?*/

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

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getButton() == MouseEvent.BUTTON1) {
                    if (event.getClickCount() == 2) {
                        reporterEditor = new ReporterEditor();
                        reporterEditor.setReporter(reporter);
                        ComponentEditor editor = new ComponentEditor(TITLE, reporterEditor);
                        editor.show();
                        if (editor.getExitCode() == 0) {
                            setReporter(reporterEditor.getReporter());
                            reportingEvent.saveReporter(id);
                        }
                    }
                }
                if (event.getButton() == MouseEvent.BUTTON3) {
                    popupMenu.show(ReporterComponent.this, event.getX(), event.getY());
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {
                ((JPanel)e.getComponent().getAccessibleContext().getAccessibleParent()).dispatchEvent(e);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                ((JPanel)e.getComponent().getAccessibleContext().getAccessibleParent()).dispatchEvent(e);
            }
            @Override
            public void mouseReleased(MouseEvent e){
                ((JPanel)e.getComponent().getAccessibleContext().getAccessibleParent()).dispatchEvent(e);
            }
        });

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
                if (transferredData.contains("Destination")) {
                    DestinationEditor destinationEditor = new DestinationEditor();
                    ComponentEditor editor = new ComponentEditor("Destination Editor", destinationEditor);
                    editor.show();
                    if (editor.getExitCode() == 0) {
                        reporter.getDestination().add(destinationEditor.getDestination());
                        setReporter(reporter);
                        reportingEvent.saveReporter(id);
                    }
                }
                return true;
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(reporterColor);
        g2D.drawRoundRect(4, 4, this.getWidth() - 8, this.getHeight() - 8, 20, 20);
    }

    public void setReporter(Scenario.Reporting.Reporter r) {
        reporter = r;

        labelReporterClass.setText(reporter.getClazz());
        FontMetrics fontMetrics = labelReporterClass.getFontMetrics(labelReporterClass.getFont());
        labelReporterClassWidth = fontMetrics.stringWidth(labelReporterClass.getText());

        reporterEnabled.setState(reporter.isEnabled());

        requiredWidth = Short.MAX_VALUE;
        panelDestinations.setDestinations(reporter.getDestination());

        this.revalidate();
    }

    public Scenario.Reporting.Reporter getReporter() {
        return reporter;
    }

    public int getId(){
        return id;
    }

    public void setRequiredWidth(int width){
        requiredWidth = width;
        panelDestinations.revalidate();
        this.revalidate();
    }
    @Override
    public Dimension getMinimumSize(){
        Dimension dimension = new Dimension();
        int widestDestinationWidth = panelDestinations.getWidestDestinationWidth();
        dimension.width = (widestDestinationWidth+20 > labelReporterClassWidth+30+20) ? widestDestinationWidth+20 : labelReporterClassWidth+30+20;
        dimension.height = panelDestinations.getDestinationsRowCount()*40 + 50;
        return dimension;
    }
    @Override
    public Dimension getPreferredSize(){
        Dimension dimension = new Dimension();
        int widestRowWidth = panelDestinations.getWidestDestinationRowWidth();
        dimension.width =  (widestRowWidth+20 > labelReporterClassWidth+30+20) ? widestRowWidth+20 : labelReporterClassWidth+30+20;
        dimension.height = panelDestinations.getDestinationsRowCount()*40 + 50;
        return dimension;
    }

    @Override
    public Dimension getMaximumSize(){
        Dimension dimension = new Dimension();
        int widestRowWidth = panelDestinations.getWidestDestinationRowWidth();
        dimension.width =  (widestRowWidth+20 > labelReporterClassWidth+30+20) ? widestRowWidth+20 : labelReporterClassWidth+30+20;
        dimension.height = panelDestinations.getDestinationsRowCount()*40 + 50;
        return dimension;
    }

    public class PanelDestinations extends JPanel { /*not private but public?*/
        private List<DestinationComponent> destinationComponentList;
        private List<Scenario.Reporting.Reporter.Destination> destinationList;
        private int widestDestinationWidth;
        private int widestDestinationRowWidth;
        private int destinationsRowCount;

        private PanelDestinations(){
            destinationComponentList = new ArrayList<>();
            destinationList = new ArrayList<>();
            widestDestinationWidth = 0;
            widestDestinationRowWidth = 0;
            destinationsRowCount = 0;
            this.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
            //this.addMouseListener(new DragListener());
            this.setBackground(Color.cyan);//this.setOpaque(false);
        }

        private void setDestinations(List<Scenario.Reporting.Reporter.Destination> destinations){
            destinationList.clear();
            destinationList.addAll(destinations);
            destinationComponentList.clear();
            this.removeAll();
            this.repaint();

            widestDestinationWidth = 0;
            widestDestinationRowWidth = 0;
            int destinationId = 0;
            for (Scenario.Reporting.Reporter.Destination destination : destinationList) {
                DestinationComponent destinationComponent = new DestinationComponent(reporterColor,destinationId,new ReporterEvent());
                destinationComponent.setDestination(destination);
                destinationComponentList.add(destinationComponent);
                this.add(destinationComponent);
                if (destinationComponent.getPreferredSize().width > widestDestinationWidth) {
                    widestDestinationWidth = destinationComponent.getPreferredSize().width;
                }
                destinationId++;
            }
            countDestinationsRowCountAndWidth();

            this.revalidate();
        }

        private int getWidestDestinationWidth(){
            return widestDestinationWidth;
        }
        private int getDestinationsRowCount(){
            return destinationsRowCount;
        }
        private int getWidestDestinationRowWidth(){return widestDestinationRowWidth;}
        private void countDestinationsRowCountAndWidth(){
            int thisPanelWidth = requiredWidth-20;

            if (widestDestinationWidth <= thisPanelWidth) {
                int controlSum = 0;
                int expectedRows = 0;
                int expectedWidestRowWidth = 0;
                for (int i = 0; i < destinationComponentList.size(); i++) {
                    if (i == 0) {
                        expectedRows = 1;
                    }
                    int iDestinationWidth = destinationComponentList.get(i).getPreferredSize().width;
                    controlSum += iDestinationWidth;
                    if (controlSum > thisPanelWidth) {
                        expectedWidestRowWidth  = (expectedWidestRowWidth < controlSum-iDestinationWidth) ? controlSum-iDestinationWidth : expectedWidestRowWidth;
                        i--;
                        controlSum = 0;
                        expectedRows++;
                    }
                }
                if (controlSum != 0) {
                    expectedWidestRowWidth  = (expectedWidestRowWidth < controlSum) ? controlSum : expectedWidestRowWidth;
                }
                widestDestinationRowWidth = (expectedWidestRowWidth != widestDestinationRowWidth) ? expectedWidestRowWidth : widestDestinationRowWidth;
                destinationsRowCount = (expectedRows != destinationsRowCount) ? expectedRows : destinationsRowCount;
            }
        }

        @Override
        public Dimension getMinimumSize(){
            Dimension dimension = new Dimension();
            dimension.width = widestDestinationWidth;
            dimension.height = destinationsRowCount*40;
            return dimension;
        }

        @Override
        public Dimension getPreferredSize(){
            countDestinationsRowCountAndWidth();

            Dimension dimension = new Dimension();
            dimension.width = ReporterComponent.this.getSize().width-20;
            dimension.height = destinationsRowCount*40;
            return dimension;
        }

        @Override
        public Dimension getMaximumSize(){
            Dimension dimension = new Dimension();
            dimension.width = ReporterComponent.this.getSize().width-20;
            dimension.height = destinationsRowCount*40;
            return dimension;
        }

        public final class ReporterEvent {
            public void saveDestination(int destinationId){
                for (int i = 0; i<destinationComponentList.size();i++){
                    if (destinationComponentList.get(i).getId() == destinationId){
                        destinationList.set(i, destinationComponentList.get(i).getDestination());

                        reporter.getDestination().clear();
                        reporter.getDestination().addAll(destinationList);
                        ReporterComponent.this.setReporter(reporter);
                        reportingEvent.saveReporter(destinationId);
                    }
                }
            }
            public void deleteDestination(int destinationId){
                for (int i = 0; i<destinationComponentList.size();i++){
                    if (destinationComponentList.get(i).getId() == destinationId){
                        destinationList.remove(i);

                        reporter.getDestination().clear();
                        reporter.getDestination().addAll(destinationList);
                        ReporterComponent.this.setReporter(reporter);
                        reportingEvent.saveReporter(destinationId);
                    }
                }
            }
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
//                if (e.getComponentModel() instanceof PropertyComponent){
//                    for (int i = 0;i< propertyComponentList.size();i++){
//                        if (e.getComponentModel().equals(propertyComponentList.get(i))){
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
//                if (e.getComponentModel() instanceof PropertyComponent){
//                    for (int i = 0;i< propertyComponentList.size();i++){
//                        if (e.getComponentModel().equals(propertyComponentList.get(i))){
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
//                            for (int i = 0; i < propertiesList.size(); i++) {
//                                if (i < selectedComponent) {
//                                    // do nothing
//                                } else {
//                                    if (i < expectedReleaseComponent) {
//                                        Collections.swap(propertiesList, i, i + 1);
//                                    }
//                                }
//                            }
//                        }
//                        if (selectedComponent > expectedReleaseComponent) {
//                            for (int i = propertiesList.size() - 1; 0 <= i; i--) {
//                                if (i < selectedComponent) {
//                                    if (i >= expectedReleaseComponent) {
//                                        Collections.swap(propertiesList, i, i + 1);
//                                    }
//                                }
//                            }
//                        }
//                        sender.getProperty().clear();
//                        sender.getProperty().addAll(propertiesList);
//                        SenderPanel.this.setComponentModel(sender);
//                        scenarioEvent.saveSender();
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
