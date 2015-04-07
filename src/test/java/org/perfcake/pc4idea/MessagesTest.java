package org.perfcake.pc4idea;

import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.fileEditor.ex.FileEditorProviderManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import org.junit.Assert;
import org.perfcake.pc4idea.editor.editor.PerfCakeEditor;
import org.perfcake.pc4idea.editor.editor.PerfCakeEditorProvider;
import org.perfcake.pc4idea.editor.modelwrapper.SenderModelWrapper;

import java.io.File;

/**
 * Created by Stanislav Kaleta on 3/31/15.
 */
public class MessagesTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return new File(PathManager.getJarPathForClass(GeneratorTest.class) + "/messagesTestFiles").getPath();
    }

    private SenderModelWrapper setUpEditorAndGetModel() {
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

        return (SenderModelWrapper) editor.getComponent().getScenarioGUI().getComponentModel(3);
    }

    //messages
    public void testEditMessages() {
        Assert.fail();
    }

    public void testAddMessage() {
        Assert.fail();
    }

    public void testAddFirstMessage() {
        Assert.fail();
    }

    public void testReorderMessages() {
        Assert.fail();
    }

    public void testDeleteMessage() {
        Assert.fail();
    }

    public void testDeleteAllMessages() {
        Assert.fail();
    }

    //single message
    public void testEditURIInSingleMessage() {
        Assert.fail();
    }

    public void testEditContentInSingleMessage() {
        Assert.fail();
    }

    public void testEditMultiplicityInSingleMessage() {
        Assert.fail();
    }

    public void testEditHeadersInSingleMessage() {
        Assert.fail();
    }

    public void testEditPropertiesInSingleMessage() {
        Assert.fail();
    }

    public void testEditAttachedValidatorsInSingleMessage() {
        Assert.fail();
    }

    public void testAddHeaderToSingleMessage() {
        Assert.fail();
    }

    public void testAddPropertyToSingleMessage() {
        Assert.fail();
    }

    public void testAttachValidatorToSingleMessage() {
        Assert.fail();
    }

    //wrong att.
    public void testMessagesWithWrongAttributes() {
        Assert.fail();
    }
}
