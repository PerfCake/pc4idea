package org.perfcake.pc4idea.editor.manager;

import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.perfcake.model.Scenario;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 27.2.2015
 */
public class DSLScenarioManager implements ScenarioManager{
    private VirtualFile file;

    public DSLScenarioManager(@NotNull VirtualFile file){
        this.file = file;
    }

    @Override
    public Scenario loadScenario() {
        return null;
    }

    @Override
    public void saveScenario(Scenario scenarioModel, String actionCommand) {

    }
}
