package org.perfcake.pc4idea.editor.gui;

import com.intellij.openapi.project.Project;
import org.perfcake.pc4idea.editor.components.ComponentEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 28.9.2014
 */
public abstract class AbstractPanel extends JPanel {
    private final Project project;

    public AbstractPanel(Project project) {
        super();
        this.project = project;
        this.setOpaque(false);

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
                     /*TODO right click -> popup menu*/
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
