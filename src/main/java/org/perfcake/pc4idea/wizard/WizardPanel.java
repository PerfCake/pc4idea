package org.perfcake.pc4idea.wizard;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBList;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.designer.editors.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
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

    private Map<Integer, AbstractEditor> editors;
    private boolean validationRunning = true;
    private String defaultURI;

    private WizardDialog.WizardEvent wizardEvent;

    WizardPanel(String defaultURI){
        this.defaultURI = defaultURI;
        initComponents();
        this.setPreferredSize(new Dimension(480,240));
    }

    private void initComponents(){
        editors = new HashMap<>();
        editors.put(0, new URIEditor(defaultURI));
        editors.put(1, new GeneratorEditor());
        editors.put(2, new SenderEditor());
        editors.put(3, new MessagesEditor());
        editors.put(4, new ReportingEditor());
        editors.put(5, new ValidationEditor());
        editors.put(6, new PropertiesEditor());

        panelEditors = new JPanel(new GridLayout(1,1));
        panelEditors.add(editors.get(0));

        wizardStepList = new JBList(new String[]{"URI","Generator", "Sender", "Messages", "Reporting", "Validation", "Properties"});
        wizardStepList.setCellRenderer(new DefaultListCellRenderer() {
            private boolean isSelected = false;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2D = (Graphics2D) g;

                if (isSelected) {
                    g2D.setColor(EditorColorsManager.getInstance().getGlobalScheme().getDefaultForeground());
                    g2D.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);
                    g2D.drawRect(1, 1, this.getWidth() - 3, this.getHeight() - 3);
                }
            }

            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                this.isSelected = isSelected;

                Color rgb = EditorColorsManager.getInstance().getGlobalScheme().getDefaultBackground();
                float brightness = Color.RGBtoHSB(rgb.getRed(),rgb.getGreen(),rgb.getBlue(),new float[]{0,0,0})[2];

                Color backgroundColor = null;
                if (index < 3){
                    backgroundColor = (editors.get(index).areInsertedValuesValid() == null) ?
                            Color.getHSBColor(120/360f,0.20f, brightness) : Color.getHSBColor(0/360f, 0.20f, brightness);
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
                    backgroundColor = (isInserted) ? Color.getHSBColor(220/360f,0.20f, brightness) : Color.getHSBColor(240/360f,0f,brightness);
                }

                super.setText(" "+value);
                super.setFont(new Font(this.getFont().getName(),this.getFont().getStyle(), 15));
                super.setBackground(backgroundColor);
                super.setForeground(EditorColorsManager.getInstance().getGlobalScheme().getDefaultForeground());

                return this;
            }
        });
        wizardStepList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        wizardStepList.setSelectedIndex(0);
        wizardStepList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                wizardEvent.wizardStepListClicked(wizardStepList.getSelectedIndex());
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

        ApplicationManager.getApplication().executeOnPooledThread(new Runnable() {
            @Override
            public void run() {
                while (validationRunning){
                    try {
                        Thread.sleep(500);
                        wizardStepList.repaint();
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        });
    }

    protected void addWizardEvent(WizardDialog.WizardEvent wizardEvent){
        this.wizardEvent = wizardEvent;
    }

    protected void selectEditor(int pointer){
        panelEditors.removeAll();
        panelEditors.add(editors.get(pointer));
        wizardStepList.setSelectedIndex(pointer);
        panelEditors.revalidate();
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
            info = new ValidationInfo("URI editor isn't complete : "+editors.get(0).areInsertedValuesValid().message);
        }
        return info;
    }

    public void stopCheckingValidity(){
        validationRunning = false;
    }

    protected String getScenarioName(){
        return ((URIEditor)editors.get(0)).getScenarioName();
    }

    protected VirtualFile getScenarioDirectory(){
        return ((URIEditor)editors.get(0)).getScenarioDirectory();
    }

    protected Scenario getScenarioModel(){
        Scenario model = new Scenario();
        model.setGenerator(((GeneratorEditor)editors.get(1)).getGenerator());
        model.setSender(((SenderEditor)editors.get(2)).getSender());

        Scenario.Messages messages = ((MessagesEditor) editors.get(3)).getMessages();
        messages = (messages.getMessage().isEmpty()) ? null : messages;
        model.setMessages(messages);

        Scenario.Reporting reporting = ((ReportingEditor) editors.get(4)).getReporting();
        reporting = (reporting.getReporter().isEmpty()) ? null : reporting;
        model.setReporting(reporting);

        Scenario.Validation validation = ((ValidationEditor) editors.get(5)).getValidation();
        validation = (validation.getValidator().isEmpty()) ? null : validation;
        model.setValidation(validation);

        Scenario.Properties properties = ((PropertiesEditor) editors.get(6)).getObjProperties();
        properties = (properties.getProperty().isEmpty()) ? null : properties;
        model.setProperties(properties);

        return model;
    }
}
