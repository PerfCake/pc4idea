package org.perfcake.pc4idea.editor.designer.outercomponents;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.PerfCakeEditorGUI;
import org.perfcake.pc4idea.editor.designer.common.ComponentDragListener;
import org.perfcake.pc4idea.editor.designer.common.ScenarioDialogEditor;
import org.perfcake.pc4idea.editor.designer.editors.AbstractEditor;
import org.perfcake.pc4idea.editor.designer.editors.ValidationEditor;
import org.perfcake.pc4idea.editor.designer.editors.ValidatorEditor;
import org.perfcake.pc4idea.editor.designer.innercomponents.ValidatorComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 28.9.2014
 */
public class ValidationPanel extends AbstractPanel {
    private Color validationColor = Color.getHSBColor(320/360f,0.75f,0.75f);

    private ValidationEditor validationEditor;
    private Scenario.Validation validation;
    private Set<String> attachedIDs;
    private Set<String> usedIDSet;
    private PerfCakeEditorGUI.ScenarioEvent scenarioEvent;

    private JLabel labelValidation;
    private PanelValidators panelValidators;

    private int labelValidationWidth;

    public ValidationPanel(PerfCakeEditorGUI.ScenarioEvent scenarioEvent){
        this.scenarioEvent = scenarioEvent;
        attachedIDs = new TreeSet<>();
        usedIDSet = new TreeSet<>();
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
    }

    public void setValidationModel(Scenario.Validation validation, Set<String> attachedIDs){
        this.attachedIDs.clear();
        this.attachedIDs.addAll(attachedIDs);
        setComponentModel(validation);
    }

    public Set<String> getUsedIDSet(){
        return usedIDSet;
    }

    public Point getValidatorAnchorPoint(String validatorID){
        Point anchorPoint = panelValidators.getValidatorAnchorPoint(validatorID);
        anchorPoint.setLocation(anchorPoint.getX()+this.getX(),anchorPoint.getY()+this.getY());
        return anchorPoint;
    }

    @Override
    protected void performImport(String transferredData){
        if (transferredData.contains("Validator")){
            Scenario.Validation.Validator validatorClass = new Scenario.Validation.Validator();
            validatorClass.setClazz(transferredData);

            ValidatorEditor validatorEditor = new ValidatorEditor(usedIDSet);
            validatorEditor.setValidator(validatorClass,false);
            ScenarioDialogEditor dialog = new ScenarioDialogEditor(validatorEditor);
            dialog.show();
            if (dialog.getExitCode() == 0) {
                validation.getValidator().add(validatorEditor.getValidator());
                setComponentModel(validation);
                scenarioEvent.saveValidation();
            }
        }
    }

    @Override
    protected Color getColor() {
        return validationColor;
    }

    @Override
    protected AbstractEditor getEditorPanel() {
        validationEditor = new ValidationEditor(attachedIDs);
        validationEditor.setValidation(validation,usedIDSet);
        return validationEditor;
    }

    @Override
    protected void applyChanges() {
        setComponentModel(validationEditor.getValidation());
        scenarioEvent.saveValidation();
    }

    @Override
    public void setComponentModel(Object componentModel) {
        if (componentModel != null) {
            validation = (Scenario.Validation) componentModel;
            panelValidators.setValidators(validation.getValidator());
        } else {
            validation = new Scenario.Validation();
            panelValidators.setValidators(new ArrayList<Scenario.Validation.Validator>());
        }
        this.revalidate();

        usedIDSet.clear();
        for (Scenario.Validation.Validator validator : validation.getValidator()){
            usedIDSet.add(validator.getId());
        }
    }

    @Override
    public Object getComponentModel() {
        return (validation.getValidator().isEmpty()) ? null : validation;
    }

    @Override
    public Dimension getMinimumSize(){
        Dimension dimension = new Dimension();
        int widestPropertyWidth = panelValidators.getWidestValidatorWidth();
        dimension.width = (widestPropertyWidth+20 > labelValidationWidth+30) ? widestPropertyWidth+20 : labelValidationWidth+30;
        dimension.height = panelValidators.getValidatorsRowCount()*40 + 50;
        return dimension;
    }
/*TODO Validation able to fit Reporting*/
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
            this.setOpaque(false);
            this.addMouseListener(new ComponentDragListener() {
                @Override
                public int mousePressedActionPerformed(MouseEvent e) {
                    int pressedComponent = -1;
                    if (e.getComponent() instanceof ValidatorComponent){
                        for (int i = 0;i< validatorComponentList.size();i++){
                            if (e.getComponent().equals(validatorComponentList.get(i))){
                                pressedComponent = i;
                            }
                        }
                    }
                    return pressedComponent;
                }

                @Override
                public int mouseEnteredActionPerformed(MouseEvent e) {
                    int enteredComponent = -1;
                    if (e.getComponent() instanceof ValidatorComponent){
                        for (int i = 0;i< validatorComponentList.size();i++){
                            if (e.getComponent().equals(validatorComponentList.get(i))){
                                enteredComponent = i;
                            }
                        }
                    }
                    return enteredComponent;
                }

                @Override
                public void mouseReleasedActionPerformed(int selectedComponent, int releasedComponent) {
                    if (selectedComponent < releasedComponent) {
                        for (int i = 0; i < validatorList.size(); i++) {
                            if (i >= selectedComponent) {
                                if (i < releasedComponent) {
                                    Collections.swap(validatorList, i, i + 1);
                                }
                            }
                        }
                    }
                    if (selectedComponent > releasedComponent) {
                        for (int i = validatorList.size() - 1; 0 <= i; i--) {
                            if (i < selectedComponent) {
                                if (i >= releasedComponent) {
                                    Collections.swap(validatorList, i, i + 1);
                                }
                            }
                        }
                    }
                    validation.getValidator().clear();
                    validation.getValidator().addAll(validatorList);
                    ValidationPanel.this.setComponentModel(validation);
                    scenarioEvent.saveValidation();
                }
            });
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
                boolean isAttached = false;
                for (String id : attachedIDs){
                    if (validator.getId().equals(id)){
                        isAttached = true;
                    }
                }
                ValidatorComponent validatorComponent = new ValidatorComponent(validationColor,validatorId,isAttached,usedIDSet, new ValidationEvent());
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

        private Point getValidatorAnchorPoint(String validatorID){
            for (ValidatorComponent validatorComponent : validatorComponentList){
                if (validatorComponent.getValidator().getId().equals(validatorID)){
                    Point anchorPoint = validatorComponent.getLocation();
                    anchorPoint.setLocation(anchorPoint.getX()+this.getX()+4+validatorComponent.getWidth()/2,anchorPoint.getY()+this.getY()+4);
                    return anchorPoint;
                }
            }
            return null;
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
                        ValidationPanel.this.setComponentModel(validation);
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
                        ValidationPanel.this.setComponentModel(validation);
                        scenarioEvent.saveValidation();
                    }
                }
            }
        }
    }
}
