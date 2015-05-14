package org.perfcake.pc4idea.impl.wizard;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBList;
import org.jetbrains.annotations.NotNull;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.editor.component.AbstractEditor;
import org.perfcake.pc4idea.api.util.Messages;
import org.perfcake.pc4idea.api.util.MessagesValidationSync;
import org.perfcake.pc4idea.impl.editor.editor.component.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 13.11.2014
 */
class WizardPanel extends JPanel {
    private JBList wizardStepList;
    private JPanel panelEditors;

    private Map<Integer, AbstractEditor> editors;
    private boolean validationRunning = true;
    private String defaultURI;

    private WizardDialog.WizardEvent wizardEvent;

    private Module module;
    MessagesValidationSync sync;

    WizardPanel(String defaultURI, @NotNull Module module) {
        this.module = module;
        this.defaultURI = defaultURI;
        sync = new MessagesValidationSync(null);
        initComponents();
        this.setPreferredSize(new Dimension(480, 240));
    }

    private void initComponents() {
        editors = new HashMap<>();
        editors.put(0, new ScenarioDefinitionEditor(defaultURI));
        editors.put(1, new GeneratorEditor(module));
        editors.put(2, new SenderEditor(module));
        editors.put(3, new MessagesEditor(sync));
        editors.put(4, new ReportingEditor(module));
        editors.put(5, new ValidationEditor(module, sync));
        editors.put(6, new PropertiesEditor());

        sync.setWizardMode((MessagesEditor) editors.get(3), (ValidationEditor) editors.get(5));

        panelEditors = new JPanel(new GridLayout(1, 1));
        panelEditors.add(editors.get(0));

        ListModel<String> model = new AbstractListModel<String>() {
            private final String[] steps = new String[]{Messages.Scenario.SCENARIO,
                    Messages.Scenario.GENERATOR, Messages.Scenario.SENDER,
                    Messages.Scenario.MESSAGES, Messages.Scenario.REPORTING,
                    Messages.Scenario.VALIDATION, Messages.Scenario.PROPERTIES};

            @Override
            public int getSize() {
                return steps.length;
            }

            @Override
            public String getElementAt(int index) {
                return steps[index];
            }
        };
        wizardStepList = new JBList(model);

        /*TODO maybe refact as class*/
        wizardStepList.setCellRenderer(new DefaultListCellRenderer() {
            private boolean isSelected = false;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2D = (Graphics2D) g;

                if (isSelected) {
                    g2D.setColor(EditorColorsManager.getInstance().getGlobalScheme().getDefaultForeground());
                    g2D.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
                    g2D.drawRect(1, 1, this.getWidth() - 3, this.getHeight() - 3);
                }
            }

            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                this.isSelected = isSelected;

                Color rgb = EditorColorsManager.getInstance().getGlobalScheme().getDefaultBackground();
                float brightness = Color.RGBtoHSB(rgb.getRed(), rgb.getGreen(), rgb.getBlue(), new float[]{0, 0, 0})[2];

                Color backgroundColor = null;
                if (index < 3) {
                    backgroundColor = (editors.get(index).areInsertedValuesValid() == null) ?
                            Color.getHSBColor(120 / 360f, 0.20f, brightness) : Color.getHSBColor(0 / 360f, 0.20f, brightness);
                } else {
                    boolean isInserted = false;
                    switch (index) {
                        case 3: {
                            isInserted = !((MessagesEditor) editors.get(index)).getMessages().getMessage().isEmpty();
                            break;
                        }
                        case 4: {
                            isInserted = !((ReportingEditor) editors.get(index)).getReporting().getReporter().isEmpty();
                            break;
                        }
                        case 5: {
                            isInserted = !((ValidationEditor) editors.get(index)).getValidation().getValidator().isEmpty();
                            break;
                        }
                        case 6: {
                            isInserted = !((PropertiesEditor) editors.get(index)).getObjProperties().getProperty().isEmpty();
                            break;
                        }
                    }
                    backgroundColor = (isInserted) ? Color.getHSBColor(220 / 360f, 0.20f, brightness) : Color.getHSBColor(240 / 360f, 0f, brightness);
                }

                super.setText(" " + value);
                super.setFont(new Font(this.getFont().getName(), this.getFont().getStyle(), 15));
                super.setBackground(backgroundColor);
                super.setForeground(EditorColorsManager.getInstance().getGlobalScheme().getDefaultForeground());

                return this;
            }
        });
        wizardStepList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        wizardStepList.setSelectedIndex(0);
        wizardStepList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                wizardEvent.wizardStepListClicked(wizardStepList.getSelectedIndex());
            }
        });

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setVerticalGroup(layout.createParallelGroup()
                .addComponent(wizardStepList)
                .addComponent(panelEditors));
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addComponent(wizardStepList, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                .addGap(25)
                .addComponent(panelEditors));

        ApplicationManager.getApplication().executeOnPooledThread(new Runnable() {
            @Override
            public void run() {
                while (validationRunning) {
                    try {
                        Thread.sleep(500);
                        wizardStepList.repaint();
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        });
    }

    protected void addWizardEvent(WizardDialog.WizardEvent wizardEvent) {
        this.wizardEvent = wizardEvent;
    }

    protected void selectEditor(int pointer) {
        panelEditors.removeAll();

        if (pointer == 5 || pointer == 3) {
            sync.syncValidatorRef();
        }

        panelEditors.add(editors.get(pointer));
        wizardStepList.setSelectedIndex(pointer);
        panelEditors.revalidate();
        panelEditors.repaint();
        wizardStepList.repaint();
    }

    protected ValidationInfo areRequiredPartsInserted() {
        ValidationInfo info = null;
        if (editors.get(2).areInsertedValuesValid() != null) {
            info = new ValidationInfo("Sender editor isn't complete : " + editors.get(2).areInsertedValuesValid().message);
        }
        if (editors.get(1).areInsertedValuesValid() != null) {
            info = new ValidationInfo("Generator editor isn't complete : " + editors.get(1).areInsertedValuesValid().message);
        }
        if (editors.get(0).areInsertedValuesValid() != null) {
            info = new ValidationInfo("Scenario editor isn't complete : " + editors.get(0).areInsertedValuesValid().message);
        }
        return info;
    }

    public void stopCheckingValidity() {
        validationRunning = false;
    }

    protected String getScenarioName() {
        return ((ScenarioDefinitionEditor) editors.get(0)).getScenarioName();
    }

    protected String getScenarioType() {
        return ((ScenarioDefinitionEditor) editors.get(0)).getScenarioType();
    }

    protected VirtualFile getScenarioDirectory() {
        return ((ScenarioDefinitionEditor) editors.get(0)).getScenarioDirectory();
    }

    protected Scenario getScenarioModel() {
        Scenario model = new Scenario();
        model.setGenerator(((GeneratorEditor) editors.get(1)).getGenerator());
        model.setSender(((SenderEditor) editors.get(2)).getSender());

        Scenario.Messages messages = ((MessagesEditor) editors.get(3)).getMessages();
        messages = (messages.getMessage().isEmpty()) ? null : messages;
        model.setMessages(messages);

        Scenario.Reporting reporting = ((ReportingEditor) editors.get(4)).getReporting();
        reporting = (reporting.getReporter().isEmpty()) ? null : reporting;
        model.setReporting(reporting);

        Scenario.Validation validation = ((ValidationEditor) editors.get(5)).getValidation();
        validation = (validation.getValidator().isEmpty()) ? null : validation;
        model.setValidation(validation);

        Scenario.Properties properties = ((PropertiesEditor) editors.get(6)).getObjProperties();
        properties = (properties.getProperty().isEmpty()) ? null : properties;
        model.setProperties(properties);

        return model;
    }
}
