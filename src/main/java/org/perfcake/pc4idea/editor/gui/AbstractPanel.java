package org.perfcake.pc4idea.editor.gui;

import org.perfcake.pc4idea.editor.components.ComponentEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 28.9.2014
 */
public abstract class AbstractPanel extends JPanel {
    private JPopupMenu popupMenu;
    private JMenuItem popupOpenEditor;

    public AbstractPanel() {
        this.setOpaque(false);

        popupOpenEditor = new JMenuItem();
        popupOpenEditor.setText("Open Editor");
        popupOpenEditor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ComponentEditor editor = new ComponentEditor(getEditorTitle(), getEditorPanel());
                editor.show();
                if (editor.getExitCode() == 0) {
                    applyChanges();
                }
            }
        });

        popupMenu = new JPopupMenu();
        popupMenu.add(popupOpenEditor);
        popupMenu.add(new JPopupMenu.Separator());
        /*TODO dalsie?*/


        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getButton() == MouseEvent.BUTTON1) {
                    if (evt.getClickCount() == 2) {
                        ComponentEditor editor = new ComponentEditor(getEditorTitle(), getEditorPanel());
                        editor.show();
                        if (editor.getExitCode() == 0) {
                            applyChanges();
                        }
                    }
                }
                if (evt.getButton() == MouseEvent.BUTTON3) {
                     popupMenu.show(AbstractPanel.this, evt.getX(), evt.getY());
                }
            }
        });

    }

    protected abstract Color getColor();
    protected abstract String getEditorTitle();
    protected abstract JPanel getEditorPanel();
    protected abstract void applyChanges();
    public abstract void setComponent(Object component);
    public abstract Object getComponent();

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(getColor());
        g2D.drawRoundRect(4, 4, this.getWidth() - 8, this.getHeight() - 8, 20, 20);
    }
}
