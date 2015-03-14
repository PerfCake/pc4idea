package org.perfcake.pc4idea.editor.gui;

import com.intellij.icons.AllIcons;
import com.intellij.util.ui.UIUtil;
import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.Messages;
import org.perfcake.pc4idea.editor.ScenarioDialogEditor;
import org.perfcake.pc4idea.editor.colors.ColorComponents;
import org.perfcake.pc4idea.editor.colors.ColorType;
import org.perfcake.pc4idea.editor.editors.PropertyEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stanislav Kaleta on 3/7/15.
 */
public class PropertyGUI extends AbstractComponentGUI {
    private Property property;
    private AbstractComponentGUI parent;
    private JLabel propertyAttr;

    private Dimension propertySize = new Dimension(40, 40);

    public PropertyGUI(AbstractComponentGUI parent) {
        super(parent.getDefaultActionMap());
        this.parent = parent;
        initComponents();
        updateColors();
    }

    private void initComponents() {
        propertyAttr = new JLabel("-");
        propertyAttr.setFont(new Font(propertyAttr.getFont().getName(), 0, 15));

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



        this.getActionMap().put("EDIT", new AbstractAction(Messages.BUNDLE.getString("EDIT")+" Property", AllIcons.Actions.Edit) {
            @Override
            public void actionPerformed(ActionEvent e) {
                PropertyGUI.this.openEditor();
            }
        });
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.SHIFT_MASK), "EDIT");

        this.getActionMap().put("DEL", new AbstractAction(Messages.BUNDLE.getString("DEL")+" Property", AllIcons.Actions.Delete) {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (parent instanceof SenderGUI){
                    Scenario.Sender sender = (Scenario.Sender) parent.getComponentModel();
                    Property propertyToDel = null;
                    for ( Property p : sender.getProperty()){
                        if (p.equals(PropertyGUI.this.property)){
                            propertyToDel = p;
                        }
                    }
                    sender.getProperty().remove(propertyToDel);
                    parent.setComponentModel(sender);
                }
                if (parent instanceof PropertiesGUI){
                    Scenario.Properties properties = (Scenario.Properties) parent.getComponentModel();
                    Property propertyToDel = null;
                    for ( Property p : properties.getProperty()){
                        if (p.equals(PropertyGUI.this.property)){
                            propertyToDel = p;
                        }
                    }
                    properties.getProperty().remove(propertyToDel);
                    parent.setComponentModel(properties);
                }
                PropertyGUI.this.commitChanges(Messages.BUNDLE.getString("DEL")+" Property");
            }
        });
        getInputMap().put(KeyStroke.getKeyStroke("DELETE"), "DEL");
    }

    @Override
    protected List<JMenuItem> getMenuItems() {
        List<JMenuItem> menuItems = new ArrayList<>();

        JMenuItem editItem = new JMenuItem();
        editItem.setAction(this.getActionMap().get("EDIT"));
        editItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.SHIFT_MASK));
        menuItems.add(editItem);

        JMenuItem deletePropertyItem = new JMenuItem();
        deletePropertyItem.setAction(this.getActionMap().get("DEL"));
        deletePropertyItem.setAccelerator(KeyStroke.getKeyStroke("DELETE"));
        menuItems.add(deletePropertyItem);

        return menuItems;
    }

    @Override
    protected void performImport(String transferredData) {
        // not used
    }

    @Override
    protected void openEditor() {
        PropertyEditor editor = new PropertyEditor();
        editor.setProperty(property);
        ScenarioDialogEditor dialog = new ScenarioDialogEditor(editor);
        dialog.show();
        if (dialog.getExitCode() == 0){
            if (parent instanceof SenderGUI){
                Scenario.Sender sender = (Scenario.Sender) parent.getComponentModel();
                Property propertyToReplace = null;
                for ( Property p : sender.getProperty()){
                    if (p.equals(PropertyGUI.this.property)){
                        propertyToReplace = p;
                    }
                }
                sender.getProperty().set(sender.getProperty().indexOf(propertyToReplace), editor.getProperty());
                parent.setComponentModel(sender);
            }
            if (parent instanceof PropertiesGUI){
                Scenario.Properties properties = (Scenario.Properties) parent.getComponentModel();
                Property propertyToReplace = null;
                for ( Property p : properties.getProperty()){
                    if (p.equals(PropertyGUI.this.property)){
                        propertyToReplace = p;
                    }
                }
                properties.getProperty().set(properties.getProperty().indexOf(propertyToReplace),editor.getProperty());
                parent.setComponentModel(properties);
            }
            this.setComponentModel(editor.getProperty());
            this.commitChanges(Messages.BUNDLE.getString("EDIT")+" Property");
        }
    }

    @Override
    public void setComponentModel(Object componentModel) {
        property = (Property) componentModel;
        propertyAttr.setText(property.getName()+" : "+property.getValue());
        FontMetrics fontMetrics = propertyAttr.getFontMetrics(propertyAttr.getFont());
        propertySize.width = fontMetrics.stringWidth(propertyAttr.getText()) + 30;
    }

    @Override
    public Object getComponentModel() {
        return property;
    }

    @Override
    public void updateColors() {
        setBackground(ColorComponents.getColor(ColorType.PROPERTIES_BACKGROUND));
        Color foregroundColor = ColorComponents.getColor(ColorType.PROPERTIES_FOREGROUND);
        setForeground(foregroundColor);
        propertyAttr.setForeground(foregroundColor);
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
