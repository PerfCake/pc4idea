package org.perfcake.pc4idea.impl.editor.editor.component;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.ValidationInfo;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.editor.component.AbstractEditor;
import org.perfcake.pc4idea.api.editor.openapi.ui.EditorDialog;
import org.perfcake.pc4idea.api.editor.swing.EditorTablePanel;
import org.perfcake.pc4idea.impl.editor.editor.tablemodel.ReportersTableModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 26.10.2014
 */
public class ReportingEditor extends AbstractEditor {
    private EditorTablePanel tablePanelReporters;
    private PropertiesEditor panelProperties;

    private boolean warningShown = false;
    private Module module;

    public ReportingEditor(Module module) {
        this.module = module;
        initComponents();
    }

    private void initComponents(){
        tablePanelReporters = new EditorTablePanel(new ReportersTableModel(new ArrayList<>(), module));

        panelProperties = new PropertiesEditor();

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup()
            .addComponent(tablePanelReporters)
            .addComponent(panelProperties));
        layout.setVerticalGroup(layout.createSequentialGroup()
            .addComponent(tablePanelReporters)
            .addGap(10)
            .addComponent(panelProperties));
    }

    public void setReporting(Scenario.Reporting reporting){
        tablePanelReporters.setTableModel(new ReportersTableModel(reporting.getReporter(), module));
        panelProperties.setListProperties(reporting.getProperty());

        /*TODO if Reporting has structure properties*/
        panelProperties.setStructureProperties(new ArrayList<>());
    }

    public Scenario.Reporting getReporting(){
        Scenario.Reporting newReporting = new Scenario.Reporting();
        newReporting.getReporter().addAll(((ReportersTableModel) tablePanelReporters.getTableModel()).getReporterList());
        newReporting.getProperty().addAll(panelProperties.getListProperties());
        return newReporting;
    }

    @Override
    public String getTitle(){
        return "Reporting Editor";
    }

    @Override
    public ValidationInfo areInsertedValuesValid() {
        ValidationInfo info = null;
        boolean noneReporter = ((ReportersTableModel) tablePanelReporters.getTableModel()).getReporterList().isEmpty();

        if (noneReporter && !panelProperties.getListProperties().isEmpty() && !warningShown){
            int result = Messages.showYesNoDialog("There are no reporters, but there are some properties\n" +
                                                  "and its not valid. If you continue, properties will be\n" +
                                                  "removed.\n\n" +
                            "Would you like to continue?",
                    "Removing Properties", AllIcons.General.WarningDialog);
            if (result != 0){
                info = new ValidationInfo("OK Interrupted...");
                warningShown = true;
            }
        }

        return info;
    }
}
