package org.perfcake.pc4idea.impl.editor.gui.component;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.actions.ActionType;
import org.perfcake.pc4idea.api.editor.color.ColorType;
import org.perfcake.pc4idea.api.editor.gui.component.AbstractComponentGUI;
import org.perfcake.pc4idea.api.editor.openapi.ui.EditorDialog;
import org.perfcake.pc4idea.api.util.MessagesValidationSync;
import org.perfcake.pc4idea.impl.editor.actions.AddPropertyAction;
import org.perfcake.pc4idea.impl.editor.actions.DeleteAction;
import org.perfcake.pc4idea.impl.editor.actions.EditAction;
import org.perfcake.pc4idea.impl.editor.editor.component.ValidatorEditor;
import org.perfcake.pc4idea.impl.editor.modelwrapper.ValidationModelWrapper;
import org.perfcake.pc4idea.impl.editor.modelwrapper.ValidatorModelWrapper;
import org.perfcake.pc4idea.todo.Messages;
import org.perfcake.pc4idea.todo.settings.ColorComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Stanislav Kaleta on 3/31/15.
 */
public class ValidatorGUI extends AbstractComponentGUI {
    private ValidatorModelWrapper modelWrapper;
    private ValidationModelWrapper parentModelWrapper;

    private JLabel validatorAttr;

    private Dimension validatorSize = new Dimension(40,40);

    public ValidatorGUI(ValidatorModelWrapper modelWrapper, ValidationModelWrapper parentModelWrapper) {
        super(parentModelWrapper.getGUI().getUtil());
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

        getActionMap().put(ActionType.EDIT, new EditAction(modelWrapper, Messages.BUNDLE.getString("EDIT") + " Validator"));
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.SHIFT_MASK), ActionType.EDIT);

        getActionMap().put(ActionType.DEL, new DeleteAction(parentModelWrapper, modelWrapper, Messages.BUNDLE.getString("DEL") + " Validator"));
        getInputMap().put(KeyStroke.getKeyStroke("DELETE"), ActionType.DEL);

        getActionMap().put(ActionType.ADDP, new AddPropertyAction(modelWrapper, Messages.BUNDLE.getString("ADD") + " Property"));
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.SHIFT_MASK), ActionType.ADDP);
    }

    @Override
    public void performImport(String transferredData) {
        // not used
    }

    @Override
    public Object openEditorDialogAndGetResult() {
        MessagesValidationSync sync = parentModelWrapper.getSync();
        ValidatorEditor editor = new ValidatorEditor(getUtil().getModule(), parentModelWrapper.getSync());
        editor.setValidator((Scenario.Validation.Validator) modelWrapper.retrieveModel());
        EditorDialog dialog = new EditorDialog(editor);
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
        parentModelWrapper.getSync().repaintDependencies();
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
