package org.perfcake.pc4idea;

import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.fileEditor.ex.FileEditorProviderManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.modelwrapper.component.ComponentModelWrapper;
import org.perfcake.pc4idea.api.manager.ScenarioManagerException;
import org.perfcake.pc4idea.impl.editor.editor.ScenarioEditor;
import org.perfcake.pc4idea.impl.editor.editor.ScenarioEditorProvider;
import org.perfcake.pc4idea.impl.editor.modelwrapper.component.DestinationModelWrapper;
import org.perfcake.pc4idea.impl.editor.modelwrapper.component.ReporterModelWrapper;
import org.perfcake.pc4idea.impl.editor.modelwrapper.component.ReportingModelWrapper;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * Created by Stanislav Kaleta on 4/7/15.
 */
public class ReportingTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return new File(PathManager.getJarPathForClass(GeneratorTest.class) +
                "/reportingTestFiles").getPath();
    }

    private ReportingModelWrapper setUpEditorAndGetModel() {
        VirtualFile file = myFixture.getFile().getVirtualFile();
        FileEditorProvider[] possibleProviders = FileEditorProviderManager.
                getInstance().getProviders(getProject(), file);
        ScenarioEditorProvider pcProvider = null;
        for (FileEditorProvider possibleProvider : possibleProviders) {
            if (possibleProvider.getEditorTypeId().equals("PerfCakeEditor")) {
                pcProvider = (ScenarioEditorProvider) possibleProvider;
            }
        }
        if (pcProvider == null) {
            throw new AssertionError("Error setting up editor - " +
                    "cant find PerfCakeEditorProvider instance");
        }
        assertTrue(pcProvider.accept(getProject(), file));
        ScenarioEditor editor =
                (ScenarioEditor) pcProvider.createEditor(getProject(), file);

        return (ReportingModelWrapper) editor.getModel().getScenarioComponents()[4];
    }

    //reporting
    public void testEditReporters() {
        myFixture.configureByFile("beforeReportingTest.xml");
        ReportingModelWrapper reportingModelWrapper = setUpEditorAndGetModel();
        Scenario.Reporting beforeModel =
                (Scenario.Reporting) reportingModelWrapper.retrieveModel();

        Scenario.Reporting afterModel = new Scenario.Reporting();
        afterModel.getProperty().addAll(beforeModel.getProperty());

        List<Scenario.Reporting.Reporter> reporterList = beforeModel.getReporter();
        reporterList.get(0).setClazz("ConsoleDestination");
        reporterList.get(0).setEnabled(false);
        reporterList.get(1).setEnabled(true);
        reporterList.get(2).setClazz("WarmUpReporter");
        afterModel.getReporter().addAll(reporterList);

        reportingModelWrapper.updateModel(afterModel);
        reportingModelWrapper.commit("test");
        myFixture.checkResultByFile("afterEditReporters.xml");
    }

    public void testEditProperties() {
        myFixture.configureByFile("beforeReportingTest.xml");
        ReportingModelWrapper reportingModelWrapper = setUpEditorAndGetModel();
        Scenario.Reporting beforeModel =
                (Scenario.Reporting) reportingModelWrapper.retrieveModel();

        Scenario.Reporting afterModel = new Scenario.Reporting();
        afterModel.getReporter().addAll(beforeModel.getReporter());

        List<Property> propertyList = beforeModel.getProperty();
        propertyList.get(0).setName("editedN");
        propertyList.get(0).setValue("editedV");
        propertyList.get(1).setName("editedN");
        propertyList.get(2).setValue("editedV");
        afterModel.getProperty().addAll(propertyList);

        reportingModelWrapper.updateModel(afterModel);
        reportingModelWrapper.commit("test");
        myFixture.checkResultByFile("afterEditProperties.xml");
    }

    public void testAddProperty() {
        myFixture.configureByFile("beforeReportingTest.xml");
        ReportingModelWrapper reportingModelWrapper = setUpEditorAndGetModel();

        Property property = new Property();
        property.setName("addedN");
        property.setValue("addedV");

        reportingModelWrapper.addProperty(property);
        reportingModelWrapper.commit("test");
        myFixture.checkResultByFile("afterAddProperty.xml");
    }

    public void testAddReporter() {
        myFixture.configureByFile("beforeReportingTest.xml");
        ReportingModelWrapper reportingModelWrapper = setUpEditorAndGetModel();

        Scenario.Reporting.Reporter reporter = new Scenario.Reporting.Reporter();
        reporter.setClazz("MemoryUsageReporter");
        reporter.setEnabled(false);

        reportingModelWrapper.addReporter(reporter);
        reportingModelWrapper.commit("test");
        myFixture.checkResultByFile("afterAddReporter.xml");
    }

    public void testAddFirstReporter() {
        myFixture.configureByFile("beforeReportingTestEmpty.xml");
        ReportingModelWrapper reportingModelWrapper = setUpEditorAndGetModel();

        Scenario.Reporting.Reporter reporter = new Scenario.Reporting.Reporter();
        reporter.setClazz("MemoryUsageReporter");
        reporter.setEnabled(false);

        reportingModelWrapper.addReporter(reporter);
        reportingModelWrapper.commit("test");
        myFixture.checkResultByFile("afterAddFirstReporter.xml");
    }

    public void testReorderReporters() {
        myFixture.configureByFile("beforeReportingTest.xml");
        ReportingModelWrapper reportingModelWrapper = setUpEditorAndGetModel();

        List<ComponentModelWrapper> reporterModelList = reportingModelWrapper.getChildrenModels();
        Collections.swap(reporterModelList, 2, 3);

        reportingModelWrapper.setChildrenFromModels(reporterModelList);
        reportingModelWrapper.commit("test");
        myFixture.checkResultByFile("afterReorderReporters.xml");
    }

    public void testDeleteReporter() {
        myFixture.configureByFile("beforeReportingTest.xml");
        ReportingModelWrapper reportingModelWrapper = setUpEditorAndGetModel();

        List<ComponentModelWrapper> reporterModelList = reportingModelWrapper.getChildrenModels();

        reportingModelWrapper.deleteChild(reporterModelList.get(2));
        reportingModelWrapper.commit("test");
        myFixture.checkResultByFile("afterDeleteReporter.xml");
    }

    public void testDeleteAllReporters() {
        myFixture.configureByFile("beforeReportingTest.xml");
        ReportingModelWrapper reportingModelWrapper = setUpEditorAndGetModel();

        List<ComponentModelWrapper> reporterModelList = reportingModelWrapper.getChildrenModels();

        for (ComponentModelWrapper modelWrapper : reporterModelList) {
            reportingModelWrapper.deleteChild(modelWrapper);
        }
        reportingModelWrapper.commit("test");
        myFixture.checkResultByFile("afterDeleteAllReporters.xml");
    }

    //single reporter
    public void testEditClassInSingleReporter() {
        myFixture.configureByFile("beforeReportingTest.xml");
        ReportingModelWrapper reportingModelWrapper = setUpEditorAndGetModel();

        List<ComponentModelWrapper> reporterModelList = reportingModelWrapper.getChildrenModels();
        Scenario.Reporting.Reporter oldReporter =
                (Scenario.Reporting.Reporter) reporterModelList.get(0).retrieveModel();

        Scenario.Reporting.Reporter newReporter = new Scenario.Reporting.Reporter();
        newReporter.setEnabled(oldReporter.isEnabled());
        newReporter.getDestination().addAll(oldReporter.getDestination());
        newReporter.getProperty().addAll(oldReporter.getProperty());

        newReporter.setClazz("WarmUpReporter");

        reporterModelList.get(0).updateModel(newReporter);
        reporterModelList.get(0).commit("test");
        myFixture.checkResultByFile("afterEditClassInSingleReporter.xml");
    }

    public void testEditEnabledInSingleReporter() {
        myFixture.configureByFile("beforeReportingTest.xml");
        ReportingModelWrapper reportingModelWrapper = setUpEditorAndGetModel();

        List<ComponentModelWrapper> reporterModelList = reportingModelWrapper.getChildrenModels();
        Scenario.Reporting.Reporter oldReporter =
                (Scenario.Reporting.Reporter) reporterModelList.get(0).retrieveModel();

        Scenario.Reporting.Reporter newReporter = new Scenario.Reporting.Reporter();
        newReporter.setClazz(oldReporter.getClazz());
        newReporter.getDestination().addAll(oldReporter.getDestination());
        newReporter.getProperty().addAll(oldReporter.getProperty());

        newReporter.setEnabled(false);

        reporterModelList.get(0).updateModel(newReporter);
        reporterModelList.get(0).commit("test");
        myFixture.checkResultByFile("afterEditEnabledInSingleReporter.xml");
    }

    public void testEditDestinationsInSingleReporter() {
        myFixture.configureByFile("beforeReportingTest.xml");
        ReportingModelWrapper reportingModelWrapper = setUpEditorAndGetModel();

        List<ComponentModelWrapper> reporterModelList = reportingModelWrapper.getChildrenModels();
        Scenario.Reporting.Reporter oldReporter =
                (Scenario.Reporting.Reporter) reporterModelList.get(0).retrieveModel();

        Scenario.Reporting.Reporter newReporter = new Scenario.Reporting.Reporter();
        newReporter.setClazz(oldReporter.getClazz());
        newReporter.setEnabled(oldReporter.isEnabled());
        newReporter.getProperty().addAll(oldReporter.getProperty());

        oldReporter.getDestination().get(1).setClazz("Log4jDestination");
        oldReporter.getDestination().get(1).getPeriod().get(0).setValue("edited");
        Property property = new Property();
        property.setName("addedN");
        property.setValue("addedV");
        oldReporter.getDestination().get(2).getProperty().add(property);
        oldReporter.getDestination().remove(0);

        newReporter.getDestination().addAll(oldReporter.getDestination());

        reporterModelList.get(0).updateModel(newReporter);
        reporterModelList.get(0).commit("test");
        myFixture.checkResultByFile("afterEditDestinationsInSingleReporter.xml");
    }

    public void testEditPropertiesInSingleReporter() {
        myFixture.configureByFile("beforeReportingTest.xml");
        ReportingModelWrapper reportingModelWrapper = setUpEditorAndGetModel();

        List<ComponentModelWrapper> reporterModelList = reportingModelWrapper.getChildrenModels();
        Scenario.Reporting.Reporter oldReporter =
                (Scenario.Reporting.Reporter) reporterModelList.get(0).retrieveModel();

        Scenario.Reporting.Reporter newReporter = new Scenario.Reporting.Reporter();
        newReporter.setClazz(oldReporter.getClazz());
        newReporter.setEnabled(oldReporter.isEnabled());
        newReporter.getDestination().addAll(oldReporter.getDestination());

        oldReporter.getProperty().get(0).setName("editedN");
        oldReporter.getProperty().get(0).setValue("editedV");
        oldReporter.getProperty().get(1).setName("editedN");
        oldReporter.getProperty().get(2).setValue("editedV");

        newReporter.getProperty().addAll(oldReporter.getProperty());

        reporterModelList.get(0).updateModel(newReporter);
        reporterModelList.get(0).commit("test");
        myFixture.checkResultByFile("afterEditPropertiesInSingleReporter.xml");
    }

    public void testAddDestinationToSingleReporter() {
        myFixture.configureByFile("beforeReportingTest.xml");
        ReportingModelWrapper reportingModelWrapper = setUpEditorAndGetModel();

        ReporterModelWrapper reporterModelWrapper =
                (ReporterModelWrapper) reportingModelWrapper.getChildrenModels().get(1);

        Scenario.Reporting.Reporter.Destination destination =
                new Scenario.Reporting.Reporter.Destination();
        destination.setClazz("Log4jDestination");
        destination.setEnabled(true);
        Scenario.Reporting.Reporter.Destination.Period period =
                new Scenario.Reporting.Reporter.Destination.Period();
        period.setType("period");
        period.setValue("value");
        destination.getPeriod().add(period);

        reporterModelWrapper.addDestination(destination);
        reporterModelWrapper.commit("test");
        myFixture.checkResultByFile("afterAddDestinationToSingleReporter.xml");
    }

    public void testAddPropertyToSingleReporter() {
        myFixture.configureByFile("beforeReportingTest.xml");
        ReportingModelWrapper reportingModelWrapper = setUpEditorAndGetModel();

        ReporterModelWrapper reporterModelWrapper =
                (ReporterModelWrapper) reportingModelWrapper.getChildrenModels().get(1);

        Property property = new Property();
        property.setName("addedName");
        property.setValue("addedValue");

        reporterModelWrapper.addProperty(property);
        reporterModelWrapper.commit("test");
        myFixture.checkResultByFile("afterAddPropertyToSingleReporter.xml");
    }

    public void testDeleteDestinationFromSingleReporter() {
        myFixture.configureByFile("beforeReportingTest.xml");
        ReportingModelWrapper reportingModelWrapper = setUpEditorAndGetModel();

        ReporterModelWrapper reporterModelWrapper =
                (ReporterModelWrapper) reportingModelWrapper.getChildrenModels().get(0);

        List<ComponentModelWrapper> destinationModelList = reporterModelWrapper.getChildrenModels();

        reporterModelWrapper.deleteChild(destinationModelList.get(1));
        reporterModelWrapper.commit("test");
        myFixture.checkResultByFile("afterDeleteDestinationFromSingleReporter.xml");
    }

    public void testReorderDestinationsInSingleReporter() {
        myFixture.configureByFile("beforeReportingTest.xml");
        ReportingModelWrapper reportingModelWrapper = setUpEditorAndGetModel();

        ReporterModelWrapper reporterModelWrapper =
                (ReporterModelWrapper) reportingModelWrapper.getChildrenModels().get(0);

        List<ComponentModelWrapper> destinationModelList = reporterModelWrapper.getChildrenModels();
        Collections.swap(destinationModelList,1,2);

        reporterModelWrapper.setChildrenFromModels(destinationModelList);
        reporterModelWrapper.commit("test");
        myFixture.checkResultByFile("afterReorderDestinationsInSingleReporter.xml");
    }

    public void testEnableSingleReporter() {
        myFixture.configureByFile("beforeReportingTest.xml");
        ReportingModelWrapper reportingModelWrapper = setUpEditorAndGetModel();

        ReporterModelWrapper reporterModelWrapper =
                (ReporterModelWrapper) reportingModelWrapper.getChildrenModels().get(3);

        reporterModelWrapper.setToggle(true);
        reporterModelWrapper.commit("test");
        myFixture.checkResultByFile("afterEnableSingleReporter.xml");
    }

    public void testDisableSingleReporter() {
        myFixture.configureByFile("beforeReportingTest.xml");
        ReportingModelWrapper reportingModelWrapper = setUpEditorAndGetModel();

        ReporterModelWrapper reporterModelWrapper =
                (ReporterModelWrapper) reportingModelWrapper.getChildrenModels().get(2);

        reporterModelWrapper.setToggle(false);
        reporterModelWrapper.commit("test");
        myFixture.checkResultByFile("afterDisableSingleReporter.xml");
    }

    //single destination
    public void testEditClassInSingleDestination() {
        myFixture.configureByFile("beforeReportingTest.xml");
        ReportingModelWrapper reportingModelWrapper = setUpEditorAndGetModel();

        ReporterModelWrapper reporterModelWrapper =
                (ReporterModelWrapper) reportingModelWrapper.getChildrenModels().get(0);
        List<ComponentModelWrapper> destinationModelList = reporterModelWrapper.getChildrenModels();

        Scenario.Reporting.Reporter.Destination oldDestination =
                (Scenario.Reporting.Reporter.Destination)
                        destinationModelList.get(0).retrieveModel();

        Scenario.Reporting.Reporter.Destination newDestination =
                new Scenario.Reporting.Reporter.Destination();
        newDestination.setEnabled(oldDestination.isEnabled());
        newDestination.getPeriod().addAll(oldDestination.getPeriod());
        newDestination.getProperty().addAll(oldDestination.getProperty());

        newDestination.setClazz("CsvDestination");

        destinationModelList.get(0).updateModel(newDestination);
        destinationModelList.get(0).commit("test");
        myFixture.checkResultByFile("afterEditClassInSingleDestination.xml");
    }

    public void testEditEnabledInSingleDestination() {
        myFixture.configureByFile("beforeReportingTest.xml");
        ReportingModelWrapper reportingModelWrapper = setUpEditorAndGetModel();

        ReporterModelWrapper reporterModelWrapper =
                (ReporterModelWrapper) reportingModelWrapper.getChildrenModels().get(0);
        List<ComponentModelWrapper> destinationModelList = reporterModelWrapper.getChildrenModels();

        Scenario.Reporting.Reporter.Destination oldDestination =
                (Scenario.Reporting.Reporter.Destination)
                        destinationModelList.get(0).retrieveModel();

        Scenario.Reporting.Reporter.Destination newDestination =
                new Scenario.Reporting.Reporter.Destination();
        newDestination.setClazz(oldDestination.getClazz());
        newDestination.getPeriod().addAll(oldDestination.getPeriod());
        newDestination.getProperty().addAll(oldDestination.getProperty());

        newDestination.setEnabled(false);

        destinationModelList.get(0).updateModel(newDestination);
        destinationModelList.get(0).commit("test");
        myFixture.checkResultByFile("afterEditEnabledInSingleDestination.xml");
    }

    public void testEditPeriodsInSingleDestination() {
        myFixture.configureByFile("beforeReportingTest.xml");
        ReportingModelWrapper reportingModelWrapper = setUpEditorAndGetModel();

        ReporterModelWrapper reporterModelWrapper =
                (ReporterModelWrapper) reportingModelWrapper.getChildrenModels().get(0);
        List<ComponentModelWrapper> destinationModelList = reporterModelWrapper.getChildrenModels();

        Scenario.Reporting.Reporter.Destination oldDestination =
                (Scenario.Reporting.Reporter.Destination)
                        destinationModelList.get(0).retrieveModel();

        Scenario.Reporting.Reporter.Destination newDestination =
                new Scenario.Reporting.Reporter.Destination();
        newDestination.setClazz(oldDestination.getClazz());
        newDestination.setEnabled(oldDestination.isEnabled());
        newDestination.getProperty().addAll(oldDestination.getProperty());

        oldDestination.getPeriod().get(0).setType("edited");
        Scenario.Reporting.Reporter.Destination.Period period =
                new Scenario.Reporting.Reporter.Destination.Period();
        period.setType("new");
        period.setValue("value");
        oldDestination.getPeriod().add(period);

        newDestination.getPeriod().addAll(oldDestination.getPeriod());

        destinationModelList.get(0).updateModel(newDestination);
        destinationModelList.get(0).commit("test");
        myFixture.checkResultByFile("afterEditPeriodsInSingleDestination.xml");
    }

    public void testEditPropertiesInSingleDestination() {
        myFixture.configureByFile("beforeReportingTest.xml");
        ReportingModelWrapper reportingModelWrapper = setUpEditorAndGetModel();

        ReporterModelWrapper reporterModelWrapper =
                (ReporterModelWrapper) reportingModelWrapper.getChildrenModels().get(0);
        List<ComponentModelWrapper> destinationModelList = reporterModelWrapper.getChildrenModels();

        Scenario.Reporting.Reporter.Destination oldDestination =
                (Scenario.Reporting.Reporter.Destination)
                        destinationModelList.get(0).retrieveModel();

        Scenario.Reporting.Reporter.Destination newDestination =
                new Scenario.Reporting.Reporter.Destination();
        newDestination.setClazz(oldDestination.getClazz());
        newDestination.setEnabled(oldDestination.isEnabled());
        newDestination.getPeriod().addAll(oldDestination.getPeriod());

        oldDestination.getProperty().get(0).setName("editedN");
        oldDestination.getProperty().get(0).setValue("editedV");
        oldDestination.getProperty().get(1).setName("editedN");
        oldDestination.getProperty().get(2).setValue("editedV");

        newDestination.getProperty().addAll(oldDestination.getProperty());

        destinationModelList.get(0).updateModel(newDestination);
        destinationModelList.get(0).commit("test");
        myFixture.checkResultByFile("afterEditPropertiesInSingleDestination.xml");
    }

    public void testAddPeriodToSingleDestination() {
        myFixture.configureByFile("beforeReportingTest.xml");
        ReportingModelWrapper reportingModelWrapper = setUpEditorAndGetModel();

        ReporterModelWrapper reporterModelWrapper =
                (ReporterModelWrapper) reportingModelWrapper.getChildrenModels().get(0);
        DestinationModelWrapper destinationModelWrapper =
                (DestinationModelWrapper) reporterModelWrapper.getChildrenModels().get(0);

        Scenario.Reporting.Reporter.Destination.Period period =
                new Scenario.Reporting.Reporter.Destination.Period();
        period.setType("new");
        period.setValue("value");

        destinationModelWrapper.addPeriod(period);
        destinationModelWrapper.commit("test");
        myFixture.checkResultByFile("afterAddPeriodToSingleDestination.xml");
    }

    public void testAddPropertyToSingleDestination() {
        myFixture.configureByFile("beforeReportingTest.xml");
        ReportingModelWrapper reportingModelWrapper = setUpEditorAndGetModel();

        ReporterModelWrapper reporterModelWrapper =
                (ReporterModelWrapper) reportingModelWrapper.getChildrenModels().get(0);
        DestinationModelWrapper destinationModelWrapper =
                (DestinationModelWrapper) reporterModelWrapper.getChildrenModels().get(1);

        Property property = new Property();
        property.setName("newN");
        property.setValue("newV");

        destinationModelWrapper.addProperty(property);
        destinationModelWrapper.commit("test");
        myFixture.checkResultByFile("afterAddPropertyToSingleDestination.xml");
    }

    public void testEnableSingleDestination() {
        myFixture.configureByFile("beforeReportingTest.xml");
        ReportingModelWrapper reportingModelWrapper = setUpEditorAndGetModel();

        ReporterModelWrapper reporterModelWrapper =
                (ReporterModelWrapper) reportingModelWrapper.getChildrenModels().get(0);
        DestinationModelWrapper destinationModelWrapper =
                (DestinationModelWrapper) reporterModelWrapper.getChildrenModels().get(3);

        destinationModelWrapper.setToggle(true);
        destinationModelWrapper.commit("test");
        myFixture.checkResultByFile("afterEnableSingleDestination.xml");
    }

    public void testDisableSingleDestination() {
        myFixture.configureByFile("beforeReportingTest.xml");
        ReportingModelWrapper reportingModelWrapper = setUpEditorAndGetModel();

        ReporterModelWrapper reporterModelWrapper =
                (ReporterModelWrapper) reportingModelWrapper.getChildrenModels().get(0);
        DestinationModelWrapper destinationModelWrapper =
                (DestinationModelWrapper) reporterModelWrapper.getChildrenModels().get(0);

        destinationModelWrapper.setToggle(false);
        destinationModelWrapper.commit("test");
        myFixture.checkResultByFile("afterDisableSingleDestination.xml");
    }

    public void testReportingWithWrongAttributes(){
        myFixture.configureByFile("beforeReportingTest.xml");
        //add null Property
        ReportingModelWrapper reportingModelWrapper = setUpEditorAndGetModel();
        try {
            reportingModelWrapper.addProperty(null);
            reportingModelWrapper.commit("test");
            fail();
        } catch (ScenarioManagerException expected) {
            // OK
        }
        myFixture.checkResultByFile("beforeReportingTest.xml");
        //add null Reporter
        ReportingModelWrapper reportingModelWrapper2 = setUpEditorAndGetModel();
        try {
            reportingModelWrapper2.addReporter(null);
            reportingModelWrapper2.commit("test");
            fail();
        } catch (NullPointerException expected) {
            // OK
        }
        myFixture.checkResultByFile("beforeReportingTest.xml");
        //delete null Reporter
        ReportingModelWrapper reportingModelWrapper3 = setUpEditorAndGetModel();
        try {
            reportingModelWrapper3.deleteChild(null);
            reportingModelWrapper3.commit("test");
            fail();
        } catch (NullPointerException expected) {
            // OK
        }
        myFixture.checkResultByFile("beforeReportingTest.xml");
        //reorder null Reporters
        ReportingModelWrapper reportingModelWrapper4 = setUpEditorAndGetModel();
        try {
            reportingModelWrapper4.setChildrenFromModels(null);
            reportingModelWrapper4.commit("test");
            fail();
        } catch (NullPointerException expected) {
            // OK
        }
        myFixture.checkResultByFile("beforeReportingTest.xml");
        // add null Property to single Reporter
        ReportingModelWrapper reportingModelWrapper5 = setUpEditorAndGetModel();
        ReporterModelWrapper reporterModelWrapper5 =
                (ReporterModelWrapper) reportingModelWrapper5.getChildrenModels().get(0);
        try {
            reporterModelWrapper5.addProperty(null);
            reporterModelWrapper5.commit("test");
            fail();
        } catch (ScenarioManagerException expected) {
            // OK
        }
        myFixture.checkResultByFile("beforeReportingTest.xml");
        // add null Destination to single Reporter
        ReportingModelWrapper reportingModelWrapper6 = setUpEditorAndGetModel();
        ReporterModelWrapper reporterModelWrapper6 =
                (ReporterModelWrapper) reportingModelWrapper6.getChildrenModels().get(0);
        try {
            reporterModelWrapper6.addDestination(null);
            reporterModelWrapper6.commit("test");
            fail();
        } catch (NullPointerException expected) {
            // OK
        }
        myFixture.checkResultByFile("beforeReportingTest.xml");
        // delete null Destination to single Reporter
        ReportingModelWrapper reportingModelWrapper7 = setUpEditorAndGetModel();
        ReporterModelWrapper reporterModelWrapper7 =
                (ReporterModelWrapper) reportingModelWrapper7.getChildrenModels().get(0);
        try {
            reporterModelWrapper7.deleteChild(null);
            reporterModelWrapper7.commit("test");
            fail();
        } catch (NullPointerException expected) {
            // OK
        }
        myFixture.checkResultByFile("beforeReportingTest.xml");
        // reorder null Destination to single Reporter
        ReportingModelWrapper reportingModelWrapper8 = setUpEditorAndGetModel();
        ReporterModelWrapper reporterModelWrapper8 =
                (ReporterModelWrapper) reportingModelWrapper8.getChildrenModels().get(0);
        try {
            reporterModelWrapper8.setChildrenFromModels(null);
            reporterModelWrapper8.commit("test");
            fail();
        } catch (NullPointerException expected) {
            // OK
        }
        myFixture.checkResultByFile("beforeReportingTest.xml");
        // add null Property to single Destination
        ReportingModelWrapper reportingModelWrapper9 = setUpEditorAndGetModel();
        ReporterModelWrapper reporterModelWrapper9 =
                (ReporterModelWrapper) reportingModelWrapper9.getChildrenModels().get(0);
        DestinationModelWrapper destinationModelWrapper9 =
                (DestinationModelWrapper) reporterModelWrapper9.getChildrenModels().get(0);
        try {
            destinationModelWrapper9.addProperty(null);
            destinationModelWrapper9.commit("test");
            fail();
        } catch (ScenarioManagerException expected) {
            // OK
        }
        myFixture.checkResultByFile("beforeReportingTest.xml");
        // add null Period to single Destination
        ReportingModelWrapper reportingModelWrapper10 = setUpEditorAndGetModel();
        ReporterModelWrapper reporterModelWrapper10 =
                (ReporterModelWrapper) reportingModelWrapper10.getChildrenModels().get(0);
        DestinationModelWrapper destinationModelWrapper10 =
                (DestinationModelWrapper) reporterModelWrapper10.getChildrenModels().get(0);
        try {
            destinationModelWrapper10.addPeriod(null);
            destinationModelWrapper10.commit("test");
            fail();
        } catch (ScenarioManagerException expected) {
            // OK
        }
        myFixture.checkResultByFile("beforeReportingTest.xml");
    }
}
