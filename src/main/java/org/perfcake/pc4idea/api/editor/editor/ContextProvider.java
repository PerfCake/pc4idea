package org.perfcake.pc4idea.api.editor.editor;

import com.intellij.designer.ModuleProvider;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * Created by Stanislav Kaleta on 4/23/15.
 */
public interface ContextProvider extends ModuleProvider {
    /**
     *
     * @return
     */
    public VirtualFile getVirtualFile();
}
