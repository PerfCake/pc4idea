package org.perfcake.pc4idea.api.editor.openapi.ui;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import org.jetbrains.annotations.Nullable;
import org.perfcake.pc4idea.api.editor.editor.component.AbstractEditor;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 3.11.2014
 */
public class EditorDialog extends DialogWrapper {
    private AbstractEditor centerPanel;

    public EditorDialog(AbstractEditor centerPanel) {
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
