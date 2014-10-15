package org.perfcake.pc4idea.editor;

import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiManager;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.treeStructure.Tree;
import org.jetbrains.annotations.NotNull;
import org.perfcake.PerfCakeConst;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.panels.*;
import org.xml.sax.SAXException;

import javax.swing.*;
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
public class PCEditorGUI extends JPanel /*implements DataProvider, ModuleProvider TODO ??? */{
//    private static final Logger LOG = Logger.getInstance("#main.java..GuiEditor"); /*TODO*/

    private final Project project;
    private Module module;
    @NotNull private final VirtualFile file;  /*TODO final?*/
    private Scenario scenarioModel;

    private JTabbedPane tabbedPane;
    private JPanel panelDesigner;
    private JPanel panelSource;
    private EditorTextField/*JTextArea*/ textAreaForPanelSource; /*TODO*/
    private JBSplitter splitterForDesigner;
    private JPanel panelPCCompsMenu;
    private JScrollPane scrollPanePCCompsMenu;
    private JPanel panelPCCompsDesigner;
    private JScrollPane scrollPanePCCompsDesigner;
    private JTree treePCcomponents;

    private AbstractPanel generatorPanel;
    private AbstractPanel senderPanel;
    private AbstractPanel messagesPanel;
    private AbstractPanel validationPanel;
    private AbstractPanel reportingPanel;
    private AbstractPanel propertiesPanel;



    public PCEditorGUI(Project project, @NotNull final Module module, @NotNull final VirtualFile file) {
//        LOG.assertTrue(file.isValid());
        this.project = project;
        this.module = module;
        this.file = file;


        initComponents();

        loadScenario(); /*TODO how saving works*/

        GridLayout layout = new GridLayout(1,1);
        this.setLayout(layout);
        this.add(tabbedPane,new GridLayout(1,1));
    }

    private void initComponents() {
        tabbedPane = new JTabbedPane();
        panelDesigner = new JPanel(new GridLayout(1,1));
        panelSource = new JPanel(new GridLayout(1,1));                         /*TODO problem with file -> try to get by path, xmlEdtiorProvider.getEditor?*/
        textAreaForPanelSource = /*new JTextArea();*/ new EditorTextField(PsiDocumentManager.getInstance(project).getDocument(PsiManager.getInstance(project).findFile(file)),project, StdFileTypes.XML);
        splitterForDesigner = new JBSplitter(false);
        panelPCCompsMenu = new JPanel();
        panelPCCompsDesigner = new JPanel();
        treePCcomponents = new Tree(new DefaultTreeModel(new DefaultMutableTreeNode("root")));
        generatorPanel = new GeneratorPanel(project);
        senderPanel = new SenderPanel(project);
        messagesPanel = new MessagesPanel(project);
        validationPanel = new ValidationPanel(project);
        reportingPanel = new ReportingPanel(project);
        propertiesPanel = new PropertiesPanel(project);


        tabbedPane.addTab("Designer", panelDesigner);
        tabbedPane.addTab("Source", panelSource); /**/
        tabbedPane.setTabPlacement(JTabbedPane.BOTTOM);

        panelSource.add(textAreaForPanelSource);
        panelSource.setBackground(EditorColorsManager.getInstance().getGlobalScheme().getDefaultBackground());
//        ApplicationManager.getApplication().runReadAction(new Runnable() {
//            public void run() {
//                try {
//                    textAreaForPanelSource.setText(new String(file.contentsToByteArray()));
//                } catch (IOException e) {
//                    e.printStackTrace();  /*TODO log*/
//                }
//            }
//        });
        //textAreaForPanelSource.setEditable(false);
        //textAreaForPanelSource.setOpaque(false);

        panelDesigner.add(splitterForDesigner);
        scrollPanePCCompsDesigner = ScrollPaneFactory.createScrollPane(panelPCCompsDesigner);
        scrollPanePCCompsDesigner.setMinimumSize(new Dimension(400,0));
        scrollPanePCCompsMenu = ScrollPaneFactory.createScrollPane(panelPCCompsMenu);
        scrollPanePCCompsMenu.setMinimumSize(new Dimension(100,0));
        splitterForDesigner.setFirstComponent(scrollPanePCCompsMenu);
        splitterForDesigner.setSecondComponent(scrollPanePCCompsDesigner);
        splitterForDesigner.setProportion(0.15f);



        GroupLayout panelPCCompsMenuLayout = new GroupLayout(panelPCCompsMenu);
        panelPCCompsMenu.setLayout(panelPCCompsMenuLayout);
        panelPCCompsMenuLayout.setVerticalGroup(panelPCCompsMenuLayout.createSequentialGroup().addComponent(treePCcomponents));
        panelPCCompsMenuLayout.setHorizontalGroup(panelPCCompsMenuLayout.createSequentialGroup().addComponent(treePCcomponents));
        panelPCCompsMenu.setBackground(EditorColorsManager.getInstance().getGlobalScheme().getDefaultBackground());

        DefaultMutableTreeNode root = (DefaultMutableTreeNode)treePCcomponents.getModel().getRoot();
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
        treePCcomponents.expandRow(0);
        treePCcomponents.setRootVisible(false);
        //treePCcomponents.setDragEnabled(true);
        treePCcomponents.setOpaque(false);


        panelPCCompsDesigner.setBackground(EditorColorsManager.getInstance().getGlobalScheme().getDefaultBackground());
        GroupLayout panelPCCompsDesignerLayout = new GroupLayout(panelPCCompsDesigner);
        panelPCCompsDesigner.setLayout(panelPCCompsDesignerLayout);
        panelPCCompsDesignerLayout.setHorizontalGroup(
                panelPCCompsDesignerLayout.createParallelGroup()
                        .addComponent(generatorPanel)
                        .addComponent(senderPanel)
                        .addGroup(panelPCCompsDesignerLayout.createSequentialGroup()
                                .addGroup(panelPCCompsDesignerLayout.createParallelGroup()
                                        .addComponent(messagesPanel)
                                        .addComponent(validationPanel))
                                .addComponent(reportingPanel))
                        .addComponent(propertiesPanel)
        );
        panelPCCompsDesignerLayout.setVerticalGroup(
                panelPCCompsDesignerLayout.createSequentialGroup()
                        .addComponent(generatorPanel)
                        .addComponent(senderPanel)
                        .addGroup(panelPCCompsDesignerLayout.createParallelGroup()
                                .addGroup(panelPCCompsDesignerLayout.createSequentialGroup()
                                        .addComponent(messagesPanel)
                                        .addComponent(validationPanel))
                                .addComponent(reportingPanel))
                        .addComponent(propertiesPanel)
                        .addContainerGap(0, Short.MAX_VALUE) /*TODO decide*/
        );
    }

    private void loadScenario(){
        try {
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(getClass().getResource("/schemas/" + "perfcake-scenario-" + PerfCakeConst.XSD_SCHEMA_VERSION + ".xsd"));

            JAXBContext context = JAXBContext.newInstance(org.perfcake.model.Scenario.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            unmarshaller.setSchema(schema);
            scenarioModel = (org.perfcake.model.Scenario) unmarshaller.unmarshal(new File(file.getPath()));
        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        // upadate apanels
        generatorPanel.setComponent(scenarioModel.getGenerator());
        senderPanel.setComponent(scenarioModel.getSender());
        reportingPanel.setComponent(scenarioModel.getReporting());




    }

    public JComponent getPreferredFocusedComponent() {
        return tabbedPane;
    }
    public void dispose() {
        /*TODO */
    }


}
