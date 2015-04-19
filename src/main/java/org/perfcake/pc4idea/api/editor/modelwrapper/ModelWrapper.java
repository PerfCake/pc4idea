package org.perfcake.pc4idea.api.editor.modelwrapper;

import org.perfcake.pc4idea.api.editor.gui.component.AbstractComponentGUI;

/**
 * Created by Stanislav Kaleta on 3/16/15. TODO documentation
 */
public interface ModelWrapper {

    public AbstractComponentGUI getGUI();
    public void updateModel(Object componentModel);
    public Object retrieveModel();
}
