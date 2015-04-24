package org.perfcake.pc4idea.api.util;

import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.perfcake.PerfCakeConst;
import org.perfcake.pc4idea.api.manager.ScenarioManager;
import org.perfcake.pc4idea.impl.manager.DSLScenarioManager;
import org.perfcake.pc4idea.impl.manager.XMLScenarioManager;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by Stanislav Kaleta on 4/23/15.
 */
public class PerfCakeScenarioUtil {

    /**
     * todo
     * @param file
     * @return
     */
    public static boolean isPerfCakeScenario(@NotNull VirtualFile file){
        return isDSLScenario(file) || isXMLScenario(file);
    }

    /**
     *
     * @param file
     * @return
     */
    public static ScenarioManager getScenarioManager(@NotNull Project project, @NotNull VirtualFile file){
        if (isXMLScenario(file)){
            return new XMLScenarioManager(file, project);
        }
        if (isDSLScenario(file)){
            return new DSLScenarioManager(file,project);
        }
        throw new UnsupportedOperationException("unexpected error - scenario type can't be find!");
    }



    private static boolean isDSLScenario(@NotNull VirtualFile file){
        /*TODO dorobit cez groovy script*/
        return file.getName().contains(".dsl");
    }

    private static boolean isXMLScenario(@NotNull VirtualFile file){
        if (file.getFileType() == StdFileTypes.XML && !StdFileTypes.XML.isBinary()){
            try {
                DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = builderFactory.newDocumentBuilder();
                org.w3c.dom.Document document = builder.parse(file.getInputStream());

                if (document != null){
                    String xmlNsAttr = document.getDocumentElement().getAttribute("xmlns");
                    if (xmlNsAttr.equals("urn:perfcake:scenario:"+PerfCakeConst.XSD_SCHEMA_VERSION)){
                        return true;
                    }
                }
            } catch (ParserConfigurationException | IOException e) {
                e.printStackTrace();
                return false;
            } catch (SAXException e){
                com.intellij.openapi.editor.Document document = FileDocumentManager.getInstance().getDocument(file);

                if (document != null){
                    String text = document.getText();
                    String version = PerfCakeConst.XSD_SCHEMA_VERSION;
                    if (text.contains("<scenario xmlns=\"urn:perfcake:scenario:" + version + "\">")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
