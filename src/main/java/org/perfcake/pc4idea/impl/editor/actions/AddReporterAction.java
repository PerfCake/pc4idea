package org.perfcake.pc4idea.impl.editor.actions;

import com.intellij.icons.AllIcons;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.openapi.ui.EditorDialog;
import org.perfcake.pc4idea.impl.editor.editor.component.ReporterEditor;
import org.perfcake.pc4idea.impl.editor.modelwrapper.ReportingModelWrapper;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Stanislav Kaleta on 4/18/15.
 */
public class AddReporterAction extends AbstractAction {
    private ReportingModelWrapper target;
    private String actionName;

    public AddReporterAction(ReportingModelWrapper target, String actionName) {
        super(actionName, AllIcons.General.Add);
        this.target = target;
        this.actionName = actionName;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        actionPerformedWrapper(null);
    }

    public void actionPerformedWrapper(String reporterClass) {
        ReporterEditor editor = new ReporterEditor(target.getGUI().getUtil().getModule());
        if (reporterClass != null) {
            Scenario.Reporting.Reporter reporter = new Scenario.Reporting.Reporter();
            reporter.setClazz(reporterClass);
            editor.setReporter(reporter);
        }
        EditorDialog dialog = new EditorDialog(editor);
        dialog.show();
        if (dialog.getExitCode() == 0) {
            Scenario.Reporting.Reporter reporter = editor.getReporter();
            target.addReporter(reporter);
            target.getGUI().commitChanges(actionName);
            target.getGUI().updateGUI();
        }
    }
}
