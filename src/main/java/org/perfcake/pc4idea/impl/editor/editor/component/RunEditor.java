package org.perfcake.pc4idea.impl.editor.editor.component;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.editor.component.AbstractEditor;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.ValidationInfo;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * @author <a href="mailto:kstanleykale@gmail.com">Stanislav Kaleta</a>
 */
public class RunEditor extends AbstractEditor {
   private JComboBox comboBoxRunType;
   private JTextField textFieldRunValue;

   private Module module;

   public RunEditor(Module module) {
      this.module = module;
      initComponents();
   }

   private void initComponents() {
      JLabel labelRunType = new JLabel("Run type:");
      JLabel labelRunValue = new JLabel("Duration:");

      String[] runs = new String[] { "iteration", "time", "percentage" };
      comboBoxRunType = new ComboBox(new DefaultComboBoxModel<>(runs));
      comboBoxRunType.setSelectedIndex(-1);
      textFieldRunValue = new JTextField();

      GroupLayout layout = new GroupLayout(this);
      this.setLayout(layout);
      layout.setHorizontalGroup(layout.createParallelGroup()
                                      .addGroup(layout.createSequentialGroup()
                                                      .addComponent(labelRunType, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                                                      .addComponent(comboBoxRunType))
                                      .addGroup(layout.createSequentialGroup()
                                                      .addComponent(labelRunValue, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                                                      .addComponent(textFieldRunValue)));
      layout.setVerticalGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup()
                                                    .addComponent(labelRunType, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(comboBoxRunType, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                                    .addGap(10)
                                    .addGroup(layout.createParallelGroup()
                                                    .addComponent(labelRunValue, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(textFieldRunValue, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                                    .addGap(10));
   }

   public void setRun(Scenario.Run run) {
      comboBoxRunType.setSelectedItem(run.getType());
      textFieldRunValue.setText(run.getValue());
   }

   public Scenario.Run getRun() {
      Scenario.Run newRun = new Scenario.Run();
      newRun.setType((String) comboBoxRunType.getSelectedItem());
      newRun.setValue(textFieldRunValue.getText());
      return newRun;
   }

   @Override
   public String getTitle() {
      return "Run Editor";
   }

   @Override
   public ValidationInfo areInsertedValuesValid() {
      ValidationInfo info = null;
      if (textFieldRunValue.getText().isEmpty()) {
         info = new ValidationInfo("Run value can't be empty");
      }
      if (comboBoxRunType.getSelectedIndex() == -1) {
         info = new ValidationInfo("Run type isn't selected");
      }
      return info;
   }
}
