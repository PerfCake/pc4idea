package org.perfcake.pc4idea.editor.gui;

import org.perfcake.model.Property;
import org.perfcake.pc4idea.editor.Messages;
import org.perfcake.pc4idea.editor.ScenarioDialogEditor;
import org.perfcake.pc4idea.editor.actions.ActionType;
import org.perfcake.pc4idea.editor.actions.DeleteAction;
import org.perfcake.pc4idea.editor.actions.EditAction;
import org.perfcake.pc4idea.editor.colors.ColorComponents;
import org.perfcake.pc4idea.editor.colors.ColorType;
import org.perfcake.pc4idea.editor.editors.PropertyEditor;
import org.perfcake.pc4idea.editor.interfaces.HasGUIChildren;
import org.perfcake.pc4idea.editor.interfaces.ModelWrapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by Stanislav Kaleta on 3/7/15.
 */
public class PropertyGUI extends AbstractComponentGUI {
    private ModelWrapper propertyModelWrapper;
    private ModelWrapper parentModelWrapper;

    private JLabel propertyAttr;

    private Dimension propertySize = new Dimension(40, 40);

    public PropertyGUI(ModelWrapper propertyModelWrapper, ModelWrapper parentModelWrapper, ActionMap baseActionMap) {
        super(baseActionMap);
        this.propertyModelWrapper = propertyModelWrapper;
        this.parentModelWrapper = parentModelWrapper;
        initComponents();
        updateColors();
    }

    private void initComponents() {
        propertyAttr = new JLabel("-");
        propertyAttr.setFont(new Font(propertyAttr.getFont().getName(), 0, 15));

        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        this.add(propertyAttr);

        layout.putConstraint(SpringLayout.NORTH, propertyAttr,
                10,
                SpringLayout.NORTH, this);

        layout.putConstraint(SpringLayout.WEST, propertyAttr,
                15,
                SpringLayout.WEST, this);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                ((JPanel)e.getComponent().getAccessibleContext().getAccessibleParent()).dispatchEvent(e);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                ((JPanel)e.getComponent().getAccessibleContext().getAccessibleParent()).dispatchEvent(e);
            }
            @Override
            public void mouseReleased(MouseEvent e){
                ((JPanel)e.getComponent().getAccessibleContext().getAccessibleParent()).dispatchEvent(e);
            }
        });



        getActionMap().put(ActionType.EDIT, new EditAction(propertyModelWrapper,Messages.BUNDLE.getString("EDIT")+" Property"));
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.SHIFT_MASK), ActionType.EDIT);

        this.getActionMap().put(ActionType.DEL, new DeleteAction((HasGUIChildren) parentModelWrapper,propertyModelWrapper,Messages.BUNDLE.getString("DEL")+" Property"));
        getInputMap().put(KeyStroke.getKeyStroke("DELETE"), ActionType.DEL);
    }

    @Override
    protected void performImport(String transferredData) {
        // not used
    }

    @Override
    public Object openEditorDialogAndGetResult() {
        PropertyEditor editor = new PropertyEditor();
        editor.setProperty((Property) propertyModelWrapper.retrieveModel());
        ScenarioDialogEditor dialog = new ScenarioDialogEditor(editor);
        dialog.show();
        if (dialog.getExitCode() == 0){
            return editor.getProperty();
        }
        return null;
    }

    @Override
    public void updateGUI() {
        Property property = (Property) propertyModelWrapper.retrieveModel();
        propertyAttr.setText(property.getName()+" : "+property.getValue());
        FontMetrics fontMetrics = propertyAttr.getFontMetrics(propertyAttr.getFont());
        propertySize.width = fontMetrics.stringWidth(propertyAttr.getText()) + 30;
    }

    @Override
    public void updateColors() {
        setBackground(ColorComponents.getColor(ColorType.PROPERTIES_BACKGROUND));
        Color foregroundColor = ColorComponents.getColor(ColorType.PROPERTIES_FOREGROUND);
        setForeground(foregroundColor);
        propertyAttr.setForeground(foregroundColor);
    }

    @Override
    public Dimension getMinimumSize(){
        return propertySize;
    }

    @Override
    public Dimension getPreferredSize(){
        return propertySize;
    }

    @Override
    public Dimension getMaximumSize(){
        return propertySize;
    }
}
