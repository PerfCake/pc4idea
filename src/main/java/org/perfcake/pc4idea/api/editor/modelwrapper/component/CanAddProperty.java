package org.perfcake.pc4idea.api.editor.modelwrapper.component;

import org.perfcake.model.Property;

/**
 * Created by Stanislav Kaleta on 3/18/15.
 */
public interface CanAddProperty extends ComponentModelWrapper {

    /**
     *  adds property to component model
     *
     * @param property property to add
     */
    public void addProperty(Property property);
}
