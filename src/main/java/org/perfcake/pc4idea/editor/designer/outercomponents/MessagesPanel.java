package org.perfcake.pc4idea.editor.designer.outercomponents;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.PerfCakeEditorGUI;
import org.perfcake.pc4idea.editor.designer.common.ComponentDragListener;
import org.perfcake.pc4idea.editor.designer.common.ScenarioDialogEditor;
import org.perfcake.pc4idea.editor.designer.editors.AbstractEditor;
import org.perfcake.pc4idea.editor.designer.editors.MessageEditor;
import org.perfcake.pc4idea.editor.designer.editors.MessagesEditor;
import org.perfcake.pc4idea.editor.designer.innercomponents.MessageComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 28.9.2014
 */
public class MessagesPanel extends AbstractPanel {
    private Color messagesColor = Color.getHSBColor(40/360f,0.75f,0.75f);

    private MessagesEditor messagesEditor;
    private Scenario.Messages messages;
    private Set<String> validatorIDSet;
    private PerfCakeEditorGUI.ScenarioEvent scenarioEvent;

    private JLabel labelMessages;
    private PanelMessages panelMessages;

    private int labelMessagesWidth;

    public MessagesPanel(PerfCakeEditorGUI.ScenarioEvent scenarioEvent){
        this.scenarioEvent = scenarioEvent;
        validatorIDSet = new TreeSet<>();
        labelMessagesWidth = 0;

        initComponents();
    }

    private void initComponents(){
        labelMessages = new JLabel("Messages");
        labelMessages.setFont(new Font(labelMessages.getFont().getName(),0,15));
        labelMessages.setForeground(messagesColor);
        FontMetrics fontMetrics = labelMessages.getFontMetrics(labelMessages.getFont());
        labelMessagesWidth = fontMetrics.stringWidth(labelMessages.getText());

        panelMessages = new PanelMessages();

        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        this.add(labelMessages);
        this.add(panelMessages);

        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, labelMessages,
                0,
                SpringLayout.HORIZONTAL_CENTER, this);
        layout.putConstraint(SpringLayout.NORTH, labelMessages,
                10,
                SpringLayout.NORTH, this);

