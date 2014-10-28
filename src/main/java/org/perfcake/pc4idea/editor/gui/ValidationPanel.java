package org.perfcake.pc4idea.editor.gui;

import com.intellij.openapi.project.Project;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.wizard.ValidationEditor;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 28.9.2014
 */
public class ValidationPanel extends AbstractPanel {
    private final String TITLE ="Validation Editor";
    private Color validationColor = Color.getHSBColor(340/360f,1f,0.75f);
    private final Project project;

    private ValidationEditor validationEditor;
    private Scenario.Validation validation;

    private JLabel labelValidation;
    private JPanel panelValidators;

    public ValidationPanel(Project project){
        super(project);
        this.project = project;

        initComponents();
    }

    private void initComponents(){
        labelValidation = new JLabel("Validation");
        labelValidation.setFont(new Font(labelValidation.getFont().getName(),0,15));
        labelValidation.setForeground(validationColor);
//        FontMetrics fontMetrics = labelValidation.getFontMetrics(labelValidation.getFont());
//        labelValidationWidth = fontMetrics.stringWidth(labelValidation.getText());

        panelValidators = new JPanel();
        panelValidators.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
        /*panelValidators.setBackground(Color.cyan);*/panelValidators.setOpaque(false);

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
        this.setComponent(validationEditor.getValidation());
    }

    @Override
    public void setComponent(Object component) {
        validation = (Scenario.Validation) component;

        panelValidators.removeAll();
        panelValidators.repaint();

        for (Scenario.Validation.Validator validator : validation.getValidator()){
            ValidatorComponent validatorComponent = new ValidatorComponent(project,validationColor);
            validatorComponent.setComponent(validator);
            panelValidators.add(validatorComponent);
        }

        panelValidators.revalidate();
        this.revalidate();
    }

    @Override
    public Object getComponent() {
        return validation;
    }


}
