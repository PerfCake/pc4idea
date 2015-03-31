package org.perfcake.pc4idea.editor.gui;

import org.perfcake.pc4idea.editor.ScenarioImportHandler;
import org.perfcake.pc4idea.editor.actions.ActionType;
import org.perfcake.pc4idea.editor.colors.ColorAdjustable;
import org.perfcake.pc4idea.editor.swing.JRoundedRectangle;
import org.perfcake.pc4idea.editor.swing.ScenarioPopupMenu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 3.3.2015
 */
public abstract class AbstractComponentGUI extends JRoundedRectangle implements ColorAdjustable {
    private ActionMap baseActionMap;
    public AbstractComponentGUI(ActionMap actionMap) {
        this.baseActionMap = actionMap;
        for (Object o : actionMap.allKeys()){
            this.getActionMap().put(o,actionMap.get(o));
        }

        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getButton() == MouseEvent.BUTTON1) {
                    if (evt.getClickCount() == 2) {
                        getActionMap().get(ActionType.EDIT).actionPerformed(null);
                    }
                }
                if (evt.getButton() == MouseEvent.BUTTON3) {
                    ScenarioPopupMenu popupMenu = new ScenarioPopupMenu(getActionMap());
                    popupMenu.show(AbstractComponentGUI.this, evt.getX(), evt.getY());
                    System.out.print(AbstractComponentGUI.this.getClass() + " = ");
                    for (Object s : AbstractComponentGUI.this.getActionMap().allKeys()) {
                        System.out.print(s + " | ");
                    }
                    System.out.println();/*TODO*/

                }
            }
        });

        this.setTransferHandler(new ScenarioImportHandler() {
            @Override
            public void performImport(String transferredData) {
                AbstractComponentGUI.this.performImport(transferredData);
            }
        });
    }

    public ActionMap getBaseActionMap(){
        return baseActionMap;
    }
    public void commitChanges(String message){
        getActionMap().get(ActionType.COMMIT).actionPerformed(new ActionEvent(this,1, message));
    }

    abstract void performImport(String transferredData);

    // returns model if dialog closed with OK, null otherwise
    public abstract Object openEditorDialogAndGetResult();

    //update
    public abstract void updateGUI();


}
