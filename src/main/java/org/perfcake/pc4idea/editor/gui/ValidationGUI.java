package org.perfcake.pc4idea.editor.gui;

import com.intellij.icons.AllIcons;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.Messages;
import org.perfcake.pc4idea.editor.ScenarioDialogEditor;
import org.perfcake.pc4idea.editor.actions.*;
import org.perfcake.pc4idea.editor.colors.ColorComponents;
import org.perfcake.pc4idea.editor.colors.ColorType;
import org.perfcake.pc4idea.editor.editors.ValidationEditor;
import org.perfcake.pc4idea.editor.modelwrapper.ValidationModelWrapper;
import org.perfcake.pc4idea.editor.swing.ComponentsPanel;
import org.perfcake.pc4idea.editor.swing.JEnabledCircle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * Created by Stanislav Kaleta on 3/7/15.
 */
public class ValidationGUI extends AbstractComponentGUI  {
    private ValidationModelWrapper modelWrapper;

    private JLabel labelValidation;
    private JEnabledCircle enabledCircle;
    private ComponentsPanel panelValidators;

    private int labelValidationWidth = 0;

    public ValidationGUI(ValidationModelWrapper modelWrapper, ActionMap baseActionMap){
        super(baseActionMap);
        this.modelWrapper = modelWrapper;
        initComponents();
        updateColors();
    }

    private void initComponents(){
        labelValidation = new JLabel("Validation");
        labelValidation.setFont(new Font(labelValidation.getFont().getName(),0,15));
        FontMetrics fontMetrics = labelValidation.getFontMetrics(labelValidation.getFont());
        labelValidationWidth = fontMetrics.stringWidth(labelValidation.getText());

        enabledCircle = new JEnabledCircle(new ToggleAction(modelWrapper, "Validation"));

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

        this.getActionMap().put(ActionType.ADDV, new AddValidatorAction(modelWrapper, Messages.BUNDLE.getString("ADD")+" Validator"));
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.SHIFT_MASK), ActionType.ADDV);

        this.getActionMap().put(ActionType.EDIT, new EditAction(modelWrapper, Messages.BUNDLE.getString("EDIT")+" Validator"));
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.SHIFT_MASK), ActionType.EDIT);

        this.getActionMap().put(ActionType.TOGGLE, new ToggleAction(modelWrapper, "Validation"));
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.SHIFT_MASK), ActionType.TOGGLE);

        this.getActionMap().put(ActionType.REORDER, new ReorderAction(modelWrapper, "Validation: " + Messages.BUNDLE.getString("REORDER") + " Validator"));
    }

    public Point getValidatorAnchorPoint(Scenario.Validation.Validator validator){
        Point anchorPoint = panelValidators.getComponentAnchorPoint(validator, false);
        anchorPoint.setLocation(anchorPoint.getX()+this.getX(),anchorPoint.getY()+this.getY());
        return anchorPoint;

    }

    @Override
    void performImport(String transferredData) {
        if (transferredData.contains("Validator")){
            ((AddValidatorAction)getActionMap().get(ActionType.ADDV)).actionPerformedWrapper(transferredData);
        }
    }

    @Override
    public Object openEditorDialogAndGetResult() {
        ValidationEditor editor = new ValidationEditor(modelWrapper.getSync());
        Scenario.Validation model = (Scenario.Validation) modelWrapper.retrieveModel();
        editor.setValidation((model == null) ? new Scenario.Validation() : model);
        ScenarioDialogEditor dialog = new ScenarioDialogEditor(editor);
        dialog.show();
        if (dialog.getExitCode() == 0) {
            return editor.getValidation();
        }
        return null;
    }

    @Override
    public void updateGUI() {
        Scenario.Validation validation = (Scenario.Validation) modelWrapper.retrieveModel();
        if (validation == null){
            enabledCircle.setVisible(false);
        } else {
            enabledCircle.setVisible(true);
            enabledCircle.setState(validation.isEnabled());
            boolean isEnabled = validation.isEnabled();
            if (isEnabled) {
                String actionName = Messages.BUNDLE.getString("DISABLE") + " Validation";
                Icon icon = AllIcons.Debugger.MuteBreakpoints;
                this.getActionMap().put(ActionType.TOGGLE, new ToggleAction(modelWrapper, actionName, icon, isEnabled));
            } else {
                String actionName = Messages.BUNDLE.getString("ENABLE") + " Validation";
                Icon icon = AllIcons.Debugger.Db_muted_verified_breakpoint;
                this.getActionMap().put(ActionType.TOGGLE, new ToggleAction(modelWrapper, actionName, icon, isEnabled));
            }
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
        int width = (panelMinWidth+20 > labelValidationWidth+30+20) ? panelMinWidth+20 : labelValidationWidth+30+20;
        return new Dimension(width,panelValidators.getMinimumSize().height + 50);
    }
}
