package org.perfcake.pc4idea;

import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.fileEditor.FileEditorProvider;
import com.intellij.openapi.fileEditor.ex.FileEditorProviderManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.fixtures.LightCodeInsightFixtureTestCase;
import org.junit.Assert;
import org.perfcake.model.Property;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.editor.editor.PerfCakeEditor;
import org.perfcake.pc4idea.editor.editor.PerfCakeEditorProvider;
import org.perfcake.pc4idea.editor.interfaces.ModelWrapper;
import org.perfcake.pc4idea.editor.manager.ScenarioManagerException;
import org.perfcake.pc4idea.editor.modelwrapper.ValidationModelWrapper;
import org.perfcake.pc4idea.editor.modelwrapper.ValidatorModelWrapper;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * Created by Stanislav Kaleta on 3/31/15.
 */
public class ValidationTest extends LightCodeInsightFixtureTestCase {

    @Override
    protected String getTestDataPath() {
        return new File(PathManager.getJarPathForClass(GeneratorTest.class) + "/validationTestFiles").getPath();
    }

    private ValidationModelWrapper setUpEditorAndGetModel() {
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

        return (ValidationModelWrapper) editor.getComponent().getScenarioGUI().getComponentModel(4);
    }

    //validation
    public void testEditValidators() {
        myFixture.configureByFile("beforeValidationTest.xml");
        ValidationModelWrapper validationModelWrapper = setUpEditorAndGetModel();
        Scenario.Validation beforeModel = (Scenario.Validation) validationModelWrapper.retrieveModel();

        Scenario.Validation afterModel = new Scenario.Validation();
        afterModel.setEnabled(beforeModel.isEnabled());
        afterModel.setFastForward(beforeModel.isFastForward());

        List<Scenario.Validation.Validator> validatorList = beforeModel.getValidator();
        validatorList.get(0).setClazz("RulesValidator");
        validatorList.get(1).setId("edited");

        afterModel.getValidator().addAll(validatorList);

        validationModelWrapper.updateModel(afterModel);
        validationModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterEditValidators.xml");
    }

    public void testEditEnableValidation() {
        myFixture.configureByFile("beforeValidationTest.xml");
        ValidationModelWrapper validationModelWrapper = setUpEditorAndGetModel();
        Scenario.Validation beforeModel = (Scenario.Validation) validationModelWrapper.retrieveModel();

        Scenario.Validation afterModel = new Scenario.Validation();
        afterModel.setFastForward(beforeModel.isFastForward());
        afterModel.getValidator().addAll(beforeModel.getValidator());

        afterModel.setEnabled(true);

        validationModelWrapper.updateModel(afterModel);
        validationModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterEditEnableValidation.xml");
    }

    public void testEditFastForwardValidation() {
        myFixture.configureByFile("beforeValidationTest.xml");
        ValidationModelWrapper validationModelWrapper = setUpEditorAndGetModel();
        Scenario.Validation beforeModel = (Scenario.Validation) validationModelWrapper.retrieveModel();

        Scenario.Validation afterModel = new Scenario.Validation();
        afterModel.setEnabled(beforeModel.isEnabled());
        afterModel.getValidator().addAll(beforeModel.getValidator());

        afterModel.setFastForward(true);

        validationModelWrapper.updateModel(afterModel);
        validationModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterEditFastForwardValidation.xml");
    }

    public void testToggleValidation() {
        myFixture.configureByFile("beforeValidationTest.xml");
        ValidationModelWrapper validationModelWrapper = setUpEditorAndGetModel();

        validationModelWrapper.setToggle(true);
        validationModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterToggleValidation.xml");
    }

    public void testAddValidator() {
        myFixture.configureByFile("beforeValidationTest.xml");
        ValidationModelWrapper validationModelWrapper = setUpEditorAndGetModel();

        Scenario.Validation.Validator validator = new Scenario.Validation.Validator();
        validator.setId("added");
        validator.setClazz("RulesValidator");

        validationModelWrapper.addValidator(validator);
        validationModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterAddValidator.xml");
    }

