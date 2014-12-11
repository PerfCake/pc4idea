package org.perfcake.pc4idea.editor;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.command.UndoConfirmationPolicy;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.event.DocumentAdapter;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.impl.text.TextEditorProvider;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileAdapter;
import com.intellij.openapi.vfs.VirtualFileEvent;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.treeStructure.Tree;
import org.jetbrains.annotations.NotNull;
import org.perfcake.PerfCakeConst;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.designer.common.DependenciesPanel;
import org.perfcake.pc4idea.editor.designer.common.ScenarioDialogEditor;
import org.perfcake.pc4idea.editor.designer.editors.AbstractEditor;
import org.perfcake.pc4idea.editor.designer.outercomponents.*;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 17.9.2014
 */
public class PerfCakeEditorGUI extends JPanel {
    private static final Logger LOG = Logger.getInstance("#org.perfcake.pc4idea.editor.PerfCakeEditorGUI");

    private final Project project;
    @NotNull private final VirtualFile file;
    @NotNull private final Module module;
    private final ScenarioVirtualFileListener scenarioVirtualFileListener;
    private final Document document;
    private final ScenarioDocumentListener scenarioDocumentListener;
    private final FileEditor xmlEditor;
    private Scenario scenarioModel;

    private boolean documentModifiedInSource;
    private boolean savingScenario;

    private JTabbedPane tabbedPane;
    private JComponent tabSource;
    private JLayeredPane tabDesigner;
    private JPanel layerBackground;
    private JBSplitter layerDesigner;
    private DependenciesPanel layerDependencies;
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
        documentModifiedInSource = false;
        savingScenario = false;

        xmlEditor = TextEditorProvider.getInstance().createEditor(project, this.file);

        initComponents();

        loadScenario();
        setDesignerComponents();

        GridLayout layout = new GridLayout(1,1);
        this.setLayout(layout);
        this.add(tabbedPane,new GridLayout(1,1));

