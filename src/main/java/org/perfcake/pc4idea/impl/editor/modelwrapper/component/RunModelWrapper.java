package org.perfcake.pc4idea.impl.editor.modelwrapper.component;

import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.editor.ContextProvider;
import org.perfcake.pc4idea.api.editor.modelwrapper.component.AccessibleModel;
import org.perfcake.pc4idea.api.editor.modelwrapper.component.CanAddProperty;
import org.perfcake.pc4idea.api.util.Messages;
import org.perfcake.pc4idea.impl.editor.actions.CommitAction;
import org.perfcake.pc4idea.impl.editor.gui.component.RunGui;

import java.awt.event.ActionEvent;
import javax.swing.JPanel;

/**
 * Created by Stanislav Kaleta on 3/16/15.
 */
public class RunModelWrapper implements AccessibleModel, CanAddProperty {
   private Scenario.Run runModel;

   private RunGui runGui;

   private ContextProvider context;

   public RunModelWrapper(ContextProvider context) {
      this.context = context;
      runGui = new RunGui(this);
   }

   @Override
   public ContextProvider getContext() {
      return context;
   }

   @Override
   public String getName() {
      return Messages.Scenario.GENERATOR;
   }

   @Override
   public void commit(String message) {
      new CommitAction(context).actionPerformed(new ActionEvent(this, 1, message));
   }

   @Override
   public JPanel getGui() {
      return runGui;
   }

   @Override
   public void updateGui() {
      runGui.updateGui();
   }

   @Override
   public void updateModel(Object componentModel) {
      if (runModel == null) {
         runModel = new Scenario.Run();
      }
   }

   @Override
   public Object retrieveModel() {
      return runModel;
   }

   @Override
   public void addProperty(final Property property) {
      // nop
   }
}
