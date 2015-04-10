package org.perfcake.pc4idea;

import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.fileEditor.ex.FileEditorProviderManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import org.junit.Assert;
import org.perfcake.message.Message;
import org.perfcake.model.Header;
import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.editor.PerfCakeEditor;
import org.perfcake.pc4idea.editor.editor.PerfCakeEditorProvider;
import org.perfcake.pc4idea.editor.interfaces.ModelWrapper;
import org.perfcake.pc4idea.editor.manager.ScenarioManagerException;
import org.perfcake.pc4idea.editor.modelwrapper.MessageModelWrapper;
import org.perfcake.pc4idea.editor.modelwrapper.MessagesModelWrapper;
import org.perfcake.pc4idea.editor.modelwrapper.PropertiesModelWrapper;
import org.perfcake.pc4idea.editor.modelwrapper.SenderModelWrapper;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * Created by Stanislav Kaleta on 3/31/15.
 */
public class MessagesTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return new File(PathManager.getJarPathForClass(GeneratorTest.class) + "/messagesTestFiles").getPath();
    }

    private MessagesModelWrapper setUpEditorAndGetModel() {
        VirtualFile file = myFixture.getFile().getVirtualFile();
        FileEditorProvider[] possibleProviders = FileEditorProviderManager.getInstance().getProviders(getProject(), file);
        PerfCakeEditorProvider pcProvider = null;
        for (int i = 0; i < possibleProviders.length; i++) {
            if (possibleProviders[i].getEditorTypeId().equals("PerfCakeEditor")) {
                pcProvider = (PerfCakeEditorProvider) possibleProviders[i];
            }
        }
        if (pcProvider == null) {
            throw new AssertionError("Error setting up editor - cant find PerfCakeEditorProvider instance");
        }
        Assert.assertTrue(pcProvider.accept(getProject(), file));
        PerfCakeEditor editor = (PerfCakeEditor) pcProvider.createEditor(getProject(), file);

        return (MessagesModelWrapper) editor.getComponent().getScenarioGUI().getComponentModel(3);
    }

    //messages
    public void testEditMessages() {
        myFixture.configureByFile("beforeMessagesTest.xml");
        MessagesModelWrapper messagesModelWrapper = setUpEditorAndGetModel();
        Scenario.Messages beforeModel = (Scenario.Messages) messagesModelWrapper.retrieveModel();

        Scenario.Messages afterModel = new Scenario.Messages();

        List<Scenario.Messages.Message> messageList = beforeModel.getMessage();
        messageList.get(0).setUri("edited");
        messageList.get(1).setContent("edited");

        afterModel.getMessage().addAll(messageList);

        messagesModelWrapper.updateModel(afterModel);
        messagesModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterEditMessages.xml");
    }

    public void testAddMessage() {
        myFixture.configureByFile("beforeMessagesTest.xml");
        MessagesModelWrapper messagesModelWrapper = setUpEditorAndGetModel();

        Scenario.Messages.Message message = new Scenario.Messages.Message();
        message.setUri("added");
        message.setMultiplicity("5");
        Header header = new Header();
        header.setName("h");
        header.setValue("h");
        message.getHeader().add(header);
        Property property = new Property();
        property.setName("p");
        property.setValue("p");
        message.getProperty().add(property);

        messagesModelWrapper.addMessage(message);
        messagesModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterAddMessage.xml");
    }

    public void testAddFirstMessage() {
        myFixture.configureByFile("beforeMessagesTestEmpty.xml");
        MessagesModelWrapper messagesModelWrapper = setUpEditorAndGetModel();

        Scenario.Messages.Message message = new Scenario.Messages.Message();
        message.setUri("added");
        message.setMultiplicity("5");
        Header header = new Header();
        header.setName("h");
        header.setValue("h");
        message.getHeader().add(header);
        Property property = new Property();
        property.setName("p");
        property.setValue("p");
        message.getProperty().add(property);

        messagesModelWrapper.addMessage(message);
        messagesModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterAddFirstMessage.xml");
    }

    public void testReorderMessages() {
        myFixture.configureByFile("beforeMessagesTest.xml");
        MessagesModelWrapper messagesModelWrapper = setUpEditorAndGetModel();

        List<ModelWrapper> messageModelList = messagesModelWrapper.getChildrenModels();
        Collections.swap(messageModelList, 1, 2);

        messagesModelWrapper.setChildrenFromModels(messageModelList);
        messagesModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterReorderMessages.xml");
    }

    public void testDeleteMessage() {
        myFixture.configureByFile("beforeMessagesTest.xml");
        MessagesModelWrapper messagesModelWrapper = setUpEditorAndGetModel();

        List<ModelWrapper> messageModelList = messagesModelWrapper.getChildrenModels();

        messagesModelWrapper.deleteChild(messageModelList.get(3));
        messagesModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterDeleteMessage.xml");
    }

    public void testDeleteAllMessages() {
        myFixture.configureByFile("beforeMessagesTest.xml");
        MessagesModelWrapper messagesModelWrapper = setUpEditorAndGetModel();

        List<ModelWrapper> messageModelList = messagesModelWrapper.getChildrenModels();

        for (ModelWrapper modelWrapper : messageModelList) {
            messagesModelWrapper.deleteChild(modelWrapper);
        }
        messagesModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterDeleteAllMessages.xml");
    }

    //single message
    public void testEditURIInSingleMessage() {
        myFixture.configureByFile("beforeMessagesTest.xml");
        MessagesModelWrapper messagesModelWrapper = setUpEditorAndGetModel();

        List<ModelWrapper> messageModelList = messagesModelWrapper.getChildrenModels();
        Scenario.Messages.Message message = (Scenario.Messages.Message) messageModelList.get(0).retrieveModel();
        message.setUri("edited");

        messageModelList.get(0).updateModel(message);
        messagesModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterEditURIInSingleMessage.xml");
    }

    public void testEditContentInSingleMessage() {
        myFixture.configureByFile("beforeMessagesTest.xml");
        MessagesModelWrapper messagesModelWrapper = setUpEditorAndGetModel();

        List<ModelWrapper> messageModelList = messagesModelWrapper.getChildrenModels();
        Scenario.Messages.Message message = (Scenario.Messages.Message) messageModelList.get(1).retrieveModel();
        message.setContent("edited");

        messageModelList.get(1).updateModel(message);
        messagesModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterEditContentInSingleMessage.xml");
    }

    public void testEditMultiplicityInSingleMessage() {
        myFixture.configureByFile("beforeMessagesTest.xml");
        MessagesModelWrapper messagesModelWrapper = setUpEditorAndGetModel();

        List<ModelWrapper> messageModelList = messagesModelWrapper.getChildrenModels();
        Scenario.Messages.Message message = (Scenario.Messages.Message) messageModelList.get(1).retrieveModel();
        message.setMultiplicity("100");

        messageModelList.get(1).updateModel(message);
        messagesModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterEditMultiplicityInSingleMessage.xml");
    }

    public void testEditHeadersInSingleMessage() {
        myFixture.configureByFile("beforeMessagesTest.xml");
        MessagesModelWrapper messagesModelWrapper = setUpEditorAndGetModel();

        List<ModelWrapper> messageModelList = messagesModelWrapper.getChildrenModels();
        Scenario.Messages.Message oldMessage = (Scenario.Messages.Message) messageModelList.get(4).retrieveModel();

        Scenario.Messages.Message newMessage = new Scenario.Messages.Message();
        newMessage.setContent(oldMessage.getContent());
        newMessage.setUri(oldMessage.getUri());
        newMessage.setMultiplicity(oldMessage.getMultiplicity());
        newMessage.getProperty().addAll(oldMessage.getProperty());
        newMessage.getValidatorRef().addAll(oldMessage.getValidatorRef());

        oldMessage.getHeader().get(0).setValue("newValue");
        oldMessage.getHeader().get(1).setName("newName");
        oldMessage.getHeader().get(2).setValue("newValue");
        oldMessage.getHeader().get(2).setName("newName");

        newMessage.getHeader().addAll(oldMessage.getHeader());

        messageModelList.get(4).updateModel(newMessage);
        messagesModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterEditHeadersInSingleMessage.xml");
    }

    public void testEditPropertiesInSingleMessage() {
        myFixture.configureByFile("beforeMessagesTest.xml");
        MessagesModelWrapper messagesModelWrapper = setUpEditorAndGetModel();

        List<ModelWrapper> messageModelList = messagesModelWrapper.getChildrenModels();
        Scenario.Messages.Message oldMessage = (Scenario.Messages.Message) messageModelList.get(3).retrieveModel();

        Scenario.Messages.Message newMessage = new Scenario.Messages.Message();
        newMessage.setContent(oldMessage.getContent());
        newMessage.setUri(oldMessage.getUri());
        newMessage.setMultiplicity(oldMessage.getMultiplicity());
        newMessage.getHeader().addAll(oldMessage.getHeader());
        newMessage.getValidatorRef().addAll(oldMessage.getValidatorRef());

        oldMessage.getProperty().get(0).setValue("newValue");
        oldMessage.getProperty().get(1).setName("newName");
        oldMessage.getProperty().get(2).setValue("newValue");
        oldMessage.getProperty().get(2).setName("newName");

        newMessage.getProperty().addAll(oldMessage.getProperty());

        messageModelList.get(3).updateModel(newMessage);
        messagesModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterEditPropertiesInSingleMessage.xml");
    }

    public void testEditAttachedValidatorsInSingleMessage() {
        myFixture.configureByFile("beforeMessagesTestAttachValidator.xml");
        MessagesModelWrapper messagesModelWrapper = setUpEditorAndGetModel();

        List<ModelWrapper> messageModelList = messagesModelWrapper.getChildrenModels();
        Scenario.Messages.Message oldMessage = (Scenario.Messages.Message) messageModelList.get(3).retrieveModel();

        Scenario.Messages.Message newMessage = new Scenario.Messages.Message();
        newMessage.setContent(oldMessage.getContent());
        newMessage.setUri(oldMessage.getUri());
        newMessage.setMultiplicity(oldMessage.getMultiplicity());
        newMessage.getHeader().addAll(oldMessage.getHeader());
        newMessage.getProperty().addAll(oldMessage.getProperty());

        oldMessage.getValidatorRef().remove(0);
        Scenario.Messages.Message.ValidatorRef ref3 = new Scenario.Messages.Message.ValidatorRef();
        ref3.setId("3");
        newMessage.getValidatorRef().add(ref3);
        Scenario.Messages.Message.ValidatorRef ref1 = new Scenario.Messages.Message.ValidatorRef();
        ref1.setId("1");
        newMessage.getValidatorRef().add(ref1);

        messageModelList.get(3).updateModel(newMessage);
        messagesModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterEditAttachedValidatorsInSingleMessage.xml");
    }

    public void testAddHeaderToSingleMessage() {
        myFixture.configureByFile("beforeMessagesTest.xml");
        MessagesModelWrapper messagesModelWrapper = setUpEditorAndGetModel();

        MessageModelWrapper messageModelWrapper = (MessageModelWrapper) messagesModelWrapper.getChildrenModels().get(1);

        Header header = new Header();
        header.setName("addedName");
        header.setValue("addedValue");

        messageModelWrapper.addHeader(header);
        messagesModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterAddHeaderToSingleMessage.xml");
    }

    public void testAddPropertyToSingleMessage() {
        myFixture.configureByFile("beforeMessagesTest.xml");
        MessagesModelWrapper messagesModelWrapper = setUpEditorAndGetModel();

        MessageModelWrapper messageModelWrapper = (MessageModelWrapper) messagesModelWrapper.getChildrenModels().get(1);

        Property property = new Property();
        property.setName("addedName");
        property.setValue("addedValue");

        messageModelWrapper.addProperty(property);
        messagesModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterAddPropertyToSingleMessage.xml");
    }

    public void testAttachValidatorToSingleMessage() {
        myFixture.configureByFile("beforeMessagesTestAttachValidator.xml");
        MessagesModelWrapper messagesModelWrapper = setUpEditorAndGetModel();

        MessageModelWrapper messageModelWrapper = (MessageModelWrapper) messagesModelWrapper.getChildrenModels().get(1);

        Scenario.Messages.Message.ValidatorRef ref = new Scenario.Messages.Message.ValidatorRef();
        ref.setId("2");

        messageModelWrapper.attachValidator(ref);
        messagesModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterAttachValidatorToSingleMessage.xml");
    }

    //wrong att.
    public void testMessagesWithWrongAttributes() {
        myFixture.configureByFile("beforeMessagesTest.xml");
        //add null Message
        MessagesModelWrapper messagesModelWrapper = setUpEditorAndGetModel();
        try {
            messagesModelWrapper.addMessage(null);
            messagesModelWrapper.getGUI().commitChanges("test");
            Assert.fail();
        } catch (NullPointerException e) {
            // OK
        }
        myFixture.checkResultByFile("beforeMessagesTest.xml");
        //delete null Message
        MessagesModelWrapper messagesModelWrapper2 = setUpEditorAndGetModel();
        try {
            messagesModelWrapper2.deleteChild(null);
            messagesModelWrapper2.getGUI().commitChanges("test");
            Assert.fail();
        } catch (NullPointerException e) {
            // OK
        }
        myFixture.checkResultByFile("beforeMessagesTest.xml");
        //reorder null messages
        MessagesModelWrapper messagesModelWrapper3 = setUpEditorAndGetModel();
        try {
            messagesModelWrapper3.setChildrenFromModels(null);
            messagesModelWrapper3.getGUI().commitChanges("test");
            Assert.fail();
        } catch (NullPointerException e) {
            // OK
        }
        myFixture.checkResultByFile("beforeMessagesTest.xml");
        // add null Header to single Message
        MessagesModelWrapper messagesModelWrapper4 = setUpEditorAndGetModel();
        MessageModelWrapper messageModelWrapper4 = (MessageModelWrapper) messagesModelWrapper4.getChildrenModels().get(1);
        try {
            messageModelWrapper4.addHeader(null);
            messagesModelWrapper4.getGUI().commitChanges("test");
            Assert.fail();
        } catch (ScenarioManagerException e) {
            // OK
        }
        myFixture.checkResultByFile("beforeMessagesTest.xml");
        // add null Property to single Message
        MessagesModelWrapper messagesModelWrapper5 = setUpEditorAndGetModel();
        MessageModelWrapper messageModelWrapper5 = (MessageModelWrapper) messagesModelWrapper5.getChildrenModels().get(1);
        try {
            messageModelWrapper5.addProperty(null);
            messagesModelWrapper5.getGUI().commitChanges("test");
            Assert.fail();
        } catch (ScenarioManagerException e) {
            // OK
        }
        myFixture.checkResultByFile("beforeMessagesTest.xml");
        // attach null validator to single message (null ValidatorRef)
        MessagesModelWrapper messagesModelWrapper6 = setUpEditorAndGetModel();
        MessageModelWrapper messageModelWrapper6 = (MessageModelWrapper) messagesModelWrapper6.getChildrenModels().get(1);
        try {
            messageModelWrapper6.attachValidator(null);
            messagesModelWrapper6.getGUI().commitChanges("test");
            Assert.fail();
        } catch (NullPointerException e) {
            // OK
        }
        myFixture.checkResultByFile("beforeMessagesTest.xml");
        // attach not existing validator to single message
        MessagesModelWrapper messagesModelWrapper7 = setUpEditorAndGetModel();
        MessageModelWrapper messageModelWrapper7 = (MessageModelWrapper) messagesModelWrapper7.getChildrenModels().get(1);
        try {
            Scenario.Messages.Message.ValidatorRef ref = new Scenario.Messages.Message.ValidatorRef();
            ref.setId("7");
            messageModelWrapper7.attachValidator(ref);
            messagesModelWrapper7.getGUI().commitChanges("test");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // OK
        }
        myFixture.checkResultByFile("beforeMessagesTest.xml");


    }
}
