package org.perfcake.pc4idea.editor.panels;

import com.intellij.openapi.project.Project;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 28.9.2014
 * To change this template use File | Settings | File Templates.
 */
public class ValidationPanel extends AbstractPanel {
    private Color panelColor = Color.getHSBColor(340/360f,1f,0.75f);
    private JPanel panelEditor;

    public ValidationPanel(Project project){
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
        return panelColor;
    }

    @Override
    protected String getEditorTitle() {
        return "Validation Editor";
    }

    @Override
    protected JPanel getEditorPanel() {
        return panelEditor;
    }

    @Override
    protected void applyChanges() {

    }

    @Override
    public void setComponent(Object component) {

    }

    @Override
    public Object getComponent() {
        return null;
    }


}
