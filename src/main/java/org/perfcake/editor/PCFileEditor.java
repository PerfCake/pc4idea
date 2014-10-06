package org.perfcake.editor;

import com.intellij.codeHighlighting.BackgroundEditorHighlighter;
import com.intellij.ide.structureView.StructureViewBuilder;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.*;
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
 * To change this template use File | Settings | File Templates.
 */
public class PCFileEditor implements FileEditor {
    private final VirtualFile file;
    private final PCEditorGUI editor;
//    private PerfCakeEditor.BackgroundEditorHighlighter backgroundEditorHighlighter; TODO ?

    public PCFileEditor(final Project project, final VirtualFile virtualFile){
        final VirtualFile vf = virtualFile instanceof LightVirtualFile ? ((LightVirtualFile)virtualFile).getOriginalFile() : virtualFile;
        final Module module = ModuleUtil.findModuleForFile(vf, project);
        if (module == null) {
            throw new IllegalArgumentException("No module for file " + virtualFile + " in project " + project);
        }
        file = virtualFile;
        editor = new PCEditorGUI(project, module, virtualFile);

    }

    @NotNull
    @Override
    public JComponent getComponent() {
        return editor;
    }

    @Nullable
    @Override
    public JComponent getPreferredFocusedComponent() {
        return editor.getPreferredFocusedComponent();
    }

    @NotNull
    @Override
    public String getName() {
        return "PerfCake Designer"; /*TODO bundle*/
    }

    public PCEditorGUI getEditor() {
        return editor;
    }

    @NotNull
    @Override
    public FileEditorState getState(@NotNull FileEditorStateLevel fileEditorStateLevel) {  /*TODO ...?, vyhladat*/
        final Document document = FileDocumentManager.getInstance().getCachedDocument(file);
        long modificationStamp = document != null ? document.getModificationStamp() : file.getModificationStamp();

        final String[] ids = new String[5];
        /*TODO RadComponent FormEditingUtil ids?*/
        return new PCFileEditorState(modificationStamp,ids);
    }

    @Override
    public void setState(@NotNull FileEditorState fileEditorState) {
        /*TODO RadComponent FormEditingUtil ids?*/
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public boolean isValid() {
        return true; /*TODO ...?, vyhladat*/
//        return FileDocumentManager.getInstance().getDocument(file) != null &&
//                        file.getFileType().getName() == "PerfCakeFileType";
    }

    @Override
    public void selectNotify() {
        /*TODO ...?, vyhladat*/
    }

    @Override
    public void deselectNotify() {
        /*TODO ...?, vyhladat*/
    }

    @Override
    public void addPropertyChangeListener(@NotNull PropertyChangeListener propertyChangeListener) {
        /*TODO ...?, vyhladat*/
    }

    @Override
    public void removePropertyChangeListener(@NotNull PropertyChangeListener propertyChangeListener) {
        /*TODO ...?, vyhladat*/
    }

    @Nullable
    @Override
    public BackgroundEditorHighlighter getBackgroundHighlighter() {
        return null; /*TODO ...?, vyhladat*/
//        if (backgroundEditorHighlighter == null) {
//            backgroundEditorHighlighter = new ThisBackgroundEditorHighlighter(editor);
//        }
//        return backgroundEditorHighlighter;
    }

    @Nullable
    @Override
    public FileEditorLocation getCurrentLocation() {
        return null; /*TODO ...?, vyhladat*/
    }

    @Nullable
    @Override
    public StructureViewBuilder getStructureViewBuilder() {
        return null; /*TODO ...?, vyhladat*/
    }

    @Override
    public void dispose() {
        editor.dispose();
    }

//    public void selectComponent(@NotNull final String binding) TODO ?
//    public void selectComponentById(@NotNull final String id)  TODO ?

    @Nullable
    @Override
    public <T> T getUserData(@NotNull Key<T> tKey) {
        return null;   /*TODO ...?, vyhladat*/
    }

    @Override
    public <T> void putUserData(@NotNull Key<T> tKey, @Nullable T t) {
        /*TODO ...?, vyhladat*/
    }

//    private class MyBackgroundEditorHighlighter implements BackgroundEditorHighlighter TODO ?
}
