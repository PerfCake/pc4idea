package org.perfcake.pc4idea.api.editor.modelwrapper.component;

import org.perfcake.pc4idea.api.editor.modelwrapper.Model;

import java.util.List;

/**
 * Created by Stanislav Kaleta on 3/11/15. TODO documentation
 */
public interface HasGUIChildren extends Model {

    /**
     *
     * @return
     */
    public List<ComponentModelWrapper> getChildrenModels();

    /**
     *
     * @param childrenModels
     */
    public void setChildrenFromModels(List<ComponentModelWrapper> childrenModels);

    /**
     *
     * @param childModelWrapper
     */
    public void deleteChild(ComponentModelWrapper childModelWrapper);
}
