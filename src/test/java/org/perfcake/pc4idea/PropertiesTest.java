package org.perfcake.pc4idea;

import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.fileEditor.ex.FileEditorProviderManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.editor.modelwrapper.ModelWrapper;
import org.perfcake.pc4idea.api.manager.ScenarioManagerException;
import org.perfcake.pc4idea.impl.editor.editor.PerfCakeEditor;
import org.perfcake.pc4idea.impl.editor.editor.PerfCakeEditorProvider;
import org.perfcake.pc4idea.impl.editor.modelwrapper.PropertiesModelWrapper;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * Created by Stanislav Kaleta on 3/31/15.
 */
public class PropertiesTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return new File(PathManager.getJarPathForClass(GeneratorTest.class) + "/propertiesTestFiles").getPath();
    }

    private PropertiesModelWrapper setUpEditorAndGetModel() {
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

        return (PropertiesModelWrapper) editor.getComponent().getScenarioGUI().getComponentModel(2);
    }

    public void testEditProperties() {
        myFixture.configureByFile("beforePropertiesTest.xml");
        PropertiesModelWrapper propertiesModelWrapper = setUpEditorAndGetModel();
        Scenario.Properties beforeModel = (Scenario.Properties) propertiesModelWrapper.retrieveModel();

        Scenario.Properties afterModel = new Scenario.Properties();

        List<Property> propertyList = beforeModel.getProperty();
        propertyList.get(0).setValue("newValue");
        propertyList.get(1).setName("newName");
        propertyList.get(2).setName("newName3");
        propertyList.get(2).setValue("newValue3");
        afterModel.getProperty().addAll(propertyList);

        propertiesModelWrapper.updateModel(afterModel);
        propertiesModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterEditProperties.xml");
    }

    public void testAddProperty() {
        myFixture.configureByFile("beforePropertiesTest.xml");
        PropertiesModelWrapper propertiesModelWrapper = setUpEditorAndGetModel();

        Property property = new Property();
        property.setName("newName");
        property.setValue("newValue");

        propertiesModelWrapper.addProperty(property);
        propertiesModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterAddProperty.xml");
    }

    public void testAddFirstProperty() {
        myFixture.configureByFile("beforePropertiesTestEmpty.xml");
        PropertiesModelWrapper propertiesModelWrapper = setUpEditorAndGetModel();

        Property property = new Property();
        property.setName("firstName");
        property.setValue("firstValue");

        propertiesModelWrapper.addProperty(property);
        propertiesModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterAddFirstProperty.xml");
    }

    public void testReorderProperties() {
        myFixture.configureByFile("beforePropertiesTest.xml");
        PropertiesModelWrapper propertiesModelWrapper = setUpEditorAndGetModel();

        List<ModelWrapper> propertyModelList = propertiesModelWrapper.getChildrenModels();
        Collections.swap(propertyModelList, 0, 1);

        propertiesModelWrapper.setChildrenFromModels(propertyModelList);
        propertiesModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterReorderProperties.xml");
    }

    public void testDeleteProperty() {
        myFixture.configureByFile("beforePropertiesTest.xml");
        PropertiesModelWrapper propertiesModelWrapper = setUpEditorAndGetModel();

        List<ModelWrapper> propertyModelList = propertiesModelWrapper.getChildrenModels();

        propertiesModelWrapper.deleteChild(propertyModelList.get(1));
        propertiesModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterDeleteProperty.xml");
    }

    public void testDeleteAllProperties() {
        myFixture.configureByFile("beforePropertiesTest.xml");
        PropertiesModelWrapper propertiesModelWrapper = setUpEditorAndGetModel();

        List<ModelWrapper> propertyModelList = propertiesModelWrapper.getChildrenModels();

        for (ModelWrapper modelWrapper : propertyModelList) {
            propertiesModelWrapper.deleteChild(modelWrapper);
        }
        propertiesModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterDeleteAllProperties.xml");
    }

    public void testEditNameInSingleProperty() {
        myFixture.configureByFile("beforePropertiesTest.xml");
        PropertiesModelWrapper propertiesModelWrapper = setUpEditorAndGetModel();

        List<ModelWrapper> propertyModelList = propertiesModelWrapper.getChildrenModels();
        Property property = (Property) propertyModelList.get(1).retrieveModel();
        property.setName("newName");

        propertyModelList.get(1).updateModel(property);
        propertiesModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterEditNameInSingleProperty.xml");
    }

    public void testEditValueInSingleProperty() {
        myFixture.configureByFile("beforePropertiesTest.xml");
        PropertiesModelWrapper propertiesModelWrapper = setUpEditorAndGetModel();

        List<ModelWrapper> propertyModelList = propertiesModelWrapper.getChildrenModels();
        Property property = (Property) propertyModelList.get(1).retrieveModel();
        property.setValue("newValue");

        propertyModelList.get(1).updateModel(property);
        propertiesModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterEditValueInSingleProperty.xml");
    }

    public void testPropertiesWithWrongAttributes() {
        myFixture.configureByFile("beforePropertiesTest.xml");
        //add null Property
        PropertiesModelWrapper propertiesModelWrapper = setUpEditorAndGetModel();
        try {
            propertiesModelWrapper.addProperty(null);
            propertiesModelWrapper.getGUI().commitChanges("test");
            fail();
        } catch (ScenarioManagerException expected) {
            // OK
        }
        myFixture.checkResultByFile("beforePropertiesTest.xml");
        //delete null Property
        PropertiesModelWrapper propertiesModelWrapper2 = setUpEditorAndGetModel();
        try {
            propertiesModelWrapper2.deleteChild(null);
            propertiesModelWrapper2.getGUI().commitChanges("test");
            fail();
        } catch (NullPointerException expected) {
            // OK
        }
        myFixture.checkResultByFile("beforePropertiesTest.xml");
        //reorder null properties
        PropertiesModelWrapper propertiesModelWrapper3 = setUpEditorAndGetModel();
        try {
            propertiesModelWrapper3.setChildrenFromModels(null);
            propertiesModelWrapper3.getGUI().commitChanges("test");
            fail();
        } catch (NullPointerException expected) {
            // OK
        }
        myFixture.checkResultByFile("beforePropertiesTest.xml");
    }
}
