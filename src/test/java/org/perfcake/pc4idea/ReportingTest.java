package org.perfcake.pc4idea;

import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.fileEditor.ex.FileEditorProviderManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.modelwrapper.ModelWrapper;
import org.perfcake.pc4idea.impl.editor.editor.PerfCakeEditor;
import org.perfcake.pc4idea.impl.editor.editor.PerfCakeEditorProvider;
import org.perfcake.pc4idea.impl.editor.modelwrapper.ReportingModelWrapper;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * Created by Stanislav Kaleta on 4/7/15.
 */
public class ReportingTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return new File(PathManager.getJarPathForClass(GeneratorTest.class) + "/reportingTestFiles").getPath();
    }

    private ReportingModelWrapper setUpEditorAndGetModel() {
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
        assertTrue(pcProvider.accept(getProject(), file));
        PerfCakeEditor editor = (PerfCakeEditor) pcProvider.createEditor(getProject(), file);

        return (ReportingModelWrapper) editor.getComponent().getScenarioGUI().getComponentModel(5);
    }

    //reporting
    public void testEditReporters() {
        myFixture.configureByFile("beforeReportingTest.xml");
        ReportingModelWrapper reportingModelWrapper = setUpEditorAndGetModel();
        Scenario.Reporting beforeModel = (Scenario.Reporting) reportingModelWrapper.retrieveModel();

        Scenario.Reporting afterModel = new Scenario.Reporting();
        afterModel.getProperty().addAll(beforeModel.getProperty());

        List<Scenario.Reporting.Reporter> reporterList = beforeModel.getReporter();
        reporterList.get(0).setClazz("ConsoleDestination");
        reporterList.get(0).setEnabled(false);
        reporterList.get(1).setEnabled(true);
        reporterList.get(2).setClazz("WarmUpReporter");
        afterModel.getReporter().addAll(reporterList);

        reportingModelWrapper.updateModel(afterModel);
        reportingModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterEditReporters.xml");
    }

    public void testEditProperties() {
        myFixture.configureByFile("beforeReportingTest.xml");
        ReportingModelWrapper reportingModelWrapper = setUpEditorAndGetModel();
        Scenario.Reporting beforeModel = (Scenario.Reporting) reportingModelWrapper.retrieveModel();

        Scenario.Reporting afterModel = new Scenario.Reporting();
        afterModel.getReporter().addAll(beforeModel.getReporter());

        List<Property> propertyList = beforeModel.getProperty();
        propertyList.get(0).setName("editedN");
        propertyList.get(0).setValue("editedV");
        propertyList.get(1).setName("editedN");
        propertyList.get(2).setValue("editedV");
        afterModel.getProperty().addAll(propertyList);

        reportingModelWrapper.updateModel(afterModel);
        reportingModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterEditProperties.xml");
    }

    public void testAddProperty() {
        myFixture.configureByFile("beforeReportingTest.xml");
        ReportingModelWrapper reportingModelWrapper = setUpEditorAndGetModel();

        Property property = new Property();
        property.setName("addedN");
        property.setValue("addedV");

        reportingModelWrapper.addProperty(property);
        reportingModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterAddProperty.xml");
    }

    public void testAddReporter() {
        myFixture.configureByFile("beforeReportingTest.xml");
        ReportingModelWrapper reportingModelWrapper = setUpEditorAndGetModel();

        Scenario.Reporting.Reporter reporter = new Scenario.Reporting.Reporter();
        reporter.setClazz("MemoryUsageReporter");
        reporter.setEnabled(false);

        reportingModelWrapper.addReporter(reporter);
        reportingModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterAddReporter.xml");
    }

    public void testAddFirstReporter() {
        myFixture.configureByFile("beforeReportingTestEmpty.xml");
        ReportingModelWrapper reportingModelWrapper = setUpEditorAndGetModel();

        Scenario.Reporting.Reporter reporter = new Scenario.Reporting.Reporter();
        reporter.setClazz("MemoryUsageReporter");
        reporter.setEnabled(false);

        reportingModelWrapper.addReporter(reporter);
        reportingModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterAddFirstReporter.xml");
    }

    public void testReorderReporters() {
        myFixture.configureByFile("beforeReportingTest.xml");
        ReportingModelWrapper reportingModelWrapper = setUpEditorAndGetModel();

        List<ModelWrapper> reporterModelList = reportingModelWrapper.getChildrenModels();
        Collections.swap(reporterModelList, 2, 3);

        reportingModelWrapper.setChildrenFromModels(reporterModelList);
        reportingModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterReorderReporters.xml");
    }

    public void testDeleteReporter() {
        myFixture.configureByFile("beforeReportingTest.xml");
        ReportingModelWrapper reportingModelWrapper = setUpEditorAndGetModel();

        List<ModelWrapper> reporterModelList = reportingModelWrapper.getChildrenModels();

        reportingModelWrapper.deleteChild(reporterModelList.get(2));
        reportingModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterDeleteReporter.xml");
    }

    public void testDeleteAllReporters() {
        myFixture.configureByFile("beforeReportingTest.xml");
        ReportingModelWrapper reportingModelWrapper = setUpEditorAndGetModel();

        List<ModelWrapper> reporterModelList = reportingModelWrapper.getChildrenModels();

        for (ModelWrapper modelWrapper : reporterModelList) {
            reportingModelWrapper.deleteChild(modelWrapper);
        }
        reportingModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterDeleteAllReporters.xml");
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

    /*TODO if Period can be null*/
}
