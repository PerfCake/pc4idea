package org.perfcake.pc4idea.api.editor.gui.component;

import org.perfcake.pc4idea.api.editor.actions.ActionType;
import org.perfcake.pc4idea.api.editor.color.ColorAdjustable;
import org.perfcake.pc4idea.api.editor.editor.ContextProvider;
import org.perfcake.pc4idea.api.editor.swing.JRoundedRectangle;
import org.perfcake.pc4idea.api.editor.swing.ScenarioImportHandler;
import org.perfcake.pc4idea.api.editor.swing.ScenarioPopupMenu;
import org.perfcake.pc4idea.impl.editor.actions.RedoAction;
import org.perfcake.pc4idea.impl.editor.actions.UndoAction;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Superclass for every Scenario component gui.
 *
 * @author Stanislav Kaleta
 */
public abstract class ComponentGui extends JRoundedRectangle implements ColorAdjustable {

    public ComponentGui(ContextProvider context) {
        getActionMap().put(ActionType.UNDO, new UndoAction(context));
        getActionMap().put(ActionType.REDO, new RedoAction(context));

        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getButton() == MouseEvent.BUTTON1) {
                    if (evt.getClickCount() == 2) {
                        getActionMap().get(ActionType.EDIT).actionPerformed(null);
                    }
                }
                if (evt.getButton() == MouseEvent.BUTTON3) {
                    ScenarioPopupMenu popupMenu = new ScenarioPopupMenu(getActionMap());
                    popupMenu.show(ComponentGui.this, evt.getX(), evt.getY());
                }
            }
        });

        this.setTransferHandler(new ScenarioImportHandler() {
            @Override
            public void performImport(String transferredData) {
                ComponentGui.this.performImport(transferredData);
            }
        });
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
    public abstract void updateGui();
}
