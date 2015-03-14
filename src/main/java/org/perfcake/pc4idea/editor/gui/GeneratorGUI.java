package org.perfcake.pc4idea.editor.gui;

import com.intellij.icons.AllIcons;
import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.Messages;
import org.perfcake.pc4idea.editor.ScenarioDialogEditor;
import org.perfcake.pc4idea.editor.colors.ColorComponents;
import org.perfcake.pc4idea.editor.colors.ColorType;
import org.perfcake.pc4idea.editor.editor.PerfCakeEditor;
import org.perfcake.pc4idea.editor.editors.GeneratorEditor;
import org.perfcake.pc4idea.editor.editors.PropertyEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stanislav Kaleta on 3/7/15.
 */
public class GeneratorGUI extends AbstractComponentGUI {
    private Scenario.Generator generator;

    private JLabel generatorAttr;
    private JLabel generatorRunAttr;

    private int minimumWidth = 0;

    public GeneratorGUI(ActionMap actionMap){
        super(actionMap);
        initComponents();
        updateColors();
    }

    private void initComponents(){
        generatorAttr = new JLabel("-");
        generatorAttr.setFont(new Font(generatorAttr.getFont().getName(), 0, 15));
        generatorRunAttr = new JLabel("-");
        generatorRunAttr.setFont(new Font(generatorRunAttr.getFont().getName(), 0, 15));

        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        this.add(generatorAttr);
        this.add(generatorRunAttr);

        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, generatorAttr,
                0,
                SpringLayout.HORIZONTAL_CENTER, this);
        layout.putConstraint(SpringLayout.NORTH, generatorAttr,
                10,
                SpringLayout.NORTH, this);

        layout.putConstraint(SpringLayout.WEST, generatorRunAttr,
                15,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, generatorRunAttr,8,SpringLayout.SOUTH, generatorAttr);

        this.getActionMap().put("ADDP", new AbstractAction(Messages.BUNDLE.getString("ADD")+" Property", AllIcons.General.Add) {
            @Override
            public void actionPerformed(ActionEvent e) {
                PropertyEditor editor = new PropertyEditor();
                ScenarioDialogEditor dialog = new ScenarioDialogEditor(editor);
                dialog.show();
                if (dialog.getExitCode() == 0) {
                    Property property = editor.getProperty();
                    Scenario.Generator generator = (Scenario.Generator) GeneratorGUI.this.getComponentModel();
                    generator.getProperty().add(property);
                    GeneratorGUI.this.setComponentModel(generator);
                    GeneratorGUI.this.commitChanges(Messages.BUNDLE.getString("ADD")+" Property");
                }
            }
        });
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.SHIFT_MASK), "ADDP");

        this.getActionMap().put("EDIT", new AbstractAction(Messages.BUNDLE.getString("EDIT")+" Generator", AllIcons.Actions.Edit) {
            @Override
            public void actionPerformed(ActionEvent e) {
                GeneratorGUI.this.openEditor();
            }
        });
        getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.SHIFT_MASK), "EDIT");
    }

    @Override
    protected List<JMenuItem> getMenuItems() {
        List<JMenuItem> menuItems = new ArrayList<>();

        JMenuItem addPropertyItem = new JMenuItem();
        addPropertyItem.setAction(this.getActionMap().get("ADDP"));
        addPropertyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.SHIFT_MASK));
        menuItems.add(addPropertyItem);

        JMenuItem editItem = new JMenuItem();
        editItem.setAction(this.getActionMap().get("EDIT"));
        editItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.SHIFT_MASK));
        menuItems.add(editItem);

        return menuItems;
    }

    @Override
    protected void performImport(String transferredData) {
        // not supported
    }

    @Override
    protected void openEditor() {
        GeneratorEditor editor = new GeneratorEditor();
        editor.setGenerator(generator);
        ScenarioDialogEditor dialog = new ScenarioDialogEditor(editor);
        dialog.show();
        if (dialog.getExitCode() == 0) {
            this.setComponentModel(editor.getGenerator());
            this.commitChanges(Messages.BUNDLE.getString("EDIT") + " Generator");
        }
    }

    @Override
    public void setComponentModel(Object componentModel) {
        generator = (Scenario.Generator) componentModel;
        generatorAttr.setText(generator.getClazz()+" ("+generator.getThreads()+")");
        generatorRunAttr.setText(generator.getRun().getType()+" : "+generator.getRun().getValue());

        FontMetrics fontMetrics = generatorAttr.getFontMetrics(generatorAttr.getFont());
        int gAttrWidth = fontMetrics.stringWidth(generatorAttr.getText()) + 30;
        int gRunAttrWidth = fontMetrics.stringWidth(generatorRunAttr.getText()) + 30;
        minimumWidth = (gAttrWidth > gRunAttrWidth) ? gAttrWidth : gRunAttrWidth;
    }

    @Override
    public Object getComponentModel() {
        return generator;
    }

    @Override
    public void updateColors() {
        setBackground(ColorComponents.getColor(ColorType.GENERATOR_BACKGROUND));
        Color foregroundColor = ColorComponents.getColor(ColorType.GENERATOR_FOREGROUND);
        setForeground(foregroundColor);
        generatorAttr.setForeground(foregroundColor);
        generatorRunAttr.setForeground(foregroundColor);
    }

    @Override
    public Dimension getMinimumSize(){
        return new Dimension(minimumWidth,70);
    }

    @Override
    public Dimension getPreferredSize(){
        return new Dimension(super.getPreferredSize().width,70);
    }

    @Override
    public Dimension getMaximumSize(){
        return new Dimension(super.getMaximumSize().width,70);
    }
}
