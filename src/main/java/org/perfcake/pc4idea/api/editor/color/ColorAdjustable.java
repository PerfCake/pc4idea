package org.perfcake.pc4idea.api.editor.color;

/**
 * Created by miron on 2.11.2014.
 * This interface is intended for Swing gui,
 * which wants to support coloring via ColorComponents API.
 */
public interface ColorAdjustable {

    /**
     * This method is called from ColorComponents to update settings according to user preferences.
     * Implementation should get the current settings from ColorComponents method getColors.
     */
    void updateColors();
}
