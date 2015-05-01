package org.perfcake.pc4idea.api.util;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.swing.DependenciesPanel;
import org.perfcake.pc4idea.impl.editor.editor.component.MessagesEditor;
import org.perfcake.pc4idea.impl.editor.editor.component.ValidationEditor;
import org.perfcake.pc4idea.impl.editor.gui.component.MessagesGui;
import org.perfcake.pc4idea.impl.editor.gui.component.ValidationGui;
import org.perfcake.pc4idea.impl.editor.modelwrapper.component.MessagesModelWrapper;
import org.perfcake.pc4idea.impl.editor.modelwrapper.component.ValidationModelWrapper;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Stanislav Kaleta on 3/27/15.           TODO documentation
 */
public class MessagesValidationSync {
    private static final Logger LOG = Logger.getInstance(MessagesValidationSync.class);

    private MessagesModelWrapper messagesModelWrapper;
    private ValidationModelWrapper validationModelWrapper;
    private DependenciesPanel dependenciesPanel;
    private MessagesEditor messagesEditor;
    private ValidationEditor validationEditor;

    private Boolean isEditorMode;

    public MessagesValidationSync(@Nullable DependenciesPanel dependenciesPanel) {
        isEditorMode = null;
        this.dependenciesPanel = dependenciesPanel;
        if (dependenciesPanel != null){
            this.dependenciesPanel.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    MessagesValidationSync.this.repaintDependencies();
                }
            });
        }
    }

    /**
     * @param messagesEditor
     * @param validationEditor
     */
    public void setWizardMode(@NotNull MessagesEditor messagesEditor,
                              @NotNull ValidationEditor validationEditor) {
        this.messagesEditor = messagesEditor;
        this.validationEditor = validationEditor;
        isEditorMode = Boolean.FALSE;
        messagesModelWrapper = null;
        validationModelWrapper = null;
    }

    /**
     *
     * @param messagesModelWrapper
     * @param validationModelWrapper
     */
    public void setEditorMode(@NotNull MessagesModelWrapper messagesModelWrapper,
                              @NotNull ValidationModelWrapper validationModelWrapper) {
        this.messagesModelWrapper = messagesModelWrapper;
        this.validationModelWrapper = validationModelWrapper;
        isEditorMode = Boolean.TRUE;
        messagesEditor = null;
        validationEditor = null;
    }

    private Scenario.Validation getValidationModel() {
        if (isEditorMode == null) {
            LOG.error("One of Editor or Wizard mode must be selected!");
            return null;
        }
        if (isEditorMode) {
            return (Scenario.Validation) validationModelWrapper.retrieveModel();
        } else {
            return validationEditor.getValidation();
        }
    }

    private Scenario.Messages getMessagesModel() {
        if (isEditorMode == null) {
            LOG.error("One of Editor or Wizard mode must be selected!");
            return null;
        }
        if (isEditorMode) {
            return (Scenario.Messages) messagesModelWrapper.retrieveModel();
        } else {
            return messagesEditor.getMessages();
        }
    }

    /**
     * @return
     */
    public Set<String> getValidatorIds() {
        Set<String> iDSet = new TreeSet<>();
        Scenario.Validation validation = getValidationModel();
        if (validation != null) {
            for (Scenario.Validation.Validator validator : validation.getValidator()) {
                iDSet.add(validator.getId());
            }
        }
        return iDSet;
    }

    /**
     *
     * @param message
     * @return
     */
    public Set<String> getUnattachedValidatorIds(Scenario.Messages.Message message) {
        Set<String> allIDs = this.getValidatorIds();
        Set<String> notAttachedIDs = new TreeSet<>();
        for (String id : allIDs){
            boolean isRef = false;
            for (Scenario.Messages.Message.ValidatorRef ref : message.getValidatorRef()){
                if(id.equals(ref.getId())){
                    isRef = true;
                }
            }
            if (!isRef){
                notAttachedIDs.add(id);
            }
        }
        return notAttachedIDs;
    }

    /**
     *
     * @return
     */
    public Set<String> getAttachedValidatorIds() {
        Set<String> attachedIDs = new TreeSet<>();
        Scenario.Messages messages = getMessagesModel();
        if (messages != null) {
            for (Scenario.Messages.Message message : messages.getMessage()) {
                for (Scenario.Messages.Message.ValidatorRef validatorRef : message.getValidatorRef()) {
                    attachedIDs.add(validatorRef.getId());
                }
            }
        }
        return attachedIDs;
    }

    /**
     *
     */
    public void syncValidatorRef() {
       Scenario.Messages messages = getMessagesModel();
        if (messages != null){
            for (Scenario.Messages.Message message : messages.getMessage()){
                java.util.List<Scenario.Messages.Message.ValidatorRef> tempList = new ArrayList<>();
                tempList.addAll(message.getValidatorRef());

                for (Scenario.Messages.Message.ValidatorRef ref : message.getValidatorRef()){
                    boolean refIsValid = false;
                    for (String id : this.getValidatorIds()) {
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
    }

    /**
     *
     * @param validator
     * @return
     */
    public boolean isValidatorAttached(Scenario.Validation.Validator validator) {
        return this.getAttachedValidatorIds().contains(validator.getId());
    }

    /**
     *
     * @param id
     * @return
     */
    public boolean isIdUsed(String id) {
        return this.getValidatorIds().contains(id);
    }

    /**
     *
     */
    public void repaintDependencies(){
        if (isEditorMode == null || !isEditorMode) {
            LOG.warn("dependency lines isn't allowed!");
            return;
        }
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                dependenciesPanel.removeAllDependencyLines();

                if (messagesModelWrapper.retrieveModel() != null && validationModelWrapper.retrieveModel() != null) {
                    for (Scenario.Messages.Message message : ((Scenario.Messages) messagesModelWrapper.retrieveModel()).getMessage()) {
                        MessagesGui messagesGUI = (MessagesGui) messagesModelWrapper.getGui();
                        Point messagePoint = messagesGUI.getMessageAnchorPoint(message);
                        for (Scenario.Messages.Message.ValidatorRef ref : message.getValidatorRef()) {
                            Scenario.Validation validation = (Scenario.Validation) validationModelWrapper.retrieveModel();
                            Point validatorPoint = new Point(0,0);
                            for (Scenario.Validation.Validator validator : validation.getValidator()){
                                if (validator.getId().equals(ref.getId())){
                                    ValidationGui validationGUI = (ValidationGui) validationModelWrapper.getGui();
                                    validatorPoint = validationGUI.getValidatorAnchorPoint(validator);
                                }
                            }
                            dependenciesPanel.addDependencyLine(messagePoint, validatorPoint);
                        }
                    }
                }
            }
        });
    }
}