    public void testAddFirstValidator() {
        myFixture.configureByFile("beforeValidationTestEmpty.xml");
        ValidationModelWrapper validationModelWrapper = setUpEditorAndGetModel();

        Scenario.Validation.Validator validator = new Scenario.Validation.Validator();
        validator.setId("added");
        validator.setClazz("RulesValidator");

        validationModelWrapper.addValidator(validator);
        validationModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterAddFirstValidator.xml");
    }

    public void testReorderValidators() {
        myFixture.configureByFile("beforeValidationTest.xml");
        ValidationModelWrapper validationModelWrapper = setUpEditorAndGetModel();

        List<ModelWrapper> validatorModelList = validationModelWrapper.getChildrenModels();
        Collections.swap(validatorModelList, 1, 2);

        validationModelWrapper.setChildrenFromModels(validatorModelList);
        validationModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterReorderValidators.xml");
    }

    public void testDeleteValidator() {
        myFixture.configureByFile("beforeValidationTest.xml");
        ValidationModelWrapper validationModelWrapper = setUpEditorAndGetModel();

        List<ModelWrapper> validatorModelList = validationModelWrapper.getChildrenModels();

        validationModelWrapper.deleteChild(validatorModelList.get(1));
        validationModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterDeleteValidator.xml");
    }

    public void testDeleteAttachedValidator() {
        myFixture.configureByFile("beforeValidationTest.xml");
        ValidationModelWrapper validationModelWrapper = setUpEditorAndGetModel();

        List<ModelWrapper> validatorModelList = validationModelWrapper.getChildrenModels();

        validationModelWrapper.deleteChild(validatorModelList.get(2));
        validationModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterDeleteAttachedValidator.xml");
    }

    public void testDeleteAllValidators() {
        myFixture.configureByFile("beforeValidationTest.xml");
        ValidationModelWrapper validationModelWrapper = setUpEditorAndGetModel();

        List<ModelWrapper> validatorModelList = validationModelWrapper.getChildrenModels();

        for (ModelWrapper modelWrapper : validatorModelList) {
            validationModelWrapper.deleteChild(modelWrapper);
        }
        validationModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterDeleteAllValidators.xml");
    }

    //single validator
    public void testEditClassInSingleValidator() {
        myFixture.configureByFile("beforeValidationTest.xml");
        ValidationModelWrapper validationModelWrapper = setUpEditorAndGetModel();

        List<ModelWrapper> validatorModelList = validationModelWrapper.getChildrenModels();
        Scenario.Validation.Validator oldValidator = (Scenario.Validation.Validator) validatorModelList.get(1).retrieveModel();

        Scenario.Validation.Validator newValidator = new Scenario.Validation.Validator();
        newValidator.setId(oldValidator.getId());
        newValidator.getProperty().addAll(oldValidator.getProperty());

        newValidator.setClazz("RulesValidator");

        validatorModelList.get(1).updateModel(newValidator);
        validationModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterEditClassInSingleValidator.xml");
    }

    public void testEditIdInSingleValidator() {
        myFixture.configureByFile("beforeValidationTest.xml");
        ValidationModelWrapper validationModelWrapper = setUpEditorAndGetModel();

        List<ModelWrapper> validatorModelList = validationModelWrapper.getChildrenModels();
        Scenario.Validation.Validator oldValidator = (Scenario.Validation.Validator) validatorModelList.get(1).retrieveModel();

        Scenario.Validation.Validator newValidator = new Scenario.Validation.Validator();
        newValidator.setClazz(oldValidator.getClazz());
        newValidator.getProperty().addAll(oldValidator.getProperty());

        newValidator.setId("edited");

        validatorModelList.get(1).updateModel(newValidator);
        validationModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterEditIdInSingleValidator.xml");
    }

