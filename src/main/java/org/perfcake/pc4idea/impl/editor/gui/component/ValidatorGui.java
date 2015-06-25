package org.perfcake.pc4idea.impl.editor.gui.component;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.actions.ActionType;
import org.perfcake.pc4idea.api.editor.color.ColorType;
import org.perfcake.pc4idea.api.editor.gui.ComponentGui;
import org.perfcake.pc4idea.api.editor.openapi.ui.EditorDialog;
import org.perfcake.pc4idea.api.util.MessagesValidationSync;
import org.perfcake.pc4idea.impl.editor.actions.AddPropertyAction;
import org.perfcake.pc4idea.impl.editor.actions.DeleteAction;
import org.perfcake.pc4idea.impl.editor.actions.EditAction;
import org.perfcake.pc4idea.impl.editor.editor.component.ValidatorEditor;
import org.perfcake.pc4idea.impl.editor.modelwrapper.component.ValidationModelWrapper;
import org.perfcake.pc4idea.impl.editor.modelwrapper.component.ValidatorModelWrapper;
import org.perfcake.pc4idea.impl.settings.ColorComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Stanislav Kaleta on 3/31/15.
 */
public class ValidatorGui extends ComponentGui {
    private ValidatorModelWrapper modelWrapper;

    private JLabel validatorAttr;

    private Dimension validatorSize = new Dimension(40,40);

    public ValidatorGui(ValidatorModelWrapper modelWrapper, ValidationModelWrapper parentModelWrapper) {
        super(modelWrapper.getContext());
        this.modelWrapper = modelWrapper;
        initComponents(parentModelWrapper);
        updateColors();
    }

    private void initComponents(ValidationModelWrapper parentModelWrapper) {
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
                ((JPanel) e.getComponent().getAccessibleContext().getAccessibleParent()).dispatchEvent(e);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                ((JPanel) e.getComponent().getAccessibleContext().getAccessibleParent()).dispatchEvent(e);
            }
            @Override
            public void mouseReleased(MouseEvent e){
                ((JPanel) e.getComponent().getAccessibleContext().getAccessibleParent()).dispatchEvent(e);
            }
        });

        getActionMap().put(ActionType.EDIT, new EditAction(modelWrapper));
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.SHIFT_MASK), ActionType.EDIT);

        getActionMap().put(ActionType.DEL, new DeleteAction(parentModelWrapper, modelWrapper));
        getInputMap().put(KeyStroke.getKeyStroke("DELETE"), ActionType.DEL);

        getActionMap().put(ActionType.ADDP, new AddPropertyAction(modelWrapper));
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.SHIFT_MASK), ActionType.ADDP);
    }

    @Override
    public void performImport(String transferredData) {
        // not used
    }

    @Override
    public Object openEditorDialogAndGetResult() {
        MessagesValidationSync sync = modelWrapper.getSync();
        ValidatorEditor editor = new ValidatorEditor(modelWrapper.getContext().getModule(), sync);
        editor.setValidator((Scenario.Validation.Validator) modelWrapper.retrieveModel());
        EditorDialog dialog = new EditorDialog(editor);
        dialog.show();
        if (dialog.getExitCode() == 0) {
            return editor.getValidator();
        }
        return null;
    }

    @Override
    public void updateGui() {
        Scenario.Validation.Validator validator =
                (Scenario.Validation.Validator) modelWrapper.retrieveModel();
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
        modelWrapper.getSync().repaintDependencies();
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
