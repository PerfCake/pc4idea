package org.perfcake.pc4idea.editor;

import org.perfcake.pc4idea.editor.gui.AbstractComponentGUI;

import java.util.List;

/**
 * Created by Stanislav Kaleta on 3/11/15.
 */
public interface ChildrenHaveGUI {

    public List<AbstractComponentGUI> getChildrenAsGUI();
    public void setChildrenFromGUI(List<AbstractComponentGUI> childrenAsGUI);
    public int getWidth();
}
