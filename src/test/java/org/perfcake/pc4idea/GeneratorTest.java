package org.perfcake.pc4idea;

import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.fileEditor.ex.FileEditorProviderManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.manager.ScenarioManagerException;
import org.perfcake.pc4idea.impl.editor.editor.ScenarioEditor;
import org.perfcake.pc4idea.impl.editor.editor.ScenarioEditorProvider;
import org.perfcake.pc4idea.impl.editor.modelwrapper.component.GeneratorModelWrapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stanislav Kaleta on 3/14/15.
 */
public class GeneratorTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return new File(PathManager.getJarPathForClass(GeneratorTest.class)+"/generatorTestFiles").getPath();
    }

    private GeneratorModelWrapper setUpEditorAndGetModel() {
        VirtualFile file = myFixture.getFile().getVirtualFile();
        myFixture.openFileInEditor(file);
        FileEditorProvider[] possibleProviders = FileEditorProviderManager.getInstance().getProviders(getProject(),file);
        ScenarioEditorProvider pcProvider = null;
        for (FileEditorProvider possibleProvider : possibleProviders) {
            if (possibleProvider.getEditorTypeId().equals("PerfCakeEditor")) {
                pcProvider = (ScenarioEditorProvider) possibleProvider;
            }
        }
        if (pcProvider == null){
            throw new AssertionError("Error setting up editor - cant find PerfCakeEditorProvider instance");
        }
        assertTrue(pcProvider.accept(getProject(), file));
        ScenarioEditor editor = (ScenarioEditor) pcProvider.createEditor(getProject(),file);

        return (GeneratorModelWrapper) editor.getModel().prepareModelWrappers()[0];
    }

    public void testEditClass(){
        myFixture.configureByFile("beforeGeneratorTest.xml");
        GeneratorModelWrapper generatorModelWrapper = setUpEditorAndGetModel();
        Scenario.Generator beforeModel = (Scenario.Generator) generatorModelWrapper.retrieveModel();

        Scenario.Generator afterModel = new Scenario.Generator();
        afterModel.setRun(beforeModel.getRun());
        afterModel.setThreads(beforeModel.getThreads());
        afterModel.getProperty().addAll(beforeModel.getProperty());

        afterModel.setClazz("RampUpDownGenerator");

        generatorModelWrapper.updateModel(afterModel);
        generatorModelWrapper.commit("test");
        myFixture.checkResultByFile("afterEditClass.xml");
    }

    public void testEditThreads(){
        myFixture.configureByFile("beforeGeneratorTest.xml");
        GeneratorModelWrapper generatorModelWrapper = setUpEditorAndGetModel();
        Scenario.Generator beforeModel = (Scenario.Generator) generatorModelWrapper.retrieveModel();

        Scenario.Generator afterModel = new Scenario.Generator();
        afterModel.setClazz(beforeModel.getClazz());
        afterModel.setRun(beforeModel.getRun());
        afterModel.getProperty().addAll(beforeModel.getProperty());

        afterModel.setThreads("100");

        generatorModelWrapper.updateModel(afterModel);
        generatorModelWrapper.commit("test");
        myFixture.checkResultByFile("afterEditThreads.xml");
    }

    public void testEditRun(){
        myFixture.configureByFile("beforeGeneratorTest.xml");
        GeneratorModelWrapper generatorModelWrapper = setUpEditorAndGetModel();
        Scenario.Generator beforeModel = (Scenario.Generator) generatorModelWrapper.retrieveModel();

        Scenario.Generator afterModel = new Scenario.Generator();
        afterModel.setClazz(beforeModel.getClazz());
        afterModel.getProperty().addAll(beforeModel.getProperty());
        afterModel.setThreads(beforeModel.getThreads());

        Scenario.Generator.Run run = new Scenario.Generator.Run();
        run.setValue("90");
        run.setType("percentage");
        afterModel.setRun(run);

        generatorModelWrapper.updateModel(afterModel);
        generatorModelWrapper.commit("test");
        myFixture.checkResultByFile("afterEditRun.xml");
    }

    public void testEditRunType(){
        myFixture.configureByFile("beforeGeneratorTest.xml");
        GeneratorModelWrapper generatorModelWrapper = setUpEditorAndGetModel();
        Scenario.Generator beforeModel = (Scenario.Generator) generatorModelWrapper.retrieveModel();

        Scenario.Generator afterModel = new Scenario.Generator();
        afterModel.setClazz(beforeModel.getClazz());
        afterModel.getProperty().addAll(beforeModel.getProperty());
        afterModel.setThreads(beforeModel.getThreads());

        Scenario.Generator.Run run = beforeModel.getRun();
        run.setType("iteration");
        afterModel.setRun(run);

        generatorModelWrapper.updateModel(afterModel);
        generatorModelWrapper.commit("test");
        myFixture.checkResultByFile("afterEditRunType.xml");
    }

    public void testEditRunValue(){
        myFixture.configureByFile("beforeGeneratorTest.xml");
        GeneratorModelWrapper generatorModelWrapper = setUpEditorAndGetModel();
        Scenario.Generator beforeModel = (Scenario.Generator) generatorModelWrapper.retrieveModel();

        Scenario.Generator afterModel = new Scenario.Generator();
        afterModel.setClazz(beforeModel.getClazz());
        afterModel.getProperty().addAll(beforeModel.getProperty());
        afterModel.setThreads(beforeModel.getThreads());

        Scenario.Generator.Run run = beforeModel.getRun();
        run.setValue("100");
        afterModel.setRun(run);

        generatorModelWrapper.updateModel(afterModel);
        generatorModelWrapper.commit("test");
        myFixture.checkResultByFile("afterEditRunValue.xml");
    }

    public void testEditProperties(){
        myFixture.configureByFile("beforeGeneratorTest.xml");
        GeneratorModelWrapper generatorModelWrapper = setUpEditorAndGetModel();
        Scenario.Generator beforeModel = (Scenario.Generator) generatorModelWrapper.retrieveModel();

        Scenario.Generator afterModel = new Scenario.Generator();
        afterModel.setClazz(beforeModel.getClazz());
        afterModel.setThreads(beforeModel.getThreads());
        afterModel.setRun(beforeModel.getRun());

        List<Property> propertyList = new ArrayList<>();
        Property p1 = new Property();
        p1.setName(beforeModel.getProperty().get(0).getName());
        p1.setValue("changedValue");
        propertyList.add(p1);
        Property p2 = new Property();
        p2.setName(beforeModel.getProperty().get(2).getName());
        p2.setValue(beforeModel.getProperty().get(2).getValue());
        propertyList.add(p2);
        Property p3 = new Property();
        p3.setName("newProperty");
        p3.setValue("newValue");
        propertyList.add(p3);
        afterModel.getProperty().addAll(propertyList);

        generatorModelWrapper.updateModel(afterModel);
        generatorModelWrapper.commit("test");
        myFixture.checkResultByFile("afterEditProperties.xml");
    }

    public void testAddProperty(){
        myFixture.configureByFile("beforeGeneratorTest.xml");
        GeneratorModelWrapper generatorModelWrapper = setUpEditorAndGetModel();

        Property property = new Property();
        property.setName("p4");
        property.setValue("p4");

        generatorModelWrapper.addProperty(property);
        generatorModelWrapper.commit("test");
        myFixture.checkResultByFile("afterAddProperty.xml");
    }

    public void testGeneratorWithWrongAttributes(){
        myFixture.configureByFile("beforeGeneratorTest.xml");
        //null Generator
        GeneratorModelWrapper generatorModelWrapper = setUpEditorAndGetModel();
        try {
            generatorModelWrapper.updateModel(null);
            generatorModelWrapper.commit("test");
            fail();
        } catch (NullPointerException expected) {
            // OK
        }
        myFixture.checkResultByFile("beforeGeneratorTest.xml");
        // null Run
        GeneratorModelWrapper generatorModelWrapper2 = setUpEditorAndGetModel();
        Scenario.Generator beforeModel = (Scenario.Generator) generatorModelWrapper2.retrieveModel();

        Scenario.Generator afterModel = new Scenario.Generator();
        afterModel.getProperty().addAll(beforeModel.getProperty());
        afterModel.setClazz(beforeModel.getClazz());
        afterModel.setThreads(beforeModel.getThreads());
        afterModel.setRun(null);
        try {
            generatorModelWrapper2.updateModel(afterModel);
            generatorModelWrapper2.commit("test");
            fail();
        } catch (ScenarioManagerException expected) {
            // OK
        }
        myFixture.checkResultByFile("beforeGeneratorTest.xml");
        //add null Property
        GeneratorModelWrapper generatorModelWrapper3 = setUpEditorAndGetModel();
        try {
            generatorModelWrapper3.addProperty(null);
            generatorModelWrapper3.commit("test");
            fail();
        } catch (ScenarioManagerException expected) {
            // OK
        }
        myFixture.checkResultByFile("beforeGeneratorTest.xml");
    }
}
