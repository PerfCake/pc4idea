package org.perfcake.pc4idea.api.editor.modelwrapper.component;

/**
 * Created by Stanislav Kaleta on 3/16/15.
 */
public interface AccessibleModel extends ComponentModelWrapper {

    /**
     *  updates component model
     *
     * @param componentModel model of scenario component
     */
    public void updateModel(Object componentModel);

    /**
     * returns component model
     *
     * @return model of scenario component
     */
    public Object retrieveModel();
}
