package org.perfcake.pc4idea.editor.wizard;

import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.table.JBTable;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 19.10.2014
 * To change this template use File | Settings | File Templates.
 */
public class PropertiesEditor extends JPanel {
    private JScrollPane scrollPaneTableProperties;
    private JTable tableProperties;
    private JButton buttonAddProperty;
    private JButton buttonEditProperty;
    private JButton buttonDeleteProperty;

     public PropertiesEditor(){


         initComponents();
     }

    private void initComponents(){
        scrollPaneTableProperties = ScrollPaneFactory.createScrollPane(tableProperties);
        /*TODO*/tableProperties = new JBTable();
        buttonAddProperty = new JButton("Add");
        buttonEditProperty = new JButton("Edit");
        buttonDeleteProperty = new JButton("Delete");

    }



    private class PropertiesTableModel extends AbstractTableModel {



        @Override
        public int getRowCount() {
            return 0;
        }

        @Override
        public int getColumnCount() {
            return 0;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return null;
        }
    }
}
