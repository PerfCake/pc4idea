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


    public WizardDialog(){
        super(false);
        setTitle("Creating PerfCake Scenario");
        this.setResizable(true);
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return new WizardPanel();
    }

    @Override
    protected ValidationInfo doValidate(){
        return null;
    }

    @NotNull
    @Override
    protected Action[] createLeftSideActions() {
        return new Action[]{new NextAction(),new BackAction()};
    }

    private class NextAction extends DialogWrapperAction {
        private NextAction() {
            super("Next");
            //putValue(DEFAULT_ACTION, Boolean.TRUE);
        }

        @Override
        protected void doAction(ActionEvent e) {
            System.out.println("NEXT");
        }
    }

    private class BackAction extends DialogWrapperAction {
        private BackAction() {
            super("Back");
            //putValue(DEFAULT_ACTION, Boolean.TRUE);
        }

        @Override
        protected void doAction(ActionEvent e) {
            System.out.println("BACK");
        }
    }
}
