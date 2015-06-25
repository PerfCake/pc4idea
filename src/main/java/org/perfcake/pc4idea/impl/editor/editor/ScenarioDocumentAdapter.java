package org.perfcake.pc4idea.impl.editor.editor;

import com.intellij.openapi.editor.event.DocumentAdapter;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import org.perfcake.pc4idea.api.editor.editor.Updatable;

/**
 * Created by Stanislav Kaleta on 5/1/15.
 */
public class ScenarioDocumentAdapter extends DocumentAdapter {
    private Updatable updatable;
    private boolean enabled;

    public ScenarioDocumentAdapter(Updatable updatable){
        this.updatable = updatable;
        enabled = true;
    }

    @Override
    public void documentChanged(DocumentEvent e) {
        if (enabled){
            if (updatable.needUpdate()){
                FileDocumentManager.getInstance().saveDocument(e.getDocument());
                updatable.update();
            }
        }
    }

    public void enable(){
        enabled = true;
    }

    public void disable(){
        enabled = false;
    }
}
