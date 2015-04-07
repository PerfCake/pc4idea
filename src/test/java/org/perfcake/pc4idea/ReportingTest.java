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
 * Created by Stanislav Kaleta on 4/7/15.
 */
public class ReportingTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return new File(PathManager.getJarPathForClass(GeneratorTest.class) + "/reportingTestFiles").getPath();
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

        return (SenderModelWrapper) editor.getComponent().getScenarioGUI().getComponentModel(5);
    }

    //reporting
    public void testEditReporters() {
        Assert.fail();
    }

    public void testEditProperties() {
        Assert.fail();
    }

    public void testAddProperty() {
        Assert.fail();
    }

    public void testAddReporter() {
        Assert.fail();
    }

    public void testAddFirstReporter() {
        Assert.fail();
    }

    public void testReorderReporters() {
        Assert.fail();
    }

    public void testDeleteReporter() {
        Assert.fail();
    }

    public void testDeleteAllReporters() {
        Assert.fail();
    }

    //single reporter
    public void testEditClassInSingleReporter() {
        Assert.fail();
    }

    public void testEditEnabledInSingleReporter() {
        Assert.fail();
    }

    public void testEditDestinationsInSingleReporter() {
        Assert.fail();
    }

    public void testEditPropertiesInSingleReporter() {
        Assert.fail();
    }

    public void testAddDestinationToSingleReporter() {
        Assert.fail();
    }

    public void testAddPropertyToSingleReporter() {
        Assert.fail();
    }

    public void testDeleteDestinationInSingleReporter() {
        Assert.fail();
    }

    public void testReorderDestinationsInSingleReporter() {
        Assert.fail();
    }

    public void testEnableSingleReporter() {
        Assert.fail();
    }

    public void testDisableSingleReporter() {
        Assert.fail();
    }

    //single destination
    public void testEditClassInSingleDestination() {
        Assert.fail();
    }

    public void testEditEnabledInSingleDestination() {
        Assert.fail();
    }

    public void testEditPeriodsInSingleDestination() {
        Assert.fail();
    }

    public void testEditPropertiesInSingleDestination() {
        Assert.fail();
    }

    public void testAddPeriodToSingleDestination() {
        Assert.fail();
    }

    public void testAddPropertyToSingleDestination() {
        Assert.fail();
    }

    public void testEnableSingleDestination() {
        Assert.fail();
    }

    public void testDisableSingleDestination() {
        Assert.fail();
    }
}
