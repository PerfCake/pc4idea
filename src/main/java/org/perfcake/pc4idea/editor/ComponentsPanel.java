package org.perfcake.pc4idea.editor;

import com.intellij.ui.GroupedElementsRenderer;
import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.gui.*;
import org.perfcake.pc4idea.editor.swing.WrapLayout;
import sun.plugin.util.UIUtil;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Stanislav Kaleta on 3/7/15.
 */
public class ComponentsPanel extends JPanel {
    private ChildrenHaveGUI parent;
    private List<AbstractComponentGUI> componentList;

    private int widestComponentWidth = 0;

    public ComponentsPanel(ChildrenHaveGUI parent){
        this.parent = parent;
        componentList = new ArrayList<>();
        this.setLayout(new WrapLayout(FlowLayout.LEADING,0,0));

        //this.setOpaque(false);
        setUpReordering();
    }

    private void setUpReordering(){
        this.addMouseListener(new ComponentDragListener() {
            @Override
            public int mousePressedActionPerformed(MouseEvent e) {
                int pressedComponent = -1;
                if (e.getComponent() instanceof AbstractComponentGUI) {
                    for (int i = 0; i < componentList.size(); i++) {
                        if (e.getComponent().equals(componentList.get(i))) {
                            pressedComponent = i;
                        }
                    }
                }
                return pressedComponent;
            }

            @Override
            public int mouseEnteredActionPerformed(MouseEvent e) {
                int enteredComponent = -1;
                if (e.getComponent() instanceof AbstractComponentGUI) {
                    for (int i = 0; i < componentList.size(); i++) {
                        if (e.getComponent().equals(componentList.get(i))) {
                            enteredComponent = i;
                        }
                    }
                }
                return enteredComponent;
            }

            @Override
            public void mouseReleasedActionPerformed(int selectedComponent, int releasedComponent) {
                if (selectedComponent < releasedComponent) {
                    for (int i = 0; i < componentList.size(); i++) {
                        if (i >= selectedComponent) {
                            if (i < releasedComponent) {
                                Collections.swap(componentList, i, i + 1);
                            }
                        }
                    }
                }
                if (selectedComponent > releasedComponent) {
                    for (int i = componentList.size() - 1; 0 <= i; i--) {
                        if (i < selectedComponent) {
                            if (i >= releasedComponent) {
                                Collections.swap(componentList, i, i + 1);
                            }
                        }
                    }
                }
                parent.setChildrenFromGUI(componentList);
            }
        });
    }

    public void updateComponents() {
        componentList.clear();
        this.removeAll();
        repaint();
        widestComponentWidth = 0;

        for (AbstractComponentGUI c : parent.getChildrenAsGUI()){
            componentList.add(c);
            this.add(c);
            if (c.getPreferredSize().width > widestComponentWidth) {
                widestComponentWidth = c.getPreferredSize().width;
            }
        }

        revalidate();
    }




    @Override
    public Dimension getMinimumSize() {
        return new Dimension(widestComponentWidth, super.getPreferredSize().height);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(parent.getWidth() - 20, super.getPreferredSize().height);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(parent.getWidth() - 20, super.getPreferredSize().height);
    }
}
