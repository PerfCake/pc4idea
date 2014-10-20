package org.perfcake.pc4idea.editor.components;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;
import org.perfcake.model.Property;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 12.10.2014
 * To change this template use File | Settings | File Templates.
 */
public class PropertyComponent extends JComponent {
    private final String TITLE ="Property Editor";
    private final int HEIGHT = 40;
    private final Color color;  /*TODO final=rerun or notfinal=setter */
    private final Project project;

    private JPanel panelEditor;
    private Property property;

    private int width;
    private JLabel propertyAttr;

    public PropertyComponent(Project project, Color color){ /*editor-depend.*/
        this.project = project;
        this.color = color;

        init();

        panelEditor = initPanelEditor();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(propertyAttr.getFont());
        width = g.getFontMetrics().stringWidth(propertyAttr.getText()) + 30;
        setComponentSize();

        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(color);
        g2D.drawRoundRect(4, 4, width - 8, HEIGHT - 8, 20, 20);
    }

    private void init(){
        propertyAttr = new JLabel();
        propertyAttr.setText("name : value");
        propertyAttr.setFont(new Font(propertyAttr.getFont().getName(), 0, 15));
        propertyAttr.setForeground(color);
        repaint();

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
        this.add(propertyAttr);

        layout.putConstraint(SpringLayout.NORTH, propertyAttr,
                10,
                SpringLayout.NORTH, this);

        layout.putConstraint(SpringLayout.WEST, propertyAttr,
                15,
                SpringLayout.WEST, this);

    }
    private void setComponentSize() {
        this.setSize(new Dimension(width, HEIGHT));          /*TODO ?*/
        this.setMinimumSize(new Dimension(width, HEIGHT));
        this.setPreferredSize(new Dimension(width, HEIGHT));  /*TODO ?*/
        this.setMaximumSize(new Dimension(width, HEIGHT));
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
        property = (Property) component;
        propertyAttr.setText(property.getName()+" : "+property.getValue());
        repaint();
    }
    public Object getComponent() {
        return property;
    }


    private class ComponentEditor extends DialogWrapper {
        private JPanel centerPanel;

        public ComponentEditor(String title, JPanel centerPanel){
            super(project, false);
            setTitle(title);
            this.centerPanel = centerPanel;
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
            return centerPanel;
        }
    }
}
