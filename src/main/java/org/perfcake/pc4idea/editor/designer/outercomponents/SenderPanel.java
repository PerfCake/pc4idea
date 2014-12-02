package org.perfcake.pc4idea.editor.designer.outercomponents;

import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.PerfCakeEditorGUI;
import org.perfcake.pc4idea.editor.designer.common.ComponentDragListener;
import org.perfcake.pc4idea.editor.designer.common.ScenarioDialogEditor;
import org.perfcake.pc4idea.editor.designer.editors.AbstractEditor;
import org.perfcake.pc4idea.editor.designer.editors.PropertyEditor;
import org.perfcake.pc4idea.editor.designer.editors.SenderEditor;
import org.perfcake.pc4idea.editor.designer.innercomponents.PropertyComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 28.9.2014
 */
public class SenderPanel extends AbstractPanel {
    private Color senderColor = Color.getHSBColor(220/360f,0.75f,0.75f);

    private SenderEditor senderEditor;
    private Scenario.Sender sender;
    private PerfCakeEditorGUI.ScenarioEvent scenarioEvent;

    private JLabel labelSenderClass;
    private PanelProperties panelProperties;

    private int labelSenderClassWidth;

    public SenderPanel(PerfCakeEditorGUI.ScenarioEvent scenarioEvent){
        this.scenarioEvent = scenarioEvent;
        labelSenderClassWidth = 0;

        initComponents();
    }

