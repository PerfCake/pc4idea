package org.perfcake.pc4idea.api.editor.swing;

import org.perfcake.pc4idea.api.editor.swing.plaf.EnabledCircleUI;
import org.perfcake.pc4idea.impl.editor.actions.ToggleAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by miron on 21. 11. 2014. + me
 */
public class JEnabledCircle extends JPanel {
    private static final String uiClassID = "EnabledCircleUI";
    private boolean state = false;

    private Color enabledColor = Color.getHSBColor(120/360f,0.75f,0.75f);
    private Color disabledColor = Color.getHSBColor(0/360f,0.75f,0.75f);

    private Dimension enabledSize = new Dimension(15,20);

    public JEnabledCircle(ToggleAction action) {
        setOpaque(Boolean.FALSE);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    action.preActionPerformed(state);
                }
            }
        });
    }

    public void setState(boolean state){
        this.state = state;
        if (state){
            this.setBackground(enabledColor);
        } else {
            this.setBackground(disabledColor);
        }
        repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public String getUIClassID() {
        return uiClassID;
    }

    public EnabledCircleUI getUI() {
        return (EnabledCircleUI) ui;
    }

    public void setUI(EnabledCircleUI ui) {
        super.setUI(ui);
    }

    public void updateUI() {
        if (UIManager.get(getUIClassID()) != null) {
            setUI((EnabledCircleUI) UIManager.getUI(this));
        } else {
            //fallback if UIDefaults table was not set
            setUI(new EnabledCircleUI());
        }
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
