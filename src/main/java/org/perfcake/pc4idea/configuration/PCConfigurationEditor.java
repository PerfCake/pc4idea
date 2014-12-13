package org.perfcake.pc4idea.configuration;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.ui.ComboBox;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 6.12.2014
 */
public class PCConfigurationEditor extends SettingsEditor<PCRunConfiguration> {
    private JTextField textFieldScenarioName;
    private JComboBox comboBoxModule;

    @Override
    protected void resetEditorFrom(PCRunConfiguration pcRunConfig) {
        String text = (pcRunConfig.getScenarioFile() == null) ? "" : pcRunConfig.getScenarioFile().getName();
        textFieldScenarioName.setText(text);
        comboBoxModule.removeAllItems();
        for (Module module : ModuleManager.getInstance(pcRunConfig.getProject()).getModules()){
            comboBoxModule.addItem(module.getName());
            if (module.equals(pcRunConfig.getMoudle())){
                comboBoxModule.setSelectedItem(module.getName());
            }
        }
        pcRunConfig.setInitialized(true);
    }

    @Override
    protected void applyEditorTo(PCRunConfiguration pcRunConfig) throws ConfigurationException {
        Module module = ModuleManager.getInstance(pcRunConfig.getProject()).findModuleByName((String)comboBoxModule.getSelectedItem());
        pcRunConfig.tryFindScenarioFileByName(textFieldScenarioName.getText(),module);
        pcRunConfig.setModule(module);
        pcRunConfig.setInitialized(true);
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        JLabel labelName = new JLabel("Scenario file:");
        textFieldScenarioName = new JTextField();
        JLabel labelModule = new JLabel("Module:");
        comboBoxModule = new ComboBox();


        JPanel bg = new JPanel();
        GroupLayout layout = new GroupLayout(bg);
        bg.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                        .addComponent(labelName, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                        .addComponent(textFieldScenarioName))
                .addGroup(layout.createSequentialGroup()
                        .addComponent(labelModule, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                        .addComponent(comboBoxModule)));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelName, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                        .addComponent(textFieldScenarioName, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                .addGap(10)
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelModule, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                        .addComponent(comboBoxModule, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                .addGap(10));

        return bg;
    }
}
