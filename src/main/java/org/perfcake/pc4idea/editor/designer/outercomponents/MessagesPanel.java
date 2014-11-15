package org.perfcake.pc4idea.editor.designer.outercomponents;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.PerfCakeEditorGUI;
import org.perfcake.pc4idea.editor.designer.common.ComponentDragListener;
import org.perfcake.pc4idea.editor.designer.common.ScenarioDialogEditor;
import org.perfcake.pc4idea.editor.designer.innercomponents.MessageComponent;
import org.perfcake.pc4idea.editor.designer.editors.AbstractEditor;
import org.perfcake.pc4idea.editor.designer.editors.MessageEditor;
import org.perfcake.pc4idea.editor.designer.editors.MessagesEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 28.9.2014
 */
public class MessagesPanel extends AbstractPanel {
    private Color messagesColor = Color.getHSBColor(40/360f,0.75f,0.75f);

    private MessagesEditor messagesEditor;
    private Scenario.Messages messages;
    private PerfCakeEditorGUI.ScenarioEvent scenarioEvent;

    private JLabel labelMessages;
    private PanelMessages panelMessages;

    private int labelMessagesWidth;

    public MessagesPanel(PerfCakeEditorGUI.ScenarioEvent scenarioEvent){
        this.scenarioEvent = scenarioEvent;
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

        this.setTransferHandler(new TransferHandler(){
            @Override
            public boolean canImport(TransferHandler.TransferSupport support){
                support.setDropAction(COPY);
                return support.isDataFlavorSupported(DataFlavor.stringFlavor);
            }
            @Override
            public boolean importData(TransferHandler.TransferSupport support){
                if (!canImport(support)) {
                    return false;
                }
                Transferable t = support.getTransferable();
                String transferredData = "";
                try {
                    transferredData = (String)t.getTransferData(DataFlavor.stringFlavor);
                } catch (UnsupportedFlavorException e) {
                    e.printStackTrace();   /*TODO log*/
                } catch (IOException e) {
                    e.printStackTrace();   /*TODO log*/
                }
                if (transferredData.equals("Message")){
                    MessageEditor messageEditor = new MessageEditor();
                    ScenarioDialogEditor dialog = new ScenarioDialogEditor(messageEditor);
                    dialog.show();
                    if (dialog.getExitCode() == 0) {
                        messages.getMessage().add(messageEditor.getMessage());
                        setComponentModel(messages);
                        scenarioEvent.saveMessages();
                    }
                }
                return true;
            }
        });
    }

    @Override
   protected Color getColor() {
        return messagesColor;
    }

    @Override
    protected AbstractEditor getEditorPanel() {
        messagesEditor = new MessagesEditor();
        messagesEditor.setMessages(messages);
        return messagesEditor;
    }

    @Override
    protected void applyChanges() {
        this.setComponentModel(messagesEditor.getMessages());
        scenarioEvent.saveMessages();
    }

    @Override
    public void setComponentModel(Object componentModel) {
        messages = (Scenario.Messages) componentModel;

        panelMessages.setMessages(messages.getMessage());

        this.revalidate();
    }

    @Override
    public Object getComponentModel() {
        return messages;
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
                MessageComponent messageComponent = new MessageComponent(messagesColor,messageId,new MessagesEvent());
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
