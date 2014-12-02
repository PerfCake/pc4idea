package org.perfcake.pc4idea.editor.designer.outercomponents;

import org.perfcake.pc4idea.editor.designer.common.ScenarioDialogEditor;
import org.perfcake.pc4idea.editor.designer.common.ScenarioImportHandler;
import org.perfcake.pc4idea.editor.designer.editors.AbstractEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 28.9.2014
 */
public abstract class AbstractPanel extends JPanel {
    private JPopupMenu popupMenu;

    public AbstractPanel() {
        this.setOpaque(false);

        popupMenu = new JPopupMenu();
        boolean separatorAfterFirst = false;
        for (JMenuItem item : getPopupMenuItems()){
            popupMenu.add(item);
            if (!separatorAfterFirst) {
                popupMenu.add(new JPopupMenu.Separator());
                separatorAfterFirst = true;
            }
        }

        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getButton() == MouseEvent.BUTTON1) {
                    if (evt.getClickCount() == 2) {
                        ScenarioDialogEditor dialog = new ScenarioDialogEditor(getEditorPanel());
                        dialog.show();
                        if (dialog.getExitCode() == 0) {
                            applyChanges();
                        }
                    }
                }
                if (evt.getButton() == MouseEvent.BUTTON3) {
                     popupMenu.show(AbstractPanel.this, evt.getX(), evt.getY());
                }
            }
        });

        this.setTransferHandler(new ScenarioImportHandler() {
            @Override
            public void performImport(String transferredData) {
                AbstractPanel.this.performImport(transferredData);
            }
        });

    }

    protected abstract List<JMenuItem> getPopupMenuItems();
    protected abstract void performImport(String transferredData);
    protected abstract Color getColor();
    protected abstract AbstractEditor getEditorPanel();
    protected abstract void applyChanges();
    public abstract void setComponentModel(Object componentModel);
    public abstract Object getComponentModel();

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(getColor());
        g2D.drawRoundRect(4, 4, this.getWidth() - 8, this.getHeight() - 8, 20, 20);
    }
}
