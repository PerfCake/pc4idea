package org.perfcake.pc4idea.impl.editor.gui.component;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.actions.ActionType;
import org.perfcake.pc4idea.api.editor.color.ColorType;
import org.perfcake.pc4idea.api.editor.gui.component.AbstractComponentGUI;
import org.perfcake.pc4idea.api.editor.openapi.ui.EditorDialog;
import org.perfcake.pc4idea.api.editor.swing.ComponentsPanel;
import org.perfcake.pc4idea.api.editor.swing.JEnabledCircle;
import org.perfcake.pc4idea.impl.editor.actions.*;
import org.perfcake.pc4idea.impl.editor.editor.component.ReporterEditor;
import org.perfcake.pc4idea.impl.editor.modelwrapper.ReporterModelWrapper;
import org.perfcake.pc4idea.impl.editor.modelwrapper.ReportingModelWrapper;
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
public class ReporterGUI extends AbstractComponentGUI {
    private ReporterModelWrapper modelWrapper;
    private ReportingModelWrapper parentModelWrapper;

    private JLabel labelReporterClass;
    private JEnabledCircle enabledCircle;
    private ComponentsPanel panelDestinations;

    private int labelReporterClassWidth = 0;
    private int requiredWidth = Short.MAX_VALUE;

    public ReporterGUI(ReporterModelWrapper modelWrapper, ReportingModelWrapper parentModelWrapper) {
        super(parentModelWrapper.getGUI().getUtil());
        this.modelWrapper = modelWrapper;
        this.parentModelWrapper = parentModelWrapper;
        initComponents();
        updateColors();
    }

    private void initComponents() {
        labelReporterClass = new JLabel("---");
        labelReporterClass.setFont(new Font(labelReporterClass.getFont().getName(), 0, 15));

        enabledCircle = new JEnabledCircle(new ToggleAction(modelWrapper, "Reporter", false));

        panelDestinations = new ComponentsPanel(modelWrapper);

        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        this.add(labelReporterClass);
        this.add(enabledCircle);
        this.add(panelDestinations);

        layout.putConstraint(SpringLayout.NORTH, labelReporterClass,
                10,
                SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.NORTH, enabledCircle,
                10,
                SpringLayout.NORTH, this);
        layout.putConstraint(SpringLayout.WEST, enabledCircle,
                10, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.WEST, labelReporterClass,
                5,
                SpringLayout.EAST, enabledCircle);

        layout.putConstraint(SpringLayout.WEST, panelDestinations,
                10,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, panelDestinations, 8, SpringLayout.SOUTH, labelReporterClass);

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

        getActionMap().put(ActionType.EDIT, new EditAction(modelWrapper, Messages.BUNDLE.getString("EDIT") + " Reporter"));
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.SHIFT_MASK), ActionType.EDIT);

        getActionMap().put(ActionType.DEL, new DeleteAction(parentModelWrapper, modelWrapper, Messages.BUNDLE.getString("DEL") + " Reporter"));
        getInputMap().put(KeyStroke.getKeyStroke("DELETE"), ActionType.DEL);

        getActionMap().put(ActionType.TOGGLE, new ToggleAction(modelWrapper, "Reporter", false));
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.SHIFT_MASK), ActionType.TOGGLE);

        getActionMap().put(ActionType.ADDP, new AddPropertyAction(modelWrapper, Messages.BUNDLE.getString("ADD") + " Property"));
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.SHIFT_MASK), ActionType.ADDP);

        getActionMap().put(ActionType.ADDD, new AddDestinationAction(modelWrapper, Messages.BUNDLE.getString("ADD") + " Destination"));
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.SHIFT_MASK), ActionType.ADDD);
    }

    @Override
    public void performImport(String transferredData) {
        if (transferredData.contains("Destination")) {
            ((AddDestinationAction) getActionMap().get(ActionType.ADDD)).actionPerformedWrapper(transferredData);
        }
    }

    @Override
    public Object openEditorDialogAndGetResult() {
        ReporterEditor editor = new ReporterEditor(getUtil().getModule());
        editor.setReporter((Scenario.Reporting.Reporter) modelWrapper.retrieveModel());
        EditorDialog dialog = new EditorDialog(editor);
        dialog.show();
        if (dialog.getExitCode() == 0) {
            return editor.getReporter();
        }
        return null;
    }

    @Override
    public void updateGUI() {
        Scenario.Reporting.Reporter reporter = (Scenario.Reporting.Reporter) modelWrapper.retrieveModel();

        labelReporterClass.setText(reporter.getClazz());
        FontMetrics fontMetrics = labelReporterClass.getFontMetrics(labelReporterClass.getFont());
        labelReporterClassWidth = fontMetrics.stringWidth(labelReporterClass.getText());

        panelDestinations.updateComponents();

        boolean isEnabled = reporter.isEnabled();
        enabledCircle.setState(isEnabled);
        getActionMap().put(ActionType.TOGGLE, new ToggleAction(modelWrapper, "Reporter", isEnabled));

        requiredWidth = Short.MAX_VALUE;
    }

    @Override
    public void updateColors() {
        setBackground(ColorComponents.getColor(ColorType.REPORTER_BACKGROUND));
        Color foregroundColor = ColorComponents.getColor(ColorType.REPORTER_FOREGROUND);
        setForeground(foregroundColor);
        labelReporterClass.setForeground(foregroundColor);
        enabledCircle.setForeground(foregroundColor);
    }

    @Override
    public Dimension getMinimumSize() {
        int panelMinWidth = panelDestinations.getMinimumSize().width;
        int width = (panelMinWidth + 20 > labelReporterClassWidth + 30 + 20) ? panelMinWidth + 20 : labelReporterClassWidth + 30 + 20;
        return new Dimension(width, panelDestinations.getMinimumSize().height + 50);
    }

    @Override
    public Dimension getPreferredSize() {
        int panelMinWidth = panelDestinations.getMinimumSize().width;
        int width = (panelMinWidth + 20 > labelReporterClassWidth + 30 + 20) ? panelMinWidth + 20 : labelReporterClassWidth + 30 + 20;
        return new Dimension(width, panelDestinations.getMinimumSize().height + 50);
    }

    @Override
    public Dimension getMaximumSize() {
        int panelMinWidth = panelDestinations.getMinimumSize().width;
        int width = (panelMinWidth + 20 > labelReporterClassWidth + 30 + 20) ? panelMinWidth + 20 : labelReporterClassWidth + 30 + 20;
        return new Dimension(width, panelDestinations.getMinimumSize().height + 50);
    }


}
