package org.perfcake.pc4idea.impl.editor.gui.component;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.actions.ActionType;
import org.perfcake.pc4idea.api.editor.color.ColorType;
import org.perfcake.pc4idea.api.editor.gui.ComponentGui;
import org.perfcake.pc4idea.api.editor.openapi.ui.EditorDialog;
import org.perfcake.pc4idea.api.editor.swing.ComponentsPanel;
import org.perfcake.pc4idea.api.editor.swing.JEnabledCircle;
import org.perfcake.pc4idea.api.util.Messages;
import org.perfcake.pc4idea.impl.editor.actions.AddValidatorAction;
import org.perfcake.pc4idea.impl.editor.actions.EditAction;
import org.perfcake.pc4idea.impl.editor.actions.ReorderAction;
import org.perfcake.pc4idea.impl.editor.actions.ToggleAction;
import org.perfcake.pc4idea.impl.editor.editor.component.ValidationEditor;
import org.perfcake.pc4idea.impl.editor.modelwrapper.component.ValidationModelWrapper;
import org.perfcake.pc4idea.todo.settings.ColorComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * Created by Stanislav Kaleta on 3/7/15.
 */
public class ValidationGui extends ComponentGui {
    private ValidationModelWrapper modelWrapper;

    private JLabel labelValidation;
    private JEnabledCircle enabledCircle;
    private ComponentsPanel panelValidators;

    private int labelValidationWidth = 0;

    public ValidationGui(ValidationModelWrapper modelWrapper) {
        super(modelWrapper.getContext());
        this.modelWrapper = modelWrapper;
        initComponents();
        updateColors();
    }

    private void initComponents(){
        labelValidation = new JLabel(Messages.Scenario.VALIDATION);
        labelValidation.setFont(new Font(labelValidation.getFont().getName(),0,15));
        FontMetrics fontMetrics = labelValidation.getFontMetrics(labelValidation.getFont());
        labelValidationWidth = fontMetrics.stringWidth(labelValidation.getText());

        enabledCircle = new JEnabledCircle(new ToggleAction(modelWrapper, false));

        panelValidators = new ComponentsPanel(modelWrapper);

        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        this.add(labelValidation);
        this.add(enabledCircle);
        this.add(panelValidators);

        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, labelValidation,
                0,
                SpringLayout.HORIZONTAL_CENTER, this);
        layout.putConstraint(SpringLayout.NORTH, labelValidation,
                10,
                SpringLayout.NORTH, this);

        layout.putConstraint(SpringLayout.EAST, enabledCircle,
                -5,
                SpringLayout.WEST, labelValidation);
        layout.putConstraint(SpringLayout.NORTH, enabledCircle,
                10,
                SpringLayout.NORTH, this);

        layout.putConstraint(SpringLayout.WEST, panelValidators,
                10,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, panelValidators,
                8,
                SpringLayout.SOUTH, labelValidation);

        getActionMap().put(ActionType.ADDV, new AddValidatorAction(modelWrapper));
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.SHIFT_MASK), ActionType.ADDV);

        getActionMap().put(ActionType.EDIT, new EditAction(modelWrapper));
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.SHIFT_MASK), ActionType.EDIT);

        getActionMap().put(ActionType.TOGGLE, new ToggleAction(modelWrapper, false));
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.SHIFT_MASK), ActionType.TOGGLE);

        getActionMap().put(ActionType.REORDER, new ReorderAction(modelWrapper, Messages.Scenario.VALIDATOR));
    }

    public Point getValidatorAnchorPoint(Scenario.Validation.Validator validator){
        Point anchorPoint = panelValidators.getComponentAnchorPoint(validator, false);
        anchorPoint.setLocation(anchorPoint.getX()+this.getX(),anchorPoint.getY()+this.getY());
        return anchorPoint;

    }

    @Override
    public void performImport(String transferredData) {
        if (transferredData.contains("Validator")){
            ((AddValidatorAction)getActionMap().get(ActionType.ADDV)).actionPerformedWrapper(transferredData);
        }
    }

    @Override
    public Object openEditorDialogAndGetResult() {
        ValidationEditor editor =
                new ValidationEditor(modelWrapper.getContext().getModule(), modelWrapper.getSync());
        editor.setValidation((Scenario.Validation) modelWrapper.retrieveModel());
        EditorDialog dialog = new EditorDialog(editor);
        dialog.show();
        if (dialog.getExitCode() == 0) {
            return editor.getValidation();
        }
        return null;
    }

    @Override
    public void updateGui() {
        Scenario.Validation validation = (Scenario.Validation) modelWrapper.retrieveModel();
        if (validation.getValidator().isEmpty()){
            enabledCircle.setVisible(false);
        } else {
            enabledCircle.setVisible(true);

            boolean isEnabled = validation.isEnabled();
            enabledCircle.setState(isEnabled);
            getActionMap().put(ActionType.TOGGLE, new ToggleAction(modelWrapper, isEnabled));
        }

        panelValidators.updateComponents();
        modelWrapper.getSync().repaintDependencies();
    }

    @Override
    public void updateColors() {
        setBackground(ColorComponents.getColor(ColorType.VALIDATION_BACKGROUND));
        Color foregroundColor = ColorComponents.getColor(ColorType.VALIDATION_FOREGROUND);
        setForeground(foregroundColor);
        labelValidation.setForeground(foregroundColor);
    }

    @Override
    public Dimension getMinimumSize(){
        int panelMinWidth = panelValidators.getMinimumSize().width;
        int width = (panelMinWidth+20 > labelValidationWidth+30+20) ?
                panelMinWidth+20 : labelValidationWidth+30+20;
        return new Dimension(width,panelValidators.getMinimumSize().height + 50);
    }
}
