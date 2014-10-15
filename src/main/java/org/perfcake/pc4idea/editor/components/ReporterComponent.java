package org.perfcake.pc4idea.editor.components;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;
import org.perfcake.model.Scenario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 15.10.2014
 * To change this template use File | Settings | File Templates.
 */
public class ReporterComponent extends JComponent {
    private final String TITLE ="Reporter Editor";
    private final int HEIGHT = 40;
    private final Color color;
    private final Project project;

    private JPanel panelEditor;
    private Scenario.Reporting.Reporter reporter;

    private boolean hasDestination;
    private int width;
    private JLabel reporterAttr;
   /*TODO private EnabledComponent reporterEnabled;*/
   //private JScrollPane scrollPaneProperties;/*TODO*/
    private JPanel panelDestinations;

    public ReporterComponent(Project project, Color color){
        this.project = project;
        this.color = color;
        hasDestination = false;
        setMinimumSize(new Dimension(0,50));
        setMaximumSize(new Dimension(Integer.MAX_VALUE,50));

        initComponents();

        panelEditor = initPanelEditor();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!hasDestination) {
            g.setFont(reporterAttr.getFont());
            width = g.getFontMetrics().stringWidth(reporterAttr.getText()) + 30/* +20 width of EnabledC. */;
            setComponentSize();
        }

        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(color);
        if (hasDestination){
            g2D.drawRoundRect(4, 4, this.getWidth() - 8, this.getHeight() - 8, 20, 20);
        } else {
            g2D.drawRoundRect(4, 4, width - 8, HEIGHT - 8, 20, 20);
        }


    }

    private void initComponents(){
        reporterAttr = new JLabel("ReporterClas");
        reporterAttr.setFont(new Font(reporterAttr.getFont().getName(),0,15));
        reporterAttr.setForeground(color);
        repaint();

        panelDestinations = new JPanel();
        panelDestinations.setLayout(new BoxLayout(panelDestinations,BoxLayout.X_AXIS));/*TODO layout*/
        panelDestinations.setMinimumSize(new Dimension(140/*400/2-2*15*-2*15*/,0));
        panelDestinations.setMaximumSize(new Dimension(Integer.MAX_VALUE,0));
        panelDestinations.setOpaque(false);

        this.setOpaque(false);
        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getButton() == MouseEvent.BUTTON1) {
                    if (evt.getClickCount() == 2) {
                        ComponentEditor editor = new ComponentEditor(TITLE,panelEditor);
                        editor.show();
                        if (editor.getExitCode() == 0) {
                            /*applyChanges();*/
                        }
                    }
                }
            }
        });

        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        this.add(reporterAttr);
        this.add(panelDestinations);

        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, reporterAttr,
                0,
                SpringLayout.HORIZONTAL_CENTER, this);
        layout.putConstraint(SpringLayout.NORTH, reporterAttr,
                10,
                SpringLayout.NORTH, this);

        layout.putConstraint(SpringLayout.WEST, panelDestinations,
                15,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.EAST, this,
                15,
                SpringLayout.EAST, panelDestinations);
        layout.putConstraint(SpringLayout.NORTH, panelDestinations,8,SpringLayout.SOUTH, reporterAttr);
        layout.putConstraint(SpringLayout.SOUTH, this,
                10,
                SpringLayout.SOUTH,panelDestinations);
    }
    private void setComponentSize(){
        this.setSize(new Dimension(width,HEIGHT));          /*TODO ?*/
        this.setMinimumSize(new Dimension(width,HEIGHT));
        this.setPreferredSize(new Dimension(width,HEIGHT)); /*TODO ?*/
        this.setMaximumSize(new Dimension(width,HEIGHT));
        this.validate();
    }

    private JPanel initPanelEditor(){
        JPanel panel = new JPanel();
        /*TODO ...*/
        return panel;
    }

    protected void applyChanges() {
        /*TODO*/
        /*+ repaint?*/
    }

    public void setComponent(Object component) {
        reporter = (Scenario.Reporting.Reporter) component;
        reporterAttr.setText(reporter.getClazz());
        /*reporterEnabled.setEnabled(reporter.isEnabled());*/
        //repaint();

        for (Scenario.Reporting.Reporter.Destination destination : reporter.getDestination()){
            DestinationComponent destinationComponent = new DestinationComponent(project,color);
            destinationComponent.setComponent(destination);
            panelDestinations.add(destinationComponent);
        }



        /*TODO*/ if (reporter.getDestination().size() > 0) { /* TODO temp n=1*/
            hasDestination = true;
            setMinimumSize(new Dimension(0, 50 + 40));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 50 + 40)); /*TODO base=50 + 40*n n=number rows*/
                                     /*TODO podla Dest. nastavit min/maxsize x*/
            panelDestinations.setMinimumSize(new Dimension(140, 40));
            panelDestinations.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));  /*TODO 0 or 40*n -//-*/
        }  else {
            hasDestination = false;
        }
        //panelProperties.validate();TODO if needed

    }
    public Object getComponent() {
        return reporter;
    }

    private class ComponentEditor extends DialogWrapper {
        private JPanel background;

        public ComponentEditor(String title, JPanel background){
            super(project, false);
            setTitle(title);
            this.background = background;
            this.setResizable(true);
//            setPreferredSize(new Dimension(200,200));
//            setSize(200,200); /*TODO not working :D*/
            this.setHorizontalStretch(4.0f); /*TODO size??*/
            this.setVerticalStretch(4.0f);
            init();
        }
        @Nullable
        @Override
        protected JComponent createCenterPanel() {
            return background;
        }
    }



}
