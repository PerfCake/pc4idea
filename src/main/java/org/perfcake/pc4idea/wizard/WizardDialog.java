package org.perfcake.pc4idea.wizard;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 13.11.2014
 */
public class WizardDialog extends DialogWrapper {
    private WizardPanel centerPanel;
    private BackAction backAction;
    private NextAction nextAction;

    private int pointer;

    public WizardDialog(WizardPanel centerPanel){
        super(false);
        setTitle("Creating PerfCake Scenario");
        this.centerPanel = centerPanel;
        centerPanel.addWizardEvent(new WizardEvent());
        backAction = new BackAction();
        nextAction = new NextAction();
        pointer = 0;
        this.setResizable(true);
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return centerPanel;
    }

    @Override
    protected ValidationInfo doValidate(){
        return centerPanel.areRequiredPartsInserted();
    }

    @NotNull
    @Override
    protected Action[] createLeftSideActions() {
        return new Action[]{backAction,nextAction};
    }

    private class NextAction extends DialogWrapperAction {
        private NextAction() {
            super("Next");
        }

        @Override
        protected void doAction(ActionEvent e) {
            if (pointer == 0){
                backAction.setEnabled(true);
            }
            pointer++;
            if (pointer == 6){
                nextAction.setEnabled(false);
            }
            centerPanel.selectEditor(pointer);

        }
    }

    private class BackAction extends DialogWrapperAction {
        private BackAction() {
            super("Back");
            this.setEnabled(false);
        }

        @Override
        protected void doAction(ActionEvent e) {
            if (pointer == 6){
                nextAction.setEnabled(true);
            }
            pointer--;
            if (pointer == 0){
                backAction.setEnabled(false);
            }
            centerPanel.selectEditor(pointer);
        }
    }

    protected class WizardEvent {
        protected void wizardStepListClicked(int selectedIndex){
            pointer = selectedIndex;
            if (!nextAction.isEnabled()){
                nextAction.setEnabled(true);
            }
            if (pointer == 6){
                nextAction.setEnabled(false);
            }
            if (!backAction.isEnabled()){
                backAction.setEnabled(true);
            }
            if (pointer == 0){
                backAction.setEnabled(false);
            }
            centerPanel.selectEditor(pointer);
        }
    }
}
