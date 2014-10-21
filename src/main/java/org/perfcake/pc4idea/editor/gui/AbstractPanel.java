package org.perfcake.pc4idea.editor.gui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 28.9.2014
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractPanel extends JPanel {
    private final Project project;

    public AbstractPanel(Project project) {
        super();
        this.project = project;
        this.setOpaque(false);
        //this.setSize(new Dimension(100,100));
        //this.setVisible(true);
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
                } /*TODO right click -> popup menu*/
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


    private class ComponentEditor extends DialogWrapper {
        private JPanel centerPanel;

        public ComponentEditor(String title, JPanel centerPanel){
            super(project, false); /*TODO can be parent?*/
            setTitle(title);
            this.centerPanel = centerPanel;
            this.setResizable(true);
            init();
        }
        @Nullable
        @Override
        protected JComponent createCenterPanel() {
            return centerPanel;
        }
    }

}
