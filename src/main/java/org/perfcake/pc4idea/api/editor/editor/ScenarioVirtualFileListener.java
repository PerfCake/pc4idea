package org.perfcake.pc4idea.api.editor.editor;

import com.intellij.openapi.vfs.VirtualFileAdapter;
import com.intellij.openapi.vfs.VirtualFileEvent;
import org.jetbrains.annotations.NotNull;
import org.perfcake.pc4idea.impl.editor.editor.ScenarioEditor;

/**
 * Created by Stanislav Kaleta on 4/27/15.
 */
public class ScenarioVirtualFileListener extends VirtualFileAdapter {

    public ScenarioVirtualFileListener(ScenarioEditor editor){
        /*TODO*/
    }
    @Override
    public void contentsChanged(@NotNull VirtualFileEvent event){
        /*TODO for testing purpose*/
        System.out.println("TEST LOG: virtualFile changed: save?:"+event.isFromSave()+" refresh?:"+event.isFromRefresh());
        if (event.getRequestor() != null){
            System.out.println(">>>> reqestor:"+event.getRequestor().getClass()+" source:"+event.getSource()+" filename:"+event.getFileName());
        }
    }
}
