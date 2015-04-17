package org.perfcake.pc4idea.api.editor.swing;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 12.11.2014
 */
public abstract class ComponentDragListener extends MouseInputAdapter {
    private boolean mousePressed;
    private int selectedComponent;
    private int expectedReleaseComponent;

    public ComponentDragListener(){
        mousePressed = false;
    }

    @Override
    public void mousePressed(MouseEvent e){
        int pressedComponent = mousePressedActionPerformed(e);
        if (pressedComponent >= 0){
            selectedComponent = pressedComponent;
            expectedReleaseComponent = pressedComponent;
            mousePressed = true;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (mousePressed) {
            expectedReleaseComponent = mouseEnteredActionPerformed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e){
        if(mousePressed) {
            if (selectedComponent != expectedReleaseComponent) {
                if (expectedReleaseComponent >= 0){
                    mouseReleasedActionPerformed(selectedComponent,expectedReleaseComponent);
                }
            }
            mousePressed = false;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e){
        MouseEvent wrappedEvent = new MouseEvent((Component)e.getSource(),e.getID(),e.getWhen(),e.getModifiers(),e.getX()+10,e.getY()+40,e.getClickCount(),e.isPopupTrigger(),e.getButton());
        ((JPanel)e.getComponent().getAccessibleContext().getAccessibleParent()).dispatchEvent(wrappedEvent);
    }

    public abstract int mousePressedActionPerformed(MouseEvent e);
    public abstract int mouseEnteredActionPerformed(MouseEvent e);
    public abstract void mouseReleasedActionPerformed(int selectedComponent, int releasedComponent);
}
