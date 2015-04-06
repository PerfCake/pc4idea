package org.perfcake.pc4idea.editor.interfaces;

import org.perfcake.pc4idea.editor.gui.AbstractComponentGUI;

/**
 * Created by Stanislav Kaleta on 3/11/15.
 */
public interface Togglable {
    public AbstractComponentGUI getGUI();
    public void setToggle(boolean toggle);
}
