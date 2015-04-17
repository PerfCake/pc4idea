package org.perfcake.pc4idea.todo;

import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.table.JBTable;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 11.11.2014
 */
public abstract class EditorTablePanel extends JPanel {
    private JTable table;
    private JScrollPane scrollPaneTable;
    private JButton buttonAdd;
    private JButton buttonEdit;
    private JButton buttonDelete;

    public EditorTablePanel(AbstractTableModel model){
        initComponents();
        table.setModel(model);
    }

    private void initComponents(){
        table = new JBTable();
        scrollPaneTable = ScrollPaneFactory.createScrollPane(table);
        table.getTableHeader().setReorderingAllowed(false);
        ((DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tableClickedActionPerformed(e);
            }
        });
        /*TODO reordering rows maybe needed: interface for tableModel, dnd support*/

        buttonAdd = new JButton("Add");
        buttonAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonAddActionPerformed(e);
            }
        });
        buttonEdit = new JButton("Edit");
        buttonEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonEditActionPerformed(e);
            }
        });
        buttonDelete = new JButton("Delete");
        buttonDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buttonDeleteActionPerformed(e);
            }
        });

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addComponent(scrollPaneTable)
                .addGap(5)
                .addGroup(layout.createParallelGroup()
                        .addComponent(buttonAdd, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                        .addComponent(buttonEdit, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                        .addComponent(buttonDelete, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)));
        layout.setVerticalGroup(layout.createParallelGroup()
                .addComponent(scrollPaneTable)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(buttonAdd)
                        .addComponent(buttonEdit)
                        .addComponent(buttonDelete)));
    }

    public JTable getTable(){
        return table;
    }

    public abstract void tableClickedActionPerformed(MouseEvent e);
    public abstract void buttonAddActionPerformed(ActionEvent e);
    public abstract void buttonEditActionPerformed(ActionEvent e);
    public abstract void buttonDeleteActionPerformed(ActionEvent e);
}
