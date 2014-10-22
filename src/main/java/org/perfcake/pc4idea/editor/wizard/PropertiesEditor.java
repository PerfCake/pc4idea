package org.perfcake.pc4idea.editor.wizard;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.table.JBTable;
import org.jetbrains.annotations.Nullable;
import org.perfcake.model.Property;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 19.10.2014
 */
public class PropertiesEditor extends JPanel {
    private JTable tableProperties;
    private JScrollPane scrollPaneTableProperties;
    private JButton buttonAddProperty;
    private JButton buttonEditProperty;
    private JButton buttonDeleteProperty;

     public PropertiesEditor(){
         initComponents();
     }

    private void initComponents(){
        tableProperties = new JBTable();
        scrollPaneTableProperties = ScrollPaneFactory.createScrollPane(tableProperties);
        tableProperties.setModel(new PropertiesTableModel());
        tableProperties.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 || e.getButton() == MouseEvent.BUTTON3) {
                    if (e.getClickCount() == 2) {
                        PropertyEditor propertyEditor = new PropertyEditor();
                        int selectedRow = tableProperties.getSelectedRow();
                        propertyEditor.setProperty(((PropertiesTableModel)tableProperties.getModel()).getProperties().get(selectedRow));
                        ComponentEditor editor = new ComponentEditor("Property Editor",propertyEditor);
                        editor.show();
                        if (editor.getExitCode() == 0) {
                            ((PropertiesTableModel)tableProperties.getModel()).getProperties().set(selectedRow, propertyEditor.getProperty());
                            tableProperties.repaint();
                        }
                    }
                }
            }
        });
        tableProperties.getTableHeader().setReorderingAllowed(false);
        ((DefaultTableCellRenderer)tableProperties.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
        buttonAddProperty = new JButton("Add");
        buttonAddProperty.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PropertyEditor propertyEditor = new PropertyEditor();
                ComponentEditor editor = new ComponentEditor("Property Editor",propertyEditor);
                editor.show();
                if (editor.getExitCode() == 0) {
                    Property property = propertyEditor.getProperty();
                    ((PropertiesTableModel)tableProperties.getModel()).getProperties().add(property);
                    tableProperties.repaint();
                    scrollPaneTableProperties.revalidate();
                }
            }
        });
        buttonEditProperty = new JButton("Edit");
        buttonEditProperty.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableProperties.getSelectedRow();
                if (selectedRow >= 0) {
                    PropertyEditor propertyEditor = new PropertyEditor();
                    propertyEditor.setProperty(((PropertiesTableModel) tableProperties.getModel()).getProperties().get(selectedRow));
                    ComponentEditor editor = new ComponentEditor("Property Editor", propertyEditor);
                    editor.show();
                    if (editor.getExitCode() == 0) {
                        ((PropertiesTableModel) tableProperties.getModel()).getProperties().set(selectedRow, propertyEditor.getProperty());
                        tableProperties.repaint();
                    }
                }
            }
        });
        buttonDeleteProperty = new JButton("Delete");
        buttonDeleteProperty.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableProperties.getSelectedRow();
                if (selectedRow >= 0) {
                    ((PropertiesTableModel)tableProperties.getModel()).getProperties().remove(selectedRow);
                    tableProperties.repaint();
                    scrollPaneTableProperties.revalidate();
                }
            }
        });

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addComponent(scrollPaneTableProperties)
                .addGap(5)
                .addGroup(layout.createParallelGroup()
                        .addComponent(buttonAddProperty, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                        .addComponent(buttonEditProperty, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
                        .addComponent(buttonDeleteProperty, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)));
        layout.setVerticalGroup(layout.createParallelGroup()
            .addComponent(scrollPaneTableProperties)
            .addGroup(layout.createSequentialGroup()
                .addComponent(buttonAddProperty)
                .addComponent(buttonEditProperty)
                .addComponent(buttonDeleteProperty)));
    }

    public void setProperties(List<Property> properties){
        PropertiesTableModel model = new PropertiesTableModel();
        model.addProperties(properties);
        tableProperties.setModel(model);
    }

    public List<Property> getProperties(){
        return ((PropertiesTableModel)tableProperties.getModel()).getProperties();
    }

    private class PropertiesTableModel extends AbstractTableModel {
        private List<Property> listProperties = new ArrayList<>();

        private PropertiesTableModel(){}

        public void addProperties(List<Property> properties){
            listProperties.addAll(properties);
        }

        public List<Property> getProperties(){
            return listProperties;
        }

        @Override
        public int getRowCount() {
            return listProperties.size();
        }
        @Override
        public int getColumnCount() {
            return 2;
        }
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Property property = listProperties.get(rowIndex);
            switch (columnIndex){
                case 0: return property.getName();
                case 1: return property.getValue();
                default: return null;
            }
        }
        @Override
        public String getColumnName(int columnIndex){
            switch (columnIndex){
                case 0: return "Property Name";
                case 1: return "Property Value";
                default: return "";
            }
        }
    }

    private class ComponentEditor extends DialogWrapper {
        private JPanel centerPanel;

        private ComponentEditor(String title, JPanel centerPanel){
            super(false);
            setTitle(title);
            this.centerPanel = centerPanel;
            this.setResizable(true);
            init();
        }
        @Nullable
        @Override
        protected JComponent createCenterPanel() {
            return centerPanel;
        }
    }
}
