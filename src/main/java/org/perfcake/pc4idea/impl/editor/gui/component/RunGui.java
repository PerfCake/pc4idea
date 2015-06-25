package org.perfcake.pc4idea.impl.editor.gui.component;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.actions.ActionType;
import org.perfcake.pc4idea.api.editor.color.ColorType;
import org.perfcake.pc4idea.api.editor.gui.ComponentGui;
import org.perfcake.pc4idea.api.editor.openapi.ui.EditorDialog;
import org.perfcake.pc4idea.impl.editor.actions.AddPropertyAction;
import org.perfcake.pc4idea.impl.editor.actions.EditAction;
import org.perfcake.pc4idea.impl.editor.editor.component.RunEditor;
import org.perfcake.pc4idea.impl.editor.modelwrapper.component.RunModelWrapper;
import org.perfcake.pc4idea.impl.settings.ColorComponents;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;

/**
 * Created by Stanislav Kaleta on 3/7/15.
 */
public class RunGui extends ComponentGui {
   private RunModelWrapper modelWrapper;

   private JLabel runAttr;

   private int minimumWidth = 0;

   public RunGui(RunModelWrapper modelWrapper) {
      super(modelWrapper.getContext());
      this.modelWrapper = modelWrapper;
      initComponents();
      updateColors();
   }

   private void initComponents() {
      runAttr = new JLabel("-");
      runAttr.setFont(new Font(runAttr.getFont().getName(), 0, 15));

      SpringLayout layout = new SpringLayout();
      this.setLayout(layout);
      this.add(runAttr);

      layout.putConstraint(SpringLayout.WEST, runAttr,
            15,
            SpringLayout.WEST, this);

      getActionMap().put(ActionType.ADDP, new AddPropertyAction(modelWrapper));
      getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.SHIFT_MASK), ActionType.ADDP);

      getActionMap().put(ActionType.EDIT, new EditAction(modelWrapper));
      getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.SHIFT_MASK), ActionType.EDIT);
   }

   @Override
   public void performImport(String transferredData) {
      // not supported
   }

   @Override
   public Object openEditorDialogAndGetResult() {
      RunEditor editor = new RunEditor(modelWrapper.getContext().getModule());
      editor.setRun((Scenario.Run) modelWrapper.retrieveModel());
      EditorDialog dialog = new EditorDialog(editor);
      dialog.show();
      if (dialog.getExitCode() == 0) {
         return editor.getRun();
      }
      return null;
   }

   @Override
   public void updateGui() {
      Scenario.Run run = (Scenario.Run) modelWrapper.retrieveModel();
      runAttr.setText(run.getType() + " : " + run.getValue());

      FontMetrics fontMetrics = runAttr.getFontMetrics(runAttr.getFont());
      minimumWidth = fontMetrics.stringWidth(runAttr.getText()) + 30;
   }

   @Override
   public void updateColors() {
      setBackground(ColorComponents.getColor(ColorType.RUN_BACKGROUND));
      Color foregroundColor = ColorComponents.getColor(ColorType.RUN_FOREGROUND);
      setForeground(foregroundColor);
      runAttr.setForeground(foregroundColor);
   }

   @Override
   public Dimension getMinimumSize() {
      return new Dimension(minimumWidth, 70);
   }

   @Override
   public Dimension getPreferredSize() {
      return new Dimension(super.getPreferredSize().width, 70);
   }

   @Override
   public Dimension getMaximumSize() {
      return new Dimension(super.getMaximumSize().width, 70);
   }
}
