package org.perfcake.pc4idea.editor.gui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;
import org.perfcake.model.Property;
import org.perfcake.pc4idea.editor.wizard.PropertyEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 12.10.2014
 */
public class PropertyComponent extends JPanel {
    private final String TITLE ="Property Editor";
    private final Project project;
    private final Color propertyColor;
    private final int id;

    private PropertyEditor propertyEditor;
    private Property property;
    private SenderPanel.PanelProperties.SenderEvent senderEvent;

    private JLabel propertyAttr;
    private Dimension propertySize;

    public PropertyComponent(Project project, Color ancestorColor, int id, SenderPanel.PanelProperties.SenderEvent senderEvent){
        this.project = project;
        this.propertyColor = ancestorColor;
        this.id = id;
        this.senderEvent = senderEvent;

        initComponents();
    }

    private void initComponents() {
        propertyAttr = new JLabel("-");
        propertyAttr.setFont(new Font(propertyAttr.getFont().getName(), 0, 15));
        propertyAttr.setForeground(propertyColor);
        propertySize = new Dimension(40, 40);

        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        this.add(propertyAttr);

        layout.putConstraint(SpringLayout.NORTH, propertyAttr,
                10,
                SpringLayout.NORTH, this);

        layout.putConstraint(SpringLayout.WEST, propertyAttr,
                15,
                SpringLayout.WEST, this);

        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                if (event.getButton() == MouseEvent.BUTTON1) {
                    if (event.getClickCount() == 2) {
                        propertyEditor = new PropertyEditor();
                        propertyEditor.setProperty(property);
                        ComponentEditor editor = new ComponentEditor(TITLE, propertyEditor);
                        editor.show();
                        if (editor.getExitCode() == 0) {
                            setProperty(propertyEditor.getProperty());
                            senderEvent.saveProperty(id);
                        }
                    }
                } /*TODO right click -> popup menu*/
            }
            @Override
            public void mousePressed(MouseEvent e) {
                ((JPanel)e.getComponent().getAccessibleContext().getAccessibleParent()).dispatchEvent(e);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                ((JPanel)e.getComponent().getAccessibleContext().getAccessibleParent()).dispatchEvent(e);
            }
            @Override
            public void mouseReleased(MouseEvent e){
                ((JPanel)e.getComponent().getAccessibleContext().getAccessibleParent()).dispatchEvent(e);
            }
        });




    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(propertyColor);
        g2D.drawRoundRect(4, 4, this.getWidth() - 8, this.getHeight() - 8, 20, 20);
    }

    public void setProperty(Property p) {
        property = p;
        propertyAttr.setText(property.getName()+" : "+property.getValue());
        FontMetrics fontMetrics = propertyAttr.getFontMetrics(propertyAttr.getFont());
        propertySize.width = fontMetrics.stringWidth(propertyAttr.getText()) + 30;
    }

    public Property getProperty() {
        return property;
    }

    public int getId(){
        return id;
    }

    @Override
    public Dimension getMinimumSize(){
        return propertySize;
    }

    @Override
    public Dimension getPreferredSize(){
        return propertySize;
    }

    @Override
    public Dimension getMaximumSize(){
        return propertySize;
    }

    private class ComponentEditor extends DialogWrapper {
        private JPanel centerPanel;

        public ComponentEditor(String title, JPanel centerPanel){
            super(project, false);
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
