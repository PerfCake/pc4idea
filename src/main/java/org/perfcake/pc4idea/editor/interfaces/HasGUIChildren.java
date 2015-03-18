package org.perfcake.pc4idea.editor.interfaces;

import org.perfcake.pc4idea.editor.gui.AbstractComponentGUI;

import java.util.List;

/**
 * Created by Stanislav Kaleta on 3/11/15.
 */
public interface HasGUIChildren {

    public AbstractComponentGUI getGUI();
    public List<ModelWrapper> getChildrenModels();
    public void setChildrenFromModels(List<ModelWrapper> childrenModels);
    public void deleteChild(ModelWrapper childModelWrapper);
}
