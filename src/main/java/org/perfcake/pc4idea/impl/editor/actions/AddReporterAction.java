package org.perfcake.pc4idea.impl.editor.actions;

import com.intellij.icons.AllIcons;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.openapi.ui.EditorDialog;
import org.perfcake.pc4idea.api.util.Messages;
import org.perfcake.pc4idea.impl.editor.editor.component.ReporterEditor;
import org.perfcake.pc4idea.impl.editor.modelwrapper.component.ReportingModelWrapper;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Stanislav Kaleta on 4/18/15.
 */
public class AddReporterAction extends AbstractAction {
    private ReportingModelWrapper target;

    public AddReporterAction(ReportingModelWrapper target) {
        super(Messages.Command.ADD + " " +Messages.Scenario.REPORTER, AllIcons.General.Add);
        this.target = target;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        actionPerformedWrapper(null);
    }

    public void actionPerformedWrapper(String reporterClass) {
        ReporterEditor editor = new ReporterEditor(target.getContext().getModule());
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
            target.commit(Messages.Command.ADD + " " +Messages.Scenario.REPORTER);
            target.updateGui();
        }
    }
}
