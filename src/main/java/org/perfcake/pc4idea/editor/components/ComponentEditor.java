package org.perfcake.pc4idea.editor.components;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 3.11.2014
 */
public class ComponentEditor extends DialogWrapper {
    private JPanel centerPanel;

    public ComponentEditor(String title, JPanel centerPanel){
        super(false);
        setTitle(title);
        this.centerPanel = centerPanel;
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
        return null; /*TODO centerPanel.insertedValuesAreValid -> return val.indo if not valid*/
    }

}
