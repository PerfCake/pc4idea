package org.perfcake.pc4idea.editor.components;

import com.intellij.openapi.project.Project;
import org.perfcake.model.Property;
import org.perfcake.model.Scenario;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 28.9.2014
 * To change this template use File | Settings | File Templates.
 */
public class SenderComponent extends AbstractComponent {
    private final String TITLE ="Sender Editor";
    private Color panelColor = Color.getHSBColor(220/360f,0.5f,0.75f);
    private final Project project;

    private JPanel panelEditor;
    private Scenario.Sender sender;

    private JLabel senderAttr;
    //private JScrollPane scrollPaneProperties;/*TODO*/
    private JPanel panelProperties;



    public SenderComponent(Project project){
        super(project);
        this.project = project;
        setMinimumSize(new Dimension(0,50));
        setMaximumSize(new Dimension(Integer.MAX_VALUE,50));

        initComponents();
        panelEditor = initPanelEditor();

    }
    private void initComponents() {
        senderAttr = new JLabel("SenderClass");
        senderAttr.setFont(new Font(senderAttr.getFont().getName(), 0, 15));
        senderAttr.setForeground(panelColor);

        panelProperties = new JPanel();
        panelProperties.setLayout(new BoxLayout(panelProperties,BoxLayout.X_AXIS));
        panelProperties.setMinimumSize(new Dimension(380,40));
        panelProperties.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        panelProperties.setOpaque(false);


        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        this.add(senderAttr);
        this.add(panelProperties);

        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, senderAttr,
                0,
                SpringLayout.HORIZONTAL_CENTER, this);
        layout.putConstraint(SpringLayout.NORTH, senderAttr,
                10,
                SpringLayout.NORTH, this);

        layout.putConstraint(SpringLayout.WEST, panelProperties,
                15,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.EAST, this,
                15,
                SpringLayout.EAST, panelProperties);
        layout.putConstraint(SpringLayout.NORTH, panelProperties,8,SpringLayout.SOUTH, senderAttr);
        layout.putConstraint(SpringLayout.SOUTH, this,
                10,
                SpringLayout.SOUTH,panelProperties);
    }

    private JPanel initPanelEditor(){
        JPanel panel = new JPanel();
        /*TODO ...*/
        return panel;
    }

    @Override
    protected Color getColor() {
        return panelColor;
    }

    @Override
    protected String getEditorTitle() {
        return TITLE;
    }

    @Override
    protected JPanel getPanelEditor() {
        return panelEditor;
    }

    @Override
    protected void applyChanges() {
        /*TODO ...*/
    }

    @Override
    public void setComponent(Object component) {
        sender = (Scenario.Sender) component;
        senderAttr.setText(sender.getClazz());

        /*TODO zarovnavat:akyLayout:podla sirky (min=400)*/
        for (Property property : sender.getProperty()){
            PropertyComponent propertyComponent = new PropertyComponent(project,panelColor);
            propertyComponent.setComponent(property);
            panelProperties.add(propertyComponent);
        }
        /*TODO del*/ if (sender.getProperty().size() > 0) {
            setMinimumSize(new Dimension(0, 50 + 40));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 50 + 40)); /*TODO base=50 + 40*n n=number rows*/
        }
        //panelProperties.validate();TODO if needed
    }

    @Override
    public Object getComponent() {
        return sender;
    }


}
