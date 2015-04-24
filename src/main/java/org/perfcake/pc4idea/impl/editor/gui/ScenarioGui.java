package org.perfcake.pc4idea.impl.editor.gui;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.color.ColorAdjustable;
import org.perfcake.pc4idea.api.editor.color.ColorType;
import org.perfcake.pc4idea.api.editor.modelwrapper.component.ComponentModelWrapper;
import org.perfcake.pc4idea.api.editor.swing.DependenciesPanel;
import org.perfcake.pc4idea.api.util.Messages;
import org.perfcake.pc4idea.impl.editor.modelwrapper.ScenarioModelWrapper;
import org.perfcake.pc4idea.todo.settings.ColorComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 6.3.2015
 */
public class ScenarioGui extends JLayeredPane implements ColorAdjustable {
    private ScenarioModelWrapper scenarioModelWrapper;

    private JPanel layerBackground;
    private JPanel layerScenario;
    private DependenciesPanel layerDependencies;

    public ScenarioGui(ScenarioModelWrapper scenarioModelWrapper) {
        this.scenarioModelWrapper = scenarioModelWrapper;
        initComponents();
        updateColors();
    }

    private void initComponents(){
        layerBackground = new JPanel();
        layerScenario = new JPanel();
        layerDependencies = new DependenciesPanel();
        this.add(layerBackground, new Integer(0));
        this.add(layerScenario, new Integer(1));
        this.add(layerDependencies, new Integer(2));
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);

                layerBackground.setBounds(0, 0, ScenarioGui.this.getSize().width, ScenarioGui.this.getSize().height);
                layerScenario.setBounds(0, 0, ScenarioGui.this.getSize().width, ScenarioGui.this.getSize().height);
                layerDependencies.setBounds(0, 0, ScenarioGui.this.getSize().width, ScenarioGui.this.getSize().height);
            }
        });

        JLabel labelError = new JLabel(Messages.Label.SCENARIO_INVALID,SwingConstants.CENTER);
        labelError.setFont(new Font(labelError.getFont().getName(),labelError.getFont().getStyle(),15));
        JLabel labelHint = new JLabel(Messages.Label.SCENARIO_INVALID_HINT,SwingConstants.CENTER);
        labelHint.setFont(new Font(labelHint.getFont().getName(),labelHint.getFont().getStyle(),15));
        SpringLayout layerBackgroundLayout = new SpringLayout();
        layerBackground.setLayout(layerBackgroundLayout);
        layerBackground.add(labelError);
        layerBackground.add(labelHint);
        layerBackgroundLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, labelError,
                0,
                SpringLayout.HORIZONTAL_CENTER, layerBackground);
        layerBackgroundLayout.putConstraint(SpringLayout.VERTICAL_CENTER, labelError,
                0,
                SpringLayout.VERTICAL_CENTER,layerBackground);
        layerBackgroundLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, labelHint,
                0,
                SpringLayout.HORIZONTAL_CENTER, layerBackground);
        layerBackgroundLayout.putConstraint(SpringLayout.NORTH, labelHint,
                0,
                SpringLayout.SOUTH,labelError);
    }

    public void updateGui(){
        Scenario scenarioModel = scenarioModelWrapper.getScenarioModel();

        if (scenarioModel != null){
            layerScenario.removeAll();
            layerScenario.revalidate();
            layerScenario.repaint();

            ComponentModelWrapper[] modelWrappers = scenarioModelWrapper.getScenarioComponents();

            GroupLayout scenarioLayout = new GroupLayout(layerScenario);
            layerScenario.setLayout(scenarioLayout);
            scenarioLayout.setHorizontalGroup(
                    scenarioLayout.createParallelGroup()
                            .addComponent(modelWrappers[0].getGui())
                            .addComponent(modelWrappers[1].getGui())
                            .addGroup(scenarioLayout.createSequentialGroup()
                                    .addGroup(scenarioLayout.createParallelGroup()
                                            .addComponent(modelWrappers[2].getGui())
                                            .addComponent(modelWrappers[3].getGui()))
                                    .addComponent(modelWrappers[4].getGui()))
                            .addComponent(modelWrappers[5].getGui())
            );
            scenarioLayout.setVerticalGroup(
                    scenarioLayout.createSequentialGroup()
                            .addComponent(modelWrappers[0].getGui())
                            .addComponent(modelWrappers[1].getGui())
                            .addGroup(scenarioLayout.createParallelGroup()
                                    .addGroup(scenarioLayout.createSequentialGroup()
                                            .addComponent(modelWrappers[2].getGui())
                                            .addComponent(modelWrappers[3].getGui()))
                                    .addComponent(modelWrappers[4].getGui()))
                            .addComponent(modelWrappers[5].getGui())
                            .addContainerGap(0, Short.MAX_VALUE)
            );
            layerScenario.revalidate();
            layerScenario.repaint();
            layerDependencies.setVisible(true);
            layerScenario.setVisible(true);
        } else {
            layerDependencies.setVisible(false);
            layerScenario.setVisible(false);
        }

    }

    public DependenciesPanel getLayerDependencies(){
        return layerDependencies;
    }

    @Override
    public void updateColors() {
        layerScenario.setBackground(ColorComponents.getColor(ColorType.SCENARIO_BACKGROUND));
    }
}
