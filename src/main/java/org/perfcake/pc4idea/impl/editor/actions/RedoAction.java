package org.perfcake.pc4idea.impl.editor.actions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.command.undo.UndoManager;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import org.perfcake.pc4idea.api.editor.editor.ContextProvider;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by Stanislav Kaleta on 3/8/15.
 */
public class RedoAction extends AbstractAction {
    private UndoManager undoManager;
    private ContextProvider context;

    public RedoAction(ContextProvider context){
        super("Redo", AllIcons.Actions.Redo);
        this.context = context;
        undoManager = UndoManager.getInstance(context.getProject());
    }

    public void update(){
        FileEditorManager editorManager = FileEditorManager.getInstance(context.getProject());
        FileEditor selectedEditor = editorManager.getSelectedEditor(context.getVirtualFile());
        boolean redoAvailable = undoManager.isRedoAvailable(selectedEditor);
        if (redoAvailable) {
            putValue(Action.NAME, undoManager.getRedoActionNameAndDescription(selectedEditor).second);
        }
        setEnabled(redoAvailable);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        FileEditorManager editorManager = FileEditorManager.getInstance(context.getProject());
        FileEditor selectedEditor = editorManager.getSelectedEditor(context.getVirtualFile());
        if (undoManager.isRedoAvailable(selectedEditor)) undoManager.redo(selectedEditor);
        FileDocumentManager.getInstance().saveDocument(FileDocumentManager.getInstance().getDocument(context.getVirtualFile()));
    }
}
