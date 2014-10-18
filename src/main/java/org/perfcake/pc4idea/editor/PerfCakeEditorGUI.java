package org.perfcake.pc4idea.editor;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.event.DocumentAdapter;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.impl.EditorHistoryManager;
import com.intellij.openapi.fileEditor.impl.text.TextEditorProvider;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileAdapter;
import com.intellij.openapi.vfs.VirtualFileEvent;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.treeStructure.Tree;
import org.jetbrains.annotations.NotNull;
import org.perfcake.PerfCakeConst;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.panels.*;
import org.perfcake.scenario.XMLFactory;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.awt.*;
import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 17.9.2014
 * To change this template use File | Settings | File Templates.
 */
class PerfCakeEditorGUI extends JPanel /*implements DataProvider, ModuleProvider TODO ??? */{
    private static final Logger LOG = Logger.getInstance("#org.perfcake.pc4idea.editor.PerfCakeEditorGUI");

    private final Project project;
    private final Module module;
    @NotNull private final VirtualFile file;
    private final ScenarioVirtualFileListener scenarioVirtualFileListener;
    private final Document document;
    private final ScenarioDocumentListener scenarioDocumentListener;
    private final FileEditor xmlEditor;
    private Scenario scenarioModel;

    private boolean documentWasModified;/*TODO*/
    private boolean isEditorValid;/*TODO*/

    private JTabbedPane tabbedPane;
    private JComponent tabDesignerComponent;
    private JComponent tabSourceComponent;

    private JBSplitter splitterDesigner;
    private JPanel panelDesignerMenu;
    private JScrollPane scrollPaneDesignerMenu;
    private JPanel panelDesignerScenario;
    private JScrollPane scrollPaneDesignerScenario;
    
    private JTree treeAdditiveCompsForScenario;
    private AbstractPanel panelGenerator;
    private AbstractPanel panelSender;
    private AbstractPanel panelMessages;
    private AbstractPanel panelValidation;
    private AbstractPanel panelReporting;
    private AbstractPanel panelProperties;



    PerfCakeEditorGUI(Project project, @NotNull final Module module, @NotNull final VirtualFile file) {
        LOG.assertTrue(file.isValid());
        this.project = project;
        this.module = module;
        this.file = file;
        scenarioVirtualFileListener = new ScenarioVirtualFileListener();
        this.file.getFileSystem().addVirtualFileListener(scenarioVirtualFileListener);
        document = FileDocumentManager.getInstance().getDocument(this.file);
        LOG.assertTrue(document!=null);
        scenarioDocumentListener = new ScenarioDocumentListener();
        document.addDocumentListener(scenarioDocumentListener);

        xmlEditor = TextEditorProvider.getInstance().createEditor(project, this.file);  /*TODO first assert accept then create*/

        documentWasModified = false;
//        isEditorValid = true   /*TODO*/
//        LOG.assertTrue(isEditorValid);

        initComponents();

        loadScenario();

        GridLayout layout = new GridLayout(1,1);
        this.setLayout(layout);
        this.add(tabbedPane,new GridLayout(1,1));
    }

