package org.perfcake.editor;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.treeStructure.Tree;
import org.perfcake.editor.panels.*;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 17.9.2014
 * To change this template use File | Settings | File Templates.
 */
public class PCEditorGUI extends JPanel /*implements DataProvider, ModuleProvider TODO*/{
//    private static final Logger LOG = Logger.getInstance("#main.java..GuiEditor"); /*TODO*/

    private final Project project;
    private Module module;
    @NotNull private final VirtualFile file;  /*TODO final?*/

    @NotNull private final JScrollPane scrollPane;

    private JTabbedPane tabbedPane;
    private JPanel panelDesigner;
    private JPanel panelSource;
    private JTextArea textAreaForPanelSource;
    private JBSplitter splitterForDesigner;
    private JPanel panelPCcomponentsMenu;
    private JPanel panelPCcomponentsDesigner;
    private JTree treePCcomponents;

    private JPanel generatorPanel;
    private JPanel senderPanel;
    private JPanel messagesPanel;
    private JPanel validationPanel;
    private JPanel reportingPanel;
    private JPanel propertiesPanel;



    public PCEditorGUI(Project project, @NotNull final Module module, @NotNull final VirtualFile file) {
//        LOG.assertTrue(file.isValid());
        this.project = project;
        this.module = module;
        this.file = file;

        initComponents();

        GridLayout layout = new GridLayout(1,1);
        this.setLayout(layout);

        scrollPane = ScrollPaneFactory.createScrollPane(tabbedPane);
        this.add(scrollPane,new GridLayout(1,1));
    }

    private void initComponents() {
        tabbedPane = new JTabbedPane();
        panelDesigner = new JPanel(new GridLayout(1,1));
        panelSource = new JPanel(new GridLayout(1,1));                         /*TODO problem with file -> try to get by path, xmlEdtiorProvider.getEditor?*/
        textAreaForPanelSource = new JTextArea();//= new EditorTextField(PsiDocumentManager.getInstance(project).getDocument(PsiManager.getInstance(project).findFile(file)),project, StdFileTypes.XML);
        splitterForDesigner = new JBSplitter(false);
        panelPCcomponentsMenu = new JPanel(new GridLayout(1,1));
        panelPCcomponentsDesigner = new JPanel();
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
        ApplicationManager.getApplication().runReadAction(new Runnable() {
            public void run() {
                try {
                    textAreaForPanelSource.setText(new String(file.contentsToByteArray()));
                } catch (IOException e) {
                    e.printStackTrace();  /*TODO log*/
                }
            }
        });
        textAreaForPanelSource.setEditable(false);
        textAreaForPanelSource.setOpaque(false);

        panelDesigner.add(splitterForDesigner);
        panelDesigner.setBackground(EditorColorsManager.getInstance().getGlobalScheme().getDefaultBackground());
        splitterForDesigner.setFirstComponent(panelPCcomponentsMenu);
        splitterForDesigner.setSecondComponent(panelPCcomponentsDesigner);
        splitterForDesigner.setProportion(0.15f);
        splitterForDesigner.setOpaque(false);

        panelPCcomponentsMenu.setMinimumSize(new Dimension(100, 0));
        panelPCcomponentsMenu.setPreferredSize(new Dimension(200, 0));
        panelPCcomponentsMenu.add(treePCcomponents);
        panelPCcomponentsMenu.setOpaque(false);
        DefaultMutableTreeNode root = (DefaultMutableTreeNode)treePCcomponents.getModel().getRoot();
        DefaultMutableTreeNode messages = new DefaultMutableTreeNode("Messages");
        messages.add(new DefaultMutableTreeNode("Message"));
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
        treePCcomponents.setDragEnabled(true);
        treePCcomponents.setOpaque(false);





        panelPCcomponentsDesigner.setMinimumSize(new Dimension(400, 0));
        panelPCcomponentsDesigner.setOpaque(false);
        GroupLayout panelPCcomponentsDesignerLayout = new GroupLayout(panelPCcomponentsDesigner);
        panelPCcomponentsDesigner.setLayout(panelPCcomponentsDesignerLayout);
        panelPCcomponentsDesignerLayout.setHorizontalGroup(
                panelPCcomponentsDesignerLayout.createParallelGroup()
                        .addComponent(generatorPanel)
                        .addComponent(senderPanel)
                        .addGroup(panelPCcomponentsDesignerLayout.createSequentialGroup()
                                .addGroup(panelPCcomponentsDesignerLayout.createParallelGroup()
                                        .addComponent(messagesPanel)
                                        .addComponent(validationPanel))
                                .addComponent(reportingPanel))
                        .addComponent(propertiesPanel)
        );
        panelPCcomponentsDesignerLayout.setVerticalGroup(
                panelPCcomponentsDesignerLayout.createSequentialGroup()
                        .addComponent(generatorPanel)
                        .addComponent(senderPanel)
                        .addGroup(panelPCcomponentsDesignerLayout.createParallelGroup()
                                .addGroup(panelPCcomponentsDesignerLayout.createSequentialGroup()
                                        .addComponent(messagesPanel)
                                        .addComponent(validationPanel))
                                .addComponent(reportingPanel))
                        .addComponent(propertiesPanel)
        );

        }

    public JComponent getPreferredFocusedComponent() {
        return tabbedPane;  /* TODO or? */
    }
    public void dispose() {
        /*TODO ? */
    }


}
