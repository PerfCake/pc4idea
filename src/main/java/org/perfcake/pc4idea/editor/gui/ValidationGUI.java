package org.perfcake.pc4idea.editor.gui;

import org.perfcake.pc4idea.editor.interfaces.ModelWrapper;

import javax.swing.*;

/**
 * Created by Stanislav Kaleta on 3/7/15.
 */
public class ValidationGUI extends AbstractComponentGUI  {

    public ValidationGUI(ModelWrapper validationModelWrapper, ActionMap baseActionMap){
        super(baseActionMap);
    }


    @Override
    void performImport(String transferredData) {

    }

    @Override
    public Object openEditorDialogAndGetResult() {
        return null;
    }

    @Override
    public void updateGUI() {

    }

    @Override
    public void updateColors() {

    }
}
