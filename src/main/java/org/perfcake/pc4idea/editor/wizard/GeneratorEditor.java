package org.perfcake.pc4idea.editor.wizard;

import org.perfcake.model.Scenario;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 19.10.2014
 * To change this template use File | Settings | File Templates.
 */
public class GeneratorEditor extends JPanel {
    private JLabel labelGeneratorType;
    private JLabel labelRunType;
    private JLabel labelRunValue;
    private JLabel labelNumOfThreads;
    private JSpinner spinnerGeneratorType;
    private JSpinner spinnerRunType;
    private JTextField textFieldRunValue;
    private JTextField textFieldNumOfThreads;
    private JPanel panelProperties;

    private Scenario.Generator generator;

    public GeneratorEditor(Scenario.Generator generator){
        this.generator = generator;
        /*TODO find spinnerGenTypeValues values*/
        initComponents();
    }

    private void initComponents(){
        labelGeneratorType = new JLabel("Generator type:");
        labelRunType = new JLabel("Run type:");
        labelRunValue = new JLabel("Duration:");
        labelNumOfThreads = new JLabel("Number of threads:");
        /*TODO*/spinnerGeneratorType = new JSpinner();
        /*TODO*/spinnerRunType = new JSpinner();
        textFieldRunValue = new JTextField();
        textFieldNumOfThreads = new JTextField();
        panelProperties = new PropertiesEditor();


        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup()
                    .addGroup(layout.createSequentialGroup()
                            .addComponent(labelGeneratorType)
                            .addComponent(spinnerGeneratorType))
                    .addGroup(layout.createSequentialGroup()
                            .addComponent(labelRunType)
                            .addComponent(spinnerRunType))
                    .addGroup(layout.createSequentialGroup()
                            .addComponent(labelRunValue)
                            .addComponent(textFieldRunValue))
                    .addGroup(layout.createSequentialGroup()
                            .addComponent(labelNumOfThreads)
                            .addComponent(textFieldNumOfThreads))
                    .addComponent(panelProperties));

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelGeneratorType)
                        .addComponent(spinnerGeneratorType))
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelRunType)
                        .addComponent(spinnerRunType))
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelRunValue)
                        .addComponent(textFieldRunValue))
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelNumOfThreads)
                        .addComponent(textFieldNumOfThreads))
                .addComponent(panelProperties));
    }

    public boolean areInsertedValuesValid() {
        //boolean areValid = false;
        /*TODO check validity of inserted data - if false OK button disabled*/
        return true;
    }
    public Scenario.Generator getGenerator(){
        /*TODO values to generator*/
        return generator;
    }
}
