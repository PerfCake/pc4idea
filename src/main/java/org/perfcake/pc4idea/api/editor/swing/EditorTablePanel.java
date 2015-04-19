package org.perfcake.pc4idea.api.editor.swing;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.table.JBTable;
import org.perfcake.pc4idea.api.editor.editor.tablemodel.EditorTableModel;
import org.perfcake.pc4idea.impl.editor.editor.tablemodel.PropertiesTableModel;
import org.perfcake.pc4idea.todo.Messages;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 11.11.2014                   TODO documentation
 */
public class EditorTablePanel extends JPanel {
    private static final Logger LOG = Logger.getInstance(EditorTablePanel.class);

    private JTable table;
    private DefaultTableCellRenderer defaultTableCellRenderer;

    public EditorTablePanel(AbstractTableModel model){
        initComponents();
        table.setModel(model);
    }

    private void initComponents(){
        table = new JBTable();
        JScrollPane scrollPaneTable = ScrollPaneFactory.createScrollPane(table);
        JButton buttonMoveUp = new JButton(Messages.BUNDLE.getString("UP"), AllIcons.Actions.MoveUp);
        JButton buttonMoveDown = new JButton(Messages.BUNDLE.getString("DOWN"), AllIcons.Actions.MoveDown);
        JButton buttonEdit = new JButton(Messages.BUNDLE.getString("EDIT"), AllIcons.Actions.Edit);
        JButton buttonDelete = new JButton(Messages.BUNDLE.getString("DEL"), AllIcons.Actions.Delete);

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
                }
            }
        });

        JButton buttonAdd = new JButton(Messages.BUNDLE.getString("ADD"), AllIcons.General.Add);
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

        defaultTableCellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (!(table.getModel() instanceof PropertiesTableModel)) {
                    return c;
                }
                PropertiesTableModel model = (PropertiesTableModel) table.getModel();
                Boolean isValid = model.isPropertyValid(row);
                if (isValid == null) {
                    return c;
                }
                float bgBrightness = 1f;
                float fgBrightness = 0.5f;
                float bgSaturation = 0.1f;
                float fgSaturation = 0.9f;
                if (isValid) {
                    c.setBackground(Color.getHSBColor(140 / 360f, bgSaturation, bgBrightness));
                    c.setForeground(Color.getHSBColor(140 / 360f, fgSaturation, fgBrightness));
                } else {
                    c.setBackground(Color.getHSBColor(0 / 360f, bgSaturation, bgBrightness));
                    c.setForeground(Color.getHSBColor(0 / 360f, fgSaturation, fgBrightness));
                }

                return c;
            }
        };
    }

    public void setTableModel(AbstractTableModel model) {
        if (!(model instanceof EditorTableModel)) {
            LOG.error("model isn't instance of EditorTableModel!");
        }
        table.setModel(model);
        table.getColumnModel().getColumn(0).setCellRenderer(defaultTableCellRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(defaultTableCellRenderer);
    }

    public TableModel getTableModel() {
        return table.getModel();
    }

}
