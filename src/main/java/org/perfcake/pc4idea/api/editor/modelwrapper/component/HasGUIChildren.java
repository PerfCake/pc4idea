package org.perfcake.pc4idea.api.editor.modelwrapper.component;

import java.util.List;

/**
 * Created by Stanislav Kaleta on 3/11/15. TODO documentation
 */
public interface HasGUIChildren extends ComponentModelWrapper {

    /**
     *
     * @return
     */
    public List<AccessibleModel> getChildrenModels();

    /**
     *
     * @param childrenModels
     */
    public void setChildrenFromModels(List<AccessibleModel> childrenModels);

    /**
     *
     * @param childModelWrapper
     */
    public void deleteChild(AccessibleModel childModelWrapper);
}
