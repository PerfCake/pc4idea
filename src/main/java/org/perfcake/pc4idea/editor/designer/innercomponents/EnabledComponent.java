package org.perfcake.pc4idea.editor.designer.innercomponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 15.10.2014
 */
public class EnabledComponent extends JComponent {
    private final Color color;
    private boolean state;
    private DestinationComponent.DestinationEvent destinationEvent;
    private ReporterComponent.ReporterEvent reporterEvent;

    private Dimension enabledSize;

    public EnabledComponent(Color color, DestinationComponent.DestinationEvent destEvent, ReporterComponent.ReporterEvent repEvent){
        this.color = color;
        this.destinationEvent = destEvent;
        this.reporterEvent = repEvent;
        state = true;

        enabledSize = new Dimension(15,20);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2){
                    state = !state;
                    EnabledComponent.this.repaint();
                    if (destinationEvent != null){
                        destinationEvent.saveEnabled(state);
                    }
                    if (reporterEvent != null){
                        reporterEvent.saveEnabled(state);
                    }
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(color);
        g2D.fillOval(2,5,11,11);
        if (state) {
            g2D.setColor(Color.getHSBColor(120/360f,0.75f,0.75f));
        } else {
            g2D.setColor(Color.getHSBColor(0/360f,0.75f,0.75f));
        }
        g2D.fillOval(3,6,9,9);
    }

    public void setState(boolean state){
        this.state = state;
        repaint();
    }

    @Override
    public Dimension getMinimumSize(){
        return enabledSize;
    }

    @Override
    public Dimension getPreferredSize(){
        return enabledSize;
    }

    @Override
    public Dimension getMaximumSize(){
        return enabledSize;
    }
}
