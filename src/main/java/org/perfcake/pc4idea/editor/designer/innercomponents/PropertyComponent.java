package org.perfcake.pc4idea.editor.designer.innercomponents;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.ui.Messages;
import org.perfcake.model.Property;
import org.perfcake.pc4idea.editor.designer.common.ScenarioDialogEditor;
import org.perfcake.pc4idea.editor.designer.outercomponents.PropertiesPanel;
import org.perfcake.pc4idea.editor.designer.outercomponents.SenderPanel;
import org.perfcake.pc4idea.editor.designer.editors.PropertyEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 12.10.2014
 */
public class PropertyComponent extends JPanel {
    private final Color propertyColor;
    private final int id;

    private PropertyEditor propertyEditor;
    private Property property;
    private SenderPanel.PanelProperties.SenderEvent senderEvent;
    private PropertiesPanel.PanelProperties.PropertiesEvent propertiesEvent;

    private JLabel propertyAttr;
    private JPopupMenu popupMenu;

    private Dimension propertySize;

    public PropertyComponent(Color ancestorColor, int id, SenderPanel.PanelProperties.SenderEvent senderEvent){
        this.propertyColor = ancestorColor;
        this.id = id;
        this.senderEvent = senderEvent;
        propertiesEvent = null;

        this.setOpaque(false);

        initComponents();
    }

    public PropertyComponent(Color ancestorColor, int id, PropertiesPanel.PanelProperties.PropertiesEvent propertiesEvent){

        this.propertyColor = ancestorColor;
        this.id = id;
        this.senderEvent = null;
        this.propertiesEvent = propertiesEvent;

        this.setOpaque(false);

        initComponents();
    }

    private void initComponents() {
        propertyAttr = new JLabel("-");
        propertyAttr.setFont(new Font(propertyAttr.getFont().getName(), 0, 15));
        propertyAttr.setForeground(propertyColor);

        propertySize = new Dimension(40, 40);

        JMenuItem itemOpenEditor = new JMenuItem("Open Editor");
        itemOpenEditor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                propertyEditor = new PropertyEditor();
                propertyEditor.setProperty(property);
                ScenarioDialogEditor dialog = new ScenarioDialogEditor(propertyEditor);
                dialog.show();
                if (dialog.getExitCode() == 0) {
                    setProperty(propertyEditor.getProperty());
                    if (senderEvent != null) {
                        senderEvent.saveProperty(id);
                    }
                    if (propertiesEvent != null) {
                        propertiesEvent.saveProperty(id);
                    }
                }
            }
        });
        JMenuItem itemDelete = new JMenuItem("Delete");
        itemDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = Messages.showYesNoDialog("Are you sure you want to delete this Property?", "Delete Property", AllIcons.Actions.Delete);
                if (result == 0) {
                    if (senderEvent != null) {
                        senderEvent.deleteProperty(id);
                    }
                    if (propertiesEvent != null) {
                        propertiesEvent.deleteProperty(id);
                    }
                }
            }
        });

        popupMenu = new JPopupMenu();
        popupMenu.add(itemOpenEditor);
        popupMenu.add(new JPopupMenu.Separator());
        popupMenu.add(itemDelete);

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
            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getButton() == MouseEvent.BUTTON1) {
                    if (event.getClickCount() == 2) {
                        propertyEditor = new PropertyEditor();
                        propertyEditor.setProperty(property);
                        ScenarioDialogEditor dialog = new ScenarioDialogEditor(propertyEditor);
                        dialog.show();
                        if (dialog.getExitCode() == 0) {
                            setProperty(propertyEditor.getProperty());
                            if (senderEvent != null) {senderEvent.saveProperty(id);}
                            if (propertiesEvent != null) {propertiesEvent.saveProperty(id);}
                        }
                    }
                }
                if (event.getButton() == MouseEvent.BUTTON3) {
                    popupMenu.show(PropertyComponent.this, event.getX(), event.getY());
                }
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
}
