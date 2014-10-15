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
public class DestinationComponent extends JComponent {
    private final String TITLE ="Destination Editor";
    private final int HEIGHT = 40;
    private final Color color;
    private final Project project;

    private JPanel panelEditor;
    private Scenario.Reporting.Reporter.Destination destination;

    private int width;
    private JLabel destinationAttr;

    public DestinationComponent(Project project, Color color){
        this.project = project;
        this.color = color;

        init();

        panelEditor = initPanelEditor();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(destinationAttr.getFont());
        width = g.getFontMetrics().stringWidth(destinationAttr.getText()) + 30;
        setComponentSize();

        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(color);
        g2D.drawRoundRect(4, 4, width - 8, HEIGHT - 8, 20, 20);
    }

    private void init(){
        destinationAttr = new JLabel("DestinationClass");
        destinationAttr.setFont(new Font(destinationAttr.getFont().getName(), 0, 15));
        destinationAttr.setForeground(color);
        repaint();/* test-skusit s.ite. na pocet*/

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
        this.add(destinationAttr);

        layout.putConstraint(SpringLayout.NORTH, destinationAttr,
                10,
                SpringLayout.NORTH, this);

        layout.putConstraint(SpringLayout.WEST, destinationAttr,
                15,
                SpringLayout.WEST, this);
    }
    private void setComponentSize(){
        this.setSize(new Dimension(width,HEIGHT));          /*TODO ?*/
        this.setMinimumSize(new Dimension(width,HEIGHT));
        this.setPreferredSize(new Dimension(width,HEIGHT));  /*TODO ?*/
        this.setMaximumSize(new Dimension(width,HEIGHT));
        this.validate();
    }

    private JPanel initPanelEditor() {
        JPanel panel = new JPanel();
            /*TODO*/
        return panel;
    }

    private void applyChenges(){
        /*TODO*/
        /*+ repaint?*/
    }

    public void setComponent(Object component) {
        destination = (Scenario.Reporting.Reporter.Destination) component;
        destinationAttr.setText(destination.getClazz());
        repaint();
    }
    public Object getComponent() {
        return destination;
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
