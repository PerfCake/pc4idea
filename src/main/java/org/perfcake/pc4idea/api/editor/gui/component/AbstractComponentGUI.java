package org.perfcake.pc4idea.api.editor.gui.component;

import org.perfcake.pc4idea.api.editor.actions.ActionType;
import org.perfcake.pc4idea.api.editor.color.ColorAdjustable;
import org.perfcake.pc4idea.api.editor.swing.JRoundedRectangle;
import org.perfcake.pc4idea.api.editor.swing.ScenarioImportHandler;
import org.perfcake.pc4idea.api.editor.swing.ScenarioPopupMenu;
import org.perfcake.pc4idea.api.util.PerfCakeEditorUtil;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 3.3.2015
 */
public abstract class AbstractComponentGUI extends JRoundedRectangle implements ColorAdjustable {
    private PerfCakeEditorUtil util;

    public AbstractComponentGUI(PerfCakeEditorUtil util) {
        this.util = util;

        this.setActionMap(util.getBaseActionMap());

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

    public PerfCakeEditorUtil getUtil() {
        return util;
    }

    public void commitChanges(String message){
        getActionMap().get(ActionType.COMMIT).actionPerformed(new ActionEvent(this,1, message));
    }

    public abstract void performImport(String transferredData);

    // returns model if dialog closed with OK, null otherwise
    public abstract Object openEditorDialogAndGetResult();

    //update
    public abstract void updateGUI();


}
