package org.perfcake.pc4idea.api.editor.swing;

import org.perfcake.pc4idea.api.editor.actions.ActionType;
import org.perfcake.pc4idea.api.editor.swing.plaf.EnabledCircleUI;
import org.perfcake.pc4idea.impl.editor.actions.ToggleAction;

import javax.swing.*;
import javax.swing.plaf.PanelUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by miron on 21. 11. 2014.
 */
public class JEnabledCircle extends JPanel {
    private static final String uiClassID = "EnabledCircleUI";

    private Color enabledColor = Color.getHSBColor(120/360f,0.75f,0.75f);
    private Color disabledColor = Color.getHSBColor(0/360f,0.75f,0.75f);

    private Dimension enabledSize = new Dimension(15,20);

    public JEnabledCircle(ToggleAction action) {
        getActionMap().put(ActionType.TOGGLE, action);
        setOpaque(Boolean.FALSE);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JEnabledCircle.this.getActionMap().get(ActionType.TOGGLE).actionPerformed(null);
                }
            }
        });
    }

    public void setState(boolean state){
        if (state){
            this.setBackground(enabledColor);
        } else {
            this.setBackground(disabledColor);
        }
        repaint();

        ((ToggleAction) getActionMap().get(ActionType.TOGGLE)).setCurrentState(state);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    @Override
    public String getUIClassID() {
        return uiClassID;
    }

    @Override
    public EnabledCircleUI getUI() {
        return (EnabledCircleUI) ui;
    }

    @Override
    public void setUI(PanelUI ui) {
        super.setUI(ui);
    }

    @Override
    public void updateUI() {
        if (UIManager.get(getUIClassID()) != null) {
            setUI((EnabledCircleUI) UIManager.getUI(this));
        } else {
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
