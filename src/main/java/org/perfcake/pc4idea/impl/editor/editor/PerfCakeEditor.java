package org.perfcake.pc4idea.impl.editor.editor;

import com.intellij.codeHighlighting.BackgroundEditorHighlighter;
import com.intellij.ide.structureView.StructureViewBuilder;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
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
import org.perfcake.pc4idea.api.manager.ScenarioManager;
import org.perfcake.pc4idea.api.manager.ScenarioManagerException;
import org.perfcake.pc4idea.api.util.PerfCakeEditorUtil;
import org.perfcake.pc4idea.impl.editor.gui.PerfCakeEditorGUI;
import org.perfcake.pc4idea.todo.Messages;

import javax.swing.*;
import java.beans.PropertyChangeListener;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 17.9.2014
 */
public class PerfCakeEditor implements FileEditor { /*TODO UNDO/REDO/externa zmena > upadte gui*/
    private static final Logger LOG = Logger.getInstance(PerfCakeEditor.class);
    public static final String PERFCAKE_NOTIFICATION_ID = "PerfCake Plugin";

    private Project project;
    private VirtualFile file;
    private ScenarioManager manager;


    private final PerfCakeEditorGUI editorGUI;

    public PerfCakeEditor(final Project project, final VirtualFile file, @NotNull final ScenarioManager manager){
        this.project = project;
        this.file = file;
        this.manager = manager;
        final VirtualFile vf = file instanceof LightVirtualFile ? ((LightVirtualFile) file).getOriginalFile() : file;
        final Module module = ModuleUtil.findModuleForFile(vf, project);
        if (module != null) {
            Notifications.Bus.register(PERFCAKE_NOTIFICATION_ID, NotificationDisplayType.NONE);/*TODO !!!depr.*/

            String type = module.getOptionValue("type");
            if (type == null || !type.equals("PERFCAKE_MODULE")) {
                LOG.info("Opening PerfCake Scenario \""+file.getName()+"\" in not PerfCake module");
                Notifications.Bus.notify(new Notification(PERFCAKE_NOTIFICATION_ID,
                        Messages.BUNDLE.getString("NOT_PC_MODULE_T"),
                        Messages.BUNDLE.getString("NOT_PC_MODULE_B"),
                        NotificationType.INFORMATION), project);
            }
        } else {
            throw new IllegalArgumentException("No module for file " + file + " in project " + project);
        }

        PerfCakeEditorUtil editorUtil = new PerfCakeEditorUtil(project, file, module, this, manager);
        editorGUI = new PerfCakeEditorGUI(editorUtil);
        setUpEditor();
    }

    private void setUpEditor(){
        try {
            editorGUI.getScenarioGUI().setScenarioModel(manager.retrieveScenario());
        } catch (ScenarioManagerException e) {
            /*TODO for testing purpose*/System.out.println(e.getCause());
            editorGUI.getScenarioGUI().setScenarioModel(null);
            Notifications.Bus.notify(new Notification(PERFCAKE_NOTIFICATION_ID, Messages.BUNDLE.getString("SCENARIO_INVALID"),
                    e.getCause().toString(), NotificationType.WARNING), project);
        }
    }

    @Override
    public void dispose() {/*TODO maybe just here*/
        editorGUI.dispose();
    }

    @NotNull
    @Override
    public PerfCakeEditorGUI getComponent() {
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
        return "Designer";
    }

    @Override
    public boolean isModified() {
        return false;
    } /*TODO po prvej zmene - hviezdicka + vfilelistener*/

    @Override
    public boolean isValid() {
        return true; /*TODO ked false tak nic sa nedeje*/
    }

    @Override
    public void selectNotify() { /*TODO load len ked nastala zmena*/
        Document document = FileDocumentManager.getInstance().getDocument(file);
        FileDocumentManager.getInstance().saveDocument(document);
        setUpEditor();
        /*TODO for testing purpose*/System.out.println("selectNotify");
    }

    @Override
    public void deselectNotify() {
        Document document = FileDocumentManager.getInstance().getDocument(file);
        FileDocumentManager.getInstance().saveDocument(document);
        /*TODO for testing purpose*/System.out.println("deselectNotify");
    }

    @Override
    public void setState(@NotNull FileEditorState state) {
        /*TODO*/
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
        // not used
        return null;
    }

    @Nullable
    @Override
    public FileEditorLocation getCurrentLocation() {
        return null; /*TODO*/
    }

    @Nullable
    @Override
    public StructureViewBuilder getStructureViewBuilder() {
        return null; /*TODO*/
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
