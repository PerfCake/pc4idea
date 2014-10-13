package org.perfcake.pc4idea.editor.components;

import com.intellij.openapi.project.Project;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 28.9.2014
 * To change this template use File | Settings | File Templates.
 */
public class PropertiesComponent extends AbstractComponent {
    private Color panelColor = Color.getHSBColor(0/360f,0.2f,0.5f);
    private JPanel panelEditor;

    public PropertiesComponent(Project project){
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
        return "Properties Editor";
    }

    @Override
    protected JPanel getPanelEditor() {
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
