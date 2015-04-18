package org.perfcake.pc4idea.api.editor.modelwrapper;

import org.perfcake.model.Property;
import org.perfcake.pc4idea.api.editor.gui.component.AbstractComponentGUI;

/**
 * Created by Stanislav Kaleta on 3/18/15.
 */
public interface CanAddProperty {

    public AbstractComponentGUI getGUI();
    public void addProperty(Property property);
}
