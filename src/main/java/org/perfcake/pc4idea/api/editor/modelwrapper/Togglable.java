package org.perfcake.pc4idea.api.editor.modelwrapper;

import org.perfcake.pc4idea.api.editor.gui.component.AbstractComponentGUI;

/**
 * Created by Stanislav Kaleta on 3/11/15.
 */
public interface Togglable {
    public AbstractComponentGUI getGUI();
    public void setToggle(boolean toggle);
}
