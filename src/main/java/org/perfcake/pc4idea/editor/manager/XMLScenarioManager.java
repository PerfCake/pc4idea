package org.perfcake.pc4idea.editor.manager;

import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.perfcake.PerfCakeConst;
import org.perfcake.model.Scenario;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 27.2.2015
 */
public class XMLScenarioManager implements ScenarioManager{
    private VirtualFile file;

    public XMLScenarioManager(@NotNull VirtualFile file){
        this.file = file;
    }

    @Override
    public Scenario loadScenario() throws ScenarioManagerException {
        Scenario scenarioModel = null;
        try {
            String schemaURI = "/schemas/" + "perfcake-scenario-" + PerfCakeConst.XSD_SCHEMA_VERSION + ".xsd";
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(this.getClass().getResource(schemaURI));

            JAXBContext context = JAXBContext.newInstance(org.perfcake.model.Scenario.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            unmarshaller.setSchema(schema);
            scenarioModel = (org.perfcake.model.Scenario) unmarshaller.unmarshal(new File(file.getPath()));
            /*TODO for testing purpose*/System.out.println("TEST LOG: scenario successfully loaded");
        } catch (JAXBException | SAXException e) {
            throw new ScenarioManagerException(e);
        }
        return scenarioModel;
    }

    @Override
    public void saveScenario(Scenario scenarioModel, String actionCommand) {

    }
}
