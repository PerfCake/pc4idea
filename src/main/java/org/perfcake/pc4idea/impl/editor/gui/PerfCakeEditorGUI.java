package org.perfcake.pc4idea.impl.editor.gui;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.treeStructure.Tree;
import org.jetbrains.annotations.NotNull;
import org.perfcake.pc4idea.api.util.PerfCakeEditorUtil;
import org.perfcake.pc4idea.api.util.PerfCakeReflectUtil;

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
    private static final Logger LOG = Logger.getInstance(PerfCakeEditorGUI.class);

    private JBSplitter jbSplitter;

    private ScenarioGUI scenarioGUI;

    public PerfCakeEditorGUI(@NotNull PerfCakeEditorUtil util) {
        scenarioGUI = new ScenarioGUI(util);
        initComponents();

        this.setLayout(new GridLayout(1,1));
        this.add(jbSplitter);
    }

    private void initComponents() {
        jbSplitter = new JBSplitter(false);
        JPanel panelMenu = new JPanel();
        JScrollPane scrollPaneMenu = ScrollPaneFactory.createScrollPane(panelMenu);
        JTree additiveComponentsTree = new Tree(new DefaultTreeModel(new DefaultMutableTreeNode("root")));

        JScrollPane scrollPaneScenario = ScrollPaneFactory.createScrollPane(scenarioGUI);

        jbSplitter.setFirstComponent(scrollPaneMenu);
        jbSplitter.setSecondComponent(scrollPaneScenario);
        jbSplitter.setProportion(0.15f);

        panelMenu.setLayout(new GridLayout(1, 1));
        panelMenu.add(additiveComponentsTree);
        panelMenu.setBackground(EditorColorsManager.getInstance().getGlobalScheme().getDefaultBackground());

        PerfCakeReflectUtil reflectUtil = new PerfCakeReflectUtil(null);
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) additiveComponentsTree.getModel().getRoot();

        DefaultMutableTreeNode messages = new DefaultMutableTreeNode("Messages");
        messages.add(new DefaultMutableTreeNode("Message"));
        root.add(messages);

        DefaultMutableTreeNode validators = new DefaultMutableTreeNode("Validators");
        for (String clazz : reflectUtil.findComponentClassNames(PerfCakeReflectUtil.VALIDATOR)) {
            validators.add(new DefaultMutableTreeNode(clazz));
        }
        root.add(validators);

        DefaultMutableTreeNode reporters = new DefaultMutableTreeNode("Reporters");
        for (String clazz : reflectUtil.findComponentClassNames(PerfCakeReflectUtil.REPORTER)) {
            reporters.add(new DefaultMutableTreeNode(clazz));
        }
        root.add(reporters);

        DefaultMutableTreeNode destinations = new DefaultMutableTreeNode("Destinations");
        for (String clazz : reflectUtil.findComponentClassNames(PerfCakeReflectUtil.DESTINATION)) {
            destinations.add(new DefaultMutableTreeNode(clazz));
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
/*TODO dont forget to save -- ale toto vsetko asi do editoru*/
//        /*TODO threadIntrpted exc.(dispose in porgress?)*/
//        /*TODO filewatcher?*/
//        file.getFileSystem().removeVirtualFileListener(scenarioVirtualFileListener);
//        document.removeDocumentListener(scenarioDocumentListener);
//
//        xmlEditor.dispose();
//        EditorHistoryManager.getInstance(project).updateHistoryEntry(file, false);
    }






}
