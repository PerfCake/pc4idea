package org.perfcake.pc4idea.editor;

import com.intellij.openapi.application.ApplicationManager;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.gui.MessagesGUI;
import org.perfcake.pc4idea.editor.gui.ValidationGUI;
import org.perfcake.pc4idea.editor.modelwrapper.MessagesModelWrapper;
import org.perfcake.pc4idea.editor.modelwrapper.ValidationModelWrapper;
import org.perfcake.pc4idea.editor.swing.DependenciesPanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Stanislav Kaleta on 3/27/15.
 */
public class MessagesValidationSync {
    private MessagesModelWrapper messagesModelWrapper;
    private ValidationModelWrapper validationModelWrapper;
    private DependenciesPanel dependenciesPanel;


    public MessagesValidationSync(DependenciesPanel dependenciesPanel){
        this.dependenciesPanel = dependenciesPanel;


    }

    public void setModels(MessagesModelWrapper messagesModelWrapper, ValidationModelWrapper validationModelWrapper){
        this.messagesModelWrapper = messagesModelWrapper;
        this.validationModelWrapper = validationModelWrapper;
    }

    public Set<String> getValidatorIDs(){
        Set<String> iDSet = new TreeSet<>();
        Scenario.Validation validation = (Scenario.Validation) validationModelWrapper.retrieveModel();
        if (validation != null) {
            for (Scenario.Validation.Validator validator : validation.getValidator()) {
                iDSet.add(validator.getId());
            }
        }
        return iDSet;
    }

    public Set<String> getUnattachedValidatorIDs(Scenario.Messages.Message message){
        Set<String> allIDs = this.getValidatorIDs();
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

    public Set<String> getAttachedValidatorIDs(){
        Set<String> attachedIDs = new TreeSet<>();
        Scenario.Messages messages = (Scenario.Messages) messagesModelWrapper.retrieveModel();
        if (messages != null) {
            for (Scenario.Messages.Message message : messages.getMessage()) {
                for (Scenario.Messages.Message.ValidatorRef validatorRef : message.getValidatorRef()) {
                    attachedIDs.add(validatorRef.getId());
                }
            }
        }
        return attachedIDs;
    }

    public void syncValidatorRef() {
       Scenario.Messages messages = (Scenario.Messages) messagesModelWrapper.retrieveModel();
        if (messages != null){
            for (Scenario.Messages.Message message : messages.getMessage()){
                java.util.List<Scenario.Messages.Message.ValidatorRef> tempList = new ArrayList<>();
                tempList.addAll(message.getValidatorRef());

                for (Scenario.Messages.Message.ValidatorRef ref : message.getValidatorRef()){
                    boolean refIsValid = false;
                    for (String id : this.getValidatorIDs()){
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


    public void repaintDependencies(){
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                dependenciesPanel.removeAllDependencyLines();

                if (messagesModelWrapper.retrieveModel() != null && validationModelWrapper.retrieveModel() != null) {
                    for (Scenario.Messages.Message message : ((Scenario.Messages) messagesModelWrapper.retrieveModel()).getMessage()) {
                        MessagesGUI messagesGUI = (MessagesGUI) messagesModelWrapper.getGUI();
                        Point messagePoint = messagesGUI.getMessageAnchorPoint(message);
                        for (Scenario.Messages.Message.ValidatorRef ref : message.getValidatorRef()) {
                            Scenario.Validation validation = (Scenario.Validation) validationModelWrapper.retrieveModel();
                            Point validatorPoint = new Point(0,0);
                            for (Scenario.Validation.Validator validator : validation.getValidator()){
                                if (validator.getId().equals(ref.getId())){
                                    ValidationGUI validationGUI = (ValidationGUI) validationModelWrapper.getGUI();
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
