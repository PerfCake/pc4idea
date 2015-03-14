package org.perfcake.pc4idea.editor.gui;

import com.intellij.icons.AllIcons;
import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.ChildrenHaveGUI;
import org.perfcake.pc4idea.editor.ComponentsPanel;
import org.perfcake.pc4idea.editor.Messages;
import org.perfcake.pc4idea.editor.ScenarioDialogEditor;
import org.perfcake.pc4idea.editor.colors.ColorComponents;
import org.perfcake.pc4idea.editor.colors.ColorType;
import org.perfcake.pc4idea.editor.editors.PropertyEditor;
import org.perfcake.pc4idea.editor.editors.SenderEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 3.3.2015
 */
public class SenderGUI extends AbstractComponentGUI implements ChildrenHaveGUI {
    private Scenario.Sender sender;

    private JLabel labelSenderClass;
    private ComponentsPanel panelProperties;

    private int labelSenderClassWidth = 0;

    public SenderGUI(ActionMap actionMap){
        super(actionMap);
        initComponents();
        updateColors();
    }

    private void initComponents(){
        labelSenderClass = new JLabel("---");
        labelSenderClass.setFont(new Font(labelSenderClass.getFont().getName(), 0, 15));

        panelProperties = new ComponentsPanel(this);

        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        this.add(labelSenderClass);
        this.add(panelProperties);

        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, labelSenderClass,
                0,
                SpringLayout.HORIZONTAL_CENTER, this);
        layout.putConstraint(SpringLayout.NORTH, labelSenderClass,
                10,
                SpringLayout.NORTH, this);

        layout.putConstraint(SpringLayout.WEST, panelProperties,
                10,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, panelProperties,8,SpringLayout.SOUTH, labelSenderClass);

        this.getActionMap().put("ADDP", new AbstractAction(Messages.BUNDLE.getString("ADD")+" Property", AllIcons.General.Add) {
            @Override
            public void actionPerformed(ActionEvent e) {
                PropertyEditor editor = new PropertyEditor();
                ScenarioDialogEditor dialog = new ScenarioDialogEditor(editor);
                dialog.show();
                if (dialog.getExitCode() == 0) {
                    Property property = editor.getProperty();
                    SenderGUI.this.sender.getProperty().add(property);
                    SenderGUI.this.setComponentModel(sender);
                    SenderGUI.this.commitChanges(Messages.BUNDLE.getString("ADD") + " Property");
                }
            }
        });
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.SHIFT_MASK), "ADDP");

        this.getActionMap().put("EDIT", new AbstractAction(Messages.BUNDLE.getString("EDIT")+" Sender", AllIcons.Actions.Edit) {
            @Override
            public void actionPerformed(ActionEvent e) {
                SenderGUI.this.openEditor();
            }
        });
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.SHIFT_MASK), "EDIT");
    }



    @Override
    protected List<JMenuItem> getMenuItems() {
        List<JMenuItem> menuItems = new ArrayList<>();

        JMenuItem addPropertyItem = new JMenuItem();
        addPropertyItem.setAction(this.getActionMap().get("ADDP"));
        addPropertyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.SHIFT_MASK));
        menuItems.add(addPropertyItem);

        JMenuItem editItem = new JMenuItem();
        editItem.setAction(this.getActionMap().get("EDIT"));
        editItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.SHIFT_MASK));
        menuItems.add(editItem);

        return menuItems;
    }

    @Override
    protected void performImport(String transferredData) {
        if (transferredData.equals("Property")) {
            PropertyEditor propertyEditor = new PropertyEditor();
            ScenarioDialogEditor dialog = new ScenarioDialogEditor(propertyEditor);
            dialog.show();
            if (dialog.getExitCode() == 0) {
                sender.getProperty().add(propertyEditor.getProperty());
                setComponentModel(sender);
                commitChanges(Messages.BUNDLE.getString("ADD") + " Property");
            }
        }
    }

    @Override
    protected void openEditor() {
        SenderEditor editor = new SenderEditor();
        editor.setSender(sender);
        ScenarioDialogEditor dialog = new ScenarioDialogEditor(editor);
        dialog.show();
        if (dialog.getExitCode() == 0) {
            this.setComponentModel(editor.getSender());
            this.commitChanges(Messages.BUNDLE.getString("EDIT")+" Sender");
        }
    }

    @Override
    public void setComponentModel(Object componentModel) {
        sender = (Scenario.Sender) componentModel;
        labelSenderClass.setText(sender.getClazz());
        FontMetrics fontMetrics = labelSenderClass.getFontMetrics(labelSenderClass.getFont());
        labelSenderClassWidth = fontMetrics.stringWidth(labelSenderClass.getText());
        panelProperties.updateComponents();
    }

    @Override
    public Object getComponentModel() {
        return sender;
    }

    @Override
    public void updateColors() {
        setBackground(ColorComponents.getColor(ColorType.SENDER_BACKGROUND));
        Color foregroundColor = ColorComponents.getColor(ColorType.SENDER_FOREGROUND);
        setForeground(foregroundColor);
        labelSenderClass.setForeground(foregroundColor);
    }

    @Override
    public List<AbstractComponentGUI> getChildrenAsGUI() {
        List<AbstractComponentGUI> childrenAsGUI = new ArrayList<>();
        for (Property property : sender.getProperty()){
            PropertyGUI propertyGUI = new PropertyGUI(this);
            propertyGUI.setComponentModel(property);
            childrenAsGUI.add(propertyGUI);
        }
        return childrenAsGUI;
    }

    @Override
    public void setChildrenFromGUI(List<AbstractComponentGUI> childrenAsGUI) {
        List<Property> properties = new ArrayList<>();
        for (AbstractComponentGUI c : childrenAsGUI){
            properties.add((Property) c.getComponentModel());
        }
        sender.getProperty().clear();
        sender.getProperty().addAll(properties);
        setComponentModel(sender);
        commitChanges("Sender: Properties "+Messages.BUNDLE.getString("REORDER"));
    }

    @Override
    public Dimension getMinimumSize(){
        int panelMinWidth = panelProperties.getMinimumSize().width;
        int width = (panelMinWidth + 20 > labelSenderClassWidth + 30) ? panelMinWidth + 20 : labelSenderClassWidth + 30;
        return new Dimension(width,panelProperties.getMinimumSize().height + 50);
    }

    @Override
    public Dimension getPreferredSize(){
        return new Dimension(super.getPreferredSize().width,panelProperties.getPreferredSize().height + 50);
    }

    @Override
    public Dimension getMaximumSize(){
        return new Dimension(super.getMaximumSize().width,panelProperties.getMaximumSize().height + 50);
    }
}
