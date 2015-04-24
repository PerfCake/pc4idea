package org.perfcake.pc4idea.impl.editor.actions;

import com.intellij.icons.AllIcons;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.openapi.ui.EditorDialog;
import org.perfcake.pc4idea.api.util.Messages;
import org.perfcake.pc4idea.impl.editor.editor.component.PeriodEditor;
import org.perfcake.pc4idea.impl.editor.modelwrapper.component.DestinationModelWrapper;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Stanislav Kaleta on 4/18/15.
 */
public class AddPeriodAction extends AbstractAction {
    private DestinationModelWrapper target;

    public AddPeriodAction(DestinationModelWrapper target) {
        super(Messages.Command.ADD + " " + Messages.Scenario.PERIOD, AllIcons.General.Add);
        this.target = target;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        PeriodEditor editor = new PeriodEditor();
        EditorDialog dialog = new EditorDialog(editor);
        dialog.show();
        if (dialog.getExitCode() == 0) {
            Scenario.Reporting.Reporter.Destination.Period period = editor.getPeriod();
            target.addPeriod(period);
            target.commit(Messages.Command.ADD + " " + Messages.Scenario.PERIOD);
            target.updateGui();
        }
    }
}