    public void testEditIdInSingleAttachedValidator() {
        myFixture.configureByFile("beforeValidationTest.xml");
        ValidationModelWrapper validationModelWrapper = setUpEditorAndGetModel();

        List<ModelWrapper> validatorModelList = validationModelWrapper.getChildrenModels();
        Scenario.Validation.Validator oldValidator = (Scenario.Validation.Validator) validatorModelList.get(2).retrieveModel();

        Scenario.Validation.Validator newValidator = new Scenario.Validation.Validator();
        newValidator.setClazz(oldValidator.getClazz());
        newValidator.getProperty().addAll(oldValidator.getProperty());

        newValidator.setId("edited");

        validatorModelList.get(2).updateModel(newValidator);
        validationModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterEditIdInSingleAttachedValidator.xml");
    }

    public void testEditPropertiesInSingleValidator() {
        myFixture.configureByFile("beforeValidationTest.xml");
        ValidationModelWrapper validationModelWrapper = setUpEditorAndGetModel();

        List<ModelWrapper> validatorModelList = validationModelWrapper.getChildrenModels();
        Scenario.Validation.Validator oldValidator = (Scenario.Validation.Validator) validatorModelList.get(1).retrieveModel();

        Scenario.Validation.Validator newValidator = new Scenario.Validation.Validator();
        newValidator.setClazz(oldValidator.getClazz());
        newValidator.setId(oldValidator.getId());

        oldValidator.getProperty().get(0).setValue("newValue");
        oldValidator.getProperty().get(1).setName("newName");
        oldValidator.getProperty().get(2).setValue("newValue");
        oldValidator.getProperty().get(2).setName("newName");

        newValidator.getProperty().addAll(oldValidator.getProperty());

        validatorModelList.get(1).updateModel(newValidator);
        validationModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterEditPropertiesInSingleValidator.xml");
    }

    public void testAddPropertyToSingleValidator() {
        myFixture.configureByFile("beforeValidationTest.xml");
        ValidationModelWrapper validationModelWrapper = setUpEditorAndGetModel();

        ValidatorModelWrapper validatorModelWrapper = (ValidatorModelWrapper) validationModelWrapper.getChildrenModels().get(0);

        Property property = new Property();
        property.setName("addedName");
        property.setValue("addedValue");

        validatorModelWrapper.addProperty(property);
        validatorModelWrapper.getGUI().commitChanges("test");
        myFixture.checkResultByFile("afterAddPropertyToSingleValidator.xml");
    }

    //wrong att.
    public void testValidationWithWrongAttributes() {
        myFixture.configureByFile("beforeValidationTest.xml");
        //add null Validator
        ValidationModelWrapper validationModelWrapper = setUpEditorAndGetModel();
        try {
            validationModelWrapper.addValidator(null);
            validationModelWrapper.getGUI().commitChanges("test");
            fail();
        } catch (NullPointerException expected) {
            // OK
        }
        myFixture.checkResultByFile("beforeValidationTest.xml");
        //delete null Validator
        ValidationModelWrapper validationModelWrapper2 = setUpEditorAndGetModel();
        try {
            validationModelWrapper2.deleteChild(null);
            validationModelWrapper2.getGUI().commitChanges("test");
            fail();
        } catch (NullPointerException expected) {
            // OK
        }
        myFixture.checkResultByFile("beforeValidationTest.xml");
        //reorder null validators
        ValidationModelWrapper validationModelWrapper3 = setUpEditorAndGetModel();
        try {
            validationModelWrapper3.setChildrenFromModels(null);
            validationModelWrapper3.getGUI().commitChanges("test");
            fail();
        } catch (NullPointerException expected) {
            // OK
        }
        myFixture.checkResultByFile("beforeValidationTest.xml");
        // add null Property to single Validator
        ValidationModelWrapper validationModelWrapper4 = setUpEditorAndGetModel();
        ValidatorModelWrapper validatorModelWrapper4 = (ValidatorModelWrapper) validationModelWrapper4.getChildrenModels().get(1);
        try {
            validatorModelWrapper4.addProperty(null);
            validatorModelWrapper4.getGUI().commitChanges("test");
            fail();
        } catch (ScenarioManagerException expected) {
            // OK
        }
        myFixture.checkResultByFile("beforeValidationTest.xml");
    }
}
