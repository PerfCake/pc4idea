package org.perfcake.pc4idea.api.editor.editor.component;

import com.intellij.openapi.ui.ValidationInfo;

import javax.swing.*;
import java.awt.*;

/**
 * Superclass for every dialog editor.
 *
 * @author Stanislav Kaleta
 */
public abstract class AbstractEditor extends JPanel {

    public AbstractEditor(){
        this.setPreferredSize(new Dimension(350,0));
    }

    /**
     * Provides title value for dialog window.
     *
     * @return title value
     */
    public abstract String getTitle();

    /**
     * Returns information about inserted values. If some value isn't valid, method returns ValidationInfo instance
     *  which interrupts closing editor action and shows error message.
     *
     * @return ValidationInfo instance if some value isn't valid, null otherwise
     */
    public abstract ValidationInfo areInsertedValuesValid();
}
