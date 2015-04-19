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
 * Superclass for every Scenario component gui.
 *
 * @author Stanislav Kaleta
 */
public abstract class AbstractComponentGUI extends JRoundedRectangle implements ColorAdjustable {

    /**
     * Editor util
     */
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

    /**
     * Returns util of this editor.
     *
     * @return editor util
     * @see org.perfcake.pc4idea.api.util.PerfCakeEditorUtil
     */
    public PerfCakeEditorUtil getUtil() {
        return util;
    }

    /**
     * Performs commit action. This will cause that competent manager will load the model from the editor
     * and will invoke update scenario file action. This methods should be called right after
     * action which modifies scenario.
     *
     * @param message name of the action which modifies scenario
     */
    public void commitChanges(String message){
        getActionMap().get(ActionType.COMMIT).actionPerformed(new ActionEvent(this,1, message));
    }

    /**
     * Performs DnD import for this component. Every subclass implements this method by its preferences.
     *
     * @param transferredData imported data
     */
    public abstract void performImport(String transferredData);

    /**
     * Opens dialog editor for this component and returns component model after end of the editing.
     *
     * @return component model if dialog was closed with confirmation button, null otherwise
     */
    public abstract Object openEditorDialogAndGetResult();

    /**
     * Updates gui according to actual component model. This method should be used to refresh gui
     * after model modifications.
     */
    public abstract void updateGUI();


}
