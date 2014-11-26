package org.perfcake.pc4idea.editor.designer.innercomponents;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.ui.Messages;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.designer.common.ScenarioDialogEditor;
import org.perfcake.pc4idea.editor.designer.editors.ValidatorEditor;
import org.perfcake.pc4idea.editor.designer.outercomponents.ValidationPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 28.10.2014
 */
public class ValidatorComponent extends JPanel{
    private final Color validatorColor;
    private final int id;
    private boolean isAttached;
    private Set<String> usedIDSet;

    private ValidatorEditor validatorEditor;
    private Scenario.Validation.Validator validator;
    private ValidationPanel.PanelValidators.ValidationEvent validationEvent;

    private JLabel validatorAttr;
    private JPopupMenu popupMenu;
    private JMenuItem popupOpenEditor;
    private JMenuItem popupDelete;

    private Dimension validatorSize;

    public ValidatorComponent(Color validationColor, int id, boolean isAttached, Set<String> usedIDSet, ValidationPanel.PanelValidators.ValidationEvent validationEvent){
        this.validatorColor = validationColor;
        this.id = id;
        this.isAttached = isAttached;
        this.usedIDSet = usedIDSet;
        this.validationEvent = validationEvent;

        this.setOpaque(false);

        initComponents();
    }

    private void initComponents(){
        validatorAttr = new JLabel("-");
        validatorAttr.setFont(new Font(validatorAttr.getFont().getName(), 0, 15));
        validatorAttr.setForeground(validatorColor);
        validatorSize = new Dimension(40,40);

        popupOpenEditor = new JMenuItem("Open Editor");
        popupOpenEditor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validatorEditor = new ValidatorEditor(usedIDSet);
                validatorEditor.setValidator(validator,isAttached);
                ScenarioDialogEditor editor = new ScenarioDialogEditor(validatorEditor);
                editor.show();
                if (editor.getExitCode() == 0) {
                    setValidator(validatorEditor.getValidator());
                    validationEvent.saveValidator(id);
                }
            }
        });
        popupDelete = new JMenuItem("Delete");
        popupDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isAttached){
                    int result = Messages.showOkCancelDialog("This Validator is attached to some message!\n" +
                            "Are you sure you want to delete this Validator?", "Warning!", AllIcons.General.Warning);
                    if (result != 0){
                        return;
                    }
                }

                validationEvent.deleteValidator(id);
            }
        });

        popupMenu = new JPopupMenu();
        popupMenu.add(popupOpenEditor);
        popupMenu.add(new JPopupMenu.Separator());
        popupMenu.add(popupDelete);
        /*TODO dalsie?*/

        SpringLayout layout = new SpringLayout();
        this.setLayout(layout);
        this.add(validatorAttr);

        layout.putConstraint(SpringLayout.NORTH, validatorAttr,
                10,
                SpringLayout.NORTH, this);

        layout.putConstraint(SpringLayout.WEST, validatorAttr,
                15,
                SpringLayout.WEST, this);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getButton() == MouseEvent.BUTTON1) {
                    if (event.getClickCount() == 2) {
                        validatorEditor = new ValidatorEditor(usedIDSet);
                        validatorEditor.setValidator(validator,isAttached);
                        ScenarioDialogEditor editor = new ScenarioDialogEditor(validatorEditor);
                        editor.show();
                        if (editor.getExitCode() == 0) {
                            setValidator(validatorEditor.getValidator());
                            validationEvent.saveValidator(id);
                        }
                    }
                }
                if (event.getButton() == MouseEvent.BUTTON3) {
                    popupMenu.show(ValidatorComponent.this, event.getX(), event.getY());
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
        g2D.setColor(validatorColor);
        g2D.drawRoundRect(4, 4, this.getWidth() - 8, this.getHeight() - 8, 20, 20);
    }

    public void setValidator(Scenario.Validation.Validator v) {
        validator = v;
        validatorAttr.setText("("+validator.getId()+") "+validator.getClazz());
        FontMetrics fontMetrics = validatorAttr.getFontMetrics(validatorAttr.getFont());
        validatorSize.width = fontMetrics.stringWidth(validatorAttr.getText()) + 30;
    }

    public Scenario.Validation.Validator getValidator() {
        return validator;
    }

    public int getId(){
        return id;
    }

    @Override
    public Dimension getMinimumSize(){
        return validatorSize;
    }

    @Override
    public Dimension getPreferredSize(){
        return validatorSize;
    }

    @Override
    public Dimension getMaximumSize(){
        return validatorSize;
    }
}
