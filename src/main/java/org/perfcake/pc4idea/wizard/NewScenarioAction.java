package org.perfcake.pc4idea.wizard;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import org.perfcake.PerfCakeConst;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.designer.common.PerfCakeIconPatcher;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 9.9.2014
 */
public class NewScenarioAction extends AnAction {

    public NewScenarioAction(){
        super(PerfCakeIconPatcher.loadIcon());
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        VirtualFile file = e.getData(DataKeys.VIRTUAL_FILE);
        String dirURI = (file.isDirectory()) ? file.getUrl() : file.getParent().getUrl();

        WizardPanel wizard = new WizardPanel(dirURI);
        WizardDialog wizardDialog = new WizardDialog(wizard);
        wizardDialog.show();
        wizard.stopCheckingValidity();
        if (wizardDialog.getExitCode() == 0){
            VirtualFile scenarioDirectory = wizard.getScenarioDirectory();
            String name = wizard.getScenarioName();
            name = (name.contains(".xml")) ? name : name+".xml";
            Scenario model = wizard.getScenarioModel();

            createScenario(scenarioDirectory,name,model,e.getProject());
        }
    }

    @Override
    public void update(AnActionEvent e) {
        VirtualFile file = e.getData(DataKeys.VIRTUAL_FILE);
        if (file == null) {
            e.getPresentation().setEnabledAndVisible(false);
        }
    }

    private void createScenario(final VirtualFile directory,
                                final String name,
                                final Scenario model,
                                final Project project){
        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            @Override
            public void run() {
                try {
                    for(VirtualFile vf : directory.getChildren()){
                        if (vf.getName().equals(name)){
                            int result = Messages.showOkCancelDialog(project,
                                    "File "+name+" already exists in this directory!\n"+
                                    "Would you like to rewrite it?",
                                    "File Already Exists!",
                                     AllIcons.General.WarningDialog);
                            if (result != 0) {
                                return;
                            }
                        }
                    }

                    VirtualFile scenarioVF = directory.createChildData(NewScenarioAction.this, name);
                    Document scenarioDocument = FileDocumentManager.getInstance().getDocument(scenarioVF);
                    if (scenarioDocument == null){
                        /*TODO log and return*/
                        return;
                    }

                    SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                    Schema schema = schemaFactory.newSchema(this.getClass().getResource("/schemas/" + "perfcake-scenario-" + PerfCakeConst.XSD_SCHEMA_VERSION + ".xsd"));

                    JAXBContext context = JAXBContext.newInstance(org.perfcake.model.Scenario.class);
                    Marshaller marshaller = context.createMarshaller();
                    marshaller.setSchema(schema);
                    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

                    StringWriter stringWriter = new StringWriter();
                    marshaller.marshal(model, stringWriter);
                    scenarioDocument.setText(stringWriter.toString());
                    stringWriter.close();

                    FileDocumentManager.getInstance().saveDocument(scenarioDocument);
                    FileEditorManager.getInstance(project).openFile(scenarioVF, true);

                    /*TODO LOG.info success*/

                   //Notifications.Bus.notify(new Notification("PerfCake Plugin", "scenario created", name, NotificationType.INFORMATION), project);
                    /*TODO decide*/


                } catch (IOException e) {     /*TODO nemoze nastat -> LOG.error*/
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (JAXBException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}