        layout.putConstraint(SpringLayout.WEST, panelMessages,
                10,
                SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.NORTH, panelMessages,8,SpringLayout.SOUTH, labelMessages);
    }

    public void setValidatorIDSet(Set<String> validatorIDSet){
        this.validatorIDSet.clear();
        this.validatorIDSet.addAll(validatorIDSet);

        for (Scenario.Messages.Message message : messages.getMessage()){
            List<Scenario.Messages.Message.ValidatorRef> tempList = new ArrayList<>();
            tempList.addAll(message.getValidatorRef());

            for (Scenario.Messages.Message.ValidatorRef ref : message.getValidatorRef()){
                boolean refIsValid = false;
                for (String id : this.validatorIDSet){
                    if (id.equals(ref.getId())){
                        refIsValid = true;
                    }
                }
                if (!refIsValid){
                    tempList.remove(ref);
                }
            }

            message.getValidatorRef().clear();
            message.getValidatorRef().addAll(tempList);
        }
    }

    public Point getMessageAnchorPoint(Scenario.Messages.Message message){
        Point anchorPoint = panelMessages.getMessageAnchorPoint(message);
        anchorPoint.setLocation(anchorPoint.getX()+this.getX(),anchorPoint.getY()+this.getY());
        return anchorPoint;
    }

    @Override
    protected List<JMenuItem> getPopupMenuItems(){
        List<JMenuItem> menuItems = new ArrayList<>();

        JMenuItem itemOpenEditor = new JMenuItem("Open Editor");
        itemOpenEditor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ScenarioDialogEditor editor = new ScenarioDialogEditor(getEditorPanel());
                editor.show();
                if (editor.getExitCode() == 0) {
                    applyChanges();
                }
            }
        });
        menuItems.add(itemOpenEditor);

        JMenuItem itemAddMessage = new JMenuItem("Add Message");
        itemAddMessage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MessageEditor messageEditor = new MessageEditor(validatorIDSet);
                ScenarioDialogEditor dialog = new ScenarioDialogEditor(messageEditor);
                dialog.show();
                if (dialog.getExitCode() == 0) {
                    Scenario.Messages.Message message = messageEditor.getMessage();
                    messages.getMessage().add(message);
                    MessagesPanel.this.setComponentModel(messages);
                    scenarioEvent.saveMessages();
                    /*TODO open dialog to create file if needed*/
                }
            }
        });
        menuItems.add(itemAddMessage);

        return menuItems;
    }

    @Override
    protected void performImport(String transferredData){
        if (transferredData.equals("Message")){
            MessageEditor messageEditor = new MessageEditor(validatorIDSet);
            ScenarioDialogEditor dialog = new ScenarioDialogEditor(messageEditor);
            dialog.show();
            if (dialog.getExitCode() == 0) {
                messages.getMessage().add(messageEditor.getMessage());
                setComponentModel(messages);
                scenarioEvent.saveMessages();
                /*TODO open dialog to create file if needed*/
            }
        }
    }

    @Override
    protected Color getColor() {
        return messagesColor;
    }

    @Override
    protected AbstractEditor getEditorPanel() {
        messagesEditor = new MessagesEditor();
        messagesEditor.setMessages(messages, validatorIDSet);
        return messagesEditor;
    }

    @Override
    protected void applyChanges() {
        this.setComponentModel(messagesEditor.getMessages());
        scenarioEvent.saveMessages();
    }

    @Override
    public void setComponentModel(Object componentModel) {
        if (componentModel != null) {
            messages = (Scenario.Messages) componentModel;
            panelMessages.setMessages(messages.getMessage());
        } else {
            messages = new Scenario.Messages();
            panelMessages.setMessages(new ArrayList<Scenario.Messages.Message>());
        }
        this.revalidate();
    }

    @Override
    public Object getComponentModel() {
        return (messages.getMessage().isEmpty()) ? null : messages;
    }

    @Override
    public Dimension getMinimumSize(){
        Dimension dimension = new Dimension();
        int widestMessageWidth = panelMessages.getWidestMessageWidth();
        dimension.width = (widestMessageWidth+20 > labelMessagesWidth+30) ? widestMessageWidth+20 : labelMessagesWidth+30;
        dimension.height = panelMessages.getMessagesRowCount()*40 + 50;
        return dimension;
    }

    @Override
    public Dimension getPreferredSize(){
        Dimension dimension = new Dimension();
        dimension.width = super.getPreferredSize().width;
        dimension.height = panelMessages.getMessagesRowCount()*40 + 50;
        return dimension;
    }

    @Override
    public Dimension getMaximumSize(){
        Dimension dimension = new Dimension();
        dimension.width = super.getMaximumSize().width;
        dimension.height = panelMessages.getMessagesRowCount()*40 + 50;
        return dimension;
    }

    public class PanelMessages extends JPanel {
        private java.util.List<MessageComponent> messageComponentList;
        private java.util.List<Scenario.Messages.Message> messagesList;
        private int widestMessageWidth;
        private int messagesRowCount;

        private PanelMessages(){
            messageComponentList = new ArrayList<>();
            messagesList = new ArrayList<>();
            widestMessageWidth = 0;
            messagesRowCount = 0;
            this.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
            this.setOpaque(false);
            this.addMouseListener(new ComponentDragListener() {
                @Override
                public int mousePressedActionPerformed(MouseEvent e) {
                    int pressedComponent = -1;
                    if (e.getComponent() instanceof MessageComponent){
                        for (int i = 0;i< messageComponentList.size();i++){
                            if (e.getComponent().equals(messageComponentList.get(i))){
                                pressedComponent = i;
                            }
                        }
                    }
                    return pressedComponent;
                }

                @Override
                public int mouseEnteredActionPerformed(MouseEvent e) {
                    int enteredComponent = -1;
                    if (e.getComponent() instanceof MessageComponent){
                        for (int i = 0;i< messageComponentList.size();i++){
                            if (e.getComponent().equals(messageComponentList.get(i))){
                                enteredComponent = i;
                            }
                        }
                    }
                    return enteredComponent;
                }

                @Override
                public void mouseReleasedActionPerformed(int selectedComponent, int releasedComponent) {
                    if (selectedComponent < releasedComponent) {
                        for (int i = 0; i < messagesList.size(); i++) {
                            if (i >= selectedComponent) {
                                if (i < releasedComponent) {
                                    Collections.swap(messagesList, i, i + 1);
                                }
                            }
                        }
                    }
                    if (selectedComponent > releasedComponent) {
                        for (int i = messagesList.size() - 1; 0 <= i; i--) {
                            if (i < selectedComponent) {
                                if (i >= releasedComponent) {
                                    Collections.swap(messagesList, i, i + 1);
                                }
                            }
                        }
                    }
                    messages.getMessage().clear();
                    messages.getMessage().addAll(messagesList);
                    MessagesPanel.this.setComponentModel(messages);
                    scenarioEvent.saveMessages();
                }
            });
        }

        private void setMessages(java.util.List<Scenario.Messages.Message> messages){
            messagesList.clear();
            messagesList.addAll(messages);
            messageComponentList.clear();
            this.removeAll();
            this.repaint();

            widestMessageWidth = 0;
            int messageId = 0;
            for (Scenario.Messages.Message message : messagesList) {
                MessageComponent messageComponent = new MessageComponent(messagesColor,messageId, validatorIDSet,new MessagesEvent());
                messageComponent.setMessage(message);
                messageComponentList.add(messageComponent);
                this.add(messageComponent);
                if (messageComponent.getPreferredSize().width > widestMessageWidth) {
                    widestMessageWidth = messageComponent.getPreferredSize().width;
                }
                messageId++;
            }
            countMessagesRowCount();

            this.revalidate();
        }

        private int getWidestMessageWidth(){
            return widestMessageWidth;
        }
        private int getMessagesRowCount(){
            return messagesRowCount;
        }
        private void countMessagesRowCount(){
            int thisPanelWidth = MessagesPanel.this.getSize().width-20;
            thisPanelWidth = (thisPanelWidth < 0) ? Integer.MAX_VALUE : thisPanelWidth;

            if (widestMessageWidth <= thisPanelWidth) {
                int controlSum = 0;
                int expectedRows = 0;
                for (int i = 0; i < messageComponentList.size(); i++) {
                    if (i == 0) {
                        expectedRows = 1;
                    }
                    controlSum += messageComponentList.get(i).getPreferredSize().width;
                    if (controlSum > thisPanelWidth) {
                        i--;
                        controlSum = 0;
                        expectedRows++;
                    }
                }
                messagesRowCount = (expectedRows != messagesRowCount) ? expectedRows : messagesRowCount;
            }
        }

        private Point getMessageAnchorPoint(Scenario.Messages.Message message){
            for (MessageComponent messageComponent : messageComponentList){
                if (messageComponent.getMessage().equals(message)){
                    Point anchorPoint = messageComponent.getLocation();
                    anchorPoint.setLocation(anchorPoint.getX()+this.getX()+4+messageComponent.getWidth()/2,anchorPoint.getY()+this.getY()+37);
                    return anchorPoint;
                }
            }
            return null;
        }

        @Override
        public Dimension getMinimumSize(){
            Dimension dimension = new Dimension();
            dimension.width = widestMessageWidth;
            dimension.height = messagesRowCount*40;
            return dimension;
        }

        @Override
        public Dimension getPreferredSize(){
            countMessagesRowCount();

            Dimension dimension = new Dimension();
            dimension.width = MessagesPanel.this.getSize().width-20;
            dimension.height = messagesRowCount*40;
            return dimension;
        }

        @Override
        public Dimension getMaximumSize(){
            Dimension dimension = new Dimension();
            dimension.width = MessagesPanel.this.getSize().width-20;
            dimension.height = messagesRowCount*40;
            return dimension;
        }

        public final class MessagesEvent {
            public void saveMessage(int messageId){
                for (int i = 0; i<messageComponentList.size();i++){
                    if (messageComponentList.get(i).getId() == messageId){
                        messagesList.set(i, messageComponentList.get(i).getMessage());

                        messages.getMessage().clear();
                        messages.getMessage().addAll(messagesList);
                        MessagesPanel.this.setComponentModel(messages);
                        scenarioEvent.saveMessages();
                    }
                }
            }
            public void deleteMessage(int messageId){
                for (int i = 0; i<messageComponentList.size();i++){
                    if (messageComponentList.get(i).getId() == messageId){
                        messagesList.remove(i);

                        messages.getMessage().clear();
                        messages.getMessage().addAll(messagesList);
                        MessagesPanel.this.setComponentModel(messages);
                        scenarioEvent.saveMessages();
                    }
                }
            }
        }
    }
}
