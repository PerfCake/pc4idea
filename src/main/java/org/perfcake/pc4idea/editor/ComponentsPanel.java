package org.perfcake.pc4idea.editor;

import org.perfcake.pc4idea.editor.gui.AbstractComponentGUI;
import org.perfcake.pc4idea.editor.swing.WrapLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stanislav Kaleta on 3/7/15.
 */
public class ComponentsPanel<T extends AbstractComponentGUI> extends JPanel {
    private AbstractComponentGUI parent;
    private List<T> componentList;

    public ComponentsPanel(AbstractComponentGUI parent){
        this.parent = parent;
        componentList = new ArrayList<>();
        this.setLayout(new WrapLayout(FlowLayout.LEADING));
        //this.setOpaque(false);
        //setUpReordering();
    }

//    private void setUpReordering(){
//        this.addMouseListener(new ComponentDragListener() {
//            @Override
//            public int mousePressedActionPerformed(MouseEvent e) {
//                int pressedComponent = -1;
//                if (e.getComponent() instanceof PropertyComponent) {
//                    for (int i = 0; i < propertyComponentList.size(); i++) {
//                        if (e.getComponent().equals(propertyComponentList.get(i))) {
//                            pressedComponent = i;
//                        }
//                    }
//                }
//                return pressedComponent;
//            }
//
//            @Override
//            public int mouseEnteredActionPerformed(MouseEvent e) {
//                int enteredComponent = -1;
//                if (e.getComponent() instanceof PropertyComponent) {
//                    for (int i = 0; i < propertyComponentList.size(); i++) {
//                        if (e.getComponent().equals(propertyComponentList.get(i))) {
//                            enteredComponent = i;
//                        }
//                    }
//                }
//                return enteredComponent;
//            }
//
//            @Override
//            public void mouseReleasedActionPerformed(int selectedComponent, int releasedComponent) {
//                if (selectedComponent < releasedComponent) {
//                    for (int i = 0; i < propertiesList.size(); i++) {
//                        if (i >= selectedComponent) {
//                            if (i < releasedComponent) {
//                                Collections.swap(propertiesList, i, i + 1);
//                            }
//                        }
//                    }
//                }
//                if (selectedComponent > releasedComponent) {
//                    for (int i = propertiesList.size() - 1; 0 <= i; i--) {
//                        if (i < selectedComponent) {
//                            if (i >= releasedComponent) {
//                                Collections.swap(propertiesList, i, i + 1);
//                            }
//                        }
//                    }
//                }
//                sender.getProperty().clear();
//                sender.getProperty().addAll(propertiesList);
//                SenderPanel.this.setComponentModel(sender);
//                scenarioEvent.saveSender();
//            }
//        });
//    }

    public void setComponentList(List<T> componentList) {
        this.componentList = componentList;
        this.removeAll();
        for (T t : componentList){
            this.add(t);
        }
    }

    public List<T> getComponentList() {
        return componentList;
    }



    @Override
    public Dimension getPreferredSize() {
        return new Dimension(parent.getWidth()-20,super.getPreferredSize().height);
    }

}
