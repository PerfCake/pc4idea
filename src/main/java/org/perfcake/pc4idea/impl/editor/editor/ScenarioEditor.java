package org.perfcake.pc4idea.impl.editor.editor;

import com.intellij.codeHighlighting.BackgroundEditorHighlighter;
import com.intellij.ide.structureView.StructureViewBuilder;
import com.intellij.notification.*;
import com.intellij.openapi.diagnostic.Logger;
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
import org.perfcake.pc4idea.api.editor.editor.ContextProvider;
import org.perfcake.pc4idea.api.editor.editor.ScenarioVirtualFileListener;
import org.perfcake.pc4idea.api.manager.ScenarioManager;
import org.perfcake.pc4idea.api.manager.ScenarioManagerException;
import org.perfcake.pc4idea.api.util.Messages;
import org.perfcake.pc4idea.api.util.PerfCakeModuleUtil;
import org.perfcake.pc4idea.api.util.PerfCakeScenarioUtil;
import org.perfcake.pc4idea.impl.editor.gui.ScenarioEditorGui;
import org.perfcake.pc4idea.impl.editor.modelwrapper.ScenarioModelWrapper;

import javax.swing.*;
import java.beans.PropertyChangeListener;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 17.9.2014
 */
public class ScenarioEditor implements FileEditor, ContextProvider { /*TODO UNDO/REDO/externa zmena > upadte gui*/
    private static final Logger LOG = Logger.getInstance(ScenarioEditor.class);
    public static final String PERFCAKE_NOTIFICATION_ID = "PerfCake Plugin";

    private Project project;
    private VirtualFile file;
    private ScenarioVirtualFileListener svfListener;
    private long modificationStamp = 0l;
    private Module module;
    private ScenarioManager manager;

    private final ScenarioEditorGui editorGui;
    private final ScenarioModelWrapper modelWrapper;

    public ScenarioEditor(@NotNull Project project, @NotNull VirtualFile file) {
        this.project = project;
        this.file = file instanceof LightVirtualFile ? ((LightVirtualFile) file).getOriginalFile() : file;
        module = ModuleUtil.findModuleForFile(this.file, project);
        if (module != null) {
            NotificationsConfiguration.getNotificationsConfiguration()
                    .register(PERFCAKE_NOTIFICATION_ID, NotificationDisplayType.BALLOON);
            if (!PerfCakeModuleUtil.isPerfCakeModule(module)) {
                String[] logMsg = Messages.Log.UNSUPPORTED_MODULE;
                LOG.info(logMsg[0] + file.getName() + logMsg[1]);
                Notifications.Bus.notify(new Notification(PERFCAKE_NOTIFICATION_ID,
                        Messages.Title.UNSUPPORTED_MODULE,
                        Messages.Dialog.UNSUPPORTED_MODULE,
                        NotificationType.INFORMATION), project);
            }
        } else {
            String[] eMsg = Messages.Exception.NULL_MODULE;
            throw new IllegalArgumentException(eMsg[0] + file + eMsg[1] + project);
        }

        svfListener = new ScenarioVirtualFileListener(this);
        this.file.getFileSystem().addVirtualFileListener(svfListener);

        manager = PerfCakeScenarioUtil.getScenarioManager(project, file);
        modelWrapper = new ScenarioModelWrapper(this);
        editorGui = new ScenarioEditorGui(modelWrapper.getScenarioGui());
        setUpEditor();
    }

    private void setUpEditor(){
        try {
            modelWrapper.setScenarioModel(manager.retrieveScenario());
        } catch (ScenarioManagerException e) {
            /*TODO for testing purpose*/System.out.println(e.getMessage());
            modelWrapper.setScenarioModel(null);
            Notifications.Bus.notify(new Notification(PERFCAKE_NOTIFICATION_ID, Messages.Label.SCENARIO_INVALID,
                    e.getCause().toString(), NotificationType.WARNING), project);
        }
    }

    @Override
    public void dispose() {/*TODO maybe just here*/
        /*TODO dont forget to save doc*/
//        /*TODO threadIntrpted exc.(dispose in porgress?)*/
//        /*TODO filewatcher?*/
//        file.getFileSystem().removeVirtualFileListener(scenarioVirtualFileListener);
//        document.removeDocumentListener(scenarioDocumentListener);
//
//        xmlEditor.dispose();
//        EditorHistoryManager.getInstance(project).updateHistoryEntry(file, false);
        file.getFileSystem().removeVirtualFileListener(svfListener);
    }

    @NotNull
    @Override
    public ScenarioEditorGui getComponent() {
        return editorGui;
    }

    @Nullable
    @Override
    public JComponent getPreferredFocusedComponent() {
        return editorGui.getPreferredFocusedComponent();
    }

    @NotNull
    @Override
    public String getName() {
        return "Designer";
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
        Document document = FileDocumentManager.getInstance().getDocument(file);
        if (document != null){
            if (modificationStamp != document.getModificationStamp()){
                FileDocumentManager.getInstance().saveDocument(document);
                setUpEditor();
            }
        }
        modelWrapper.repaintDependencies();
    }

    @Override
    public void deselectNotify() {
        Document document = FileDocumentManager.getInstance().getDocument(file);
        if (document != null){
            if (modificationStamp != document.getModificationStamp()){
                FileDocumentManager.getInstance().saveDocument(document);
                modificationStamp = document.getModificationStamp();
            }
        }
    }

    @Override
    public VirtualFile getVirtualFile() {
        return file;
    }

    @Override
    public ScenarioModelWrapper getModel(){
        return modelWrapper;
    }

    @Override
    public Module getModule() {
        return module;
    }

    @Override
    public Project getProject() {
        return project;
    }

    @Override
    public void setState(@NotNull FileEditorState state) {
        // not used
    }

    @NotNull
    @Override
    public FileEditorState getState(@NotNull FileEditorStateLevel level) {
        return new FileEditorState() {
            @Override
            public boolean canBeMergedWith(FileEditorState fileEditorState, FileEditorStateLevel fileEditorStateLevel) {
                return true;
            }
        };
    }

    @Override
    public void addPropertyChangeListener(@NotNull PropertyChangeListener listener) {
        // not used
    }
    @Override
    public void removePropertyChangeListener(@NotNull PropertyChangeListener listener) {
        // not used
    }

    @Nullable
    @Override
    public BackgroundEditorHighlighter getBackgroundHighlighter() {
        // not used
        return null;
    }

    @Nullable
    @Override
    public FileEditorLocation getCurrentLocation() {
        // not used
        return null;
    }

    @Nullable
    @Override
    public StructureViewBuilder getStructureViewBuilder() {
        // not used
        return null;
    }

    @Nullable
    @Override
    public <T> T getUserData(@NotNull Key<T> tKey) {
        // not used
        return null;
    }

    @Override
    public <T> void putUserData(@NotNull Key<T> tKey, @Nullable T t) {
        // not used
    }
}
