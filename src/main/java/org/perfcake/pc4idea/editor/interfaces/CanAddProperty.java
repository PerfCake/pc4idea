package org.perfcake.pc4idea.editor.interfaces;

import org.perfcake.model.Property;
import org.perfcake.pc4idea.editor.gui.AbstractComponentGUI;

/**
 * Created by Stanislav Kaleta on 3/18/15.
 */
public interface CanAddProperty {

    public AbstractComponentGUI getGUI();
    public void addProperty(Property property);
}
