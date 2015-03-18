package org.perfcake.pc4idea.editor.gui;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.*;
import org.perfcake.pc4idea.editor.swing.JEnabledCircle;

import javax.swing.*;

/**
 * Created by Stanislav Kaleta on 3/11/15.
 */
public class ReporterGUI /*extends AbstractComponentGUI implements Activatable, HasListableChildren*/ {
    private Scenario.Reporting.Reporter reporter;
    private AbstractComponentGUI parent;

    private JLabel labelReporterClass;
    private JEnabledCircle enabledCircle;
    private ComponentsPanel panelDestinations;

    private int labelReporterClassWidth = 0;
    private int requiredWidth = Short.MAX_VALUE;

//    public ReporterGUI(ModelWrapper propertyModelWrapper, ModelWrapper parentModelWrapper, ActionMap baseActionMap){
//        super(baseActionMap);
//        this.parent = parent;
//        initComponents();
//        updateColors();
//    }

//    private void initComponents() {
//        labelReporterClass = new JLabel("---");
//        labelReporterClass.setFont(new Font(labelReporterClass.getFont().getName(), 0, 15));
//
//        enabledCircle = new JEnabledCircle(this);
//        panelDestinations = new ComponentsPanel(this);
//
//        SpringLayout layout = new SpringLayout();
//        this.setLayout(layout);
//        this.add(labelReporterClass);
//        this.add(enabledCircle);
//        this.add(panelDestinations);
//
//        layout.putConstraint(SpringLayout.NORTH, labelReporterClass,
//                10,
//                SpringLayout.NORTH, this);
//        layout.putConstraint(SpringLayout.NORTH, enabledCircle,
//                10,
//                SpringLayout.NORTH, this);
//        layout.putConstraint(SpringLayout.WEST,enabledCircle,
//                10,SpringLayout.WEST,this);
//        layout.putConstraint(SpringLayout.WEST, labelReporterClass,
//                5,
//                SpringLayout.EAST,enabledCircle);
//
//        layout.putConstraint(SpringLayout.WEST, panelDestinations,
//                10,
//                SpringLayout.WEST, this);
//        layout.putConstraint(SpringLayout.NORTH, panelDestinations,8,SpringLayout.SOUTH, labelReporterClass);
//
//        this.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mousePressed(MouseEvent e) {
//                ((JPanel)e.getComponent().getAccessibleContext().getAccessibleParent()).dispatchEvent(e);
//            }
//            @Override
//            public void mouseEntered(MouseEvent e) {
//                ((JPanel)e.getComponent().getAccessibleContext().getAccessibleParent()).dispatchEvent(e);
//            }
//            @Override
//            public void mouseReleased(MouseEvent e){
//                ((JPanel)e.getComponent().getAccessibleContext().getAccessibleParent()).dispatchEvent(e);
//            }
//        });
//
//        this.getActionMap().put("ADDD", new AbstractAction(Messages.BUNDLE.getString("ADD")+" Destination", AllIcons.General.Add) {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                DestinationEditor destinationEditor = new DestinationEditor();
//                ScenarioDialogEditor dialog = new ScenarioDialogEditor(destinationEditor);
//                dialog.show();
//                if (dialog.getExitCode() == 0) {
//                    Scenario.Reporting.Reporter.Destination destination = destinationEditor.getDestination();
//                    ReporterGUI.this.reporter.getDestination().add(destination);
//                    ReporterGUI.this.setComponentModel(reporter);
//                    ReporterGUI.this.commitChanges(Messages.BUNDLE.getString("ADD") + " Destination");
//                }
//            }
//        });
//        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.SHIFT_MASK), "ADDD");
//
//        this.getActionMap().put("ADDP", new AbstractAction(Messages.BUNDLE.getString("ADD")+" Property", AllIcons.General.Add) {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                PropertyEditor editor = new PropertyEditor();
//                ScenarioDialogEditor dialog = new ScenarioDialogEditor(editor);
//                dialog.show();
//                if (dialog.getExitCode() == 0) {
//                    Property property = editor.getProperty();
//                    ReporterGUI.this.reporter.getProperty().add(property);
//                    ReporterGUI.this.setComponentModel(reporter);
//                    ReporterGUI.this.commitChanges(Messages.BUNDLE.getString("ADD") + " Property");
//                }
//            }
//        });
//        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.SHIFT_MASK), "ADDP");
//
//        this.getActionMap().put("ENABLE", new AbstractAction(Messages.BUNDLE.getString("ENABLE")+" Reporter", AllIcons.Debugger.Db_muted_verified_breakpoint) {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if (!ReporterGUI.this.reporter.isEnabled()) {
//                    ReporterGUI.this.reporter.setEnabled(true);
//                    ReporterGUI.this.setComponentModel(reporter);
//                    ReporterGUI.this.commitChanges(Messages.BUNDLE.getString("ENABLE") + " Reporter");
//                }
//            }
//        });
//        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.SHIFT_MASK), "ENABLE");
//
//        this.getActionMap().put("DISABLE", new AbstractAction(Messages.BUNDLE.getString("DISABLE")+" Reporter", AllIcons.Debugger.MuteBreakpoints) {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if (ReporterGUI.this.reporter.isEnabled()) {
//                    ReporterGUI.this.reporter.setEnabled(false);
//                    ReporterGUI.this.setComponentModel(reporter);
//                    ReporterGUI.this.commitChanges(Messages.BUNDLE.getString("DISABLE") + " Reporter");
//                }
//            }
//        });
//        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.SHIFT_MASK), "DISABLE");
//
//        this.getActionMap().put("EDIT", new AbstractAction(Messages.BUNDLE.getString("EDIT")+" Reporter", AllIcons.Actions.Edit) {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                ReporterGUI.this.openEditorDialogAndGetResult();
//            }
//        });
//        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.SHIFT_MASK), "EDIT");
//
//        this.getActionMap().put("DEL", new AbstractAction(Messages.BUNDLE.getString("DEL")+" Reporter", AllIcons.Actions.Delete) {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                Scenario.Reporting reporting = (Scenario.Reporting) parent.getComponentModel();
//                Scenario.Reporting.Reporter reporterToDel = null;
//                for (Scenario.Reporting.Reporter r : reporting.getReporter()) {
//                    if (r.equals(ReporterGUI.this.reporter)) {
//                        reporterToDel = r;
//                    }
//                }
//                reporting.getReporter().remove(reporterToDel);
//                parent.setComponentModel(reporting);
//                ReporterGUI.this.commitChanges(Messages.BUNDLE.getString("DEL") + " Reporter");
//            }
//        });
//        getInputMap().put(KeyStroke.getKeyStroke("DELETE"), "DEL");
//    }
//
//    @Override
//    protected List<JMenuItem> getMenuItems() {
//        List<JMenuItem> menuItems = new ArrayList<>();
//
//        JMenuItem addDestinationItem = new JMenuItem();
//        addDestinationItem.setAction(this.getActionMap().get("ADDD"));
//        addDestinationItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.SHIFT_MASK));
//        menuItems.add(addDestinationItem);
//
//        JMenuItem addPropertyItem = new JMenuItem();
//        addPropertyItem.setAction(this.getActionMap().get("ADDP"));
//        addPropertyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.SHIFT_MASK));
//        menuItems.add(addPropertyItem);
//
//        JMenuItem enabledDisabledItem = new JMenuItem();
//        enabledDisabledItem.setAction((reporter.isEnabled()) ? this.getActionMap().get("DISABLE") :
//                this.getActionMap().get("ENABLE"));
//        enabledDisabledItem.setAccelerator((reporter.isEnabled()) ? KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.SHIFT_MASK) :
//                        KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.SHIFT_MASK));
//        menuItems.add(enabledDisabledItem);
//
//        JMenuItem editItem = new JMenuItem();
//        editItem.setAction(this.getActionMap().get("EDIT"));
//        editItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.SHIFT_MASK));
//        menuItems.add(editItem);
//
//        JMenuItem deleteReporterItem = new JMenuItem();
//        deleteReporterItem.setAction(this.getActionMap().get("DEL"));
//        deleteReporterItem.setAccelerator(KeyStroke.getKeyStroke("DELETE"));
//        menuItems.add(deleteReporterItem);
//
//        return menuItems;
//    }
//
//    @Override
//    protected void performImport(String transferredData) {
//        if (transferredData.contains("Destination")) {
//            Scenario.Reporting.Reporter.Destination destinationClass = new Scenario.Reporting.Reporter.Destination();
//            destinationClass.setClazz(transferredData);
//
//            DestinationEditor destinationEditor = new DestinationEditor();
//            destinationEditor.setDestination(destinationClass);
//            ScenarioDialogEditor dialog = new ScenarioDialogEditor(destinationEditor);
//            dialog.show();
//            if (dialog.getExitCode() == 0) {
//                reporter.getDestination().add(destinationEditor.getDestination());
//                setComponentModel(reporter);
//                commitChanges(Messages.BUNDLE.getString("ADD") + " Destination");
//            }
//        }
//    }
//
//    @Override
//    protected void openEditorDialogAndGetResult() {
//        ReporterEditor editor = new ReporterEditor();
//        editor.setReporter(reporter);
//        ScenarioDialogEditor dialog = new ScenarioDialogEditor(editor);
//        dialog.show();
//        if (dialog.getExitCode() == 0){
//            this.setComponentModel(editor.getReporter());
//            this.commitChanges(Messages.BUNDLE.getString("EDIT")+" Reporter");
//        }
//
//    }
//
//    @Override
//    public void setComponentModel(Object componentModel) {
//        reporter = (Scenario.Reporting.Reporter) componentModel;
//
//        labelReporterClass.setText(reporter.getClazz());
//        FontMetrics fontMetrics = labelReporterClass.getFontMetrics(labelReporterClass.getFont());
//        labelReporterClassWidth = fontMetrics.stringWidth(labelReporterClass.getText());
//
//        enabledCircle.setState(reporter.isEnabled());
//
//        requiredWidth = Short.MAX_VALUE;
//        panelDestinations.updateComponents();
//    }
//
//    @Override
//    public Object getComponentModel() {
//        return reporter;
//    }
//
//    @Override
//    public void updateColors() {
//        setBackground(ColorComponents.getColor(ColorType.REPORTER_BACKGROUND));
//        Color foregroundColor = ColorComponents.getColor(ColorType.REPORTER_FOREGROUND);
//        setForeground(foregroundColor);
//        labelReporterClass.setForeground(foregroundColor);
//        enabledCircle.setForeground(foregroundColor);
//
//    }
//
//    @Override
//    public void setState(boolean state) {
//        reporter.setEnabled(state);
//        this.setComponentModel(reporter);
//        this.commitChanges((state) ? Messages.BUNDLE.getString("ENABLE") + " Reporter" :
//                Messages.BUNDLE.getString("DISABLE") + " Reporter");
//    }
//
//    @Override
//    public List<AbstractComponentGUI> getChildrenModels() {
//        List<AbstractComponentGUI> childrenAsGUI = new ArrayList<>();
//        for (Scenario.Reporting.Reporter.Destination destination : reporter.getDestination()) {
//            DestinationGUI destinationGUI = new DestinationGUI(this);
//            destinationGUI.setComponentModel(destination);
//            childrenAsGUI.add(destinationGUI);
//        }
//        return childrenAsGUI;
//    }
//
//    @Override
//    public void setChildrenFromModels(List<AbstractComponentGUI> childrenAsGUI) {
//        List<Scenario.Reporting.Reporter.Destination> destinations = new ArrayList<>();
//        for (AbstractComponentGUI c : childrenAsGUI){
//            destinations.add((Scenario.Reporting.Reporter.Destination) c.getComponentModel());
//        }
//        reporter.getDestination().clear();
//        reporter.getDestination().addAll(destinations);
//        setComponentModel(reporter);
//        commitChanges("Reporter: Destinations " + Messages.BUNDLE.getString("REORDER"));
//    }
//
//    /*TODO destination chceme v riadku ak je to mozne !!!*/
//    @Override
//    public Dimension getMinimumSize() {
//        int panelMinWidth = panelDestinations.getMinimumSize().width;
//        int width = (panelMinWidth+20 > labelReporterClassWidth+30+20) ? panelMinWidth+20 : labelReporterClassWidth+30+20;
//        return new Dimension(width,panelDestinations.getMinimumSize().height + 50);
//    }
//
//    @Override
//    public Dimension getPreferredSize() {
//        int panelMinWidth = panelDestinations.getMinimumSize().width;
//        int width = (panelMinWidth+20 > labelReporterClassWidth+30+20) ? panelMinWidth+20 : labelReporterClassWidth+30+20;
//        return new Dimension(width,panelDestinations.getMinimumSize().height + 50);
//    }
//
//    @Override
//    public Dimension getMaximumSize() {
//        int panelMinWidth = panelDestinations.getMinimumSize().width;
//        int width = (panelMinWidth+20 > labelReporterClassWidth+30+20) ? panelMinWidth+20 : labelReporterClassWidth+30+20;
//        return new Dimension(width,panelDestinations.getMinimumSize().height + 50);
//    }


}
