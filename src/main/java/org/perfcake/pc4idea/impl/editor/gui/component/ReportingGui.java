package org.perfcake.pc4idea.impl.editor.gui.component;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.actions.ActionType;
import org.perfcake.pc4idea.api.editor.color.ColorType;
import org.perfcake.pc4idea.api.editor.gui.ComponentGui;
import org.perfcake.pc4idea.api.editor.openapi.ui.EditorDialog;
import org.perfcake.pc4idea.api.editor.swing.ComponentsPanel;
import org.perfcake.pc4idea.api.util.Messages;
import org.perfcake.pc4idea.impl.editor.actions.AddPropertyAction;
import org.perfcake.pc4idea.impl.editor.actions.AddReporterAction;
import org.perfcake.pc4idea.impl.editor.actions.EditAction;
import org.perfcake.pc4idea.impl.editor.actions.ReorderAction;
import org.perfcake.pc4idea.impl.editor.editor.component.ReportingEditor;
import org.perfcake.pc4idea.impl.editor.modelwrapper.component.ReportingModelWrapper;
import org.perfcake.pc4idea.impl.settings.ColorComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * Created by Stanislav Kaleta on 3/7/15.
 */
public class ReportingGui extends ComponentGui {
    private ReportingModelWrapper modelWrapper;

    private JLabel labelReporting;
    private ComponentsPanel panelReporters;

    private int labelReportingWidth = 0;

    public ReportingGui(ReportingModelWrapper modelWrapper) {
        super(modelWrapper.getContext());
        this.modelWrapper = modelWrapper;
        initComponents();
        updateColors();
    }

    private void initComponents() {
        labelReporting = new JLabel(Messages.Scenario.REPORTING);
        labelReporting.setFont(new Font(labelReporting.getFont().getName(), 0, 15));
        FontMetrics fontMetrics = labelReporting.getFontMetrics(labelReporting.getFont());
        labelReportingWidth = fontMetrics.stringWidth(labelReporting.getText());

        panelReporters = new ComponentsPanel(modelWrapper);

        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        this.add(labelReporting);
        this.add(panelReporters);

        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, labelReporting,
                0,
                SpringLayout.HORIZONTAL_CENTER, this);
        layout.putConstraint(SpringLayout.NORTH, labelReporting,
                10,
                SpringLayout.NORTH, this);

        layout.putConstraint(SpringLayout.WEST, panelReporters,
                10,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, panelReporters, 8, SpringLayout.SOUTH, labelReporting);

        getActionMap().put(ActionType.ADDR, new AddReporterAction(modelWrapper));
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.SHIFT_MASK), ActionType.ADDR);

        getActionMap().put(ActionType.ADDP, new AddPropertyAction(modelWrapper));
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.SHIFT_MASK), ActionType.ADDP);

        getActionMap().put(ActionType.EDIT, new EditAction(modelWrapper));
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.SHIFT_MASK), ActionType.EDIT);

        getActionMap().put(ActionType.REORDER, new ReorderAction(modelWrapper, Messages.Scenario.REPORTER));
    }

    @Override
    public void performImport(String transferredData) {
        if (transferredData.contains("Reporter")) {
            ((AddReporterAction) getActionMap().get(ActionType.ADDR)).actionPerformedWrapper(transferredData);
        }
    }

    @Override
    public Object openEditorDialogAndGetResult() {
        ReportingEditor editor = new ReportingEditor(modelWrapper.getContext().getModule());
        editor.setReporting((Scenario.Reporting) modelWrapper.retrieveModel());
        EditorDialog dialog = new EditorDialog(editor);
        dialog.show();
        if (dialog.getExitCode() == 0) {
            return editor.getReporting();
        }
        return null;
    }

    @Override
    public void updateGui() {
        Scenario.Reporting reporting = (Scenario.Reporting) modelWrapper.retrieveModel();
        if (reporting.getReporter().isEmpty()) {
            this.getActionMap().get(ActionType.ADDP).setEnabled(false);
        } else {
            this.getActionMap().get(ActionType.ADDP).setEnabled(true);
        }
        panelReporters.updateComponents();
    }

    @Override
    public void updateColors() {
        setBackground(ColorComponents.getColor(ColorType.REPORTING_BACKGROUND));
        Color foregroundColor = ColorComponents.getColor(ColorType.REPORTING_FOREGROUND);
        setForeground(foregroundColor);
        labelReporting.setForeground(foregroundColor);
    }

    @Override
    public Dimension getMinimumSize() {
        int panelMinWidth = panelReporters.getMinimumSize().width;
        int width = (panelMinWidth + 20 > labelReportingWidth + 30) ?
                panelMinWidth + 20 : labelReportingWidth + 30;
        return new Dimension(width, panelReporters.getMinimumSize().height + 50);
    }
}
