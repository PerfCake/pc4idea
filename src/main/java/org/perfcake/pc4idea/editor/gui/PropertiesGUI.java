package org.perfcake.pc4idea.editor.gui;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.ComponentsPanel;
import org.perfcake.pc4idea.editor.interfaces.ModelWrapper;

import javax.swing.*;

/**
 * Created by Stanislav Kaleta on 3/7/15.
 */
public class PropertiesGUI extends AbstractComponentGUI /*implements HasListableChildren*/ {
    private Scenario.Properties properties;

    private JLabel labelProperties;
    private ComponentsPanel panelProperties;

    private int labelPropertiesWidth;

    public PropertiesGUI(ModelWrapper generatorModelWrapper, ActionMap baseActionMap){
        super(baseActionMap);
        //initComponents();
        updateColors();
    }

    @Override
    void performImport(String transferredData) {

    }

    @Override
    public Object openEditorDialogAndGetResult() {
        return null;
    }

    @Override
    public void updateGUI() {

    }

    @Override
    public void updateColors() {

    }

//    private void initComponents(){
//        labelProperties = new JLabel("Scenario Properties");
//        labelProperties.setFont(new Font(labelProperties.getFont().getName(),0,15));
//
//        FontMetrics fontMetrics = labelProperties.getFontMetrics(labelProperties.getFont());
//        labelPropertiesWidth = fontMetrics.stringWidth(labelProperties.getText());
//
//        panelProperties = new ComponentsPanel(this);
//
//        SpringLayout layout = new SpringLayout();
//        this.setLayout(layout);
//        this.add(labelProperties);
//        this.add(panelProperties);
//
//        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, labelProperties,
//                0,
//                SpringLayout.HORIZONTAL_CENTER, this);
//        layout.putConstraint(SpringLayout.NORTH, labelProperties,
//                10,
//                SpringLayout.NORTH, this);
//
//        layout.putConstraint(SpringLayout.WEST, panelProperties,
//                10,
//                SpringLayout.WEST, this);
//        layout.putConstraint(SpringLayout.NORTH, panelProperties,8,SpringLayout.SOUTH, labelProperties);
//
//        this.getActionMap().put("ADDP", new AbstractAction(Messages.BUNDLE.getString("ADD")+" Property", AllIcons.General.Add) {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                PropertyEditor editor = new PropertyEditor();
//                ScenarioDialogEditor dialog = new ScenarioDialogEditor(editor);
//                dialog.show();
//                if (dialog.getExitCode() == 0) {
//                    Property property = editor.getProperty();
//                    PropertiesGUI.this.properties.getProperty().add(property);
//                    PropertiesGUI.this.setComponentModel(properties);
//                    PropertiesGUI.this.commitChanges(Messages.BUNDLE.getString("ADD") + " Property");
//                }
//            }
//        });
//        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.SHIFT_MASK), "ADDP");
//
//        this.getActionMap().put("EDIT", new AbstractAction(Messages.BUNDLE.getString("EDIT")+" Properties", AllIcons.Actions.Edit) {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                PropertiesGUI.this.openEditorDialogAndGetResult();
//            }
//        });
//        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.SHIFT_MASK), "EDIT");
//    }
//
//
//    @Override
//    protected List<JMenuItem> getMenuItems() {
//        List<JMenuItem> menuItems = new ArrayList<>();
//
//        JMenuItem addPropertyItem = new JMenuItem();
//        addPropertyItem.setAction(this.getActionMap().get("ADDP"));
//        addPropertyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.SHIFT_MASK));
//        menuItems.add(addPropertyItem);
//
//        JMenuItem editItem = new JMenuItem();
//        editItem.setAction(this.getActionMap().get("EDIT"));
//        editItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.SHIFT_MASK));
//        menuItems.add(editItem);
//
//        return menuItems;
//    }
//
//    @Override
//    protected void performImport(String transferredData) {
//        if (transferredData.equals("Property")) {
//            PropertyEditor propertyEditor = new PropertyEditor();
//            ScenarioDialogEditor dialog = new ScenarioDialogEditor(propertyEditor);
//            dialog.show();
//            if (dialog.getExitCode() == 0) {
//                properties.getProperty().add(propertyEditor.getProperty());
//                setComponentModel(properties);
//                commitChanges(Messages.BUNDLE.getString("ADD") + " Property");
//            }
//        }
//    }
//
//    @Override
//    protected void openEditorDialogAndGetResult() {
//        PropertiesEditor editor = new PropertiesEditor();
//        editor.setObjProperties(properties);
//        ScenarioDialogEditor dialog = new ScenarioDialogEditor(editor);
//        dialog.show();
//        if (dialog.getExitCode() == 0) {
//            this.setComponentModel(editor.getObjProperties());
//            this.commitChanges(Messages.BUNDLE.getString("EDIT")+" Properties");
//        }
//    }
//
//    @Override
//    public void setComponentModel(Object componentModel) {
//        properties = (componentModel != null) ? (Scenario.Properties) componentModel : new Scenario.Properties();
//        panelProperties.updateComponents();
//    }
//
//    @Override
//    public Object getComponentModel() {
//        return (properties.getProperty().isEmpty()) ? null : properties;
//    }
//
//    @Override
//    public void updateColors() {
//        setBackground(ColorComponents.getColor(ColorType.PROPERTIES_BACKGROUND));
//        Color foregroundColor = ColorComponents.getColor(ColorType.PROPERTIES_FOREGROUND);
//        setForeground(foregroundColor);
//        labelProperties.setForeground(foregroundColor);
//    }
//
//    @Override
//    public List<AbstractComponentGUI> getChildrenModels() {
//        List<AbstractComponentGUI> childrenAsGUI = new ArrayList<>();
//        for (Property property : properties.getProperty()){
//            PropertyGUI propertyGUI = new PropertyGUI(this);
//            propertyGUI.setComponentModel(property);
//            childrenAsGUI.add(propertyGUI);
//        }
//        return childrenAsGUI;
//    }
//
//    @Override
//    public void setChildrenFromModels(List<AbstractComponentGUI> childrenAsGUI) {
//        List<Property> propertiesList = new ArrayList<>();
//        for (AbstractComponentGUI c : childrenAsGUI){
//            propertiesList.add((Property) c.getComponentModel());
//        }
//        properties.getProperty().clear();
//        properties.getProperty().addAll(propertiesList);
//        setComponentModel(properties);
//        commitChanges("Properties: Properties " + Messages.BUNDLE.getString("REORDER"));
//
//    }
//
//    @Override
//    public Dimension getMinimumSize(){
//        int panelMinWidth = panelProperties.getMinimumSize().width;
//        int width = (panelMinWidth + 20 > labelPropertiesWidth + 30) ? panelMinWidth + 20 : labelPropertiesWidth + 30;
//        return new Dimension(width,panelProperties.getMinimumSize().height + 50);
//    }
//
//    @Override
//    public Dimension getPreferredSize(){
//        return new Dimension(super.getPreferredSize().width,panelProperties.getPreferredSize().height + 50);
//    }
//
//    @Override
//    public Dimension getMaximumSize(){
//        return new Dimension(super.getMaximumSize().width,panelProperties.getMaximumSize().height + 50);
//    }
}
