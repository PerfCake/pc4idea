package org.perfcake.pc4idea.editor;

import com.intellij.codeHighlighting.BackgroundEditorHighlighter;
import com.intellij.ide.structureView.StructureViewBuilder;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorLocation;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.fileEditor.FileEditorStateLevel;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.LightVirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.beans.PropertyChangeListener;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 17.9.2014
 */
class PerfCakeEditor implements FileEditor {
    private final PerfCakeEditorGUI editorGUI;

    PerfCakeEditor(final Project project, final VirtualFile virtualFile){
        final VirtualFile vf = virtualFile instanceof LightVirtualFile ? ((LightVirtualFile)virtualFile).getOriginalFile() : virtualFile;
        final Module module = ModuleUtil.findModuleForFile(vf, project);
        if (module == null) {
            throw new IllegalArgumentException("No module for file " + virtualFile + " in project " + project);
        }
        editorGUI = new PerfCakeEditorGUI(project, module, virtualFile);
    }

    @Override
    public void dispose() {
        editorGUI.dispose();
    }

    @NotNull
    @Override
    public JComponent getComponent() {
        return editorGUI;
    }

    @Nullable
    @Override
    public JComponent getPreferredFocusedComponent() {
        return editorGUI.getPreferredFocusedComponent();
    }

    @NotNull
    @Override
    public String getName() {
        return "PerfCakeDesigner";
    }

    @NotNull
    @Override
    public FileEditorState getState(@NotNull FileEditorStateLevel level) {
        /*TODO*/
        return new FileEditorState() {
            @Override
            public boolean canBeMergedWith(FileEditorState fileEditorState, FileEditorStateLevel fileEditorStateLevel) {
                return true;
            }
        };
    }

    @Override
    public void setState(@NotNull FileEditorState state) {
        /*TODO*/
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void selectNotify() {
    }

    @Override
    public void deselectNotify() {
    }

    @Override
    public void addPropertyChangeListener(@NotNull PropertyChangeListener listener) {
        /*TODO*/
    }
    @Override
    public void removePropertyChangeListener(@NotNull PropertyChangeListener listener) {
        /*TODO*/
    }

    @Nullable
    @Override
    public BackgroundEditorHighlighter getBackgroundHighlighter() {
        return null;
    }

    @Nullable
    @Override
    public FileEditorLocation getCurrentLocation() {
        return null;
    }

    @Nullable
    @Override
    public StructureViewBuilder getStructureViewBuilder() {
        return null;
    }


    @Nullable
    @Override
    public <T> T getUserData(@NotNull Key<T> tKey) {
        return null;          /*TODO zistit*/
    }

    @Override
    public <T> void putUserData(@NotNull Key<T> tKey, @Nullable T t) {  /*TODO zistit*/

    }
}
