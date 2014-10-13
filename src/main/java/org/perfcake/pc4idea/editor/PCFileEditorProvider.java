package org.perfcake.pc4idea.editor;

import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorPolicy;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.fileEditor.FileEditorState;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.util.ArrayUtil;
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
 * To change this template use File | Settings | File Templates.
 */
public class PCFileEditorProvider implements FileEditorProvider, DumbAware {
    //private static final Logger LOG = Logger.getInstance("#main.java..PerfCakeEditorProvider");/*TODO*/
    @Override
    public boolean accept(@NotNull Project project, @NotNull VirtualFile virtualFile) {  /*TODO z PC ns*/
        boolean acceptCondition = false;
        if (virtualFile.getFileType() == StdFileTypes.XML && !StdFileTypes.XML.isBinary() &&
           (ModuleUtil.findModuleForFile(virtualFile, project) != null || virtualFile instanceof LightVirtualFile)){
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); /*TODO maybe PC?*/
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document temp = builder.parse(virtualFile.getPath());  /*TODO maybe runReadAction*/


                if (temp.getDocumentElement().getAttribute("xmlns").equals("urn:perfcake:scenario:3.0")) {
                    acceptCondition = true;
                    /*TODO preco 2krat?*/
                    //System.out.println(virtualFile.getName() + " | " + virtualFile.getFileType().getName()+ " | "+temp.getDocumentElement().getAttribute("xmlns"));
                }

            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return acceptCondition;
    }

    @NotNull
    @Override
    public FileEditor createEditor(@NotNull Project project, @NotNull VirtualFile virtualFile) {
        //LOG.assertTrue(accept(project, virtualFile));
        return new PCFileEditor(project, virtualFile);
    }

    @Override
    public void disposeEditor(@NotNull FileEditor fileEditor) {
        Disposer.dispose(fileEditor);
    }

    @NotNull
    @Override
    public FileEditorState readState(@NotNull Element element, @NotNull Project project, @NotNull VirtualFile virtualFile) {
        /*TODO ...?, vyhladat*/
        return new PCFileEditorState(-1, ArrayUtil.EMPTY_STRING_ARRAY);
    }

    @Override
    public void writeState(@NotNull FileEditorState fileEditorState, @NotNull Project project, @NotNull Element element) {
        /*TODO ...?, vyhladat*/
    }

    @NotNull
    @Override
    public String getEditorTypeId() {
        return "PerfCakeDesigner";
    }

    @NotNull
    @Override
    public FileEditorPolicy getPolicy() {
        return FileEditorPolicy.HIDE_DEFAULT_EDITOR; /*TODO mozno podla ns*/
    }
}
