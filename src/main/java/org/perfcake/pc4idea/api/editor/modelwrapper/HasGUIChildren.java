package org.perfcake.pc4idea.api.editor.modelwrapper;

import org.perfcake.pc4idea.api.editor.gui.component.AbstractComponentGUI;

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