        LOG.info("PerfCake Scenario "+this.file.getName()+" successfully loaded");
    }

    private void initComponents() {
        tabbedPane = new JTabbedPane();
        tabSource = xmlEditor.getComponent();
        tabDesigner = new JLayeredPane();
        layerBackground = new JPanel();
        layerDesigner = new JBSplitter(false);
        layerDependencies = new DependenciesPanel();
        panelDesignerMenu = new JPanel();
        scrollPaneDesignerMenu = ScrollPaneFactory.createScrollPane(panelDesignerMenu);
        panelDesignerScenario = new JPanel();
        scrollPaneDesignerScenario = ScrollPaneFactory.createScrollPane(panelDesignerScenario);
        treeAdditiveCompsForScenario = new Tree(new DefaultTreeModel(new DefaultMutableTreeNode("root")));
        panelGenerator = new GeneratorPanel(new ScenarioEvent());
        panelSender = new SenderPanel(new ScenarioEvent());
        panelMessages = new MessagesPanel(new ScenarioEvent(),module);
        panelValidation = new ValidationPanel(new ScenarioEvent());
        panelReporting = new ReportingPanel(new ScenarioEvent());
        panelProperties = new PropertiesPanel(new ScenarioEvent());

        tabbedPane.addTab("Designer", tabDesigner);
        tabbedPane.addTab("Source", tabSource);
        tabbedPane.setTabPlacement(JTabbedPane.BOTTOM);
        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (tabbedPane.getSelectedIndex() == 0) {
                    if (documentModifiedInSource) {
                        FileDocumentManager.getInstance().saveDocument(document);
                        loadScenario();
                        setDesignerComponents();

                        repaintLayerDependencies();
                    }
                }
                if (tabbedPane.getSelectedIndex() == 1) {
                    documentModifiedInSource = false;
                }
            }
        });

        tabDesigner.add(layerBackground, new Integer(0));
        tabDesigner.add(layerDesigner, new Integer(1));
        tabDesigner.add(layerDependencies, new Integer(2));
        tabDesigner.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);

                layerBackground.setBounds(0, 0, tabDesigner.getSize().width,tabDesigner.getSize().height);
                layerDesigner.setBounds(0, 0, tabDesigner.getSize().width,tabDesigner.getSize().height);
                layerDependencies.setBounds(0, 0, tabDesigner.getSize().width,tabDesigner.getSize().height);
            }
        });

        JLabel labelError = new JLabel("Designer is invalid!",SwingConstants.CENTER);
        labelError.setFont(new Font(labelError.getFont().getName(),labelError.getFont().getStyle(),15));
        JLabel labelHint = new JLabel("(Please continue to the tab Source)",SwingConstants.CENTER);
        labelHint.setFont(new Font(labelHint.getFont().getName(),labelHint.getFont().getStyle(),15));
        SpringLayout layerBackgroundLayout = new SpringLayout();
        layerBackground.setLayout(layerBackgroundLayout);
        layerBackground.add(labelError);
        layerBackground.add(labelHint);
        layerBackgroundLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, labelError,
                0,
                SpringLayout.HORIZONTAL_CENTER, layerBackground);
        layerBackgroundLayout.putConstraint(SpringLayout.VERTICAL_CENTER, labelError,
                0,
                SpringLayout.VERTICAL_CENTER,layerBackground);
        layerBackgroundLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, labelHint,
                0,
                SpringLayout.HORIZONTAL_CENTER, layerBackground);
        layerBackgroundLayout.putConstraint(SpringLayout.NORTH, labelHint,
                0,
                SpringLayout.SOUTH,labelError);

        layerDesigner.setFirstComponent(scrollPaneDesignerMenu);
        layerDesigner.setSecondComponent(scrollPaneDesignerScenario);
        layerDesigner.setProportion(0.15f);

        panelDesignerMenu.setLayout(new GridLayout(1,1));
        panelDesignerMenu.add(treeAdditiveCompsForScenario);
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
        treeAdditiveCompsForScenario.setOpaque(false);
        treeAdditiveCompsForScenario.setDragEnabled(true);
        treeAdditiveCompsForScenario.setTransferHandler(new TransferHandler(){
            @Override
            public int getSourceActions(JComponent c) {
                return COPY;
            }
            @Override
            public Transferable createTransferable(JComponent c) {
                DefaultMutableTreeNode node = ((DefaultMutableTreeNode)((JTree)c).getLastSelectedPathComponent());
                if (!node.isLeaf()){
                    return null;
                } else {
                    return new StringSelection(node.toString());
                }
            }
        });

        panelDesignerScenario.setBackground(EditorColorsManager.getInstance().getGlobalScheme().getDefaultBackground());
        GroupLayout panelDesignerScenarioLayout= new GroupLayout(panelDesignerScenario);
        panelDesignerScenario.setLayout(panelDesignerScenarioLayout);
        panelDesignerScenarioLayout.setHorizontalGroup(
                panelDesignerScenarioLayout.createParallelGroup()
                        .addComponent(panelGenerator)
                        .addComponent(panelSender)
                        .addGroup(panelDesignerScenarioLayout.createSequentialGroup()
                                .addGroup(panelDesignerScenarioLayout.createParallelGroup()
                                        .addComponent(panelMessages)
                                        .addComponent(panelValidation))
                                .addComponent(panelReporting))
                        .addComponent(panelProperties)
        );
        panelDesignerScenarioLayout.setVerticalGroup(
                panelDesignerScenarioLayout.createSequentialGroup()
                        .addComponent(panelGenerator)
                        .addComponent(panelSender)
                        .addGroup(panelDesignerScenarioLayout.createParallelGroup()
                                .addGroup(panelDesignerScenarioLayout.createSequentialGroup()
                                        .addComponent(panelMessages)
                                        .addComponent(panelValidation))
                                .addComponent(panelReporting))
                        .addComponent(panelProperties)
                        .addContainerGap(0, Short.MAX_VALUE)
        );

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                repaintLayerDependencies();
            }
        });
        layerDesigner.getSecondComponent().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                repaintLayerDependencies();
            }
        });
        scrollPaneDesignerScenario.getHorizontalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                repaintLayerDependencies();
            }
        });
        scrollPaneDesignerScenario.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                repaintLayerDependencies();
            }
        });
    }

    private void loadScenario(){
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
            fireScenarioInvalid(e.getCause().toString());
        }
    }
    private void setDesignerComponents(){
        if (scenarioModel != null) {
            panelGenerator.setComponentModel(scenarioModel.getGenerator());
            panelSender.setComponentModel(scenarioModel.getSender());
            panelReporting.setComponentModel(scenarioModel.getReporting());
            panelMessages.setComponentModel(scenarioModel.getMessages());
            panelValidation.setComponentModel(scenarioModel.getValidation());

            if (scenarioModel.getValidation() != null) {
                if (scenarioModel.getMessages() != null) {
                    Set<String> attachedIDs = new TreeSet<>();
                    for (Scenario.Messages.Message message : scenarioModel.getMessages().getMessage()) {
                        for (Scenario.Messages.Message.ValidatorRef validatorRef : message.getValidatorRef()) {
                            attachedIDs.add(validatorRef.getId());
                        }
                    }
                    ((ValidationPanel) panelValidation).setValidationModel(scenarioModel.getValidation(), attachedIDs);
                } else {
                    ((ValidationPanel) panelValidation).setValidationModel(scenarioModel.getValidation(), new TreeSet<String>());
                }
            }

            if (scenarioModel.getMessages() != null) {
                if (scenarioModel.getValidation() != null) {
                    ((MessagesPanel) panelMessages).setValidatorIDSet(((ValidationPanel) panelValidation).getUsedIDSet());
                } else {
                    ((MessagesPanel) panelMessages).setValidatorIDSet(new TreeSet<String>());
                }
            }

            panelProperties.setComponentModel(scenarioModel.getProperties());

            /*TODO for testing purpose*/System.out.println("TEST LOG: designer was set");
            layerDesigner.setVisible(true);
            layerDependencies.setVisible(true);
        }
    }

    private void repaintLayerDependencies(){
        if (scenarioModel != null) {
            ApplicationManager.getApplication().invokeLater(new Runnable() {
                @Override
                public void run() {
                    layerDependencies.removeAllDependencyLines();

                    int adjustmentX = scrollPaneDesignerScenario.getX() - scrollPaneDesignerScenario.getHorizontalScrollBar().getValue();
                    int adjustmentY = scrollPaneDesignerScenario.getY() - scrollPaneDesignerScenario.getVerticalScrollBar().getValue();

                    if (scenarioModel.getMessages() != null && scenarioModel.getValidation() != null) {
                        for (Scenario.Messages.Message message : scenarioModel.getMessages().getMessage()) {
                            Point messagePoint = ((MessagesPanel) panelMessages).getMessageAnchorPoint(message);
                            messagePoint.setLocation(messagePoint.getX() + adjustmentX, messagePoint.getY() + adjustmentY);

                            for (Scenario.Messages.Message.ValidatorRef ref : message.getValidatorRef()) {
                                Point validatorPoint = ((ValidationPanel) panelValidation).getValidatorAnchorPoint(ref.getId());
                                validatorPoint.setLocation(validatorPoint.getX() + adjustmentX, validatorPoint.getY() + adjustmentY);

                                layerDependencies.addDependencyLine(messagePoint, validatorPoint);
                            }
                        }
                    }
                }
            });
        }
    }

    public JComponent getPreferredFocusedComponent() {
        return tabbedPane;
    }

    public void dispose() {

//        System.out.println(Thread.activeCount() + " " +
//                Thread.currentThread().getName());
        /*TODO threadIntrpted exc.(dispose in porgress?)*/
        /*TODO filewatcher?*/
        file.getFileSystem().removeVirtualFileListener(scenarioVirtualFileListener);
        document.removeDocumentListener(scenarioDocumentListener);

        xmlEditor.dispose();
        /*TODO maven cant find method*///EditorHistoryManager.getInstance(project).updateHistoryEntry(file, false);

    }

    private final class ScenarioDocumentListener extends DocumentAdapter {
        @Override
        public void documentChanged(DocumentEvent e) {
            if (tabbedPane.getSelectedIndex() == 1) {
                /*TODO for testing purpose*/System.out.println("TEST LOG: document changed (in Source)");
                documentModifiedInSource = true;
            }
            if (tabbedPane.getSelectedIndex() == 0){
                if (savingScenario){
                    /*TODO for testing purpose*/System.out.println("TEST LOG: document changed (while saving)");
                    FileDocumentManager.getInstance().saveDocument(document);
                    savingScenario = false;
                } else {
                    /*TODO for testing purpose*/System.out.println("TEST LOG: document changed (undo/external)");
                    FileDocumentManager.getInstance().saveDocument(document);
                    loadScenario();
                    setDesignerComponents();
                }

                repaintLayerDependencies();
            }
        }
    }

    private final class ScenarioVirtualFileListener extends VirtualFileAdapter {
        @Override
        public void contentsChanged(@NotNull VirtualFileEvent event){
            /*TODO for testing purpose*/System.out.println("TEST LOG: virtualFile changed: save?:"+event.isFromSave()+" refresh?:"+event.isFromRefresh());
        }
    }

    private void fireScenarioInvalid(String message){
        layerDesigner.setVisible(false);
        layerDependencies.setVisible(false);

        if (scenarioModel != null){
            AbstractEditor scenarioInvalid = new AbstractEditor() {
                @Override
                public String getTitle() {
                    return "Scenario Invalid";
                }
                @Override
                public ValidationInfo areInsertedValuesValid() {
                    return null;
                }
            };
            scenarioInvalid.setLayout(new BoxLayout(scenarioInvalid,BoxLayout.Y_AXIS));
            JLabel labelTitle = new JLabel("Last change made scenario INVALID!");
            labelTitle.setFont(new Font(labelTitle.getFont().getName(),Font.BOLD,15));
            scenarioInvalid.add(labelTitle);
            String[] cause = message.split("; ");
            for (int i=0;i<cause.length;i++){
                scenarioInvalid.add(new JLabel("<html>"+cause[i]+"</html>"));
            }
            scenarioInvalid.add(new JLabel(" "));
            JLabel labelQuestion = new JLabel("Would you like to load last known configuration?");
            labelQuestion.setFont(new Font(labelQuestion.getFont().getName(),labelQuestion.getFont().getStyle(),15));
            scenarioInvalid.add(labelQuestion);

            ScenarioDialogEditor dialog = new ScenarioDialogEditor(scenarioInvalid);
            dialog.show();
            if (dialog.getExitCode() == 0){
                new ScenarioEvent().saveScenario("reload last configuration");
            } else {
                scenarioModel = null;
            }
        }
    }

    public final class ScenarioEvent {
        public void saveGenerator(){
            scenarioModel.setGenerator((Scenario.Generator)panelGenerator.getComponentModel());
            saveScenario("Generator modification");
        }
        public void saveSender(){
            scenarioModel.setSender((Scenario.Sender)panelSender.getComponentModel());
            saveScenario("Sender modification");
        }
        public void saveProperties(){
            scenarioModel.setProperties((Scenario.Properties) panelProperties.getComponentModel());
            saveScenario("Properties modification");
        }
        public void saveMessages(){
            scenarioModel.setMessages((Scenario.Messages) panelMessages.getComponentModel());

            if (scenarioModel.getValidation() != null) {
                if (scenarioModel.getMessages() != null) {
                    Set<String> attachedIDs = new TreeSet<>();
                    for (Scenario.Messages.Message message : scenarioModel.getMessages().getMessage()) {
                        for (Scenario.Messages.Message.ValidatorRef validatorRef : message.getValidatorRef()) {
                            attachedIDs.add(validatorRef.getId());
                        }
                    }
                    ((ValidationPanel) panelValidation).setValidationModel(scenarioModel.getValidation(), attachedIDs);
                } else {
                    ((ValidationPanel) panelValidation).setValidationModel(scenarioModel.getValidation(), new TreeSet<String>());
                }
            }

            saveScenario("Messages modification");
        }
        public void saveValidation(){
            scenarioModel.setValidation((Scenario.Validation) panelValidation.getComponentModel());

            if (scenarioModel.getMessages() != null) {
                if (scenarioModel.getValidation() != null) {
                    ((MessagesPanel) panelMessages).setValidatorIDSet(((ValidationPanel) panelValidation).getUsedIDSet());
                    //if attached validator is deleted, validatorRef will be removed, so messages model needs to be updated
                    scenarioModel.setMessages((Scenario.Messages) panelMessages.getComponentModel());
                } else {
                    ((MessagesPanel) panelMessages).setValidatorIDSet(new TreeSet<String>());
                }
            }

            saveScenario("Validation modification");
        }
        public void saveReporting(){
            scenarioModel.setReporting((Scenario.Reporting) panelReporting.getComponentModel());
            saveScenario("Reporting modification");
        }

        private void saveScenario(final String source) {
            /*TODO uklada do undo zasobnika ale neaktivuje undo menu, az source to aktivuje...*/
            /*TODO undo redo problem - backspace/reload config- (undo last config not apply to doc.)*/
            CommandProcessor.getInstance().executeCommand(project, new Runnable() {
                @Override
                public void run() {
                    ApplicationManager.getApplication().runWriteAction(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                                Schema schema = schemaFactory.newSchema(PerfCakeEditorGUI.this.getClass().getResource(
                                        "/schemas/" + "perfcake-scenario-" + PerfCakeConst.XSD_SCHEMA_VERSION + ".xsd"));

                                JAXBContext context = JAXBContext.newInstance(org.perfcake.model.Scenario.class);
                                Marshaller marshaller = context.createMarshaller();
                                marshaller.setSchema(schema);
                                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

                                StringWriter stringWriter = new StringWriter();

                                marshaller.marshal(scenarioModel, stringWriter);
                                if (!stringWriter.toString().trim().isEmpty() && stringWriter.toString() != null) {
                                    savingScenario = true;
                                    /*TODO for testing purpose*/System.out.println("SAVE: " + source);
                                    document.setText(stringWriter.toString());
                                }
                                stringWriter.close();
                            } catch (JAXBException | SAXException | IOException e) {
                                LOG.error(e.getCause());
                            }
                        }
                    });
                }
            }, source, null, UndoConfirmationPolicy.DEFAULT, document);
        }
    }
}
