package org.perfcake.pc4idea.editor.gui;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.interfaces.ModelWrapper;
import org.perfcake.pc4idea.editor.swing.ComponentsPanel;

import javax.swing.*;

/**
 * Created by Stanislav Kaleta on 3/7/15.
 */
public class ReportingGUI extends AbstractComponentGUI /*implements HasListableChildren*/ {
    private Scenario.Reporting reporting;

    private JLabel labelReporting;
    private ComponentsPanel panelReporters;

    private int labelReportingWidth = 0;
    private boolean addPropertyEnabled = true;

    @Override
    void performImport(String transferredData) {

    }

    @Override
    public Object openEditorDialogAndGetResult() {
        return null;
    }

    @Override
    public void updateGUI() {

    }

    @Override
    public void updateColors() {

    }

    public ReportingGUI(ModelWrapper reportingModelWrapper, ActionMap actionMap){
        super(actionMap);
        //initComponents();
        updateColors();
    }
//
//    private void initComponents(){
//        labelReporting = new JLabel("Reporting");
//        labelReporting.setFont(new Font(labelReporting.getFont().getName(),0,15));
//
//        FontMetrics fontMetrics = labelReporting.getFontMetrics(labelReporting.getFont());
//        labelReportingWidth = fontMetrics.stringWidth(labelReporting.getText());
//
//        panelReporters = new ComponentsPanel(this);
//
//        SpringLayout layout = new SpringLayout();
//        this.setLayout(layout);
//        this.add(labelReporting);
//        this.add(panelReporters);
//
//        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, labelReporting,
//                0,
//                SpringLayout.HORIZONTAL_CENTER, this);
//        layout.putConstraint(SpringLayout.NORTH, labelReporting,
//                10,
//                SpringLayout.NORTH, this);
//
//        layout.putConstraint(SpringLayout.WEST, panelReporters,
//                10,
//                SpringLayout.WEST, this);
//        layout.putConstraint(SpringLayout.NORTH, panelReporters,8,SpringLayout.SOUTH, labelReporting);
//
//        this.getActionMap().put("ADDR", new AbstractAction(Messages.BUNDLE.getString("ADD")+" Reporter", AllIcons.General.Add) {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                ReporterEditor editor = new ReporterEditor();
//                ScenarioDialogEditor dialog = new ScenarioDialogEditor(editor);
//                dialog.show();
//                if (dialog.getExitCode() == 0) {
//                    Scenario.Reporting.Reporter reporter = editor.getReporter();
//                    ReportingGUI.this.reporting.getReporter().add(reporter);
//                    ReportingGUI.this.setComponentModel(reporting);
//                    ReportingGUI.this.commitChanges(Messages.BUNDLE.getString("ADD") + " Reporter");
//                }
//
//            }
//        });
//        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.SHIFT_MASK), "ADDR");
//
//        this.getActionMap().put("ADDP", new AbstractAction(Messages.BUNDLE.getString("ADD")+" Property", AllIcons.General.Add) {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if (addPropertyEnabled) {
//                    PropertyEditor editor = new PropertyEditor();
//                    ScenarioDialogEditor dialog = new ScenarioDialogEditor(editor);
//                    dialog.show();
//                    if (dialog.getExitCode() == 0) {
//                        Property property = editor.getProperty();
//                        ReportingGUI.this.reporting.getProperty().add(property);
//                        ReportingGUI.this.setComponentModel(reporting);
//                        ReportingGUI.this.commitChanges(Messages.BUNDLE.getString("ADD") + " Property");
//                    }
//                }
//            }
//        });
//        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.SHIFT_MASK), "ADDP");
//
//        this.getActionMap().put("EDIT", new AbstractAction(Messages.BUNDLE.getString("EDIT")+" Reporting", AllIcons.Actions.Edit) {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                ReportingGUI.this.openEditorDialogAndGetResult();
//            }
//        });
//        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.SHIFT_MASK), "EDIT");
//    }
//
//    @Override
//    protected List<JMenuItem> getMenuItems() {
//        List<JMenuItem> menuItems = new ArrayList<>();
//
//        JMenuItem addReporterItem = new JMenuItem();
//        addReporterItem.setAction(this.getActionMap().get("ADDR"));
//        addReporterItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.SHIFT_MASK));
//        menuItems.add(addReporterItem);
//
//        JMenuItem addPropertyItem = new JMenuItem();
//        addPropertyItem.setAction(this.getActionMap().get("ADDP"));
//        addPropertyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.SHIFT_MASK));
//        addPropertyItem.setEnabled(addPropertyEnabled);
//        addPropertyItem.getAction().setEnabled(addPropertyEnabled);
//        menuItems.add(addPropertyItem);
//
//        JMenuItem editItem = new JMenuItem();
//        editItem.setAction(this.getActionMap().get("EDIT"));
//        editItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.SHIFT_MASK));
//        menuItems.add(editItem);
//
//        return menuItems;
//    }
//
//    @Override
//    protected void performImport(String transferredData) {
//        if (transferredData.contains("Reporter")) {
//            Scenario.Reporting.Reporter reporterClass = new Scenario.Reporting.Reporter();
//            reporterClass.setClazz(transferredData);
//
//            ReporterEditor reporterEditor = new ReporterEditor();
//            reporterEditor.setReporter(reporterClass);
//            ScenarioDialogEditor dialog = new ScenarioDialogEditor(reporterEditor);
//            dialog.show();
//            if (dialog.getExitCode() == 0) {
//                reporting.getReporter().add(reporterEditor.getReporter());
//                setComponentModel(reporting);
//                commitChanges(Messages.BUNDLE.getString("ADD") + " Reporter");
//            }
//        }
//    }
//
//    @Override
//    protected void openEditorDialogAndGetResult() {
//        ReportingEditor editor = new ReportingEditor();
//        editor.setReporting(reporting);
//        ScenarioDialogEditor dialog = new ScenarioDialogEditor(editor);
//        dialog.show();
//        if (dialog.getExitCode() == 0) {
//            this.setComponentModel((editor.getReporting().getReporter().isEmpty()) ?
//                    null : editor.getReporting());
//            this.commitChanges(Messages.BUNDLE.getString("EDIT")+" Reporting");
//        }
//    }
//
//    @Override
//    public void setComponentModel(Object componentModel) {
//        if (componentModel != null) {
//            reporting = (Scenario.Reporting) componentModel;
//            addPropertyEnabled = true;
//        } else {
//            reporting = new Scenario.Reporting();
//            addPropertyEnabled = false;
//        }
//        panelReporters.updateComponents();
//    }
//
//    @Override
//    public Object getComponentModel() {
//        return (reporting.getReporter().isEmpty()) ? null : reporting;
//    }
//
//    @Override
//    public void updateColors() {
//        setBackground(ColorComponents.getColor(ColorType.REPORTING_BACKGROUND));
//        Color foregroundColor = ColorComponents.getColor(ColorType.REPORTING_FOREGROUND);
//        setForeground(foregroundColor);
//        labelReporting.setForeground(foregroundColor);
//    }
//
//    @Override
//    public List<AbstractComponentGUI> getChildrenModels() {
//        List<AbstractComponentGUI> childrenAsGUI = new ArrayList<>();
//        for (Scenario.Reporting.Reporter reporter : reporting.getReporter()) {
//            ReporterGUI reporterGUI = new ReporterGUI(this);
//            reporterGUI.setComponentModel(reporter);
//            childrenAsGUI.add(reporterGUI);
//        }
//        return childrenAsGUI;
//    }
//
//    @Override
//    public void setChildrenFromModels(List<AbstractComponentGUI> childrenAsGUI) {
//        List<Scenario.Reporting.Reporter> reporters = new ArrayList<>();
//        for (AbstractComponentGUI c : childrenAsGUI){
//            reporters.add((Scenario.Reporting.Reporter) c.getComponentModel());
//        }
//        reporting.getReporter().clear();
//        reporting.getReporter().addAll(reporters);
//        setComponentModel(reporting);
//        commitChanges("Reporting: Reporters " + Messages.BUNDLE.getString("REORDER"));
//    }
//
//    @Override
//    public Dimension getMinimumSize(){
//        int panelMinWidth = panelReporters.getMinimumSize().width;
//        int width = (panelMinWidth+20 > labelReportingWidth+30) ? panelMinWidth+20 : labelReportingWidth+30;
//        return new Dimension(width,panelReporters.getMinimumSize().height + 50);
//    }


}
