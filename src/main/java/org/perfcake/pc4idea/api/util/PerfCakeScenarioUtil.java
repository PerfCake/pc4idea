package org.perfcake.pc4idea.api.util;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.perfcake.PerfCakeConst;
import org.perfcake.pc4idea.api.manager.ScenarioManager;
import org.perfcake.pc4idea.impl.manager.DslScenarioManager;
import org.perfcake.pc4idea.impl.manager.XmlScenarioManager;
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
     * checks if file is PerfCake scenario
     * @param file file to check
     * @return true if file is PerfCake scenario, false otherwise
     */
    public static boolean isPerfCakeScenario(@NotNull VirtualFile file){
        return isDSLScenario(file) || isXMLScenario(file);
    }

    /**
     *  returns scenario manager for scenario file
     *
     * @param file file
     * @return scenario manager
     */
    public static ScenarioManager getScenarioManager(@NotNull Project project, @NotNull VirtualFile file){
        if (isXMLScenario(file)){
            return new XmlScenarioManager(file, project);
        }
        if (isDSLScenario(file)){
            return new DslScenarioManager(file, project);
        }
        throw new UnsupportedOperationException("unexpected error - scenario type can't be find!");
    }

    private static boolean isDSLScenario(@NotNull VirtualFile file){
        if (!file.getName().contains(".dsl")){
            return false;
        }
        Document document = FileDocumentManager.getInstance().getDocument(file);
        if (document == null){
            return false;
        }
        if (document.getText().split("\n")[0].startsWith("scenario ")){
            return true;
        }
        return false;
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
