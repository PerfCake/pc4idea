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
import org.perfcake.pc4idea.impl.editor.modelwrapper.SenderModelWrapper;

import java.io.File;
import java.util.Collections;
import java.util.List;

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
            throw new AssertionError("Error setting up todo - cant find PerfCakeEditorProvider instance");
        }
        assertTrue(pcProvider.accept(getProject(), file));
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
        myFixture.configureByFile("beforeSenderTest.xml");
        SenderModelWrapper senderModelWrapper = setUpEditorAndGetModel();
        Scenario.Sender beforeModel = (Scenario.Sender) senderModelWrapper.retrieveModel();

        Scenario.Sender afterModel = new Scenario.Sender();
        afterModel.setClazz(beforeModel.getClazz());

        List<Property> propertyList = beforeModel.getProperty();
        propertyList.get(0).setValue("newValue");
        propertyList.get(1).setName("newName");
        propertyList.get(2).setName("newP3");
        propertyList.get(2).setValue("newV3");
        afterModel.getProperty().addAll(propertyList);

        senderModelWrapper.updateModel(afterModel);
        senderModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterEditProperties.xml");
    }

    public void testAddProperty() {
        myFixture.configureByFile("beforeSenderTest.xml");
        SenderModelWrapper senderModelWrapper = setUpEditorAndGetModel();

        Property property = new Property();
        property.setName("added");
        property.setValue("v");

        senderModelWrapper.addProperty(property);
        senderModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterAddProperty.xml");
    }

    public void testReorderProperties() {
        myFixture.configureByFile("beforeSenderTest.xml");
        SenderModelWrapper senderModelWrapper = setUpEditorAndGetModel();

        List<ModelWrapper> propertyModelList = senderModelWrapper.getChildrenModels();
        Collections.swap(propertyModelList, 1, 2);

        senderModelWrapper.setChildrenFromModels(propertyModelList);
        senderModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterReorderProperties.xml");
    }

    public void testDeleteProperty() {
        myFixture.configureByFile("beforeSenderTest.xml");
        SenderModelWrapper senderModelWrapper = setUpEditorAndGetModel();

        List<ModelWrapper> propertyModelList = senderModelWrapper.getChildrenModels();

        senderModelWrapper.deleteChild(propertyModelList.get(1));
        senderModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterDeleteProperty.xml");
    }

    public void testEditNameInSingleProperty() {
        myFixture.configureByFile("beforeSenderTest.xml");
        SenderModelWrapper senderModelWrapper = setUpEditorAndGetModel();

        List<ModelWrapper> propertyModelList = senderModelWrapper.getChildrenModels();
        Property property = (Property) propertyModelList.get(0).retrieveModel();
        property.setName("newName");

        propertyModelList.get(0).updateModel(property);
        senderModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterEditNameInSingleProperty.xml");
    }

    public void testEditValueInSingleProperty() {
        myFixture.configureByFile("beforeSenderTest.xml");
        SenderModelWrapper senderModelWrapper = setUpEditorAndGetModel();

        List<ModelWrapper> propertyModelList = senderModelWrapper.getChildrenModels();
        Property property = (Property) propertyModelList.get(0).retrieveModel();
        property.setValue("newValue");

        propertyModelList.get(0).updateModel(property);
        senderModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterEditValueInSingleProperty.xml");
    }

    public void testSenderWithWrongAttributes() {
        myFixture.configureByFile("beforeSenderTest.xml");
        // null Sender
        SenderModelWrapper senderModelWrapper = setUpEditorAndGetModel();
        try {
            senderModelWrapper.updateModel(null);
            senderModelWrapper.getGUI().commitChanges("test");
            fail();
        } catch (ScenarioManagerException expected) {
            // OK
        }
        myFixture.checkResultByFile("beforeSenderTest.xml");
        //add null Property
        SenderModelWrapper senderModelWrapper2 = setUpEditorAndGetModel();
        try {
            senderModelWrapper2.addProperty(null);
            senderModelWrapper2.getGUI().commitChanges("test");
            fail();
        } catch (ScenarioManagerException expected) {
            // OK
        }
        myFixture.checkResultByFile("beforeSenderTest.xml");
        //delete null Property
        SenderModelWrapper senderModelWrapper3 = setUpEditorAndGetModel();
        try {
            senderModelWrapper3.deleteChild(null);
            senderModelWrapper3.getGUI().commitChanges("test");
            fail();
        } catch (NullPointerException expected) {
            // OK
        }
        myFixture.checkResultByFile("beforeSenderTest.xml");
        //reorder null properties
        SenderModelWrapper senderModelWrapper4 = setUpEditorAndGetModel();
        try {
            senderModelWrapper4.setChildrenFromModels(null);
            senderModelWrapper4.getGUI().commitChanges("test");
            fail();
        } catch (NullPointerException expected) {
            // OK
        }
        myFixture.checkResultByFile("beforeSenderTest.xml");
    }
}
