package org.perfcake.pc4idea.editor.gui;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.PerfCakeEditorGUI;
import org.perfcake.pc4idea.editor.components.ComponentEditor;
import org.perfcake.pc4idea.editor.components.MessageComponent;
import org.perfcake.pc4idea.editor.wizard.MessageEditor;
import org.perfcake.pc4idea.editor.wizard.MessagesEditor;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
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
    private final String TITLE ="Messages Editor";
    private Color messagesColor = Color.getHSBColor(40/360f,1f,0.75f);

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
                    ComponentEditor editor = new ComponentEditor("Message Editor",messageEditor);
                    editor.show();
                    if (editor.getExitCode() == 0) {
                        messages.getMessage().add(messageEditor.getMessage());
                        setComponent(messages);
                        scenarioEvent.saveMessages();
                    }
                }
                return true;   /*TODO attach validator!!!! (in MessageC.?)*/
            }
        });
    }

    @Override
   protected Color getColor() {
        return messagesColor;
    }

    @Override
    protected String getEditorTitle() {
        return TITLE;
    }

    @Override
    protected JPanel getEditorPanel() {
        messagesEditor = new MessagesEditor();
        messagesEditor.setMessages(messages);
        return messagesEditor;
    }

    @Override
    protected void applyChanges() {
        this.setComponent(messagesEditor.getMessages());
        scenarioEvent.saveMessages();
    }

    @Override
    public void setComponent(Object component) {
        messages = (Scenario.Messages) component;

        panelMessages.setMessages(messages.getMessage());

        this.revalidate();
    }

    @Override
    public Object getComponent() {
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
            this.addMouseListener(new DragListener());
            this.setOpaque(false);
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
                        MessagesPanel.this.setComponent(messages);
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
                        MessagesPanel.this.setComponent(messages);
                        scenarioEvent.saveMessages();
                    }
                }
            }
        }

        private class DragListener extends MouseInputAdapter {
            private boolean mousePressed;
            private int selectedComponent;
            private int expectedReleaseComponent;

            private DragListener(){
                mousePressed = false;
            }

            @Override
            public void mousePressed(MouseEvent e){
                if (e.getComponent() instanceof MessageComponent){
                    for (int i = 0;i< messageComponentList.size();i++){
                        if (e.getComponent().equals(messageComponentList.get(i))){
                            selectedComponent = i;
                            expectedReleaseComponent = i;
                            mousePressed = true;
                        }
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                if (e.getComponent() instanceof MessageComponent){
                    for (int i = 0;i< messageComponentList.size();i++){
                        if (e.getComponent().equals(messageComponentList.get(i))){
                            expectedReleaseComponent = i;
                        }
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e){
                if(mousePressed) {
                    if (selectedComponent == expectedReleaseComponent) {
                        // do nothing
                    } else {
                        if (selectedComponent < expectedReleaseComponent) {
                            for (int i = 0; i < messagesList.size(); i++) {
                                if (i < selectedComponent) {
                                    // do nothing
                                } else {
                                    if (i < expectedReleaseComponent) {
                                        Collections.swap(messagesList, i, i + 1);
                                    }
                                }
                            }
                        }
                        if (selectedComponent > expectedReleaseComponent) {
                            for (int i = messagesList.size() - 1; 0 <= i; i--) {
                                if (i < selectedComponent) {
                                    if (i >= expectedReleaseComponent) {
                                        Collections.swap(messagesList, i, i + 1);
                                    }
                                }
                            }
                        }
                        messages.getMessage().clear();
                        messages.getMessage().addAll(messagesList);
                        MessagesPanel.this.setComponent(messages);
                        scenarioEvent.saveMessages();
                    }
                    mousePressed = false;
                }
            }

            @Override
            public void mouseClicked(MouseEvent e){
                MouseEvent wrappedEvent = new MouseEvent((Component)e.getSource(),e.getID(),e.getWhen(),e.getModifiers(),e.getX()+10,e.getY()+40,e.getClickCount(),e.isPopupTrigger(),e.getButton());
                ((JPanel)e.getComponent().getAccessibleContext().getAccessibleParent()).dispatchEvent(wrappedEvent);
            }
        }
    }




}
