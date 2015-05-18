package org.perfcake.pc4idea.api.editor.swing;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.table.JBTable;
import org.perfcake.pc4idea.api.editor.editor.tablemodel.EditorTableModel;
import org.perfcake.pc4idea.api.util.Messages;
import org.perfcake.pc4idea.impl.editor.editor.tablemodel.PropertiesTableModel;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * User: Stanislav Kaleta
 * Date: 11.11.2014
 */
public class EditorTablePanel extends JPanel {
    private static final Logger LOG = Logger.getInstance(EditorTablePanel.class);

    private JTable table;

    public EditorTablePanel(AbstractTableModel model){
        initComponents();
        table.setModel(model);
        EditorTableCellRenderer cellRenderer = new EditorTableCellRenderer();
        for (int c=0;c<table.getColumnCount();c++){
            table.getColumnModel().getColumn(c).setCellRenderer(cellRenderer);
        }
    }

    private void initComponents(){
        table = new JBTable();
        JScrollPane scrollPaneTable = ScrollPaneFactory.createScrollPane(table);
        JButton buttonMoveUp = new JButton(Messages.Command.UP, AllIcons.Actions.MoveUp);
        JButton buttonMoveDown = new JButton(Messages.Command.DOWN, AllIcons.Actions.MoveDown);
        JButton buttonEdit = new JButton(Messages.Command.EDIT, AllIcons.Actions.Edit);
        JButton buttonDelete = new JButton(Messages.Command.DEL, AllIcons.Actions.Delete);

        table.getTableHeader().setReorderingAllowed(false);
        ((DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
        table.setDragEnabled(true);
        table.setDropMode(DropMode.INSERT_ROWS);
        table.setTransferHandler(new TableRowTransferHandler(table));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 || e.getButton() == MouseEvent.BUTTON3) {
                    if (e.getClickCount() == 2) {
                        int selectedRow = table.getSelectedRow();
                        if (selectedRow >= 0) {
                            ((EditorTableModel) table.getModel()).editRow(selectedRow);
                            return;
                        }
                    }
                    buttonEdit.setEnabled(true);
                    buttonDelete.setEnabled(true);
                    if (table.getSelectedRow() == 0) {
                        buttonMoveUp.setEnabled(false);
                    } else {
                        buttonMoveUp.setEnabled(true);
                    }
                    if (table.getSelectedRow() == table.getRowCount() - 1) {
                        buttonMoveDown.setEnabled(false);
                    } else {
                        buttonMoveDown.setEnabled(true);
                    }

                    if (table.getModel() instanceof PropertiesTableModel){
                        if (!((PropertiesTableModel) table.getModel()).isUserProperty(table.getSelectedRow())){
                            buttonDelete.setEnabled(false);
                        }
                    }
                }
            }
        });

        JButton buttonAdd = new JButton(Messages.Command.ADD, AllIcons.General.Add);
        buttonAdd.setHorizontalAlignment(JButton.LEFT);
        buttonAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((EditorTableModel) table.getModel()).addRow();
            }
        });

        buttonEdit.setHorizontalAlignment(JButton.LEFT);
        buttonEdit.setEnabled(false);
        buttonEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    ((EditorTableModel) table.getModel()).editRow(selectedRow);
                    buttonEdit.setEnabled(false);
                    buttonDelete.setEnabled(false);
                }
            }
        });

        buttonDelete.setHorizontalAlignment(JButton.LEFT);
        buttonDelete.setEnabled(false);
        buttonDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    ((EditorTableModel) table.getModel()).deleteRow(selectedRow);
                    buttonEdit.setEnabled(false);
                    buttonDelete.setEnabled(false);
                }
            }
        });

        buttonMoveUp.setHorizontalAlignment(JButton.LEFT);
        buttonMoveUp.setEnabled(false);
        buttonMoveUp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow > 0) {
                    ((EditorTableModel) table.getModel()).reorderRows(selectedRow, selectedRow - 1);
                    table.setRowSelectionInterval(selectedRow - 1, selectedRow - 1);
                    if (selectedRow - 1 == 0) {
                        buttonMoveUp.setEnabled(false);
                    }
                    buttonMoveDown.setEnabled(true);
                }
            }
        });

        buttonMoveDown.setHorizontalAlignment(JButton.LEFT);
        buttonMoveDown.setEnabled(false);
        buttonMoveDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow < table.getRowCount() - 1 && selectedRow >= 0) {
                    ((EditorTableModel) table.getModel()).reorderRows(selectedRow, selectedRow + 1);
                    table.setRowSelectionInterval(selectedRow + 1, selectedRow + 1);
                    if (selectedRow + 1 == table.getRowCount() - 1) {
                        buttonMoveDown.setEnabled(false);

                    }
                    buttonMoveUp.setEnabled(true);
                }
            }
        });

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addComponent(scrollPaneTable)
                .addGap(5)
                .addGroup(layout.createParallelGroup()
                        .addComponent(buttonAdd, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                        .addComponent(buttonEdit, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                        .addComponent(buttonDelete, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                        .addComponent(buttonMoveUp, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                        .addComponent(buttonMoveDown, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)));
        layout.setVerticalGroup(layout.createParallelGroup()
                .addComponent(scrollPaneTable, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(buttonAdd)
                        .addComponent(buttonEdit)
                        .addComponent(buttonDelete)
                        .addComponent(buttonMoveUp)
                        .addComponent(buttonMoveDown)));
    }

    public void setTableModel(AbstractTableModel model) {
        if (!(model instanceof EditorTableModel)) {
            LOG.error(Messages.Log.INVALID_TABLE_MODEL);
        }
        table.setModel(model);
    }

    public TableModel getTableModel() {
        return table.getModel();
    }

}
