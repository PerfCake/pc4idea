package org.perfcake.pc4idea.editor.designer.innercomponents;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 15.10.2014
 */
public class EnabledComponent extends JComponent {
    private final Color color;
    private boolean state;

    public EnabledComponent(Color color){
        this.color = color;
        state = true;

        this.setMinimumSize(new Dimension(15,20));
        this.setPreferredSize(new Dimension(15,20));
        this.setMaximumSize(new Dimension(15,20));
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(color);
        g2D.fillOval(2,5,11,11);
        if (state) {
            g2D.setColor(Color.getHSBColor(120/360f,1f,1f));
        } else {
            g2D.setColor(Color.getHSBColor(0/360f,1f,1f));
        }
        g2D.fillOval(3,6,9,9);
    }

    public void setState(boolean state){
        this.state = state;
        repaint();
    }
}
