package org.perfcake.pc4idea.impl.editor.gui.component;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.actions.ActionType;
import org.perfcake.pc4idea.api.editor.color.ColorType;
import org.perfcake.pc4idea.api.editor.gui.component.AbstractComponentGUI;
import org.perfcake.pc4idea.api.editor.openapi.ui.EditorDialog;
import org.perfcake.pc4idea.api.editor.swing.JEnabledCircle;
import org.perfcake.pc4idea.impl.editor.actions.*;
import org.perfcake.pc4idea.impl.editor.editor.component.DestinationEditor;
import org.perfcake.pc4idea.impl.editor.modelwrapper.DestinationModelWrapper;
import org.perfcake.pc4idea.impl.editor.modelwrapper.ReporterModelWrapper;
import org.perfcake.pc4idea.todo.Messages;
import org.perfcake.pc4idea.todo.settings.ColorComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Stanislav Kaleta on 3/11/15.
 */
public class DestinationGUI extends AbstractComponentGUI {
    private DestinationModelWrapper modelWrapper;
    private ReporterModelWrapper parentModelWrapper;

    private JLabel destinationAttr;
    private JEnabledCircle enabledCircle;

    private Dimension destinationSize = new Dimension(40, 40);

    public DestinationGUI(DestinationModelWrapper modelWrapper, ReporterModelWrapper parentModelWrapper) {
        super(parentModelWrapper.getGUI().getUtil());
        this.modelWrapper = modelWrapper;
        this.parentModelWrapper = parentModelWrapper;
        initComponents();
        updateColors();
    }

    private void initComponents() {
        destinationAttr = new JLabel("-");
        destinationAttr.setFont(new Font(destinationAttr.getFont().getName(), 0, 15));

        enabledCircle = new JEnabledCircle(new ToggleAction(modelWrapper, "Destination", false));

        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        this.add(destinationAttr);
        this.add(enabledCircle);

        layout.putConstraint(SpringLayout.NORTH, enabledCircle,
                10,
                SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.WEST, enabledCircle,
                10,
                SpringLayout.WEST, this);

        layout.putConstraint(SpringLayout.NORTH, destinationAttr,
                10,
                SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.WEST, destinationAttr,
                5,
                SpringLayout.EAST, enabledCircle);

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
            public void mouseReleased(MouseEvent e) {
                ((JPanel) e.getComponent().getAccessibleContext().getAccessibleParent()).dispatchEvent(e);
            }
        });

        getActionMap().put(ActionType.EDIT, new EditAction(modelWrapper, Messages.BUNDLE.getString("EDIT") + " Destination"));
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.SHIFT_MASK), ActionType.EDIT);

        getActionMap().put(ActionType.DEL, new DeleteAction(parentModelWrapper, modelWrapper, Messages.BUNDLE.getString("DEL") + " Destination"));
        getInputMap().put(KeyStroke.getKeyStroke("DELETE"), ActionType.DEL);

        getActionMap().put(ActionType.TOGGLE, new ToggleAction(modelWrapper, "Destination", false));
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.SHIFT_MASK), ActionType.TOGGLE);

        getActionMap().put(ActionType.ADDP, new AddPropertyAction(modelWrapper, Messages.BUNDLE.getString("ADD") + " Property"));
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.SHIFT_MASK), ActionType.ADDP);

        getActionMap().put(ActionType.ADDPE, new AddPeriodAction(modelWrapper, Messages.BUNDLE.getString("ADD") + " Period"));
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.SHIFT_MASK), ActionType.ADDPE);
    }

    @Override
    public void performImport(String transferredData) {
        // not used
    }

    @Override
    public Object openEditorDialogAndGetResult() {
        DestinationEditor editor = new DestinationEditor(getUtil().getModule());
        editor.setDestination((Scenario.Reporting.Reporter.Destination) modelWrapper.retrieveModel());
        EditorDialog dialog = new EditorDialog(editor);
        dialog.show();
        if (dialog.getExitCode() == 0) {
            return editor.getDestination();
        }
        return null;
    }

    @Override
    public void updateGUI() {
        Scenario.Reporting.Reporter.Destination destination = (Scenario.Reporting.Reporter.Destination) modelWrapper.retrieveModel();
        destinationAttr.setText(destination.getClazz());
        FontMetrics fontMetrics = destinationAttr.getFontMetrics(destinationAttr.getFont());
        destinationSize.width = fontMetrics.stringWidth(destinationAttr.getText()) + 25 + 20;

        boolean isEnabled = destination.isEnabled();
        enabledCircle.setState(isEnabled);
        getActionMap().put(ActionType.TOGGLE, new ToggleAction(modelWrapper, "Destination", isEnabled));
    }

    @Override
    public void updateColors() {
        setBackground(ColorComponents.getColor(ColorType.DESTINATION_BACKGROUND));
        Color foregroundColor = ColorComponents.getColor(ColorType.DESTINATION_FOREGROUND);
        setForeground(foregroundColor);
        destinationAttr.setForeground(foregroundColor);
        enabledCircle.setForeground(foregroundColor);
    }

    @Override
    public Dimension getMinimumSize() {
        return destinationSize;
    }

    @Override
    public Dimension getPreferredSize() {
        return destinationSize;
    }

    @Override
    public Dimension getMaximumSize() {
        return destinationSize;
    }
}
