package org.perfcake.pc4idea.impl.editor.actions;

import com.intellij.icons.AllIcons;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.openapi.ui.EditorDialog;
import org.perfcake.pc4idea.impl.editor.editor.component.DestinationEditor;
import org.perfcake.pc4idea.impl.editor.modelwrapper.ReporterModelWrapper;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Stanislav Kaleta on 4/18/15.
 */
public class AddDestinationAction extends AbstractAction {
    private ReporterModelWrapper target;
    private String actionName;

    public AddDestinationAction(ReporterModelWrapper target, String actionName) {
        super(actionName, AllIcons.General.Add);
        this.target = target;
        this.actionName = actionName;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        actionPerformedWrapper(null);
    }

    public void actionPerformedWrapper(String destinationClass) {
        DestinationEditor editor = new DestinationEditor(target.getGUI().getUtil().getModule());
        if (destinationClass != null) {
            Scenario.Reporting.Reporter.Destination destination = new Scenario.Reporting.Reporter.Destination();
            destination.setClazz(destinationClass);
            editor.setDestination(destination);
        }
        EditorDialog dialog = new EditorDialog(editor);
        dialog.show();
        if (dialog.getExitCode() == 0) {
            Scenario.Reporting.Reporter.Destination destination = editor.getDestination();
            target.addDestination(destination);
            target.getGUI().commitChanges(actionName);
            target.getGUI().updateGUI();
        }
    }
}
