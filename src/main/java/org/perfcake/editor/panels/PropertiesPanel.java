package org.perfcake.editor.panels;

import com.intellij.openapi.project.Project;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 28.9.2014
 * To change this template use File | Settings | File Templates.
 */
public class PropertiesPanel extends AbstractComponentPanel {
    private JPanel panelEditor;

    public PropertiesPanel(Project project){
        super(project);
        panelEditor = initPanelEditor();

    }
    private JPanel initPanelEditor(){
        JPanel panel = new JPanel();
        /*TODO ...*/
        return panel;
    }

    @Override
    protected Color getColor() {
        return Color.gray;
    }

    @Override
    protected String getEditorTitle() {
        return "Properties Editor";
    }

    @Override
    protected JPanel getPanelEditor() {
        return panelEditor;
    }

    @Override
    protected void applyChanges() {

    }


}
