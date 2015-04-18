package org.perfcake.pc4idea.api.editor.swing;

import org.perfcake.pc4idea.api.editor.actions.ActionType;
import org.perfcake.pc4idea.api.editor.awt.WrapLayout;
import org.perfcake.pc4idea.api.editor.gui.component.AbstractComponentGUI;
import org.perfcake.pc4idea.api.editor.modelwrapper.HasGUIChildren;
import org.perfcake.pc4idea.api.editor.modelwrapper.ModelWrapper;
import org.perfcake.pc4idea.impl.editor.actions.ReorderAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Stanislav Kaleta on 3/7/15.
 */
public class ComponentsPanel extends JPanel {
    private HasGUIChildren parent;
    private List<ModelWrapper> componentList;

    private int widestComponentWidth = 0;

    public ComponentsPanel(HasGUIChildren parent){
        this.parent = parent;
        componentList = new ArrayList<>();
        this.setLayout(new WrapLayout(FlowLayout.LEADING,0,0));

        //this.setOpaque(false);/*TODO*/
        setUpReordering();
    }

    private void setUpReordering(){
        this.addMouseListener(new ComponentDragListener() {
            @Override
            public int mousePressedActionPerformed(MouseEvent e) {
                int pressedComponent = -1;
                if (e.getComponent() instanceof AbstractComponentGUI) {
                    for (int i = 0; i < componentList.size(); i++) {
                        if (e.getComponent().equals(componentList.get(i).getGUI())) {
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
                        if (e.getComponent().equals(componentList.get(i).getGUI())) {
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
                ReorderAction action = (ReorderAction) parent.getGUI().getActionMap().get(ActionType.REORDER);
                action.preActionPerformed(componentList);
            }
        });
    }

    public void updateComponents() {
        componentList.clear();
        this.removeAll();
        repaint();
        widestComponentWidth = 0;

        for (ModelWrapper modelWrapper : parent.getChildrenModels()){
            componentList.add(modelWrapper);
            AbstractComponentGUI gui = modelWrapper.getGUI();
            this.add(gui);
            if (gui.getPreferredSize().width > widestComponentWidth) {
                widestComponentWidth = gui.getPreferredSize().width;
            }
        }
        revalidate();
    }

    public Point getComponentAnchorPoint(Object object, boolean bottomEdge){
        Point anchorPoint = new Point(0,0);
        for (ModelWrapper component : componentList){
            if (component.retrieveModel().equals(object)){
                anchorPoint = component.getGUI().getLocation();
                int heightOffset = (bottomEdge) ? 37 : 4;
                anchorPoint.setLocation(anchorPoint.getX()+this.getX()+4+component.getGUI().getWidth()/2,anchorPoint.getY()+this.getY()+heightOffset);
            }
        }
        return anchorPoint;
    }



    @Override
    public Dimension getMinimumSize() {
        return new Dimension(widestComponentWidth, super.getPreferredSize().height);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(parent.getGUI().getWidth() - 20, super.getPreferredSize().height);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(parent.getGUI().getWidth() - 20, super.getPreferredSize().height);
    }
}
