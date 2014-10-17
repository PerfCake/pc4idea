package org.perfcake.pc4idea.editor;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
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
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;


/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 17.9.2014
 */
public class PerfCakeEditorProvider implements FileEditorProvider, DumbAware {
    private static final Logger LOG = Logger.getInstance("#org.perfcake.pc4idea.editor.PerfCakeEditorProvider");
    private final String EDITOR_TYPE_ID = "PerfCakeDesigner";

    public static PerfCakeEditorProvider getInstance() {
        return ApplicationManager.getApplication().getComponent(PerfCakeEditorProvider.class);
    }

    // accepting perfcake-scenario-3.0 xml
    @Override
    public boolean accept(@NotNull Project project, @NotNull VirtualFile virtualFile) {
        String xmlnsAttr = "";

        if (virtualFile.getFileType() == StdFileTypes.XML && !StdFileTypes.XML.isBinary() &&
           (ModuleUtil.findModuleForFile(virtualFile, project) != null || virtualFile instanceof LightVirtualFile)){
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document temp = builder.parse(virtualFile.getPath());

                xmlnsAttr = temp.getDocumentElement().getAttribute("xmlns");
            } catch (ParserConfigurationException ignored) {}
            catch (SAXException ignored) {}
            catch (IOException ignored) {}
        }

        return xmlnsAttr.equals("urn:perfcake:scenario:3.0");
    }

    @NotNull
    @Override
    public FileEditor createEditor(@NotNull Project project, @NotNull VirtualFile virtualFile) {
        LOG.assertTrue(accept(project, virtualFile));
        return new PerfCakeEditor(project, virtualFile);
    }

    @Override
    public void disposeEditor(@NotNull FileEditor fileEditor) {
        Disposer.dispose(fileEditor);
    }

    @NotNull
    @Override
    public FileEditorState readState(@NotNull Element element, @NotNull Project project, @NotNull VirtualFile virtualFile) {
        return new FileEditorState() {
            @Override
            public boolean canBeMergedWith(FileEditorState fileEditorState, FileEditorStateLevel fileEditorStateLevel) {
                return true;
            }
        };
    }

    @Override
    public void writeState(@NotNull FileEditorState fileEditorState, @NotNull Project project, @NotNull Element element) {

    }

    @NotNull
    @Override
    public String getEditorTypeId() {
        return EDITOR_TYPE_ID;
    }

    @NotNull
    @Override
    public FileEditorPolicy getPolicy() {
        return FileEditorPolicy.HIDE_DEFAULT_EDITOR;
    }
}
