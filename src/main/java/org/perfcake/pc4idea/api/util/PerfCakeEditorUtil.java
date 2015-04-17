package org.perfcake.pc4idea.api.util;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.perfcake.pc4idea.api.editor.actions.ActionType;
import org.perfcake.pc4idea.api.manager.ScenarioManager;
import org.perfcake.pc4idea.impl.editor.actions.CommitAction;
import org.perfcake.pc4idea.impl.editor.actions.RedoAction;
import org.perfcake.pc4idea.impl.editor.actions.UndoAction;
import org.perfcake.pc4idea.impl.editor.editor.PerfCakeEditor;

import javax.swing.*;

/**
 * Created by Stanislav Kaleta on 4/15/15.
 */
public class PerfCakeEditorUtil {
    private Project project;
    private VirtualFile file;
    private Module module;
    private PerfCakeEditor editor;
    private ScenarioManager manager;

    public PerfCakeEditorUtil(@NotNull Project project,
                              @NotNull VirtualFile file,
                              @NotNull Module module,
                              @NotNull PerfCakeEditor editor,
                              @NotNull ScenarioManager manager) {
        this.project = project;
        this.file = file;
        this.module = module;
        this.editor = editor;
        this.manager = manager;
    }

    public Project getProject() {
        return project;
    }

    public VirtualFile getFile() {
        return file;
    }

    public Module getModule() {
        return module;
    }

    public PerfCakeEditor getEditor() {
        return editor;
    }

    public ScenarioManager getManager() {
        return manager;
    }

    public ActionMap getBaseActionMap() {
        ActionMap baseActionMap = new ActionMap();

        baseActionMap.put(ActionType.COMMIT, new CommitAction(editor, manager));
        baseActionMap.put(ActionType.UNDO, new UndoAction());
        baseActionMap.put(ActionType.REDO, new RedoAction());

        return baseActionMap;
    }
}
