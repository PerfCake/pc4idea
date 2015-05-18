package org.perfcake.pc4idea.api.editor.modelwrapper.component;

import java.util.List;

/**
 * Created by Stanislav Kaleta on 3/11/15.
 */
public interface HasGUIChildren extends ComponentModelWrapper {

    /**
     * returns model of component children
     *
     * @return models of children
     */
    public List<AccessibleModel> getChildrenModels();

    /**
     *  set up component's children from models
     * @param childrenModels
     */
    public void setChildrenFromModels(List<AccessibleModel> childrenModels);

    /**
     *  deletes specified child
     *
     * @param childModelWrapper child to delete
     */
    public void deleteChild(AccessibleModel childModelWrapper);
}
