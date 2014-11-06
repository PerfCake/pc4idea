package org.perfcake.pc4idea.editor.components;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.wizard.DestinationEditor;

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
    private final String TITLE ="Destination Editor";
    private final Color destinationColor;
    private final int id;

    private DestinationEditor destinationEditor;
    private Scenario.Reporting.Reporter.Destination destination;
    private ReporterComponent.PanelDestinations.ReporterEvent reporterEvent;

    private JLabel destinationAttr;
    private EnabledIndicator destinationEnabled;
    private JPopupMenu popupMenu;
    private JMenuItem popupOpenEditor;
    private JMenuItem popupDelete;

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

        destinationEnabled = new EnabledIndicator(destinationColor);

        destinationSize = new Dimension(40, 40);

        popupOpenEditor = new JMenuItem("Open Editor");
        popupOpenEditor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                destinationEditor = new DestinationEditor();
                destinationEditor.setDestination(destination);
                ComponentEditor editor = new ComponentEditor(TITLE, destinationEditor);
                editor.show();
                if (editor.getExitCode() == 0) {
                    setDestination(destinationEditor.getDestination());
                    reporterEvent.saveDestination(id);
                }
            }
        });
        popupDelete = new JMenuItem("Delete");
        popupDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reporterEvent.deleteDestination(id);
            }
        });

        popupMenu = new JPopupMenu();
        popupMenu.add(popupOpenEditor);
        popupMenu.add(new JPopupMenu.Separator());
        popupMenu.add(popupDelete);
        /*TODO dalsie?*/

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
                        ComponentEditor editor = new ComponentEditor(TITLE, destinationEditor);
                        editor.show();
                        if (editor.getExitCode() == 0) {
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
}
