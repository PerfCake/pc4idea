package org.perfcake.pc4idea.editor.gui;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.Messages;
import org.perfcake.pc4idea.editor.MessagesValidationSync;
import org.perfcake.pc4idea.editor.ScenarioDialogEditor;
import org.perfcake.pc4idea.editor.actions.ActionType;
import org.perfcake.pc4idea.editor.actions.AddPropertyAction;
import org.perfcake.pc4idea.editor.actions.DeleteAction;
import org.perfcake.pc4idea.editor.actions.EditAction;
import org.perfcake.pc4idea.editor.colors.ColorComponents;
import org.perfcake.pc4idea.editor.colors.ColorType;
import org.perfcake.pc4idea.editor.editors.ValidatorEditor;
import org.perfcake.pc4idea.editor.modelwrapper.ValidationModelWrapper;
import org.perfcake.pc4idea.editor.modelwrapper.ValidatorModelWrapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Set;

/**
 * Created by Stanislav Kaleta on 3/31/15.
 */
public class ValidatorGUI extends AbstractComponentGUI {
    private ValidatorModelWrapper modelWrapper;
    private ValidationModelWrapper parentModelWrapper;

    private JLabel validatorAttr;

    private Dimension validatorSize = new Dimension(40,40);

    public ValidatorGUI(ValidatorModelWrapper modelWrapper, ValidationModelWrapper parentModelWrapper, ActionMap actionMap) {
        super(actionMap);
        this.modelWrapper = modelWrapper;
        this.parentModelWrapper = parentModelWrapper;
        initComponents();
        updateColors();
    }

    private void initComponents() {
        validatorAttr = new JLabel("-");
        validatorAttr.setFont(new Font(validatorAttr.getFont().getName(), 0, 15));

        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        this.add(validatorAttr);

        layout.putConstraint(SpringLayout.NORTH, validatorAttr,
                10,
                SpringLayout.NORTH, this);

        layout.putConstraint(SpringLayout.WEST, validatorAttr,
                15,
                SpringLayout.WEST, this);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ((JPanel)e.getComponent().getAccessibleContext().getAccessibleParent()).dispatchEvent(e);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                ((JPanel)e.getComponent().getAccessibleContext().getAccessibleParent()).dispatchEvent(e);
            }
            @Override
            public void mouseReleased(MouseEvent e){
                ((JPanel)e.getComponent().getAccessibleContext().getAccessibleParent()).dispatchEvent(e);
            }
        });

        this.getActionMap().put(ActionType.EDIT, new EditAction(modelWrapper, Messages.BUNDLE.getString("EDIT")+" Validator"));
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.SHIFT_MASK), ActionType.EDIT);

        this.getActionMap().put(ActionType.DEL, new DeleteAction(parentModelWrapper, modelWrapper,Messages.BUNDLE.getString("DEL")+" Validator"));
        this.getInputMap().put(KeyStroke.getKeyStroke("DELETE"), ActionType.DEL);

        this.getActionMap().put(ActionType.ADDP, new AddPropertyAction(modelWrapper, Messages.BUNDLE.getString("ADD")+" Property"));
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.SHIFT_MASK), ActionType.ADDP);
    }

    @Override
    void performImport(String transferredData) {
        // not used
    }

    @Override
    public Object openEditorDialogAndGetResult() {
        MessagesValidationSync sync = parentModelWrapper.getSync();
        ValidatorEditor editor = new ValidatorEditor(sync.getValidatorIDs());
        Set<String> attachedValidatorIDs = sync.getAttachedValidatorIDs();
        Scenario.Validation.Validator validator = (Scenario.Validation.Validator) modelWrapper.retrieveModel();
        editor.setValidator(validator,attachedValidatorIDs.contains(validator.getId()));
        ScenarioDialogEditor dialog = new ScenarioDialogEditor(editor);
        dialog.show();
        if (dialog.getExitCode() == 0) {
            return editor.getValidator();
        }
        return null;
    }

    @Override
    public void updateGUI() {
        Scenario.Validation.Validator validator = (Scenario.Validation.Validator) modelWrapper.retrieveModel();
        FontMetrics fontMetrics = validatorAttr.getFontMetrics(validatorAttr.getFont());
        int idWidth = fontMetrics.stringWidth(validator.getId());
        String id;
        if (idWidth > 100){
            id = validator.getId().substring(0,5)+"...";
        } else {
            id = validator.getId();
        }
        validatorAttr.setText("("+id+") "+validator.getClazz());
        validatorSize.width = fontMetrics.stringWidth(validatorAttr.getText()) + 30;
    }

    @Override
    public void updateColors() {
        setBackground(ColorComponents.getColor(ColorType.VALIDATOR_BACKGROUND));
        Color foregroundColor = ColorComponents.getColor(ColorType.VALIDATOR_FOREGROUND);
        setForeground(foregroundColor);
        validatorAttr.setForeground(foregroundColor);
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
