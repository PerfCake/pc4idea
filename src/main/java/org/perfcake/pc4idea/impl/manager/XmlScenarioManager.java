package org.perfcake.pc4idea.impl.manager;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.command.UndoConfirmationPolicy;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.perfcake.PerfCakeConst;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.manager.ScenarioManager;
import org.perfcake.pc4idea.api.manager.ScenarioManagerException;
import org.perfcake.pc4idea.api.util.Messages;
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
public class XmlScenarioManager implements ScenarioManager {
    private static final Logger LOG = Logger.getInstance(XmlScenarioManager.class);

    private VirtualFile file;
    private Project project;

    public XmlScenarioManager(@NotNull VirtualFile file, @NotNull Project project){
        this.file = file;
        this.project = project;
    }

    public XmlScenarioManager(@NotNull Project project){
        this.file = null ;
        this.project = project;
    }

    @Override
    public void createScenario(@NotNull VirtualFile directoryFile,
                               @NotNull String name,
                               @NotNull Scenario model) throws ScenarioManagerException {
        if (!directoryFile.isDirectory()){
            String wrongFileName = directoryFile.getName();
            directoryFile = directoryFile.getParent();
            String[] logMsg = Messages.Log.NOT_DIR_FILE;
            LOG.warn(logMsg[0] + wrongFileName + logMsg[1] + directoryFile.getName() + logMsg[2]);
        }
        for (VirtualFile vf : directoryFile.getChildren()) {
            if (vf.getName().equals(name)) {
                String[] msg = Messages.Dialog.FILE_EXISTS;
                int result = com.intellij.openapi.ui.Messages.showOkCancelDialog(project,
                        msg[0] + name + msg[1],
                        Messages.Title.FILE_EXISTS,
                        AllIcons.General.WarningDialog);
                if (result != 0) {
                    return;
                }
            }
        }

        name = name + ".xml";

        final String finalName = name;
        final VirtualFile finalDirectoryFile = directoryFile;
        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            @Override
            public void run() {
                try {
                    file = finalDirectoryFile.createChildData(XmlScenarioManager.this, finalName);
                    XmlScenarioManager.this.updateScenario(model,
                            Messages.Command.CREATE + " " +Messages.Scenario.SCENARIO);
                    Document document = FileDocumentManager.getInstance().getDocument(file);
                    if (document != null) {
                        FileDocumentManager.getInstance().saveDocument(document);
                    }
                    FileEditorManager.getInstance(project).openFile(file, true);
                } catch (IOException e) {
                    String[] msg = Messages.Exception.UNABLE_TO_CREATE_SCENARIO;
                    LOG.error(msg[0] + finalName + msg[1]);
                    throw new ScenarioManagerException(msg[0] + finalName + msg[1]);
                }
            }
        });
    }

    @Override
    public Scenario retrieveScenario() throws ScenarioManagerException {
        if (file == null){
            String msg = Messages.Exception.NULL_VIRTUAL_FILE;
            LOG.error(msg);
            throw new ScenarioManagerException(msg);
        }

        Scenario scenarioModel = null;
        try {
            String schemaUri = "/schemas/perfcake-scenario-" + PerfCakeConst.XSD_SCHEMA_VERSION + ".xsd";
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(this.getClass().getResource(schemaUri));

            JAXBContext context = JAXBContext.newInstance(org.perfcake.model.Scenario.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            unmarshaller.setSchema(schema);
            scenarioModel = (Scenario) unmarshaller.unmarshal(file.getInputStream());
            /*TODO for testing purpose*/System.out.println("TEST LOG: scenario successfully loadedd");
        } catch (JAXBException | SAXException | IOException e) {
            LOG.error(e.getMessage());
            throw new ScenarioManagerException(e);
        }
        return scenarioModel;
    }

    @Override
    public void updateScenario(@NotNull Scenario model,
                               @NotNull String actionCommand) throws ScenarioManagerException {
        if (file == null){
            String msg = Messages.Exception.NULL_VIRTUAL_FILE;
            LOG.error(msg);
            throw new ScenarioManagerException(msg);
        }

        Document document = FileDocumentManager.getInstance().getDocument(file);
        if (document == null){
            String msg = Messages.Exception.NULL_DOCUMENT;
            LOG.error(msg);
            throw new ScenarioManagerException(msg);
        }

        CommandProcessor.getInstance().executeCommand(project, new Runnable() {
            @Override
            public void run() {
                ApplicationManager.getApplication().runWriteAction(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String schemaUri = "/schemas/perfcake-scenario-" + PerfCakeConst.XSD_SCHEMA_VERSION + ".xsd";
                            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                            Schema schema = schemaFactory.newSchema(XmlScenarioManager.class.getResource(schemaUri));

                            JAXBContext context = JAXBContext.newInstance(org.perfcake.model.Scenario.class);
                            Marshaller marshaller = context.createMarshaller();
                            marshaller.setSchema(schema);
                            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

                            StringWriter stringWriter = new StringWriter();

                            marshaller.marshal(model, stringWriter);
                            if (!stringWriter.toString().trim().isEmpty() && stringWriter.toString() != null) {
                                /*TODO for testing purpose*/System.out.println("TEST LOG: scenario successfully saved: " + actionCommand);
                                document.setText(stringWriter.toString());
                            }
                            stringWriter.close();
                        } catch (JAXBException | SAXException | IOException e) {
                            LOG.error(e.getMessage());
                            throw new ScenarioManagerException(e);
                        }
                    }
                });
            }
        }, actionCommand, null, UndoConfirmationPolicy.DEFAULT, document);
    }

    @Override
    public void deleteScenario() throws ScenarioManagerException {
        throw new ScenarioManagerException(Messages.Exception.UNSUPPORTED_OPERATION);
    }
}