    private void initComponents() {
        labelSenderClass = new JLabel("---");
        labelSenderClass.setFont(new Font(labelSenderClass.getFont().getName(), 0, 15));
        labelSenderClass.setForeground(senderColor);

        panelProperties = new PanelProperties();

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

        this.addComponentListener( new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                e.getComponent().revalidate();
                e.getComponent().repaint();
            }
        });
    }

    @Override
    protected List<JMenuItem> getPopupMenuItems(){
        List<JMenuItem> menuItems = new ArrayList<>();

        JMenuItem itemOpenEditor = new JMenuItem("Open Editor");
        itemOpenEditor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ScenarioDialogEditor editor = new ScenarioDialogEditor(getEditorPanel());
                editor.show();
                if (editor.getExitCode() == 0) {
                    applyChanges();
                }
            }
        });
        menuItems.add(itemOpenEditor);

        JMenuItem itemAddProperty = new JMenuItem("Add Property");
        itemAddProperty.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PropertyEditor propertyEditor = new PropertyEditor();
                ScenarioDialogEditor dialog = new ScenarioDialogEditor(propertyEditor);
                dialog.show();
                if (dialog.getExitCode() == 0) {
                    Property property = propertyEditor.getProperty();
                    sender.getProperty().add(property);
                    SenderPanel.this.setComponentModel(sender);
                    scenarioEvent.saveSender();
                }
            }
        });
        menuItems.add(itemAddProperty);

        return menuItems;
    }

    @Override
    protected void performImport(String transferredData){  /*TODO bez dialogu -> just create*/
        if (transferredData.equals("Property")) {
            PropertyEditor propertyEditor = new PropertyEditor();
            ScenarioDialogEditor dialog = new ScenarioDialogEditor(propertyEditor);
            dialog.show();
            if (dialog.getExitCode() == 0) {
                sender.getProperty().add(propertyEditor.getProperty());
                setComponentModel(sender);
                scenarioEvent.saveSender();
            }
        }
    }

    @Override
    protected Color getColor() {
        return senderColor;
    }

    @Override
    protected AbstractEditor getEditorPanel() {
        senderEditor = new SenderEditor();
        senderEditor.setSender(sender);
        return senderEditor;
    }

    @Override
    protected void applyChanges() {
        setComponentModel(senderEditor.getSender());
        scenarioEvent.saveSender();
    }

    @Override
    public void setComponentModel(Object componentModel) {
        sender = (Scenario.Sender) componentModel;
        labelSenderClass.setText(sender.getClazz());
        FontMetrics fontMetrics = labelSenderClass.getFontMetrics(labelSenderClass.getFont());
        labelSenderClassWidth = fontMetrics.stringWidth(labelSenderClass.getText());

        panelProperties.setProperties(sender.getProperty());

        this.revalidate();
    }

    @Override
    public Object getComponentModel() {
        return sender;
    }

    @Override
    public Dimension getMinimumSize(){
        Dimension dimension = new Dimension();
        int widestPropertyWidth = panelProperties.getWidestPropertyWidth();
        dimension.width = (widestPropertyWidth+20 > labelSenderClassWidth+30) ? widestPropertyWidth+20 : labelSenderClassWidth+30;
        dimension.height = panelProperties.getPropertiesRowCount()*40 + 50;
        return dimension;
    }

    @Override
    public Dimension getPreferredSize(){
        Dimension dimension = new Dimension();
        dimension.width = super.getPreferredSize().width;
        dimension.height = panelProperties.getPropertiesRowCount()*40 + 50;
        return dimension;
    }

    @Override
    public Dimension getMaximumSize(){
        Dimension dimension = new Dimension();
        dimension.width = super.getMaximumSize().width;
        dimension.height = panelProperties.getPropertiesRowCount()*40 + 50;
        return dimension;
    }

    public class PanelProperties extends JPanel {
        private List<PropertyComponent> propertyComponentList;
        private List<Property> propertiesList;
        private int widestPropertyWidth;
        private int propertiesRowCount;

        private PanelProperties(){
            propertyComponentList = new ArrayList<>();
            propertiesList = new ArrayList<>();
            widestPropertyWidth = 0;
            propertiesRowCount = 0;
            this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
            this.setOpaque(false);
            this.addMouseListener(new ComponentDragListener() {
                @Override
                public int mousePressedActionPerformed(MouseEvent e) {
                    int pressedComponent = -1;
                    if (e.getComponent() instanceof PropertyComponent){
                        for (int i = 0;i< propertyComponentList.size();i++){
                            if (e.getComponent().equals(propertyComponentList.get(i))){
                                pressedComponent = i;
                            }
                        }
                    }
                    return pressedComponent;
                }

                @Override
                public int mouseEnteredActionPerformed(MouseEvent e) {
                    int enteredComponent = -1;
                    if (e.getComponent() instanceof PropertyComponent){
                        for (int i = 0;i< propertyComponentList.size();i++){
                            if (e.getComponent().equals(propertyComponentList.get(i))){
                                enteredComponent = i;
                            }
                        }
                    }
                    return enteredComponent;
                }

                @Override
                public void mouseReleasedActionPerformed(int selectedComponent, int releasedComponent) {
                    if (selectedComponent < releasedComponent) {
                        for (int i = 0; i < propertiesList.size(); i++) {
                            if (i >= selectedComponent) {
                                if (i < releasedComponent) {
                                    Collections.swap(propertiesList, i, i + 1);
                                }
                            }
                        }
                    }
                    if (selectedComponent > releasedComponent) {
                        for (int i = propertiesList.size() - 1; 0 <= i; i--) {
                            if (i < selectedComponent) {
                                if (i >= releasedComponent) {
                                    Collections.swap(propertiesList, i, i + 1);
                                }
                            }
                        }
                    }
                    sender.getProperty().clear();
                    sender.getProperty().addAll(propertiesList);
                    SenderPanel.this.setComponentModel(sender);
                    scenarioEvent.saveSender();
                }
            });
        }

        private void setProperties(List<Property> properties){
            propertiesList.clear();
            propertiesList.addAll(properties);
            propertyComponentList.clear();
            this.removeAll();
            this.repaint();

            widestPropertyWidth = 0;
            int propertyId = 0;
            for (Property property : propertiesList) {
                PropertyComponent propertyComponent = new PropertyComponent(senderColor,propertyId,new SenderEvent());
                propertyComponent.setProperty(property);
                propertyComponentList.add(propertyComponent);
                this.add(propertyComponent);
                if (propertyComponent.getPreferredSize().width > widestPropertyWidth) {
                    widestPropertyWidth = propertyComponent.getPreferredSize().width;
                }
                propertyId++;
            }
            countPropertiesRowCount();

            this.revalidate();
        }

        private int getWidestPropertyWidth(){
            return widestPropertyWidth;
        }
        private int getPropertiesRowCount(){
            return propertiesRowCount;
        }
        private void countPropertiesRowCount(){
            int thisPanelWidth = SenderPanel.this.getSize().width-20;
            thisPanelWidth = (thisPanelWidth < 0) ? Integer.MAX_VALUE : thisPanelWidth;

            if (widestPropertyWidth <= thisPanelWidth) {
                int controlSum = 0;
                int expectedRows = 0;
                for (int i = 0; i < propertyComponentList.size(); i++) {
                    if (i == 0) {
                        expectedRows = 1;
                    }
                    controlSum += propertyComponentList.get(i).getPreferredSize().width;
                    if (controlSum > thisPanelWidth) {
                        i--;
                        controlSum = 0;
                        expectedRows++;
                    }
                }
                propertiesRowCount = (expectedRows != propertiesRowCount) ? expectedRows : propertiesRowCount;
            }
        }

        @Override
        public Dimension getMinimumSize(){
            Dimension dimension = new Dimension();
            dimension.width = widestPropertyWidth;   /*TODO maybe same as pref,max*/
            dimension.height = propertiesRowCount*40;
            return dimension;
        }

        @Override
        public Dimension getPreferredSize(){
            countPropertiesRowCount();

            Dimension dimension = new Dimension();
            dimension.width = SenderPanel.this.getSize().width-20;
            dimension.height = propertiesRowCount*40;
            return dimension;
        }

        @Override
        public Dimension getMaximumSize(){
            Dimension dimension = new Dimension();
            dimension.width = SenderPanel.this.getSize().width-20;
            dimension.height = propertiesRowCount*40;
            return dimension;
        }

        public final class SenderEvent {
            public void saveProperty(int propertyId){
                for (int i = 0; i<propertyComponentList.size();i++){
                    if (propertyComponentList.get(i).getId() == propertyId){
                        propertiesList.set(i, propertyComponentList.get(i).getProperty());

                        sender.getProperty().clear();
                        sender.getProperty().addAll(propertiesList);
                        SenderPanel.this.setComponentModel(sender);
                        scenarioEvent.saveSender();
                    }
                }

            }
            public void deleteProperty(int propertyId){
                for (int i = 0; i<propertyComponentList.size();i++){
                    if (propertyComponentList.get(i).getId() == propertyId){
                        propertiesList.remove(i);

                        sender.getProperty().clear();
                        sender.getProperty().addAll(propertiesList);
                        SenderPanel.this.setComponentModel(sender);
                        scenarioEvent.saveSender();
                    }
                }
            }
        }
    }

}