    private void initComponents() {
        tabbedPane = new JTabbedPane();
        tabDesignerComponent = new JPanel(new GridLayout(1,1));
        tabSourceComponent = xmlEditor.getComponent();
        splitterDesigner = new JBSplitter(false);
        panelDesignerMenu = new JPanel();
        panelDesignerScenario = new JPanel();
        treeAdditiveCompsForScenario = new Tree(new DefaultTreeModel(new DefaultMutableTreeNode("root")));
        panelGenerator = new GeneratorPanel(project);
        panelSender = new SenderPanel(project);
        panelMessages = new MessagesPanel(project);
        panelValidation = new ValidationPanel(project);
        panelReporting = new ReportingPanel(project);
        panelProperties = new PropertiesPanel(project);


        tabbedPane.addTab("Designer", tabDesignerComponent);
        tabbedPane.addTab("Source", tabSourceComponent);
        tabbedPane.setTabPlacement(JTabbedPane.BOTTOM);
        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (tabbedPane.getSelectedIndex() == 0){
                    if (documentWasModified){
                        FileDocumentManager.getInstance().saveDocument(document);
                        loadScenario();
                        documentWasModified = false;
                    }
                }
                if (tabbedPane.getSelectedIndex() == 1){
                    /*TODO save scenario from designer*/
                }
            }
        });

        tabDesignerComponent.add(splitterDesigner);
        scrollPaneDesignerScenario = ScrollPaneFactory.createScrollPane(panelDesignerScenario);
        scrollPaneDesignerScenario.setMinimumSize(new Dimension(400, 0));
        scrollPaneDesignerMenu = ScrollPaneFactory.createScrollPane(panelDesignerMenu);
        scrollPaneDesignerMenu.setMinimumSize(new Dimension(100, 0));
        splitterDesigner.setFirstComponent(scrollPaneDesignerMenu);
        splitterDesigner.setSecondComponent(scrollPaneDesignerScenario);
        splitterDesigner.setProportion(0.15f);

        GroupLayout panelPCCompsMenuLayout = new GroupLayout(panelDesignerMenu);
        panelDesignerMenu.setLayout(panelPCCompsMenuLayout);
        panelPCCompsMenuLayout.setVerticalGroup(panelPCCompsMenuLayout.createSequentialGroup().addComponent(treeAdditiveCompsForScenario));
        panelPCCompsMenuLayout.setHorizontalGroup(panelPCCompsMenuLayout.createSequentialGroup().addComponent(treeAdditiveCompsForScenario));
        panelDesignerMenu.setBackground(EditorColorsManager.getInstance().getGlobalScheme().getDefaultBackground());

        DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeAdditiveCompsForScenario.getModel().getRoot();
        DefaultMutableTreeNode messages = new DefaultMutableTreeNode("Messages");
        messages.add(new DefaultMutableTreeNode("Message"));                                               /*TODO from classpath*/
        root.add(messages);
        DefaultMutableTreeNode validators = new DefaultMutableTreeNode("Validators");
        validators.add(new DefaultMutableTreeNode("DictionaryValidator"));
        validators.add(new DefaultMutableTreeNode("RegExpValidator"));
        validators.add(new DefaultMutableTreeNode("RulesValidator"));
        validators.add(new DefaultMutableTreeNode("ScriptValidator"));
        root.add(validators);
        DefaultMutableTreeNode reporters = new DefaultMutableTreeNode("Reporters");
        reporters.add(new DefaultMutableTreeNode("ThroughputStatsReporter"));
        reporters.add(new DefaultMutableTreeNode("MemoryUsageReporter"));
        reporters.add(new DefaultMutableTreeNode("ResponseTimeStatsReporter"));
        reporters.add(new DefaultMutableTreeNode("WarmUpReporter"));
        reporters.add(new DefaultMutableTreeNode("ThroughputStatsReporter"));
        root.add(reporters);
        DefaultMutableTreeNode destinations = new DefaultMutableTreeNode("Destinations");
        destinations.add(new DefaultMutableTreeNode("ConsoleDestination"));
        destinations.add(new DefaultMutableTreeNode("CsvDestination"));
        destinations.add(new DefaultMutableTreeNode("Log4jDestination"));
        root.add(destinations);
        DefaultMutableTreeNode properties = new DefaultMutableTreeNode("Properties");
        properties.add(new DefaultMutableTreeNode("Property"));
        root.add(properties);
        DefaultMutableTreeNode connections = new DefaultMutableTreeNode("Connections");
        connections.add(new DefaultMutableTreeNode("Attach validator"));
        root.add(connections);
        treeAdditiveCompsForScenario.expandRow(0);
        treeAdditiveCompsForScenario.setRootVisible(false);
        //treeAdditiveCompsForScenario.setDragEnabled(true);
        treeAdditiveCompsForScenario.setOpaque(false);


        panelDesignerScenario.setBackground(EditorColorsManager.getInstance().getGlobalScheme().getDefaultBackground());
        GroupLayout panelPCCompsDesignerLayout = new GroupLayout(panelDesignerScenario);
        panelDesignerScenario.setLayout(panelPCCompsDesignerLayout);
        panelPCCompsDesignerLayout.setHorizontalGroup(
                panelPCCompsDesignerLayout.createParallelGroup()
                        .addComponent(panelGenerator)
                        .addComponent(panelSender)
                        .addGroup(panelPCCompsDesignerLayout.createSequentialGroup()
                                .addGroup(panelPCCompsDesignerLayout.createParallelGroup()
                                        .addComponent(panelMessages)
                                        .addComponent(panelValidation))
                                .addComponent(panelReporting))
                        .addComponent(panelProperties)
        );
        panelPCCompsDesignerLayout.setVerticalGroup(
                panelPCCompsDesignerLayout.createSequentialGroup()
                        .addComponent(panelGenerator)
                        .addComponent(panelSender)
                        .addGroup(panelPCCompsDesignerLayout.createParallelGroup()
                                .addGroup(panelPCCompsDesignerLayout.createSequentialGroup()
                                        .addComponent(panelMessages)
                                        .addComponent(panelValidation))
                                .addComponent(panelReporting))
                        .addComponent(panelProperties)
                        .addContainerGap(0, Short.MAX_VALUE) /*TODO decide*/
        );
    }

    private void loadScenario(){
        try {
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(XMLFactory.class.getResource("/schemas/" + "perfcake-scenario-" + PerfCakeConst.XSD_SCHEMA_VERSION + ".xsd"));

            JAXBContext context = JAXBContext.newInstance(org.perfcake.model.Scenario.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            unmarshaller.setSchema(schema);
            scenarioModel = (org.perfcake.model.Scenario) unmarshaller.unmarshal(new File(file.getPath()));
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        // upadate panels
        panelGenerator.setComponent(scenarioModel.getGenerator());
        panelSender.setComponent(scenarioModel.getSender());
        panelReporting.setComponent(scenarioModel.getReporting());

    }
    // private void saveScenario(){}

    public boolean isModified(){
        return false; /*TODO for save?*/
    }
    public boolean isEditorValid(){
        return true; /*TODO for not valid xml?*/
    }
    public JComponent getPreferredFocusedComponent() {
        return tabbedPane;
    }
    public void dispose() {
//
//        myConnection.disconnect();
//        editor.removeEditorMouseListener(myEditorMouseListener);

        /*TODO filewatcher?*/
        /*TODO save before dispose */
        xmlEditor.dispose();
        EditorHistoryManager.getInstance(project).updateHistoryEntry(file, false);
        file.getFileSystem().removeVirtualFileListener(scenarioVirtualFileListener);
        document.removeDocumentListener(scenarioDocumentListener);
    }

    private final class ScenarioDocumentListener extends DocumentAdapter {
        @Override
        public void documentChanged(DocumentEvent e) {
            /*TODO for testing purpose*/System.out.println("docChanged");
            documentWasModified = true;
        }
    }

    private final class ScenarioVirtualFileListener extends VirtualFileAdapter {
        @Override
        public void contentsChanged(@NotNull VirtualFileEvent event){
            /*TODO for testing purpose*/System.out.println("virtualFileChanged");
            /*TODO maybe loadScenario here - for external/other changes*/

        }
    }

}
