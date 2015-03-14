package org.perfcake.pc4idea.editor.gui;

import com.intellij.icons.AllIcons;
import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.Activatable;
import org.perfcake.pc4idea.editor.Messages;
import org.perfcake.pc4idea.editor.ScenarioDialogEditor;
import org.perfcake.pc4idea.editor.colors.ColorComponents;
import org.perfcake.pc4idea.editor.colors.ColorType;
import org.perfcake.pc4idea.editor.editors.DestinationEditor;
import org.perfcake.pc4idea.editor.editors.PeriodEditor;
import org.perfcake.pc4idea.editor.editors.PropertyEditor;
import org.perfcake.pc4idea.editor.swing.JEnabledCircle;
import org.perfcake.reporting.destinations.Destination;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stanislav Kaleta on 3/11/15.
 */
public class DestinationGUI extends AbstractComponentGUI implements Activatable {
    private Scenario.Reporting.Reporter.Destination destination;
    private AbstractComponentGUI parent;

    private JLabel destinationAttr;
    private JEnabledCircle enabledCircle;

    private Dimension destinationSize = new Dimension(40, 40);

    public DestinationGUI(AbstractComponentGUI parent) {
        super(parent.getDefaultActionMap());
        this.parent = parent;
        initComponents();
        updateColors();
    }

