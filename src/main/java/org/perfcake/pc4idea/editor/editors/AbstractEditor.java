package org.perfcake.pc4idea.editor.editors;

import com.intellij.openapi.ui.ValidationInfo;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 11.11.2014
 */
public abstract class AbstractEditor extends JPanel {
    public AbstractEditor(){
        this.setPreferredSize(new Dimension(350,0));
    }

    public abstract String getTitle();
    public abstract ValidationInfo areInsertedValuesValid();
}
