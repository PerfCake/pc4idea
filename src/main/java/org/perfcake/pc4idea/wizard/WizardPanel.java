package org.perfcake.pc4idea.wizard;

import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.components.JBList;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.designer.editors.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 13.11.2014
 */
class WizardPanel extends JPanel {
    private JList<String> wizardStepList;
    private JPanel panelEditors;

    private WizardDialog.WizardEvent wizardEvent;

    private int selectedEditor;

    private Map<Integer, AbstractEditor> editors;

    WizardPanel(){
        selectedEditor = 0;
        initComponents();
        this.setPreferredSize(new Dimension(480,240));
    }

    private void initComponents(){
        editors = new HashMap<>();
        editors.put(0, new NameEditor());
        editors.put(1, new GeneratorEditor());
        editors.put(2, new SenderEditor());
        editors.put(3, new MessagesEditor());
        editors.put(4, new ReportingEditor());
        editors.put(5, new ValidationEditor());
        editors.put(6, new PropertiesEditor());

        panelEditors = new JPanel(new GridLayout(1,1));
        panelEditors.add(editors.get(selectedEditor));

        wizardStepList = new JBList(new String[]{"Name","Generator", "Sender", "Messages", "Reporting", "Validation", "Properties"});
        wizardStepList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                final Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                Color foregroundColor = null;
                if (index < 3){
                    foregroundColor = (editors.get(index).areInsertedValuesValid() == null) ?
                            Color.getHSBColor(120/360f,0.75f, 0.5f) : Color.getHSBColor(0 / 360f, 0.75f, 0.75f);
                } else {
                    boolean isInserted = false;
                    switch (index){
                        case 3: {
                            isInserted = !((MessagesEditor)editors.get(index)).getMessages().getMessage().isEmpty();
                            break;
                        }
                        case 4: {
                            isInserted = !((ReportingEditor)editors.get(index)).getReporting().getReporter().isEmpty();
                            break;
                        }
                        case 5: {
                            isInserted = !((ValidationEditor)editors.get(index)).getValidation().getValidator().isEmpty();
                            break;
                        }
                        case 6: {
                            isInserted = !((PropertiesEditor)editors.get(index)).getObjProperties().getProperty().isEmpty();
                            break;
                        }
                    }
                    foregroundColor = (isInserted) ? Color.getHSBColor(220/360f,0.75f, 0.75f) : Color.getHSBColor(240/360f,0f,0.3f);
                }
                c.setForeground(foregroundColor);

                    if (index == selectedEditor){
                    c.setBackground(Color.getHSBColor(0/360f,0f,0.7f));
                }

                return c;
            }
        });
        Font defaultFont = new JLabel("-").getFont();
        wizardStepList.setFont(new Font(defaultFont.getName(),defaultFont.getStyle(), 15));
        wizardStepList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        wizardStepList.setBackground(EditorColorsManager.getInstance().getGlobalScheme().getDefaultBackground());
        wizardStepList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2){
                    wizardEvent.wizardStepListClicked(wizardStepList.getSelectedIndex());
                }
            }
        });


        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setVerticalGroup(layout.createParallelGroup()
                .addComponent(wizardStepList)
                .addComponent(panelEditors));
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addComponent(wizardStepList, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                .addGap(25)
                .addComponent(panelEditors));
    }

    protected void addWizardEvent(WizardDialog.WizardEvent wizardEvent){
        this.wizardEvent = wizardEvent;
    }

    protected void selectEditor(int pointer){
        selectedEditor = pointer;
        panelEditors.removeAll();
        panelEditors.add(editors.get(selectedEditor));
        wizardStepList.clearSelection();
        panelEditors.revalidate();/*TODO musi byt?*/
        panelEditors.repaint();
        wizardStepList.repaint();
    }

    protected ValidationInfo areRequiredPartsInserted(){
        ValidationInfo info = null;
        if (editors.get(2).areInsertedValuesValid() != null){
            info = new ValidationInfo("sender editor isn't complete : "+editors.get(2).areInsertedValuesValid().message);
        }
        if (editors.get(1).areInsertedValuesValid() != null){
            info = new ValidationInfo("generator editor isn't complete : "+editors.get(1).areInsertedValuesValid().message);
        }
        if (editors.get(0).areInsertedValuesValid() != null){
            info = new ValidationInfo("name editor isn't complete : "+editors.get(0).areInsertedValuesValid().message);
        }
        return info;
    }

    protected String getScenarioName(){
        return ((NameEditor)editors.get(0)).getScenarioName();
    }

    protected Scenario getScenarioModel(){
        Scenario model = new Scenario();
        model.setGenerator(((GeneratorEditor)editors.get(1)).getGenerator());
        model.setSender(((SenderEditor)editors.get(2)).getSender());
        /*TODO atd. + check null rep. etc*/
        return model;
    }
}
