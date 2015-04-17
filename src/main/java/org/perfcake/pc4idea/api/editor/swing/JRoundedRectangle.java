package org.perfcake.pc4idea.api.editor.swing;

import org.perfcake.pc4idea.api.editor.swing.plaf.RoundedRectangleUI;

import javax.swing.*;
import javax.swing.plaf.PanelUI;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by miron on 15.4.2014. + me
 */
public class JRoundedRectangle extends JPanel {
    private static final String uiClassID = "RoundedRectangleUI";
    private Boolean selected = Boolean.FALSE;

    public JRoundedRectangle() {
        setLayout(new BorderLayout());
        setOpaque(Boolean.FALSE);

        setFocusable(true);
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                select();
            }

            @Override
            public void focusLost(FocusEvent e) {
                deselect();
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                requestFocusInWindow();
            }
        });
    }

    @Override
    public String getUIClassID() {
        return uiClassID;
    }

    @Override
    public RoundedRectangleUI getUI() {
        return (RoundedRectangleUI) ui;
    }

    @Override
    public void setUI(PanelUI ui) {
        super.setUI(ui);
    }

    @Override
    public void updateUI() {
        if (UIManager.get(getUIClassID()) != null) {
            setUI((RoundedRectangleUI) UIManager.getUI(this));
        } else {
            setUI(new RoundedRectangleUI());
        }

    }

    private void select() {
        if (!selected) {
            selected = Boolean.TRUE;
            repaint();
        }
    }

    private void deselect() {
        if (selected) {
            selected = Boolean.FALSE;
            repaint();
        }
    }

    public Boolean isSelected() {
        return selected;
    }


}
