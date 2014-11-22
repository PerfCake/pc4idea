package org.perfcake.pc4idea.editor.designer.outercomponents;

import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.PerfCakeEditorGUI;
import org.perfcake.pc4idea.editor.designer.common.ComponentDragListener;
import org.perfcake.pc4idea.editor.designer.common.ScenarioDialogEditor;
import org.perfcake.pc4idea.editor.designer.editors.AbstractEditor;
import org.perfcake.pc4idea.editor.designer.editors.PropertiesEditor;
import org.perfcake.pc4idea.editor.designer.editors.PropertyEditor;
import org.perfcake.pc4idea.editor.designer.innercomponents.PropertyComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 28.9.2014
 */
public class PropertiesPanel extends AbstractPanel {
    private Color propertiesColor = Color.getHSBColor(160/360f,0.75f,0.75f);

    private PropertiesEditor propertiesEditor;
    private Scenario.Properties properties;
    private PerfCakeEditorGUI.ScenarioEvent scenarioEvent;

    private JLabel labelProperties;
    private PanelProperties panelProperties;

    private int labelPropertiesWidth;

    public PropertiesPanel(PerfCakeEditorGUI.ScenarioEvent scenarioEvent){
        this.scenarioEvent = scenarioEvent;

        initComponents();
    }

    private void initComponents(){
        labelProperties = new JLabel("Scenario Properties");
        labelProperties.setFont(new Font(labelProperties.getFont().getName(),0,15));
        labelProperties.setForeground(propertiesColor);
        FontMetrics fontMetrics = labelProperties.getFontMetrics(labelProperties.getFont());
        labelPropertiesWidth = fontMetrics.stringWidth(labelProperties.getText());

        panelProperties = new PanelProperties();

        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        this.add(labelProperties);
        this.add(panelProperties);

        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, labelProperties,
                0,
                SpringLayout.HORIZONTAL_CENTER, this);
        layout.putConstraint(SpringLayout.NORTH, labelProperties,
                10,
                SpringLayout.NORTH, this);

        layout.putConstraint(SpringLayout.WEST, panelProperties,
                10,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, panelProperties,8,SpringLayout.SOUTH, labelProperties);

        this.addComponentListener( new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                e.getComponent().revalidate();
                e.getComponent().repaint();
            }
        });
    }

    @Override
    protected void performImport(String transferredData){  /*TODO bez dialogu -> just create*/
        if (transferredData.equals("Property")) {
            PropertyEditor propertyEditor = new PropertyEditor();
            ScenarioDialogEditor dialog = new ScenarioDialogEditor(propertyEditor);
            dialog.show();
            if (dialog.getExitCode() == 0) {
                properties.getProperty().add(propertyEditor.getProperty());
                setComponentModel(properties);
                scenarioEvent.saveProperties();
            }
        }
    }

    @Override
    protected Color getColor() {
        return propertiesColor;
    }

    @Override
    protected AbstractEditor getEditorPanel() {
        propertiesEditor = new PropertiesEditor();
        propertiesEditor.setObjProperties(properties);
        return propertiesEditor;
    }

    @Override
    protected void applyChanges() {
        setComponentModel(propertiesEditor.getObjProperties());
        scenarioEvent.saveProperties();
    }

    @Override
    public void setComponentModel(Object componentModel) {
        if (componentModel != null) {
            properties = (Scenario.Properties) componentModel;
            panelProperties.setProperties(properties.getProperty());
        } else {
            properties = new Scenario.Properties();
            panelProperties.setProperties(new ArrayList<Property>());
        }
        this.revalidate();
    }

    @Override
    public Object getComponentModel() {
        return (properties.getProperty().isEmpty()) ? null : properties;
    }

    @Override
    public Dimension getMinimumSize(){
        Dimension dimension = new Dimension();
        int widestPropertyWidth = panelProperties.getWidestPropertyWidth();
        dimension.width = (widestPropertyWidth+20 > labelPropertiesWidth+30) ? widestPropertyWidth+20 : labelPropertiesWidth+30;
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
        private java.util.List<PropertyComponent> propertyComponentList;
        private java.util.List<Property> propertiesList;
        private int widestPropertyWidth;
        private int propertiesRowCount;

        private PanelProperties(){
            propertyComponentList = new ArrayList<>();
            propertiesList = new ArrayList<>();
            widestPropertyWidth = 0;
            propertiesRowCount = 0;
            this.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
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
                    properties.getProperty().clear();
                    properties.getProperty().addAll(propertiesList);
                    PropertiesPanel.this.setComponentModel(properties);
                    scenarioEvent.saveProperties();
                }
            });
        }

        private void setProperties(java.util.List<Property> properties){
            propertiesList.clear();
            propertiesList.addAll(properties);
            propertyComponentList.clear();
            this.removeAll();
            this.repaint();

            widestPropertyWidth = 0;
            int propertyId = 0;
            for (Property property : propertiesList) {
                PropertyComponent propertyComponent = new PropertyComponent(propertiesColor,propertyId,new PropertiesEvent());
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
            int thisPanelWidth = PropertiesPanel.this.getSize().width-20;
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
            dimension.width = widestPropertyWidth;
            dimension.height = propertiesRowCount*40;
            return dimension;
        }

        @Override
        public Dimension getPreferredSize(){
            countPropertiesRowCount();

            Dimension dimension = new Dimension();
            dimension.width = PropertiesPanel.this.getSize().width-20;
            dimension.height = propertiesRowCount*40;
            return dimension;
        }

        @Override
        public Dimension getMaximumSize(){
            Dimension dimension = new Dimension();
            dimension.width = PropertiesPanel.this.getSize().width-20;
            dimension.height = propertiesRowCount*40;
            return dimension;
        }

        public final class PropertiesEvent {
            public void saveProperty(int propertyId){
                for (int i = 0; i<propertyComponentList.size();i++){
                    if (propertyComponentList.get(i).getId() == propertyId){
                        propertiesList.set(i, propertyComponentList.get(i).getProperty());

                        properties.getProperty().clear();
                        properties.getProperty().addAll(propertiesList);
                        PropertiesPanel.this.setComponentModel(properties);
                        scenarioEvent.saveProperties();
                    }
                }
            }
            public void deleteProperty(int propertyId){
                for (int i = 0; i<propertyComponentList.size();i++){
                    if (propertyComponentList.get(i).getId() == propertyId){
                        propertiesList.remove(i);

                        properties.getProperty().clear();
                        properties.getProperty().addAll(propertiesList);
                        PropertiesPanel.this.setComponentModel(properties);
                        scenarioEvent.saveProperties();
                    }
                }
            }
        }
    }
}
