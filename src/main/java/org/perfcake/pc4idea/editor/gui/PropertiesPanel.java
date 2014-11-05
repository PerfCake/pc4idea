package org.perfcake.pc4idea.editor.gui;

import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.PerfCakeEditorGUI;
import org.perfcake.pc4idea.editor.components.ComponentEditor;
import org.perfcake.pc4idea.editor.components.PropertyComponent;
import org.perfcake.pc4idea.editor.wizard.PropertiesEditor;
import org.perfcake.pc4idea.editor.wizard.PropertyEditor;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 28.9.2014
 */
public class PropertiesPanel extends AbstractPanel {
    private final String TITLE ="Properties Editor";
    private Color propertiesColor = Color.getHSBColor(0/360f,0.2f,0.5f);

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

        this.setTransferHandler(new TransferHandler(){
            @Override
            public boolean canImport(TransferHandler.TransferSupport support){
                support.setDropAction(COPY);
                return support.isDataFlavorSupported(DataFlavor.stringFlavor);
            }
            @Override
            public boolean importData(TransferHandler.TransferSupport support){
                if (!canImport(support)) {
                    return false;
                }
                Transferable t = support.getTransferable();
                String transferredData = "";
                try {
                    transferredData = (String)t.getTransferData(DataFlavor.stringFlavor);
                } catch (UnsupportedFlavorException e) {
                    e.printStackTrace();   /*TODO log*/
                } catch (IOException e) {
                    e.printStackTrace();   /*TODO log*/
                }
                if (transferredData.equals("Property")) {
                    PropertyEditor propertyEditor = new PropertyEditor();
                    ComponentEditor editor = new ComponentEditor("Property Editor", propertyEditor);
                    editor.show();
                    if (editor.getExitCode() == 0) {
                        properties.getProperty().add(propertyEditor.getProperty());
                        setComponent(properties);
                        scenarioEvent.saveProperties();
                    }
                }
                return true;
            }
        });
    }

    @Override
    protected Color getColor() {
        return propertiesColor;
    }

    @Override
    protected String getEditorTitle() {
        return TITLE;
    }

    @Override
    protected JPanel getEditorPanel() {
        propertiesEditor = new PropertiesEditor();
        propertiesEditor.setObjProperties(properties);
        return propertiesEditor;
    }

    @Override
    protected void applyChanges() {
        setComponent(propertiesEditor.getObjProperties());
        scenarioEvent.saveProperties();
    }

    @Override
    public void setComponent(Object component) {
        properties = (Scenario.Properties) component;

        panelProperties.setProperties(properties.getProperty());

        this.revalidate();
    }

    @Override
    public Object getComponent() {
        return properties;
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
            this.addMouseListener(new DragListener());
            this.setOpaque(false);
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
                        PropertiesPanel.this.setComponent(properties);
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
                        PropertiesPanel.this.setComponent(properties);
                        scenarioEvent.saveProperties();
                    }
                }
            }
        }

        private class DragListener extends MouseInputAdapter {
            private boolean mousePressed;
            private int selectedComponent;
            private int expectedReleaseComponent;

            private DragListener(){
                mousePressed = false;
            }

            @Override
            public void mousePressed(MouseEvent e){
                if (e.getComponent() instanceof PropertyComponent){
                    for (int i = 0;i< propertyComponentList.size();i++){
                        if (e.getComponent().equals(propertyComponentList.get(i))){
                            selectedComponent = i;
                            expectedReleaseComponent = i;
                            mousePressed = true;
                        }
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (e.getComponent() instanceof PropertyComponent){
                    for (int i = 0;i< propertyComponentList.size();i++){
                        if (e.getComponent().equals(propertyComponentList.get(i))){
                            expectedReleaseComponent = i;
                        }
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e){
                if(mousePressed) {
                    if (selectedComponent == expectedReleaseComponent) {
                        // do nothing
                    } else {
                        if (selectedComponent < expectedReleaseComponent) {
                            for (int i = 0; i < propertiesList.size(); i++) {
                                if (i < selectedComponent) {
                                    // do nothing
                                } else {
                                    if (i < expectedReleaseComponent) {
                                        Collections.swap(propertiesList, i, i + 1);
                                    }
                                }
                            }
                        }
                        if (selectedComponent > expectedReleaseComponent) {
                            for (int i = propertiesList.size() - 1; 0 <= i; i--) {
                                if (i < selectedComponent) {
                                    if (i >= expectedReleaseComponent) {
                                        Collections.swap(propertiesList, i, i + 1);
                                    }
                                }
                            }
                        }
                        properties.getProperty().clear();
                        properties.getProperty().addAll(propertiesList);
                        PropertiesPanel.this.setComponent(properties);
                        scenarioEvent.saveProperties();
                    }
                    mousePressed = false;

                }
            }

            @Override
            public void mouseClicked(MouseEvent e){
                MouseEvent wrappedEvent = new MouseEvent((Component)e.getSource(),e.getID(),e.getWhen(),e.getModifiers(),e.getX()+10,e.getY()+40,e.getClickCount(),e.isPopupTrigger(),e.getButton());
                ((JPanel)e.getComponent().getAccessibleContext().getAccessibleParent()).dispatchEvent(wrappedEvent);
            }
        }
    }

}
