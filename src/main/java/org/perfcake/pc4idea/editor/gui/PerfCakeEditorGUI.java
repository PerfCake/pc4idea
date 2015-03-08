package org.perfcake.pc4idea.editor.gui;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.treeStructure.Tree;
import org.jetbrains.annotations.NotNull;
import org.perfcake.pc4idea.editor.PerfCakeClassProvider;
import org.perfcake.pc4idea.editor.PerfCakeClassProviderException;
import org.perfcake.pc4idea.editor.actions.CommitAction;
import org.perfcake.pc4idea.editor.actions.RedoAction;
import org.perfcake.pc4idea.editor.actions.UndoAction;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 27.2.2015
 */
public class PerfCakeEditorGUI extends JPanel {
    private static final Logger LOG = Logger.getInstance("#...editor.PerfCakeEditorGUI");



    private JBSplitter jbSplitter;
    private JScrollPane scrollPaneMenu;
    private JPanel panelMenu;
    private JTree additiveComponentsTree;

    private JScrollPane scrollPaneScenario;

    private ScenarioGUI scenarioGUI;




    public PerfCakeEditorGUI(@NotNull CommitAction commitAction,@NotNull UndoAction undoAction, @NotNull RedoAction redoAction){
        this.getActionMap().put("COMMIT",commitAction);
        this.getActionMap().put("UNDO",undoAction);
        this.getActionMap().put("REDO",redoAction);
        initComponents();

        this.setLayout(new GridLayout(1,1));
        this.add(jbSplitter);
    }

    private void initComponents() {
        jbSplitter = new JBSplitter(false);
        panelMenu = new JPanel();
        scrollPaneMenu = ScrollPaneFactory.createScrollPane(panelMenu);
        additiveComponentsTree = new Tree(new DefaultTreeModel(new DefaultMutableTreeNode("root")));
        scenarioGUI = new ScenarioGUI(this.getActionMap());
        scrollPaneScenario = ScrollPaneFactory.createScrollPane(scenarioGUI);

        jbSplitter.setFirstComponent(scrollPaneMenu);
        jbSplitter.setSecondComponent(scrollPaneScenario);
        jbSplitter.setProportion(0.15f);

        panelMenu.setLayout(new GridLayout(1, 1));
        panelMenu.add(additiveComponentsTree);
        panelMenu.setBackground(EditorColorsManager.getInstance().getGlobalScheme().getDefaultBackground());/*TODO maybe coloring is solving that(need to impl interface!!)*/

        PerfCakeClassProvider classProvider = new PerfCakeClassProvider();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) additiveComponentsTree.getModel().getRoot();
        DefaultMutableTreeNode messages = new DefaultMutableTreeNode("Messages");
        messages.add(new DefaultMutableTreeNode("Message"));
        root.add(messages);
        DefaultMutableTreeNode validators = new DefaultMutableTreeNode("Validators");
        try {
            for (String clazz : classProvider.findValidators()){
                validators.add(new DefaultMutableTreeNode(clazz));
            }
        } catch (PerfCakeClassProviderException e) {
            LOG.error(e.getMessage());
        }
        root.add(validators);
        DefaultMutableTreeNode reporters = new DefaultMutableTreeNode("Reporters");
        try {
            for (String clazz : classProvider.findReporters()){
                reporters.add(new DefaultMutableTreeNode(clazz));
            }
        } catch (PerfCakeClassProviderException e) {
            LOG.error(e.getMessage());
        }
        root.add(reporters);
        DefaultMutableTreeNode destinations = new DefaultMutableTreeNode("Destinations");
        try {
            for (String clazz : classProvider.findDestinations()){
                destinations.add(new DefaultMutableTreeNode(clazz));
            }
        } catch (PerfCakeClassProviderException e) {
            LOG.error(e.getMessage());
        }
        root.add(destinations);
        DefaultMutableTreeNode properties = new DefaultMutableTreeNode("Properties");
        properties.add(new DefaultMutableTreeNode("Property"));
        root.add(properties);
        DefaultMutableTreeNode connections = new DefaultMutableTreeNode("Connections");
        connections.add(new DefaultMutableTreeNode("Attach validator"));
        root.add(connections);
        additiveComponentsTree.expandRow(0);
        additiveComponentsTree.setRootVisible(false);
        additiveComponentsTree.setOpaque(false);
        additiveComponentsTree.setDragEnabled(true);
        additiveComponentsTree.setTransferHandler(new TransferHandler() {
            @Override
            public int getSourceActions(JComponent c) {
                return COPY;
            }

            @Override
            public Transferable createTransferable(JComponent c) {
                DefaultMutableTreeNode node = ((DefaultMutableTreeNode) ((JTree) c).getLastSelectedPathComponent());
                if (!node.isLeaf()) {
                    return null;
                } else {
                    return new StringSelection(node.toString());
                }
            }
        });







    }

    public ScenarioGUI getScenarioGUI(){
        return scenarioGUI;
    }

    public JComponent getPreferredFocusedComponent() {
        return jbSplitter;
    }

    public void dispose() {
/*TODO dont forget to save*/
//        System.out.println(Thread.activeCount() + " " +
//                Thread.currentThread().getName());
//        /*TODO threadIntrpted exc.(dispose in porgress?)*/
//        /*TODO filewatcher?*/
//        file.getFileSystem().removeVirtualFileListener(scenarioVirtualFileListener);
//        document.removeDocumentListener(scenarioDocumentListener);
//
//        xmlEditor.dispose();
//        EditorHistoryManager.getInstance(project).updateHistoryEntry(file, false);

    }






}
