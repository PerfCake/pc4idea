package org.perfcake.pc4idea.editor;

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

    public ScenarioPopupMenu(ActionMap generalActions, List<JMenuItem> specificMenuItems){
        JMenuItem undoItem = new JMenuItem();
        UndoAction undoAction = (UndoAction) generalActions.get("UNDO");
        undoAction.updateState();
        undoItem.setAction(undoAction);
        undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
        add(undoItem);

        JMenuItem redoItem = new JMenuItem();
        RedoAction redoAction = (RedoAction) generalActions.get("REDO");
        redoAction.updateState();
        redoItem.setAction(redoAction);
        redoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK + InputEvent.SHIFT_MASK));
        add(redoItem);

        add(new JSeparator());

        if (specificMenuItems != null) {
            for (JMenuItem item : specificMenuItems) {
                add(item);
            }
        }
    }





}
