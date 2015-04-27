package org.perfcake.pc4idea.impl.editor.gui;

import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.treeStructure.Tree;
import org.perfcake.pc4idea.api.util.Messages;
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
public class ScenarioEditorGui extends JPanel {
    private ScenarioGui scenarioGui;

    public ScenarioEditorGui(ScenarioGui scenarioGui) {
        this.scenarioGui = scenarioGui;
        initComponents();
    }

    private void initComponents() {
        JBSplitter jbSplitter;
        jbSplitter = new JBSplitter(false);
        JPanel panelMenu = new JPanel();
        JScrollPane scrollPaneMenu = ScrollPaneFactory.createScrollPane(panelMenu);
        JTree additiveComponentsTree = new Tree(new DefaultTreeModel(new DefaultMutableTreeNode("root")));

        jbSplitter.setFirstComponent(scrollPaneMenu);

        JScrollPane scrollPaneScenario = ScrollPaneFactory.createScrollPane(scenarioGui);
        jbSplitter.setSecondComponent(scrollPaneScenario);

        jbSplitter.setProportion(0.15f);

        panelMenu.setLayout(new GridLayout(1, 1));
        panelMenu.add(additiveComponentsTree);
        panelMenu.setBackground(EditorColorsManager.getInstance().getGlobalScheme().getDefaultBackground());

        PerfCakeReflectUtil reflectUtil = new PerfCakeReflectUtil(null);
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) additiveComponentsTree.getModel().getRoot();

        DefaultMutableTreeNode messages = new DefaultMutableTreeNode(Messages.Scenario.MESSAGES);
        messages.add(new DefaultMutableTreeNode(Messages.Scenario.MESSAGE));
        root.add(messages);

        DefaultMutableTreeNode validators = new DefaultMutableTreeNode(Messages.Scenario.VALIDATOR + "s");
        for (String clazz : reflectUtil.findComponentClassNames(PerfCakeReflectUtil.VALIDATOR)) {
            validators.add(new DefaultMutableTreeNode(clazz));
        }
        root.add(validators);

        DefaultMutableTreeNode reporters = new DefaultMutableTreeNode(Messages.Scenario.REPORTER + "s");
        for (String clazz : reflectUtil.findComponentClassNames(PerfCakeReflectUtil.REPORTER)) {
            reporters.add(new DefaultMutableTreeNode(clazz));
        }
        root.add(reporters);

        DefaultMutableTreeNode destinations = new DefaultMutableTreeNode(Messages.Scenario.DESTINATION + "s");
        for (String clazz : reflectUtil.findComponentClassNames(PerfCakeReflectUtil.DESTINATION)) {
            destinations.add(new DefaultMutableTreeNode(clazz));
        }
        root.add(destinations);

        DefaultMutableTreeNode properties = new DefaultMutableTreeNode(Messages.Scenario.PROPERTIES);
        properties.add(new DefaultMutableTreeNode(Messages.Scenario.PROPERTY));
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

        this.setLayout(new GridLayout(1,1));
        this.add(jbSplitter);
    }

    public ScenarioGui getPreferredFocusedComponent() {
        return scenarioGui;
    }
}
