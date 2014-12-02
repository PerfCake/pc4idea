package org.perfcake.pc4idea.editor.designer.innercomponents;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.ui.Messages;
import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.designer.common.ScenarioDialogEditor;
import org.perfcake.pc4idea.editor.designer.editors.DestinationEditor;
import org.perfcake.pc4idea.editor.designer.editors.PeriodEditor;
import org.perfcake.pc4idea.editor.designer.editors.PropertyEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 15.10.2014
 */
public class DestinationComponent extends JPanel {
    private final Color destinationColor;
    private final int id;

    private DestinationEditor destinationEditor;
    private Scenario.Reporting.Reporter.Destination destination;
    private ReporterComponent.PanelDestinations.ReporterEvent reporterEvent;

    private JLabel destinationAttr;
    private EnabledComponent destinationEnabled;
    private JPopupMenu popupMenu;
    private JMenuItem itemEnabledDisabled;

    private Dimension destinationSize;

    public DestinationComponent(Color reporterColor, int id, ReporterComponent.PanelDestinations.ReporterEvent reporterEvent){
        this.destinationColor = reporterColor;
        this.id = id;
        this.reporterEvent = reporterEvent;

        this.setOpaque(false);

        initComponents();
    }

    private void initComponents(){
        destinationAttr = new JLabel("-");
        destinationAttr.setFont(new Font(destinationAttr.getFont().getName(), 0, 15));
        destinationAttr.setForeground(destinationColor);

        destinationEnabled = new EnabledComponent(destinationColor, new DestinationEvent(),null);

        destinationSize = new Dimension(40, 40);

        JMenuItem itemOpenEditor = new JMenuItem("Open Editor");
        itemOpenEditor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                destinationEditor = new DestinationEditor();
                destinationEditor.setDestination(destination);
                ScenarioDialogEditor dialog = new ScenarioDialogEditor(destinationEditor);
                dialog.show();
                if (dialog.getExitCode() == 0) {
                    setDestination(destinationEditor.getDestination());
                    reporterEvent.saveDestination(id);
                }
            }
        });
        JMenuItem itemDelete = new JMenuItem("Delete");
        itemDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = Messages.showYesNoDialog("Are you sure you want to delete this Destination?", "Delete Destination", AllIcons.Actions.Delete);
                if (result == 0) {
                    reporterEvent.deleteDestination(id);
                }
            }
        });
        JMenuItem itemAddPeriod = new JMenuItem("Add Period");
        itemAddPeriod.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PeriodEditor periodEditor = new PeriodEditor();
                ScenarioDialogEditor dialog = new ScenarioDialogEditor(periodEditor);
                dialog.show();
                if (dialog.getExitCode() == 0) {
                    Scenario.Reporting.Reporter.Destination.Period period = periodEditor.getPeriod();
                    destination.getPeriod().add(period);
                    DestinationComponent.this.setDestination(destination);
                    reporterEvent.saveDestination(id);
                }
            }
        });
        JMenuItem itemAddProperty = new JMenuItem("Add Property");
        itemAddProperty.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PropertyEditor propertyEditor = new PropertyEditor();
                ScenarioDialogEditor dialog = new ScenarioDialogEditor(propertyEditor);
                dialog.show();
                if (dialog.getExitCode() == 0) {
                    Property property = propertyEditor.getProperty();
                    destination.getProperty().add(property);
                    DestinationComponent.this.setDestination(destination);
                    reporterEvent.saveDestination(id);
                }
            }
        });
        itemEnabledDisabled = new JMenuItem("-");
        itemEnabledDisabled.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                destination.setEnabled(!destination.isEnabled());
                DestinationComponent.this.setDestination(destination);
                reporterEvent.saveDestination(id);
            }
        });

        popupMenu = new JPopupMenu();
        popupMenu.add(itemOpenEditor);
        popupMenu.add(new JPopupMenu.Separator());
        popupMenu.add(itemEnabledDisabled);
        popupMenu.add(itemAddPeriod);
        popupMenu.add(itemAddProperty);
        popupMenu.add(new JPopupMenu.Separator());
        popupMenu.add(itemDelete);


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

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getButton() == MouseEvent.BUTTON1) {
                    if (event.getClickCount() == 2) {
                        destinationEditor = new DestinationEditor();
                        destinationEditor.setDestination(destination);
                        ScenarioDialogEditor dialog = new ScenarioDialogEditor(destinationEditor);
                        dialog.show();
                        if (dialog.getExitCode() == 0) {
                            setDestination(destinationEditor.getDestination());
                            reporterEvent.saveDestination(id);
                        }
                    }
                }
                if (event.getButton() == MouseEvent.BUTTON3) {
                    popupMenu.show(DestinationComponent.this, event.getX(), event.getY());
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
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(destinationColor);
        g2D.drawRoundRect(4, 4, this.getWidth() - 8, this.getHeight() - 8, 20, 20);
    }

    public void setDestination(Scenario.Reporting.Reporter.Destination d) {
        destination = d;
        destinationAttr.setText(destination.getClazz());
        FontMetrics fontMetrics = destinationAttr.getFontMetrics(destinationAttr.getFont());
        destinationSize.width = fontMetrics.stringWidth(destinationAttr.getText()) + 25 + 20;

        destinationEnabled.setState(destination.isEnabled());
        itemEnabledDisabled.setText((destination.isEnabled()) ? "Disable" : "Enable");
    }

    public Scenario.Reporting.Reporter.Destination getDestination() {
        return destination;
    }

    public int getId(){
        return id;
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

    public final class DestinationEvent {
        public void saveEnabled(boolean isEnabled){
            destination.setEnabled(isEnabled);
            DestinationComponent.this.setDestination(destination);
            reporterEvent.saveDestination(id);
        }
    }
}
