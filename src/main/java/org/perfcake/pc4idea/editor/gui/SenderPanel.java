package org.perfcake.pc4idea.editor.gui;

import com.intellij.openapi.project.Project;
import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.wizard.SenderEditor;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 28.9.2014
 */
public class SenderPanel extends AbstractPanel {
    private final String TITLE ="Sender Editor";
    private Color senderColor = Color.getHSBColor(220/360f,0.5f,0.75f);
    private final Project project;

    private SenderEditor panelEditor;
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
    }

    private void initComponents() {
        senderAttr = new JLabel("SenderClass");
        senderAttr.setFont(new Font(senderAttr.getFont().getName(), 0, 15));
        senderAttr.setForeground(senderColor);

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
                10,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.EAST, this,
                10,
                SpringLayout.EAST, panelProperties);
        layout.putConstraint(SpringLayout.NORTH, panelProperties,10,SpringLayout.SOUTH, senderAttr);
        layout.putConstraint(SpringLayout.SOUTH, this,
                10,
                SpringLayout.SOUTH,panelProperties);
    }

    @Override
    protected Color getColor() {
        return senderColor;
    }

    @Override
    protected String getEditorTitle() {
        return TITLE;
    }

    @Override
    protected JPanel getEditorPanel() {
        panelEditor = new SenderEditor();
        panelEditor.setSender(sender);
        return panelEditor;
    }

    @Override
    protected void applyChanges() {
        this.setComponent(panelEditor.getSender());
    }

    @Override
    public void setComponent(Object component) { /*TODO*/
        sender = (Scenario.Sender) component;
        senderAttr.setText(sender.getClazz());

        panelProperties.removeAll();
        panelProperties.repaint();
        /*TODO zarovnavat:akyLayout:podla sirky (min=380)*/
        for (Property property : sender.getProperty()){
            /*TODO ukladat zoznam prop.*/
            PropertyComponent propertyComponent = new PropertyComponent(project, senderColor);
            propertyComponent.setComponent(property);
            panelProperties.add(propertyComponent);
        }
        /*TODO*/ if (sender.getProperty().size() > 0) { /* TODO temp n=1*/
            setMinimumSize(new Dimension(0, 50 + 40));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 50 + 40)); /*TODO base=50 + 40*n n=number rows*/

            panelProperties.setMinimumSize(new Dimension(370, 40));
            panelProperties.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));   /*TODO 0 or 40*n -//-*/
        }
        panelProperties.revalidate();
        this.revalidate();

    }

    @Override
    public Object getComponent() {
        return sender;
    }


}
