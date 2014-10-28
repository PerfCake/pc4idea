package org.perfcake.pc4idea.editor.gui;

import com.intellij.openapi.project.Project;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.wizard.ValidatorEditor;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 28.10.2014
 */
public class ValidatorComponent extends AbstractPanel {
    private final String TITLE ="Validator Editor";
    private final Color validatorColor;

    private ValidatorEditor validatorEditor;
    private Scenario.Validation.Validator validator;

    private JLabel validatorAttr;
    private Dimension validatorSize;

    public ValidatorComponent(Project project, Color validationColor){
        super(project);
        this.validatorColor = validationColor;

        initComponents();
    }

    private void initComponents(){
        validatorAttr = new JLabel("-");
        validatorAttr.setFont(new Font(validatorAttr.getFont().getName(), 0, 15));
        validatorAttr.setForeground(validatorColor);
        validatorSize = new Dimension(40,40);

        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        this.add(validatorAttr);

        layout.putConstraint(SpringLayout.NORTH, validatorAttr,
                10,
                SpringLayout.NORTH, this);

        layout.putConstraint(SpringLayout.WEST, validatorAttr,
                15,
                SpringLayout.WEST, this);
    }

    @Override
    protected Color getColor() {
        return validatorColor;
    }

    @Override
    protected String getEditorTitle() {
        return TITLE;
    }

    @Override
    protected JPanel getEditorPanel() {
        validatorEditor = new ValidatorEditor();
        validatorEditor.setValidator(validator);
        return validatorEditor;
    }

    @Override
    protected void applyChanges() {
        this.setComponent(validatorEditor.getValidator());
    }

    @Override
    public void setComponent(Object component) {
        validator = (Scenario.Validation.Validator) component;
        validatorAttr.setText("("+validator.getId()+") "+validator.getClazz());
        FontMetrics fontMetrics = validatorAttr.getFontMetrics(validatorAttr.getFont());
        validatorSize.width = fontMetrics.stringWidth(validatorAttr.getText()) + 30;
    }

    @Override
    public Object getComponent() {
        return validator;
    }

    @Override
    public Dimension getMinimumSize(){
        return validatorSize;
    }

    @Override
    public Dimension getPreferredSize(){
        return validatorSize;
    }

    @Override
    public Dimension getMaximumSize(){
        return validatorSize;
    }
}
