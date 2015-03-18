package org.perfcake.pc4idea.editor.editor;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.*;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.LightVirtualFile;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.perfcake.pc4idea.editor.manager.DSLScenarioManager;
import org.perfcake.pc4idea.editor.manager.ScenarioManager;
import org.perfcake.pc4idea.editor.manager.XMLScenarioManager;


/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 17.9.2014
 */
public class PerfCakeEditorProvider implements FileEditorProvider, DumbAware {
    private static final Logger LOG = Logger.getInstance("#...editor.PerfCakeEditorProvider");
    private static final String EDITOR_TYPE_ID = "PerfCakeEditor";

    private ScenarioType scenarioType;

    public static PerfCakeEditorProvider getInstance() {
        return ApplicationManager.getApplication().getComponent(PerfCakeEditorProvider.class);
    }

    @Override
    public boolean accept(@NotNull Project project, @NotNull VirtualFile virtualFile) {
        /*TODO xml or DSL*/
        if (virtualFile.getFileType() == StdFileTypes.XML && !StdFileTypes.XML.isBinary() &&
                (ModuleUtil.findModuleForFile(virtualFile, project) != null || virtualFile instanceof LightVirtualFile) ) {
            Document document = FileDocumentManager.getInstance().getDocument(virtualFile);
            if (document != null){
                String text = document.getText();
                if (text.contains("<scenario xmlns=\"urn:perfcake:scenario:3.0\">")){
                    scenarioType = ScenarioType.XML;
                    return true;
                }
            }
        }
        if (virtualFile.getName().contains(".dsl")){/*TODO urn or someting*/
            scenarioType = ScenarioType.DSL;
            return true;
        }

        return false;
    }

    @NotNull
    @Override
    public FileEditor createEditor(@NotNull Project project, @NotNull VirtualFile virtualFile) {
        LOG.assertTrue(accept(project, virtualFile));

        ScenarioManager manager;
        switch (scenarioType){
            case XML:
                manager = new XMLScenarioManager(virtualFile, project);
                break;
            case DSL:
                manager = new DSLScenarioManager(virtualFile);
                break;
            default: manager = null;
        }

        return new PerfCakeEditor(project, virtualFile, manager);
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
