package org.perfcake.pc4idea.api.editor.modelwrapper.component;

import org.perfcake.pc4idea.api.editor.modelwrapper.Model;

/**
 * Created by Stanislav Kaleta on 3/11/15. TODO documentation
 */
public interface Togglable extends Model {

    /**
     *
     * @param toggle
     */
    public void setToggle(boolean toggle);
}
