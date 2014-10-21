package org.perfcake.pc4idea.editor.gui;

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
public class SenderPanel extends AbstractPanel {
    private final String TITLE ="Sender Editor";
    private Color panelColor = Color.getHSBColor(220/360f,0.5f,0.75f);
    private final Project project;

    private JPanel panelEditor;
    private Scenario.Sender sender;

    private JLabel senderAttr;
    //private JScrollPane scrollPaneProperties;/*TODO*/
    private JPanel panelProperties;



    public SenderPanel(Project project){
        super(project);
        this.project = project;
        setMinimumSize(new Dimension(0,50)); /*50 = 2*10gap+20title+10gap to panel*/
        setMaximumSize(new Dimension(Integer.MAX_VALUE,50));

        initComponents();
        panelEditor = initPanelEditor();

    }
    private void initComponents() {
        senderAttr = new JLabel("SenderClass");
        senderAttr.setFont(new Font(senderAttr.getFont().getName(), 0, 15));
        senderAttr.setForeground(panelColor);

        panelProperties = new JPanel();
        panelProperties.setLayout(new BoxLayout(panelProperties,BoxLayout.X_AXIS));/*TODO layout*/
        panelProperties.setMinimumSize(new Dimension(370,0));
        panelProperties.setMaximumSize(new Dimension(Integer.MAX_VALUE, 0));
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
    protected JPanel getEditorPanel() {
        return panelEditor;
    }

    @Override
    protected void applyChanges() {
        /*TODO ...*/
        /*upravi sender + */
        /*editor.save(){natiahne scen. + ulozi}*/

        /*+ zarovnat (setComponent part -> align)*/
    }

    @Override
    public void setComponent(Object component) {  /*u xxxComp. maybe in constructor*/
        sender = (Scenario.Sender) component;
        senderAttr.setText(sender.getClazz());

        panelProperties.removeAll();
        /*TODO zarovnavat:akyLayout:podla sirky (min=380)*/
        for (Property property : sender.getProperty()){
            /*TODO ukladat zoznam prop.*/
            PropertyComponent propertyComponent = new PropertyComponent(project,panelColor);
            propertyComponent.setComponent(property);
            panelProperties.add(propertyComponent);
        }
        /*TODO*/ if (sender.getProperty().size() > 0) { /* TODO temp n=1*/
            setMinimumSize(new Dimension(0, 50 + 40));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 50 + 40)); /*TODO base=50 + 40*n n=number rows*/

            panelProperties.setMinimumSize(new Dimension(370, 40));
            panelProperties.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));   /*TODO 0 or 40*n -//-*/
        }
        //panelProperties.validate();TODO if needed
    }

    @Override
    public Object getComponent() {
        return sender;
    }


}
