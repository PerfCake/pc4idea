package org.perfcake.pc4idea;

import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.fileEditor.ex.FileEditorProviderManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import org.perfcake.pc4idea.impl.editor.editor.PerfCakeEditor;
import org.perfcake.pc4idea.impl.editor.editor.PerfCakeEditorProvider;
import org.perfcake.pc4idea.impl.editor.modelwrapper.SenderModelWrapper;

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
            throw new AssertionError("Error setting up todo - cant find PerfCakeEditorProvider instance");
        }
        assertTrue(pcProvider.accept(getProject(), file));
        PerfCakeEditor editor = (PerfCakeEditor) pcProvider.createEditor(getProject(), file);

        return (SenderModelWrapper) editor.getComponent().getScenarioGUI().getComponentModel(5);
    }

    //reporting
    public void testEditReporters() {/*com.intellij.testFramework.fixtures.JavaCodeInsightTestFixture*/
        fail();
    }

    public void testEditProperties() {
        fail();
    }

    public void testAddProperty() {
        fail();
    }

    public void testAddReporter() {
        fail();
    }

    public void testAddFirstReporter() {
        fail();
    }

    public void testReorderReporters() {
        fail();
    }

    public void testDeleteReporter() {
        fail();
    }

    public void testDeleteAllReporters() {
        fail();
    }

    //single reporter
    public void testEditClassInSingleReporter() {
        fail();
    }

    public void testEditEnabledInSingleReporter() {
        fail();
    }

    public void testEditDestinationsInSingleReporter() {
        fail();
    }

    public void testEditPropertiesInSingleReporter() {
        fail();
    }

    public void testAddDestinationToSingleReporter() {
        fail();
    }

    public void testAddPropertyToSingleReporter() {
        fail();
    }

    public void testDeleteDestinationInSingleReporter() {
        fail();
    }

    public void testReorderDestinationsInSingleReporter() {
        fail();
    }

    public void testEnableSingleReporter() {
        fail();
    }

    public void testDisableSingleReporter() {
        fail();
    }

    //single destination
    public void testEditClassInSingleDestination() {
        fail();
    }

    public void testEditEnabledInSingleDestination() {
        fail();
    }

    public void testEditPeriodsInSingleDestination() {
        fail();
    }

    public void testEditPropertiesInSingleDestination() {
        fail();
    }

    public void testAddPeriodToSingleDestination() {
        fail();
    }

    public void testAddPropertyToSingleDestination() {
        fail();
    }

    public void testEnableSingleDestination() {
        fail();
    }

    public void testDisableSingleDestination() {
        fail();
    }
}
