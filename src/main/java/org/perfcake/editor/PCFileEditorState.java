package org.perfcake.editor;

import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.fileEditor.FileEditorStateLevel;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 17.9.2014
 * To change this template use File | Settings | File Templates.
 */
public class PCFileEditorState implements FileEditorState {
    private final transient long modificationStamp;
    private final String[] selectedComponents;

    public PCFileEditorState(final long modificationStamp, @NotNull final String[] selectedComponents) {
        this.modificationStamp = modificationStamp;
        this.selectedComponents = selectedComponents;
    }

    @Override
    public boolean canBeMergedWith(FileEditorState fileEditorState, FileEditorStateLevel fileEditorStateLevel) {
        return fileEditorState instanceof PCFileEditorState;
    }

    public String[] getSelectedComponentIds(){
        return selectedComponents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PCFileEditorState that = (PCFileEditorState) o;

        if (modificationStamp != that.modificationStamp) return false;
        if (!Arrays.equals(selectedComponents, that.selectedComponents)) return false;

        return true;
    }
    @Override
    public int hashCode() {
        int result = (int) (modificationStamp ^ (modificationStamp >>> 32));
        result = 31 * result + (selectedComponents != null ? Arrays.hashCode(selectedComponents) : 0);
        return result;
    }
}
