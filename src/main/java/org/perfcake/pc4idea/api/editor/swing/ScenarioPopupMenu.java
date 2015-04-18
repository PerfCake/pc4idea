package org.perfcake.pc4idea.api.editor.swing;

import org.perfcake.pc4idea.api.editor.actions.ActionType;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 3.3.2015
 */
public class ScenarioPopupMenu extends JPopupMenu {

    public ScenarioPopupMenu(ActionMap actionMap){
        Action undoAction = actionMap.get(ActionType.UNDO);
        if (undoAction != null) {
            JMenuItem undoItem = new JMenuItem();
            undoItem.setAction(undoAction);
            undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
            add(undoItem);
        }

        Action redoAction = actionMap.get(ActionType.REDO);
        if (redoAction != null) {
            JMenuItem redoItem = new JMenuItem();
            redoItem.setAction(redoAction);
            redoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK + InputEvent.SHIFT_MASK));
            add(redoItem);
        }

        add(new JSeparator());


        //adds
        Action addPropertyAction = actionMap.get(ActionType.ADDP);
        if (addPropertyAction != null) {
            JMenuItem addPropertyItem = new JMenuItem();
            addPropertyItem.setAction(addPropertyAction);
            addPropertyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.SHIFT_MASK));
            add(addPropertyItem);
        }

        Action addMessageAction = actionMap.get(ActionType.ADDM);
        if (addMessageAction != null) {
            JMenuItem addMessageItem = new JMenuItem();
            addMessageItem.setAction(addMessageAction);
            addMessageItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.SHIFT_MASK));
            add(addMessageItem);
        }

        Action addHeaderAction = actionMap.get(ActionType.ADDH);
        if (addHeaderAction != null) {
            JMenuItem addHeaderItem = new JMenuItem();
            addHeaderItem.setAction(addHeaderAction);
            addHeaderItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.SHIFT_MASK));
            add(addHeaderItem);
        }

        Action attachValidatorAction = actionMap.get(ActionType.ATTV);
        if (attachValidatorAction != null) {
            JMenuItem attachValItem = new JMenuItem();
            attachValItem.setAction(attachValidatorAction);
            attachValItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.SHIFT_MASK));
            add(attachValItem);
        }

        Action addValidatorAction = actionMap.get(ActionType.ADDV);
        if (addValidatorAction != null) {
            JMenuItem addValItem = new JMenuItem();
            addValItem.setAction(addValidatorAction);
            addValItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.SHIFT_MASK));
            add(addValItem);
        }

        Action addReporterAction = actionMap.get(ActionType.ADDR);
        if (addReporterAction != null) {
            JMenuItem addRepItem = new JMenuItem();
            addRepItem.setAction(addReporterAction);
            addRepItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.SHIFT_MASK));
            add(addRepItem);
        }

        Action addDestinationAction = actionMap.get(ActionType.ADDD);
        if (addDestinationAction != null) {
            JMenuItem addDestItem = new JMenuItem();
            addDestItem.setAction(addDestinationAction);
            addDestItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.SHIFT_MASK));
            add(addDestItem);
        }

        Action addPeriodAction = actionMap.get(ActionType.ADDPE);
        if (addPeriodAction != null) {
            JMenuItem addPeriodItem = new JMenuItem();
            addPeriodItem.setAction(addPeriodAction);
            addPeriodItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.SHIFT_MASK));
            add(addPeriodItem);
        }

        //edit
        Action editAction = actionMap.get(ActionType.EDIT);
        if (editAction != null) {
            JMenuItem editItem = new JMenuItem();
            editItem.setAction(editAction);
            editItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.SHIFT_MASK));
            add(editItem);
        }

        //toggle
        Action toggleAction = actionMap.get(ActionType.TOGGLE);
        if (toggleAction != null){
            JMenuItem toggleItem = new JMenuItem();
            toggleItem.setAction(toggleAction);
            toggleItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.SHIFT_MASK));
            add(toggleItem);
        }

        //del
        Action delAction = actionMap.get(ActionType.DEL);
        if (delAction != null){
            JMenuItem delItem = new JMenuItem();
            delItem.setAction(delAction);
            delItem.setAccelerator(KeyStroke.getKeyStroke("DELETE"));
            add(delItem);
        }



    }





}
