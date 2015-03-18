package org.perfcake.pc4idea.editor.manager;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.command.UndoConfirmationPolicy;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.perfcake.PerfCakeConst;
import org.perfcake.model.Scenario;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 27.2.2015
 */
public class XMLScenarioManager implements ScenarioManager{
    private static final Logger LOG = Logger.getInstance("#.editor.manager.XMLScenarioManager");
    private VirtualFile file;
    private Project project;

    public XMLScenarioManager(@NotNull VirtualFile file, @NotNull Project project){
        this.file = file;
        this.project = project;
    }

    @Override
    public Scenario loadScenario() throws ScenarioManagerException { /*TODO WRAP IN READ ACTION*/
        Scenario scenarioModel = null;
        try {
            String schemaURI = "/schemas/" + "perfcake-scenario-" + PerfCakeConst.XSD_SCHEMA_VERSION + ".xsd";
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(this.getClass().getResource(schemaURI));

            JAXBContext context = JAXBContext.newInstance(org.perfcake.model.Scenario.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            unmarshaller.setSchema(schema);
            //scenarioModel = (org.perfcake.modelwrapper.Scenario) unmarshaller.unmarshal(new File(file.getPath()));
            scenarioModel = (Scenario) unmarshaller.unmarshal(file.getInputStream());

            /*TODO for testing purpose*/System.out.println("TEST LOG: scenario successfully loadedd");
        } catch (JAXBException | SAXException | IOException e) {
            throw new ScenarioManagerException(e);
        }
        return scenarioModel;
    }

    @Override
    public void saveScenario(Scenario scenarioModel, String actionCommand) { /*TODO OPT!*/
        Document document = FileDocumentManager.getInstance().getDocument(file);

        CommandProcessor.getInstance().executeCommand(project, new Runnable() {
            @Override
            public void run() {
                ApplicationManager.getApplication().runWriteAction(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                            Schema schema = schemaFactory.newSchema(XMLScenarioManager.this.getClass().getResource(
                                    "/schemas/" + "perfcake-scenario-" + PerfCakeConst.XSD_SCHEMA_VERSION + ".xsd"));

                            JAXBContext context = JAXBContext.newInstance(org.perfcake.model.Scenario.class);
                            Marshaller marshaller = context.createMarshaller();
                            marshaller.setSchema(schema);
                            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

                            StringWriter stringWriter = new StringWriter();

                            marshaller.marshal(scenarioModel, stringWriter);
                            if (!stringWriter.toString().trim().isEmpty() && stringWriter.toString() != null) {
                                /*TODO for testing purpose*/
                                System.out.println("TEST LOG: scenario successfully saved");
                                document.setText(stringWriter.toString());
                            }
                            stringWriter.close();
                        } catch (JAXBException | SAXException | IOException e) {
                            LOG.error(e.getCause());
                        }
                    }
                });
            }
        }, actionCommand, null, UndoConfirmationPolicy.DEFAULT, document);

    }
}
