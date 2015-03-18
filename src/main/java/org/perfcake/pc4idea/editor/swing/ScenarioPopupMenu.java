package org.perfcake.pc4idea.editor.swing;

import org.perfcake.pc4idea.editor.actions.ActionType;
import org.perfcake.pc4idea.editor.actions.RedoAction;
import org.perfcake.pc4idea.editor.actions.UndoAction;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;

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


        //edit
        Action editAction = actionMap.get(ActionType.EDIT);
        if (editAction != null) {
            JMenuItem editItem = new JMenuItem();
            editItem.setAction(editAction);
            editItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.SHIFT_MASK));
            add(editItem);
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
