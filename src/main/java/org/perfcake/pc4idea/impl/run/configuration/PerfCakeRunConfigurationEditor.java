package org.perfcake.pc4idea.impl.run.configuration;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * User: Stanislav Kaleta
 * Date: 6.12.2014
 */
public class PerfCakeRunConfigurationEditor extends SettingsEditor<PerfCakeRunConfiguration> {
    private JTextField textFieldScenarioPath;
    private JTextField textFieldScenariosDirPath;
    private JTextField textFieldMessagesDirPath;
    private JTextField textFieldLibDirsPath;



    @Override
    protected void resetEditorFrom(PerfCakeRunConfiguration pcRunConfig) {
        String[] paths = pcRunConfig.getPaths();
        textFieldScenarioPath.setText(paths[0]);
        textFieldScenariosDirPath.setText(paths[1]);
        textFieldMessagesDirPath.setText(paths[2]);
        textFieldLibDirsPath.setText(paths[3]);

        pcRunConfig.setInitialized(true);
    }

    @Override
    protected void applyEditorTo(PerfCakeRunConfiguration pcRunConfig) throws ConfigurationException {
        pcRunConfig.setPaths(new String[]{
                textFieldScenarioPath.getText(),
                textFieldScenariosDirPath.getText(),
                textFieldMessagesDirPath.getText(),
                textFieldLibDirsPath.getText()
        });

        pcRunConfig.checkConfiguration();

        pcRunConfig.setInitialized(true);
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        JLabel labelScenarioFile = new JLabel("Scenario file:");
        textFieldScenarioPath = new JTextField();

        JLabel labelScenarioDir = new JLabel("Scenarios directory:");
        textFieldScenariosDirPath = new JTextField();

        JLabel labelMessagesDir = new JLabel("Messages directory:");
        textFieldMessagesDirPath = new JTextField();

        JLabel labelLibDir = new JLabel("Lib directory:");
        textFieldLibDirsPath = new JTextField();

        JPanel bg = new JPanel();
        GroupLayout layout = new GroupLayout(bg);
        bg.setLayout(layout);
        int prefSize = GroupLayout.PREFERRED_SIZE;
        layout.setHorizontalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                        .addComponent(labelScenarioFile, prefSize, 100, prefSize)
                        .addComponent(textFieldScenarioPath))
                .addGroup(layout.createSequentialGroup()
                        .addComponent(labelScenarioDir, prefSize, 100, prefSize)
                        .addComponent(textFieldScenariosDirPath))
                .addGroup(layout.createSequentialGroup()
                        .addComponent(labelMessagesDir, prefSize, 100, prefSize)
                        .addComponent(textFieldMessagesDirPath))
                .addGroup(layout.createSequentialGroup()
                        .addComponent(labelLibDir, prefSize, 100, prefSize)
                        .addComponent(textFieldLibDirsPath)));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelScenarioFile, prefSize, 25, prefSize)
                        .addComponent(textFieldScenarioPath, prefSize, 25, prefSize))
                .addGap(10)
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelScenarioDir, prefSize, 25, prefSize)
                        .addComponent(textFieldScenariosDirPath, prefSize, 25, prefSize))
                .addGap(10)
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelMessagesDir, prefSize, 25, prefSize)
                        .addComponent(textFieldMessagesDirPath, prefSize, 25, prefSize))
                .addGap(10)
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelLibDir, prefSize, 25, prefSize)
                        .addComponent(textFieldLibDirsPath, prefSize, 25, prefSize))
                .addGap(10));

        return bg;
    }
}
