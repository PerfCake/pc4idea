package org.perfcake.pc4idea.api.editor.modelwrapper.component;

import org.perfcake.model.Property;
import org.perfcake.pc4idea.api.editor.modelwrapper.Model;

/**
 * Created by Stanislav Kaleta on 3/18/15. TODO documentation
 */
public interface CanAddProperty extends Model {

    /**
     *
     * @param property
     */
    public void addProperty(Property property);
}
