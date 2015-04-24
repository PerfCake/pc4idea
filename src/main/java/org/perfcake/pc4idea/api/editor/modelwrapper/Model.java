package org.perfcake.pc4idea.api.editor.modelwrapper;

import org.perfcake.pc4idea.api.editor.editor.ContextProvider;

import javax.swing.*;

/**
 * Created by Stanislav Kaleta on 4/24/15.
 */
public interface Model {
    /**
     *
     * @return
     */
    public ContextProvider getContext();

    /**
     *
     * @return
     */
    public String getName();

    /**
     *
     * @return
     */
    public JPanel getGui();

    /**
     *
     */
    public void updateGui();

    /**
     * Performs commit action. This will cause that competent manager will load the model from the editor todo
     * and will invoke update scenario file action. This methods should be called right after
     * action which modifies scenario.
     */
    public void commit(String message);



}
