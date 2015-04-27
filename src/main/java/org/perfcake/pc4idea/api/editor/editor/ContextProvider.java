package org.perfcake.pc4idea.api.editor.editor;

import com.intellij.designer.ModuleProvider;
import com.intellij.openapi.vfs.VirtualFile;
import org.perfcake.pc4idea.impl.editor.modelwrapper.ScenarioModelWrapper;

/**
 * Created by Stanislav Kaleta on 4/23/15.
 */
public interface ContextProvider extends ModuleProvider {
    /**
     *
     * @return
     */
    public VirtualFile getVirtualFile();

    /**
     *
     * @return
     */
    public ScenarioModelWrapper getModel();
}
