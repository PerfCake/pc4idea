package org.perfcake.pc4idea.api.editor.modelwrapper.component;

/**
 * Created by Stanislav Kaleta on 3/11/15.
 */
public interface Togglable extends ComponentModelWrapper {

    /**
     *  set component as enabled / disabled
     *
     * @param toggle
     */
    public void setToggle(boolean toggle);
}