    private void initComponents() {
        destinationAttr = new JLabel("-");
        destinationAttr.setFont(new Font(destinationAttr.getFont().getName(), 0, 15));

        enabledCircle = new JEnabledCircle(this);

        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        this.add(destinationAttr);
        this.add(enabledCircle);

        layout.putConstraint(SpringLayout.NORTH, enabledCircle,
                10,
                SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.WEST, enabledCircle,
                10,
                SpringLayout.WEST, this);

        layout.putConstraint(SpringLayout.NORTH, destinationAttr,
                10,
                SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.WEST,destinationAttr,
                5,
                SpringLayout.EAST,enabledCircle);

        this.addMouseListener(new MouseAdapter() {
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

        this.getActionMap().put("ADDPE", new AbstractAction(Messages.BUNDLE.getString("ADD")+" Period", AllIcons.General.Add) {
            @Override
            public void actionPerformed(ActionEvent e) {
                PeriodEditor periodEditor = new PeriodEditor();
                ScenarioDialogEditor dialog = new ScenarioDialogEditor(periodEditor);
                dialog.show();
                if (dialog.getExitCode() == 0) {
                    Scenario.Reporting.Reporter.Destination.Period period = periodEditor.getPeriod();
                    DestinationGUI.this.destination.getPeriod().add(period);
                    DestinationGUI.this.setComponentModel(destination);
                    DestinationGUI.this.commitChanges(Messages.BUNDLE.getString("ADD") + " Period");
                }
            }
        });
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.SHIFT_MASK), "ADDPE");

        this.getActionMap().put("ADDP", new AbstractAction(Messages.BUNDLE.getString("ADD")+" Property", AllIcons.General.Add) {
            @Override
            public void actionPerformed(ActionEvent e) {
                PropertyEditor editor = new PropertyEditor();
                ScenarioDialogEditor dialog = new ScenarioDialogEditor(editor);
                dialog.show();
                if (dialog.getExitCode() == 0) {
                    Property property = editor.getProperty();
                    DestinationGUI.this.destination.getProperty().add(property);
                    DestinationGUI.this.setComponentModel(destination);
                    DestinationGUI.this.commitChanges(Messages.BUNDLE.getString("ADD") + " Property");
                }
            }
        });
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.SHIFT_MASK), "ADDP");

        this.getActionMap().put("ENABLE", new AbstractAction(Messages.BUNDLE.getString("ENABLE")+" Destination", AllIcons.Debugger.Db_muted_verified_breakpoint) {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!DestinationGUI.this.destination.isEnabled()) {
                    DestinationGUI.this.destination.setEnabled(true);
                    DestinationGUI.this.setComponentModel(destination);
                    DestinationGUI.this.commitChanges(Messages.BUNDLE.getString("ENABLE") + " Destination");
                }
            }
        });
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.SHIFT_MASK), "ENABLE");

        this.getActionMap().put("DISABLE", new AbstractAction(Messages.BUNDLE.getString("DISABLE")+" Destination", AllIcons.Debugger.MuteBreakpoints) {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (DestinationGUI.this.destination.isEnabled()) {
                    DestinationGUI.this.destination.setEnabled(false);
                    DestinationGUI.this.setComponentModel(destination);
                    DestinationGUI.this.commitChanges(Messages.BUNDLE.getString("DISABLE") + " Destination");
                }
            }
        });
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.SHIFT_MASK), "DISABLE");

        this.getActionMap().put("EDIT", new AbstractAction(Messages.BUNDLE.getString("EDIT")+" Destination", AllIcons.Actions.Edit) {
            @Override
            public void actionPerformed(ActionEvent e) {
                DestinationGUI.this.openEditor();
            }
        });
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.SHIFT_MASK), "EDIT");

        this.getActionMap().put("DEL", new AbstractAction(Messages.BUNDLE.getString("DEL")+" Destination", AllIcons.Actions.Delete) {
            @Override
            public void actionPerformed(ActionEvent e) {
                Scenario.Reporting.Reporter reporter = (Scenario.Reporting.Reporter) parent.getComponentModel();
                Scenario.Reporting.Reporter.Destination destinationToDel = null;
                for (Scenario.Reporting.Reporter.Destination d : reporter.getDestination()) {
                    if (d.equals(DestinationGUI.this.destination)) {
                        destinationToDel = d;
                    }
                }
                reporter.getDestination().remove(destinationToDel);
                parent.setComponentModel(reporter);
                DestinationGUI.this.commitChanges(Messages.BUNDLE.getString("DEL") + " Destination");
            }
        });
        getInputMap().put(KeyStroke.getKeyStroke("DELETE"), "DEL");
    }

    @Override
    protected List<JMenuItem> getMenuItems() {
        List<JMenuItem> menuItems = new ArrayList<>();

        JMenuItem addPeriodItem = new JMenuItem();
        addPeriodItem.setAction(this.getActionMap().get("ADDPE"));
        addPeriodItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.SHIFT_MASK));
        menuItems.add(addPeriodItem);

        JMenuItem addPropertyItem = new JMenuItem();
        addPropertyItem.setAction(this.getActionMap().get("ADDP"));
        addPropertyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.SHIFT_MASK));
        menuItems.add(addPropertyItem);

        JMenuItem enabledDisabledItem = new JMenuItem();
        enabledDisabledItem.setAction((destination.isEnabled()) ? this.getActionMap().get("DISABLE") :
                this.getActionMap().get("ENABLE"));
        enabledDisabledItem.setAccelerator((destination.isEnabled()) ? KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.SHIFT_MASK) :
                KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.SHIFT_MASK));
        menuItems.add(enabledDisabledItem);

        JMenuItem editItem = new JMenuItem();
        editItem.setAction(this.getActionMap().get("EDIT"));
        editItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.SHIFT_MASK));
        menuItems.add(editItem);

        JMenuItem deleteDestinationitem = new JMenuItem();
        deleteDestinationitem.setAction(this.getActionMap().get("DEL"));
        deleteDestinationitem.setAccelerator(KeyStroke.getKeyStroke("DELETE"));
        menuItems.add(deleteDestinationitem);

        return menuItems;
    }

    @Override
    protected void performImport(String transferredData) {
        // not used
    }

    @Override
    protected void openEditor() {
        DestinationEditor editor = new DestinationEditor();
        editor.setDestination(destination);
        ScenarioDialogEditor dialog = new ScenarioDialogEditor(editor);
        dialog.show();
        if (dialog.getExitCode() == 0){
            setComponentModel(editor.getDestination());
            this.commitChanges(Messages.BUNDLE.getString("EDIT")+" Destination");
        }
    }

    @Override
    public void setComponentModel(Object componentModel) {
        destination = (Scenario.Reporting.Reporter.Destination) componentModel;
        destinationAttr.setText(destination.getClazz());
        FontMetrics fontMetrics = destinationAttr.getFontMetrics(destinationAttr.getFont());
        destinationSize.width = fontMetrics.stringWidth(destinationAttr.getText()) + 25 + 20;

        enabledCircle.setState(destination.isEnabled());
    }

    @Override
    public Object getComponentModel() {
        return destination;
    }

    @Override
    public void updateColors() {
        setBackground(ColorComponents.getColor(ColorType.DESTINATION_BACKGROUND));
        Color foregroundColor = ColorComponents.getColor(ColorType.DESTINATION_FOREGROUND);
        setForeground(foregroundColor);
        destinationAttr.setForeground(foregroundColor);
        enabledCircle.setForeground(foregroundColor);
    }

    @Override
    public void setState(boolean state) {
        destination.setEnabled(state);
        this.setComponentModel(destination);
        this.commitChanges((state) ? Messages.BUNDLE.getString("ENABLE") + " Destination" :
                Messages.BUNDLE.getString("DISABLE") + " Destination");

    }

    @Override
    public Dimension getMinimumSize(){
        return destinationSize;
    }

    @Override
    public Dimension getPreferredSize(){
        return destinationSize;
    }

    @Override
    public Dimension getMaximumSize(){
        return destinationSize;
    }
}
