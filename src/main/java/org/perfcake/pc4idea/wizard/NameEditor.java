package org.perfcake.pc4idea.wizard;

import com.intellij.openapi.ui.ValidationInfo;
import org.perfcake.pc4idea.editor.designer.editors.AbstractEditor;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 14.11.2014
 */
public class NameEditor extends AbstractEditor {




    public String getScenarioName(){
        return "-"; /*TODO*/
    }

    public NameEditor(){
        this.setBackground(Color.orange);
    }
    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public ValidationInfo areInsertedValuesValid() {
        return null;//new ValidationInfo("not implemented");
    }
}
