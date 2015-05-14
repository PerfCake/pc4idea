package org.perfcake.pc4idea.impl.wizard;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.perfcake.pc4idea.api.editor.editor.component.AbstractEditor;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 14.11.2014
 */
public class ScenarioDefinitionEditor extends AbstractEditor {
    private JTextField textFieldScenarioName;
    private JComboBox comboBoxScenarioType;
    private JTextField textFieldDirectoryURI;

    public ScenarioDefinitionEditor(String defaultURI) {
        initComponents();
        textFieldDirectoryURI.setText(defaultURI);
    }

    private void initComponents() { /*TODO refactor to ScenarioEditor - add sc.type combobox*/
        JLabel labelScenarioName = new JLabel("Scenario Name:");
        JLabel labelScenarioType = new JLabel("Directory Type:");
        JLabel labelDirectoryURI = new JLabel("Directory URI:");
        textFieldScenarioName = new JTextField();
        comboBoxScenarioType = new ComboBox(new DefaultComboBoxModel<>(new String[]{"XML", "DSL"}));
        textFieldDirectoryURI = new JTextField();

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                        .addComponent(labelScenarioName, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                        .addComponent(textFieldScenarioName))
                .addGroup(layout.createSequentialGroup()
                        .addComponent(labelScenarioType, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                        .addComponent(comboBoxScenarioType))
                .addGroup(layout.createSequentialGroup()
                        .addComponent(labelDirectoryURI, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                        .addComponent(textFieldDirectoryURI)));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelScenarioName, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                        .addComponent(textFieldScenarioName, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                .addGap(10)
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelScenarioType, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                        .addComponent(comboBoxScenarioType, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                .addGap(10)
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelDirectoryURI, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                        .addComponent(textFieldDirectoryURI, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)));

    }

    public String getScenarioName() {
        return textFieldScenarioName.getText();
    }

    public String getScenarioType(){
        return (String) comboBoxScenarioType.getSelectedItem();
    }

    public VirtualFile getScenarioDirectory() {
        return VirtualFileManager.getInstance().findFileByUrl(textFieldDirectoryURI.getText());
    }

    @Override
    public String getTitle() {
        return "URI Editor";
    }

    @Override
    public ValidationInfo areInsertedValuesValid() {
        ValidationInfo info = null;
        VirtualFile fileByURI = VirtualFileManager.getInstance().findFileByUrl(textFieldDirectoryURI.getText());
        if (fileByURI == null) {
            info = new ValidationInfo("directory doesn't exist");
        } else {
            if (!fileByURI.isDirectory()) {
                info = new ValidationInfo("inserted URI isn't directory");
            }
        }
        if (textFieldScenarioName.getText().isEmpty()) {
            info = new ValidationInfo("scenario name isn't inserted");
        }
        return info;
    }
}
