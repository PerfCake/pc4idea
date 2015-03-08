package org.perfcake.pc4idea.editor.gui;

import org.perfcake.pc4idea.editor.ScenarioImportHandler;
import org.perfcake.pc4idea.editor.ScenarioPopupMenu;
import org.perfcake.pc4idea.editor.colors.ColorAdjustable;
import org.perfcake.pc4idea.editor.swing.JRoundedRectangle;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 3.3.2015
 */
public abstract class AbstractComponentGUI extends JRoundedRectangle implements ColorAdjustable {

    public AbstractComponentGUI(ActionMap actionMap) {
        this.setActionMap(actionMap);

        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getButton() == MouseEvent.BUTTON1) {
                    if (evt.getClickCount() == 2) {
                        openEditor();
                    }
                }
                if (evt.getButton() == MouseEvent.BUTTON3) {
                    ScenarioPopupMenu popupMenu = new ScenarioPopupMenu(getActionMap(),getMenuItems());
                    popupMenu.show(AbstractComponentGUI.this, evt.getX(), evt.getY());
//                    System.out.print(AbstractComponentGUI.this.getClass() + " = ");
//                    for (Object s : AbstractComponentGUI.this.getActionMap().allKeys()) {
//                        System.out.print(s + " | ");
//                    }
//                    System.out.println();

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

    abstract List<JMenuItem> getMenuItems();
    abstract void performImport(String transferredData);
    abstract void openEditor();

    public abstract void setComponentModel(Object componentModel);
    public abstract Object getComponentModel();

    protected void commitChanges(){
        /*TODO commit action or something*/
    }





}
