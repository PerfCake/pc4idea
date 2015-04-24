package org.perfcake.pc4idea.api.editor.modelwrapper.component;

import org.perfcake.pc4idea.api.editor.modelwrapper.Model;

/**
 * Created by Stanislav Kaleta on 3/16/15. TODO documentation
 */
public interface ComponentModelWrapper extends Model {

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
