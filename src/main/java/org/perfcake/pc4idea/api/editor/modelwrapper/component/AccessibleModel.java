package org.perfcake.pc4idea.api.editor.modelwrapper.component;

/**
 * Created by Stanislav Kaleta on 3/16/15. TODO documentation
 */
public interface AccessibleModel extends ComponentModelWrapper {

    /**
     *
     * @param componentModel
     */
    public void updateModel(Object componentModel);

    /**
     *
     * @return
     */
    public Object retrieveModel();
}
