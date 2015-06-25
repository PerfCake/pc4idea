package org.perfcake.pc4idea.impl.editor.gui.component;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.actions.ActionType;
import org.perfcake.pc4idea.api.editor.color.ColorType;
import org.perfcake.pc4idea.api.editor.gui.ComponentGui;
import org.perfcake.pc4idea.api.editor.openapi.ui.EditorDialog;
import org.perfcake.pc4idea.impl.editor.actions.EditAction;
import org.perfcake.pc4idea.impl.editor.editor.component.GeneratorEditor;
import org.perfcake.pc4idea.impl.editor.modelwrapper.component.GeneratorModelWrapper;
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
public class GeneratorGui extends ComponentGui {
   private GeneratorModelWrapper modelWrapper;

   private JLabel generatorAttr;

   private int minimumWidth = 0;

   public GeneratorGui(GeneratorModelWrapper modelWrapper) {
      super(modelWrapper.getContext());
      this.modelWrapper = modelWrapper;
      initComponents();
      updateColors();
   }

   private void initComponents() {
      generatorAttr = new JLabel("-");
      generatorAttr.setFont(new Font(generatorAttr.getFont().getName(), 0, 15));

      SpringLayout layout = new SpringLayout();
      this.setLayout(layout);
      this.add(generatorAttr);

      layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, generatorAttr,
            0,
            SpringLayout.HORIZONTAL_CENTER, this);
      layout.putConstraint(SpringLayout.NORTH, generatorAttr,
            10,
            SpringLayout.NORTH, this);

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
      GeneratorEditor editor = new GeneratorEditor(modelWrapper.getContext().getModule());
      editor.setGenerator((Scenario.Generator) modelWrapper.retrieveModel());
      EditorDialog dialog = new EditorDialog(editor);
      dialog.show();
      if (dialog.getExitCode() == 0) {
         return editor.getGenerator();
      }
      return null;
   }

   @Override
   public void updateGui() {
      Scenario.Generator generator = (Scenario.Generator) modelWrapper.retrieveModel();
      generatorAttr.setText(generator.getClazz() + " (" + generator.getThreads() + ")");

      FontMetrics fontMetrics = generatorAttr.getFontMetrics(generatorAttr.getFont());
      minimumWidth = fontMetrics.stringWidth(generatorAttr.getText()) + 30;
   }

   @Override
   public void updateColors() {
      setBackground(ColorComponents.getColor(ColorType.GENERATOR_BACKGROUND));
      Color foregroundColor = ColorComponents.getColor(ColorType.GENERATOR_FOREGROUND);
      setForeground(foregroundColor);
      generatorAttr.setForeground(foregroundColor);
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
