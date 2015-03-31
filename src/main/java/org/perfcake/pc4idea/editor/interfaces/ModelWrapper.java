package org.perfcake.pc4idea.editor.interfaces;

import org.perfcake.pc4idea.editor.gui.AbstractComponentGUI;

/**
 * Created by Stanislav Kaleta on 3/16/15.
 */
public interface ModelWrapper {

    public AbstractComponentGUI getGUI();
    public void updateModel(Object componentModel);
    public Object retrieveModel();
}
