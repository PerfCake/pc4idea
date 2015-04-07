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
public class ValidationTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return new File(PathManager.getJarPathForClass(GeneratorTest.class) + "/validationTestFiles").getPath();
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

        return (SenderModelWrapper) editor.getComponent().getScenarioGUI().getComponentModel(4);
    }

    //validation
    public void testEditValidators() {
        Assert.fail();
    }

    public void testEditEnabledValidation() {
        Assert.fail();
    }

    public void testEditFastForwardValidation() {
        Assert.fail();
    }

    public void testToggleValidation() {
        Assert.fail();
    }

    public void testAddValidator() {
        Assert.fail();
    }

    public void testAddFirstValidator() {
        Assert.fail();
    }

    public void testReorderValidators() {
        Assert.fail();
    }

    public void testDeleteValidator() {
        Assert.fail();
    }

    public void testDeleteAllValidators() {
        Assert.fail();
    }

    //single validator
    public void testEditClassInSingleValidator() {
        Assert.fail();
    }

    public void testEditIDInSingleValidator() {
        Assert.fail();
    }

    public void testEditPropertiesInSingleValidator() {
        Assert.fail();
    }

    public void testAddPropertyToSingleValidator() {
        Assert.fail();
    }

    //wrong att.
    public void testValidationWithWrongAttributes() {
        Assert.fail();
    }
}
