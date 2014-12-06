package org.perfcake.pc4idea.configuration;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 6.12.2014
 */
public class PCConfigurationEditor extends SettingsEditor<PCRunConfiguration> {
    private JTextField textFieldScenarioName;

    @Override
    protected void resetEditorFrom(PCRunConfiguration pcRunConfig) {
        textFieldScenarioName.setText(pcRunConfig.getScenarioName());
        pcRunConfig.setInitialized(true);
    }

    @Override
    protected void applyEditorTo(PCRunConfiguration pcRunConfig) throws ConfigurationException {
        pcRunConfig.setScenarioName(textFieldScenarioName.getText());
        pcRunConfig.setInitialized(true);
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        JLabel label = new JLabel("Scenario name:");
        textFieldScenarioName = new JTextField();

        JPanel bg = new JPanel();
        GroupLayout layout = new GroupLayout(bg);
        bg.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                        .addComponent(label, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                        .addComponent(textFieldScenarioName)));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                        .addComponent(label, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                        .addComponent(textFieldScenarioName, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                .addGap(10));

        return bg;
    }
}
