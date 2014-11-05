package org.perfcake.pc4idea.editor.gui;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.PerfCakeEditorGUI;
import org.perfcake.pc4idea.editor.components.ComponentEditor;
import org.perfcake.pc4idea.editor.components.ValidatorComponent;
import org.perfcake.pc4idea.editor.wizard.ValidationEditor;
import org.perfcake.pc4idea.editor.wizard.ValidatorEditor;

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
public class ValidationPanel extends AbstractPanel {
    private final String TITLE ="Validation Editor";
    private Color validationColor = Color.getHSBColor(340/360f,1f,0.75f);

    private ValidationEditor validationEditor;
    private Scenario.Validation validation;
    private PerfCakeEditorGUI.ScenarioEvent scenarioEvent;

    private JLabel labelValidation;
    private PanelValidators panelValidators;

    private int labelValidationWidth;

    public ValidationPanel(PerfCakeEditorGUI.ScenarioEvent scenarioEvent){
        this.scenarioEvent = scenarioEvent;
        labelValidationWidth = 0;

        initComponents();
    }

    private void initComponents(){
        labelValidation = new JLabel("Validation");
        labelValidation.setFont(new Font(labelValidation.getFont().getName(),0,15));
        labelValidation.setForeground(validationColor);
        FontMetrics fontMetrics = labelValidation.getFontMetrics(labelValidation.getFont());
        labelValidationWidth = fontMetrics.stringWidth(labelValidation.getText());

        panelValidators = new PanelValidators();

        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        this.add(labelValidation);
        this.add(panelValidators);

        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, labelValidation,
                0,
                SpringLayout.HORIZONTAL_CENTER, this);
        layout.putConstraint(SpringLayout.NORTH, labelValidation,
                10,
                SpringLayout.NORTH, this);

        layout.putConstraint(SpringLayout.WEST, panelValidators,
                10,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, panelValidators,8,SpringLayout.SOUTH, labelValidation);

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
                if (transferredData.contains("Validator")){
                    ValidatorEditor validatorEditor = new ValidatorEditor();
                    Scenario.Validation.Validator validatorClass = new Scenario.Validation.Validator();
                    validatorClass.setClazz(transferredData);
                    ComponentEditor editor = new ComponentEditor("Validator Editor",validatorEditor);
                    editor.show();
                    if (editor.getExitCode() == 0) {
                        validation.getValidator().add(validatorEditor.getValidator());
                        setComponent(validation);
                        scenarioEvent.saveValidation();
                    }
                }
                return true;
            }
        });
    }

    @Override
    protected Color getColor() {
        return validationColor;
    }

    @Override
    protected String getEditorTitle() {
        return TITLE;
    }

    @Override
    protected JPanel getEditorPanel() {
        validationEditor = new ValidationEditor();
        validationEditor.setValidation(validation);
        return validationEditor;
    }

    @Override
    protected void applyChanges() {
        setComponent(validationEditor.getValidation());
        scenarioEvent.saveValidation();
    }

    @Override
    public void setComponent(Object component) {
        validation = (Scenario.Validation) component;

        panelValidators.setValidators(validation.getValidator());

        this.revalidate();
    }

    @Override
    public Object getComponent() {
        return validation;
    }

    @Override
    public Dimension getMinimumSize(){
        Dimension dimension = new Dimension();
        int widestPropertyWidth = panelValidators.getWidestValidatorWidth();
        dimension.width = (widestPropertyWidth+20 > labelValidationWidth+30) ? widestPropertyWidth+20 : labelValidationWidth+30;
        dimension.height = panelValidators.getValidatorsRowCount()*40 + 50;
        return dimension;
    }
