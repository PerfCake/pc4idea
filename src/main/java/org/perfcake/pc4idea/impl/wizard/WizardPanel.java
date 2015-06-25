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
   private static final int SCENARIO_DEFINITION_WIZARD_INDEX = 0;
   private static final int RUN_WIZARD_INDEX = 1;
   private static final int GENERATOR_WIZARD_INDEX = 2;
   private static final int SENDER_WIZARD_INDEX = 3;
   private static final int MESSAGES_WIZARD_INDEX = 4;
   private static final int REPORTING_WIZARD_INDEX = 5;
   private static final int VALIDATION_WIZARD_INDEX = 6;
   private static final int PROPERTIES_WIZARD_INDEX = 7;

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
      editors.put(SCENARIO_DEFINITION_WIZARD_INDEX, new ScenarioDefinitionEditor(defaultURI));
      editors.put(RUN_WIZARD_INDEX, new RunEditor(module));
      editors.put(GENERATOR_WIZARD_INDEX, new GeneratorEditor(module));
      editors.put(SENDER_WIZARD_INDEX, new SenderEditor(module));
      editors.put(MESSAGES_WIZARD_INDEX, new MessagesEditor(sync, null));
      editors.put(REPORTING_WIZARD_INDEX, new ReportingEditor(module));
      editors.put(VALIDATION_WIZARD_INDEX, new ValidationEditor(module, sync));
      editors.put(PROPERTIES_WIZARD_INDEX, new PropertiesEditor());

      sync.setWizardMode((MessagesEditor) editors.get(MESSAGES_WIZARD_INDEX), (ValidationEditor) editors.get(VALIDATION_WIZARD_INDEX));

      panelEditors = new JPanel(new GridLayout(1, 1));
      panelEditors.add(editors.get(SCENARIO_DEFINITION_WIZARD_INDEX));

      ListModel<String> model = new AbstractListModel<String>() {
         private final String[] steps = new String[] { Messages.Scenario.SCENARIO, Messages.Scenario.RUN,
               Messages.Scenario.GENERATOR, Messages.Scenario.SENDER,
               Messages.Scenario.MESSAGES, Messages.Scenario.REPORTING,
               Messages.Scenario.VALIDATION, Messages.Scenario.PROPERTIES };

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
            float brightness = Color.RGBtoHSB(rgb.getRed(), rgb.getGreen(), rgb.getBlue(), new float[] { 0, 0, 0 })[2];

            Color backgroundColor = null;
            if (index < 4) {
               backgroundColor = (editors.get(index).areInsertedValuesValid() == null) ?
                     Color.getHSBColor(120 / 360f, 0.20f, brightness) : Color.getHSBColor(0 / 360f, 0.20f, brightness);
            } else {
               boolean isInserted = false;
               switch (index) {
                  case MESSAGES_WIZARD_INDEX: {
                     isInserted = !((MessagesEditor) editors.get(index)).getMessages().getMessage().isEmpty();
                     break;
                  }
                  case REPORTING_WIZARD_INDEX: {
                     isInserted = !((ReportingEditor) editors.get(index)).getReporting().getReporter().isEmpty();
                     break;
                  }
                  case VALIDATION_WIZARD_INDEX: {
                     isInserted = !((ValidationEditor) editors.get(index)).getValidation().getValidator().isEmpty();
                     break;
                  }
                  case PROPERTIES_WIZARD_INDEX: {
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
      if (editors.get(SENDER_WIZARD_INDEX).areInsertedValuesValid() != null) {
         info = new ValidationInfo("Sender editor isn't complete : " + editors.get(SENDER_WIZARD_INDEX).areInsertedValuesValid().message);
      }
      if (editors.get(GENERATOR_WIZARD_INDEX).areInsertedValuesValid() != null) {
         info = new ValidationInfo("Generator editor isn't complete : " + editors.get(GENERATOR_WIZARD_INDEX).areInsertedValuesValid().message);
      }
      if (editors.get(RUN_WIZARD_INDEX).areInsertedValuesValid() != null) {
         info = new ValidationInfo("Run editor isn't complete : " + editors.get(RUN_WIZARD_INDEX).areInsertedValuesValid().message);
      }
      if (editors.get(SCENARIO_DEFINITION_WIZARD_INDEX).areInsertedValuesValid() != null) {
         info = new ValidationInfo("Scenario editor isn't complete : " + editors.get(SCENARIO_DEFINITION_WIZARD_INDEX).areInsertedValuesValid().message);
      }
      return info;
   }

   public void stopCheckingValidity() {
      validationRunning = false;
   }

   protected String getScenarioName() {
      return ((ScenarioDefinitionEditor) editors.get(SCENARIO_DEFINITION_WIZARD_INDEX)).getScenarioName();
   }

   protected String getScenarioType() {
      return ((ScenarioDefinitionEditor) editors.get(SCENARIO_DEFINITION_WIZARD_INDEX)).getScenarioType();
   }

   protected VirtualFile getScenarioDirectory() {
      return ((ScenarioDefinitionEditor) editors.get(SCENARIO_DEFINITION_WIZARD_INDEX)).getScenarioDirectory();
   }

   protected Scenario getScenarioModel() {
      Scenario model = new Scenario();
      model.setRun((((RunEditor) editors.get(RUN_WIZARD_INDEX)).getRun()));
      model.setGenerator(((GeneratorEditor) editors.get(GENERATOR_WIZARD_INDEX)).getGenerator());
      model.setSender(((SenderEditor) editors.get(SENDER_WIZARD_INDEX)).getSender());

      Scenario.Messages messages = ((MessagesEditor) editors.get(MESSAGES_WIZARD_INDEX)).getMessages();
      messages = (messages.getMessage().isEmpty()) ? null : messages;
      model.setMessages(messages);

      Scenario.Reporting reporting = ((ReportingEditor) editors.get(REPORTING_WIZARD_INDEX)).getReporting();
      reporting = (reporting.getReporter().isEmpty()) ? null : reporting;
      model.setReporting(reporting);

      Scenario.Validation validation = ((ValidationEditor) editors.get(VALIDATION_WIZARD_INDEX)).getValidation();
      validation = (validation.getValidator().isEmpty()) ? null : validation;
      model.setValidation(validation);

      Scenario.Properties properties = ((PropertiesEditor) editors.get(PROPERTIES_WIZARD_INDEX)).getObjProperties();
      properties = (properties.getProperty().isEmpty()) ? null : properties;
      model.setProperties(properties);

      return model;
   }
}
