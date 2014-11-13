package org.perfcake.pc4idea.editor.designer.innercomponents;

import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.designer.common.ScenarioDialogEditor;
import org.perfcake.pc4idea.editor.designer.outercomponents.MessagesPanel;
import org.perfcake.pc4idea.editor.designer.editors.MessageEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 28.10.2014
 */
public class MessageComponent extends JPanel{
    private final Color messageColor;
    private final int id;

    private MessageEditor messageEditor;
    private Scenario.Messages.Message message;
    private MessagesPanel.PanelMessages.MessagesEvent messagesEvent;

    private JLabel messageAttr;
    private JPopupMenu popupMenu;
    private JMenuItem popupOpenEditor;
    private JMenuItem popupDelete;

    private Dimension messageSize;

    public MessageComponent(Color messagesColor, int id, MessagesPanel.PanelMessages.MessagesEvent messagesEvent){
        this.messageColor = messagesColor;
        this.id = id;
        this.messagesEvent = messagesEvent;

        this.setOpaque(false);

        initComponents();
    }

    private void initComponents(){
        messageAttr = new JLabel("-");
        messageAttr.setFont(new Font(messageAttr.getFont().getName(), 0, 15));
        messageAttr.setForeground(messageColor);
        messageSize = new Dimension(40,40);

        popupOpenEditor = new JMenuItem("Open Editor");
        popupOpenEditor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                messageEditor = new MessageEditor();
                messageEditor.setMessage(message);
                ScenarioDialogEditor editor = new ScenarioDialogEditor(messageEditor);
                editor.show();
                if (editor.getExitCode() == 0) {
                    setMessage(messageEditor.getMessage());
                    messagesEvent.saveMessage(id);
                }
            }
        });
        popupDelete = new JMenuItem("Delete");
        popupDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                messagesEvent.deleteMessage(id);
            }
        });

        popupMenu = new JPopupMenu();
        popupMenu.add(popupOpenEditor);
        popupMenu.add(new JPopupMenu.Separator());
        popupMenu.add(popupDelete);
        /*TODO dalsie?*/

        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        this.add(messageAttr);

        layout.putConstraint(SpringLayout.NORTH, messageAttr,
                10,
                SpringLayout.NORTH, this);

        layout.putConstraint(SpringLayout.WEST, messageAttr,
                15,
                SpringLayout.WEST, this);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getButton() == MouseEvent.BUTTON1) {
                    if (event.getClickCount() == 2) {
                        messageEditor = new MessageEditor();
                        messageEditor.setMessage(message);
                        ScenarioDialogEditor editor = new ScenarioDialogEditor(messageEditor);
                        editor.show();
                        if (editor.getExitCode() == 0) {
                            setMessage(messageEditor.getMessage());
                            messagesEvent.saveMessage(id);
                        }
                    }
                }
                if (event.getButton() == MouseEvent.BUTTON3) {
                    popupMenu.show(MessageComponent.this, event.getX(), event.getY());
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {
                ((JPanel)e.getComponent().getAccessibleContext().getAccessibleParent()).dispatchEvent(e);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                ((JPanel)e.getComponent().getAccessibleContext().getAccessibleParent()).dispatchEvent(e);
            }
            @Override
            public void mouseReleased(MouseEvent e){
                ((JPanel)e.getComponent().getAccessibleContext().getAccessibleParent()).dispatchEvent(e);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(messageColor);
        g2D.drawRoundRect(4, 4, this.getWidth() - 8, this.getHeight() - 8, 20, 20);
    }

    public void setMessage(Scenario.Messages.Message m) {
        message = m;
        messageAttr.setText(message.getUri());/*TODO mozny aj druhy typ message!!*/
        FontMetrics fontMetrics = messageAttr.getFontMetrics(messageAttr.getFont());
        messageSize.width = fontMetrics.stringWidth(messageAttr.getText()) + 30;
    }

    public Scenario.Messages.Message getMessage() {
        return message;
    }

    public int getId(){
        return id;
    }

    @Override
    public Dimension getMinimumSize(){
        return messageSize;
    }

    @Override
    public Dimension getPreferredSize(){
        return messageSize;
    }

    @Override
    public Dimension getMaximumSize(){
        return messageSize;
    }
}
