package org.perfcake.pc4idea.editor.designer.innercomponents;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.ui.Messages;
import org.perfcake.model.Header;
import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.designer.common.ScenarioDialogEditor;
import org.perfcake.pc4idea.editor.designer.common.ScenarioImportHandler;
import org.perfcake.pc4idea.editor.designer.editors.AttachValidatorEditor;
import org.perfcake.pc4idea.editor.designer.editors.HeaderEditor;
import org.perfcake.pc4idea.editor.designer.editors.MessageEditor;
import org.perfcake.pc4idea.editor.designer.editors.PropertyEditor;
import org.perfcake.pc4idea.editor.designer.outercomponents.MessagesPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 28.10.2014
 */
public class MessageComponent extends JPanel{
    private final Color messageColor;
    private final int id;
    private final Set<String> usedValidatorIDSet;

    private MessageEditor messageEditor;
    private Scenario.Messages.Message message;
    private MessagesPanel.PanelMessages.MessagesEvent messagesEvent;

    private JLabel messageAttr;
    private JPopupMenu popupMenu;

    private Dimension messageSize;

    public MessageComponent(Color messagesColor, int id, Set<String> usedValidatorIDSet, MessagesPanel.PanelMessages.MessagesEvent messagesEvent){
        this.messageColor = messagesColor;
        this.id = id;
        this.usedValidatorIDSet = usedValidatorIDSet;
        this.messagesEvent = messagesEvent;

        this.setOpaque(false);

        initComponents();
    }

    private void initComponents(){
        messageAttr = new JLabel("-");
        messageAttr.setFont(new Font(messageAttr.getFont().getName(), 0, 15));
        messageAttr.setForeground(messageColor);
        messageSize = new Dimension(40,40);

        JMenuItem itemOpenEditor = new JMenuItem("Open Editor");
        itemOpenEditor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                messageEditor = new MessageEditor(usedValidatorIDSet);
                messageEditor.setMessage(message);
                ScenarioDialogEditor dialog = new ScenarioDialogEditor(messageEditor);
                dialog.show();
                if (dialog.getExitCode() == 0) {
                    setMessage(messageEditor.getMessage());
                    messagesEvent.saveMessage(id);
                }
            }
        });
        JMenuItem itemDelete = new JMenuItem("Delete");
        itemDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = Messages.showYesNoDialog("Are you sure you want to delete this Message?", "Delete Message", AllIcons.Actions.Delete);
                if (result == 0) {
                    messagesEvent.deleteMessage(id);
                }

            }
        });
        JMenuItem itemAddHeader = new JMenuItem("Add Header");
        itemAddHeader.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HeaderEditor headerEditor = new HeaderEditor();
                ScenarioDialogEditor dialog = new ScenarioDialogEditor(headerEditor);
                dialog.show();
                if (dialog.getExitCode() == 0) {
                    Header header = headerEditor.getHeader();
                    message.getHeader().add(header);
                    MessageComponent.this.setMessage(message);
                    messagesEvent.saveMessage(id);
                }
            }
        });
        JMenuItem itemAddProperty = new JMenuItem("Add Property");
        itemAddProperty.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PropertyEditor propertyEditor = new PropertyEditor();
                ScenarioDialogEditor dialog = new ScenarioDialogEditor(propertyEditor);
                dialog.show();
                if (dialog.getExitCode() == 0) {
                    Property property = propertyEditor.getProperty();
                    message.getProperty().add(property);
                    MessageComponent.this.setMessage(message);
                    messagesEvent.saveMessage(id);
                }
            }
        });
        JMenuItem itemAttachValidator = new JMenuItem("Attach Validator");
        itemAttachValidator.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AttachValidatorEditor attachValidatorEditor = new AttachValidatorEditor(usedValidatorIDSet);
                ScenarioDialogEditor dialog = new ScenarioDialogEditor(attachValidatorEditor);
                dialog.show();
                if (dialog.getExitCode() == 0) {
                    Scenario.Messages.Message.ValidatorRef ref = attachValidatorEditor.getAttachedValidatorRef();
                    message.getValidatorRef().add(ref);
                    MessageComponent.this.setMessage(message);
                    messagesEvent.saveMessage(id);
                }
            }
        });

        popupMenu = new JPopupMenu();
        popupMenu.add(itemOpenEditor);
        popupMenu.add(new JPopupMenu.Separator());
        popupMenu.add(itemAddHeader);
        popupMenu.add(itemAddProperty);
        popupMenu.add(itemAttachValidator);
        popupMenu.add(new JPopupMenu.Separator());
        popupMenu.add(itemDelete);

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
                        messageEditor = new MessageEditor(usedValidatorIDSet);
                        messageEditor.setMessage(message);
                        ScenarioDialogEditor dialog = new ScenarioDialogEditor(messageEditor);
                        dialog.show();
                        if (dialog.getExitCode() == 0) {
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

        this.setTransferHandler(new ScenarioImportHandler() {
            @Override
            public void performImport(String transferredData) {
                if (transferredData.equals("Attach validator")){
                    Set<String> notAttachedID = new TreeSet<>();
                    for (String id : usedValidatorIDSet){
                        boolean isRef = false;
                        for (Scenario.Messages.Message.ValidatorRef ref : message.getValidatorRef()){
                            if(id.equals(ref.getId())){
                                isRef = true;
                            }
                        }
                        if (!isRef){
                            notAttachedID.add(id);
                        }
                    }
                    AttachValidatorEditor attachValidatorEditor = new AttachValidatorEditor(notAttachedID);
                    ScenarioDialogEditor dialog = new ScenarioDialogEditor(attachValidatorEditor);
                    dialog.show();
                    if (dialog.getExitCode() == 0){
                        message.getValidatorRef().add(attachValidatorEditor.getAttachedValidatorRef());
                        setMessage(message);
                        messagesEvent.saveMessage(id);
                    }
                }
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
        messageAttr.setText(message.getUri());
        if (messageAttr.getText() == null){
            messageSize.width = 40;
        } else {
            FontMetrics fontMetrics = messageAttr.getFontMetrics(messageAttr.getFont());
            messageSize.width = fontMetrics.stringWidth(messageAttr.getText()) + 30;
        }
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
