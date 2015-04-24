package org.perfcake.pc4idea.api.editor.swing;

import org.perfcake.pc4idea.api.editor.actions.ActionType;
import org.perfcake.pc4idea.api.editor.awt.WrapLayout;
import org.perfcake.pc4idea.api.editor.gui.component.ComponentGui;
import org.perfcake.pc4idea.api.editor.modelwrapper.component.ComponentModelWrapper;
import org.perfcake.pc4idea.api.editor.modelwrapper.component.HasGUIChildren;
import org.perfcake.pc4idea.impl.editor.actions.ReorderAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Stanislav Kaleta on 3/7/15.        TODO documentation
 */
public class ComponentsPanel extends JPanel {
    private HasGUIChildren parent;
    private List<ComponentModelWrapper> componentList;

    private int widestComponentWidth = 0;

    public ComponentsPanel(HasGUIChildren parent){
        this.parent = parent;
        componentList = new ArrayList<>();
        this.setLayout(new WrapLayout(FlowLayout.LEADING,0,0));

        this.setOpaque(false);
        setUpReordering();
    }

    private void setUpReordering(){
        this.addMouseListener(new ComponentDragListener() {
            @Override
            public int mousePressedActionPerformed(MouseEvent e) {
                int pressedComponent = -1;
                if (e.getComponent() instanceof ComponentGui) {
                    for (int i = 0; i < componentList.size(); i++) {
                        if (e.getComponent().equals(componentList.get(i).getGui())) {
                            pressedComponent = i;
                        }
                    }
                }
                return pressedComponent;
            }

            @Override
            public int mouseEnteredActionPerformed(MouseEvent e) {
                int enteredComponent = -1;
                if (e.getComponent() instanceof ComponentGui) {
                    for (int i = 0; i < componentList.size(); i++) {
                        if (e.getComponent().equals(componentList.get(i).getGui())) {
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
                ReorderAction action = (ReorderAction) parent.getGui().getActionMap().get(ActionType.REORDER);
                action.actionPerformed(componentList);
            }
        });
    }

    public void updateComponents() {
        componentList.clear();
        this.removeAll();
        repaint();
        widestComponentWidth = 0;

        for (ComponentModelWrapper modelWrapper : parent.getChildrenModels()){
            componentList.add(modelWrapper);
            JPanel gui = modelWrapper.getGui();
            this.add(gui);
            if (gui.getPreferredSize().width > widestComponentWidth) {
                widestComponentWidth = gui.getPreferredSize().width;
            }
        }
        revalidate();
    }

    public Point getComponentAnchorPoint(Object object, boolean bottomEdge){
        Point anchorPoint = new Point(0,0);
        for (ComponentModelWrapper component : componentList){
            if (component.retrieveModel().equals(object)){
                anchorPoint = component.getGui().getLocation();
                int heightOffset = (bottomEdge) ? 37 : 4;
                anchorPoint.setLocation(anchorPoint.getX()+this.getX()+4+component.getGui().getWidth()/2,anchorPoint.getY()+this.getY()+heightOffset);
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
        return new Dimension(parent.getGui().getWidth() - 20, super.getPreferredSize().height);
    }

    @Override
    public Dimension getMaximumSize() {
        return new Dimension(parent.getGui().getWidth() - 20, super.getPreferredSize().height);
    }
}
