package org.perfcake.pc4idea.editor.gui;

import com.intellij.openapi.project.Project;
import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.PerfCakeEditorGUI;
import org.perfcake.pc4idea.editor.components.ComponentEditor;
import org.perfcake.pc4idea.editor.components.PropertyComponent;
import org.perfcake.pc4idea.editor.wizard.PropertyEditor;
import org.perfcake.pc4idea.editor.wizard.SenderEditor;

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
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 28.9.2014
 */
public class SenderPanel extends AbstractPanel {
    private final String TITLE ="Sender Editor";
    private Color senderColor = Color.getHSBColor(220/360f,0.5f,0.75f);
    private final Project project;

    private SenderEditor senderEditor;
    private Scenario.Sender sender;
    PerfCakeEditorGUI.ScenarioEvent scenarioEvent;

    private JLabel labelSenderClass;
    private PanelProperties panelProperties;

    private int labelSenderClassWidth;

    public SenderPanel(Project project, PerfCakeEditorGUI.ScenarioEvent scenarioEvent){
        super(project);
        this.project = project;
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
                switch (transferredData){
                    case "Property": {
                        PropertyEditor propertyEditor = new PropertyEditor();
                        ComponentEditor editor = new ComponentEditor("Property Editor",propertyEditor);
                        editor.show();
                        if (editor.getExitCode() == 0) {
                            sender.getProperty().add(propertyEditor.getProperty());
                            setComponent(sender);
                            scenarioEvent.saveSender();
                        }
                        break;
                    }
                    default: break;
                }
                return true;
            }
        });
    }

    @Override
    protected Color getColor() {
        return senderColor;
    }

    @Override
    protected String getEditorTitle() {
        return TITLE;
    }

    @Override
    protected JPanel getEditorPanel() {
        senderEditor = new SenderEditor();
        senderEditor.setSender(sender);
        return senderEditor;
    }

    @Override
    protected void applyChanges() {
        setComponent(senderEditor.getSender());
        scenarioEvent.saveSender();
    }

    @Override
    public void setComponent(Object component) {
        sender = (Scenario.Sender) component;
        labelSenderClass.setText(sender.getClazz());
        FontMetrics fontMetrics = labelSenderClass.getFontMetrics(labelSenderClass.getFont());
        labelSenderClassWidth = fontMetrics.stringWidth(labelSenderClass.getText());

        panelProperties.setProperties(sender.getProperty());

        this.revalidate();
    }

    @Override
    public Object getComponent() {
        return sender;
    }

    @Override
    public Dimension getMinimumSize(){
        Dimension dimension = new Dimension();
        int widestPropertyWidth = panelProperties.getWidestPropertyWidth();
        dimension.width = (widestPropertyWidth+20 > labelSenderClassWidth+30) ? widestPropertyWidth+20 : labelSenderClassWidth+30;
        dimension.height = 50;
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
            this.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
            this.addMouseListener(new DragListener());
            this.setOpaque(false);
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
            dimension.width = widestPropertyWidth;
            dimension.height = 20;
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
            dimension.height = Integer.MAX_VALUE;
            return dimension;
        }

        public final class SenderEvent {
            public void saveProperty(int propertyId){
                for (int i = 0; i<propertyComponentList.size();i++){
                    if (propertyComponentList.get(i).getId() == propertyId){
                        propertiesList.set(i, propertyComponentList.get(i).getProperty());

                        sender.getProperty().clear();
                        sender.getProperty().addAll(propertiesList);
                        SenderPanel.this.setComponent(sender);
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
                        SenderPanel.this.setComponent(sender);
                        scenarioEvent.saveSender();
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
                } else {
                    expectedReleaseComponent = -1;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e){
                if(mousePressed){
                    if (expectedReleaseComponent >= 0) {
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
                            sender.getProperty().clear();
                            sender.getProperty().addAll(propertiesList);
                            SenderPanel.this.setComponent(sender);
                            scenarioEvent.saveSender();
                        }
                        mousePressed = false;
                    }
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