/*TODO validation able to fit Reporting*/
//    @Override
//    public Dimension getPreferredSize(){
//        Dimension dimension = new Dimension();
//        dimension.width = super.getPreferredSize().width;
//        dimension.height = panelValidators.getValidatorsRowCount()*40 + 50;
//        return dimension;
//    }
//
//    @Override
//    public Dimension getMaximumSize(){
//        Dimension dimension = new Dimension();
//        dimension.width = super.getMaximumSize().width;
//        dimension.height = panelValidators.getValidatorsRowCount()*40 + 50;
//        return dimension;
//    }

    public class PanelValidators extends JPanel {
        private List<ValidatorComponent> validatorComponentList;
        private List<Scenario.Validation.Validator> validatorList;
        private int widestValidatorWidth;
        private int validatorsRowCount;

        private PanelValidators(){
            validatorComponentList = new ArrayList<>();
            validatorList = new ArrayList<>();
            widestValidatorWidth = 0;
            validatorsRowCount = 0;
            this.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
            this.addMouseListener(new DragListener());
            this.setOpaque(false);
        }

        private void setValidators(List<Scenario.Validation.Validator> validators){
            validatorList.clear();
            validatorList.addAll(validators);
            validatorComponentList.clear();
            this.removeAll();
            this.repaint();

            widestValidatorWidth = 0;
            int validatorId = 0;
            for (Scenario.Validation.Validator validator : validatorList) {
                ValidatorComponent validatorComponent = new ValidatorComponent(validationColor,validatorId,new ValidationEvent());
                validatorComponent.setValidator(validator);
                validatorComponentList.add(validatorComponent);
                this.add(validatorComponent);
                if (validatorComponent.getPreferredSize().width > widestValidatorWidth) {
                    widestValidatorWidth = validatorComponent.getPreferredSize().width;
                }
                validatorId++;
            }
            countValidatorsRowCount();

            this.revalidate();
        }

        private int getWidestValidatorWidth(){
            return widestValidatorWidth;
        }
        private int getValidatorsRowCount(){
            return validatorsRowCount;
        }
        private void countValidatorsRowCount(){
            int thisPanelWidth = ValidationPanel.this.getSize().width-20;
            thisPanelWidth = (thisPanelWidth < 0) ? Integer.MAX_VALUE : thisPanelWidth;

            if (widestValidatorWidth <= thisPanelWidth) {
                int controlSum = 0;
                int expectedRows = 0;
                for (int i = 0; i < validatorComponentList.size(); i++) {
                    if (i == 0) {
                        expectedRows = 1;
                    }
                    controlSum += validatorComponentList.get(i).getPreferredSize().width;
                    if (controlSum > thisPanelWidth) {
                        i--;
                        controlSum = 0;
                        expectedRows++;
                    }
                }
                validatorsRowCount = (expectedRows != validatorsRowCount) ? expectedRows : validatorsRowCount;
            }
        }

        @Override
        public Dimension getMinimumSize(){
            Dimension dimension = new Dimension();
            dimension.width = widestValidatorWidth;
            dimension.height = validatorsRowCount*40;
            return dimension;
        }

        @Override
        public Dimension getPreferredSize(){
            countValidatorsRowCount();

            Dimension dimension = new Dimension();
            dimension.width = ValidationPanel.this.getSize().width-20;
            dimension.height = validatorsRowCount*40;
            return dimension;
        }

        @Override
        public Dimension getMaximumSize(){
            Dimension dimension = new Dimension();
            dimension.width = ValidationPanel.this.getSize().width-20;
            dimension.height = validatorsRowCount*40;
            return dimension;
        }

        public final class ValidationEvent {
            public void saveValidator(int validatorId){
                for (int i = 0; i<validatorComponentList.size();i++){
                    if (validatorComponentList.get(i).getId() == validatorId){
                        validatorList.set(i, validatorComponentList.get(i).getValidator());

                        validation.getValidator().clear();
                        validation.getValidator().addAll(validatorList);
                        ValidationPanel.this.setComponent(validation);
                        scenarioEvent.saveValidation();
                    }
                }

            }
            public void deleteValidator(int validatorId){
                for (int i = 0; i<validatorComponentList.size();i++){
                    if (validatorComponentList.get(i).getId() == validatorId){
                        validatorList.remove(i);

                        validation.getValidator().clear();
                        validation.getValidator().addAll(validatorList);
                        ValidationPanel.this.setComponent(validation);
                        scenarioEvent.saveValidation();
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
                if (e.getComponent() instanceof ValidatorComponent){
                    for (int i = 0;i< validatorComponentList.size();i++){
                        if (e.getComponent().equals(validatorComponentList.get(i))){
                            selectedComponent = i;
                            expectedReleaseComponent = i;
                            mousePressed = true;
                        }
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (e.getComponent() instanceof ValidatorComponent){
                    for (int i = 0;i< validatorComponentList.size();i++){
                        if (e.getComponent().equals(validatorComponentList.get(i))){
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
                            for (int i = 0; i < validatorList.size(); i++) {
                                if (i < selectedComponent) {
                                    // do nothing
                                } else {
                                    if (i < expectedReleaseComponent) {
                                        Collections.swap(validatorList, i, i + 1);
                                    }
                                }
                            }
                        }
                        if (selectedComponent > expectedReleaseComponent) {
                            for (int i = validatorList.size() - 1; 0 <= i; i--) {
                                if (i < selectedComponent) {
                                    if (i >= expectedReleaseComponent) {
                                        Collections.swap(validatorList, i, i + 1);
                                    }
                                }
                            }
                        }
                        validation.getValidator().clear();
                        validation.getValidator().addAll(validatorList);
                        ValidationPanel.this.setComponent(validation);
                        scenarioEvent.saveValidation();
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
