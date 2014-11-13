package org.perfcake.pc4idea.editor.designer.common;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import org.jetbrains.annotations.Nullable;
import org.perfcake.pc4idea.editor.designer.editors.AbstractEditor;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 3.11.2014
 */
public class ScenarioDialogEditor extends DialogWrapper {
    private AbstractEditor centerPanel;

    public ScenarioDialogEditor(AbstractEditor centerPanel){
        super(false);
        setTitle(centerPanel.getTitle());
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
        return centerPanel.areInsertedValuesValid();
    }

}
