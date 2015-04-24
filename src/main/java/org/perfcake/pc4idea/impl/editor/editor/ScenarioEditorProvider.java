package org.perfcake.pc4idea.impl.editor.editor;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorPolicy;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.vfs.VirtualFile;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.perfcake.pc4idea.api.util.PerfCakeScenarioUtil;


/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 17.9.2014
 */
public class ScenarioEditorProvider implements FileEditorProvider, DumbAware {
    private static final Logger LOG = Logger.getInstance(ScenarioEditorProvider.class);
    private static final String EDITOR_TYPE_ID = "PerfCakeEditor";

    public static ScenarioEditorProvider getInstance() {
        return ApplicationManager.getApplication().getComponent(ScenarioEditorProvider.class);
    }

    @Override
    public boolean accept(@NotNull Project project, @NotNull VirtualFile file) {
        Module module = ModuleUtil.findModuleForFile(file, project);

        return module != null && PerfCakeScenarioUtil.isPerfCakeScenario(file);
    }

    @NotNull
    @Override
    public FileEditor createEditor(@NotNull Project project, @NotNull VirtualFile file) {
        LOG.assertTrue(accept(project, file));

        return new ScenarioEditor(project, file, PerfCakeScenarioUtil.getScenarioManager(project,file));
    }

    @Override
    public void disposeEditor(@NotNull FileEditor fileEditor) {
        Disposer.dispose(fileEditor);
    }

    @NotNull
    @Override
    public FileEditorState readState(@NotNull Element element, @NotNull Project project, @NotNull VirtualFile virtualFile) {
        return FileEditorState.INSTANCE;
    }

    @Override
    public void writeState(@NotNull FileEditorState fileEditorState, @NotNull Project project, @NotNull Element element) {
        // not used
    }

    @NotNull
    @Override
    public String getEditorTypeId() {
        return EDITOR_TYPE_ID;
    }

    @NotNull
    @Override
    public FileEditorPolicy getPolicy() {
        return FileEditorPolicy.PLACE_BEFORE_DEFAULT_EDITOR;
    }
}
