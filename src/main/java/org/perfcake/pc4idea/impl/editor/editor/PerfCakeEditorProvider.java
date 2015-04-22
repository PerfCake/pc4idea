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
import org.perfcake.pc4idea.api.manager.ScenarioManager;
import org.perfcake.pc4idea.api.util.PerfCakeModuleUtil;
import org.perfcake.pc4idea.impl.manager.DSLScenarioManager;
import org.perfcake.pc4idea.impl.manager.XMLScenarioManager;


/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 17.9.2014
 */
public class PerfCakeEditorProvider implements FileEditorProvider, DumbAware {
    private static final Logger LOG = Logger.getInstance(PerfCakeEditorProvider.class);
    private static final String EDITOR_TYPE_ID = "PerfCakeEditor";

    private final String[] scenarioTypes = new String[]{"xml", "dsl"};

    public static PerfCakeEditorProvider getInstance() {
        return ApplicationManager.getApplication().getComponent(PerfCakeEditorProvider.class);
    }

    @Override
    public boolean accept(@NotNull Project project, @NotNull VirtualFile file) {
        Module module = ModuleUtil.findModuleForFile(file, project);

        if (module != null) {
            boolean isScenario = PerfCakeModuleUtil.isPerfCakeScenarioFile(module,file);
            if (isScenario){
                int nameLength = file.getName().length();
                String extension = file.getName().substring(nameLength - 3, nameLength);
                for (String scenarioType : scenarioTypes) {
                    if (scenarioType.equals(extension)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @NotNull
    @Override
    public FileEditor createEditor(@NotNull Project project, @NotNull VirtualFile file) {
        LOG.assertTrue(accept(project, file));

        ScenarioManager manager;
        int nameLength = file.getName().length();
        String extension = file.getName().substring(nameLength - 3, nameLength);
        switch (extension) {
            case "xml":
                manager = new XMLScenarioManager(file, project);
                break;
            case "dsl":
                manager = new DSLScenarioManager(file);
                break;
            default:
                throw new UnsupportedOperationException("unexpected file extension!");
        }

        return new PerfCakeEditor(project, file, manager);
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
