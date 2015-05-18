package org.perfcake.pc4idea.api.editor.modelwrapper.component;

import org.perfcake.pc4idea.api.editor.editor.ContextProvider;

import javax.swing.*;

/**
 * Created by Stanislav Kaleta on 4/24/15.
 */
public interface ComponentModelWrapper {
    /**
     *  returns context provider
     *
     * @return context provider
     */
    public ContextProvider getContext();

    /**
     *  returns component name
     *
     * @return component name
     */
    public String getName();

    /**
     *  returns component gui
     *
     * @return component gui
     */
    public JPanel getGui();

    /**
     * updates component gui according to component model
     *
     */
    public void updateGui();

    /**
     * Performs commit action. This will invoke update scenario file action.
     * This methods should be called right after action which modifies scenario.
     */
    public void commit(String message);
}
