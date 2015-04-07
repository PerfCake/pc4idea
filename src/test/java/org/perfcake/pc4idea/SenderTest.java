package org.perfcake.pc4idea;

import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.fileEditor.ex.FileEditorProviderManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import org.junit.Assert;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.editor.PerfCakeEditor;
import org.perfcake.pc4idea.editor.editor.PerfCakeEditorProvider;
import org.perfcake.pc4idea.editor.modelwrapper.SenderModelWrapper;

import java.io.File;

/**
 * Created by Stanislav Kaleta on 3/18/15.
 */
public class SenderTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return new File(PathManager.getJarPathForClass(GeneratorTest.class) + "/senderTestFiles").getPath();
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

        return (SenderModelWrapper) editor.getComponent().getScenarioGUI().getComponentModel(1);
    }

    public void testEditClass() {
        myFixture.configureByFile("beforeSenderTest.xml");
        SenderModelWrapper senderModelWrapper = setUpEditorAndGetModel();
        Scenario.Sender beforeModel = (Scenario.Sender) senderModelWrapper.retrieveModel();

        Scenario.Sender afterModel = new Scenario.Sender();
        afterModel.getProperty().addAll(beforeModel.getProperty());

        afterModel.setClazz("WebSocketSender");

        senderModelWrapper.updateModel(afterModel);
        senderModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterEditClass.xml");
    }

    public void testEditProperties() {
        Assert.fail();
    }

    public void testAddProperty() {
        Assert.fail();
    }

    public void testReorderProperties() {
        Assert.fail();
    }

    public void testDeleteProperty() {
        Assert.fail();
    }

    public void testEditNameInSingleProperty() {
        Assert.fail();
    }

    public void testEditValueInSingleProperty() {
        Assert.fail();
    }

    public void testSenderWithWrongAttributes() {
        Assert.fail();
    }
}
