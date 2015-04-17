package org.perfcake.pc4idea.impl.manager;

import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.manager.ScenarioManager;
import org.perfcake.pc4idea.api.manager.ScenarioManagerException;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 27.2.2015
 */
public class DSLScenarioManager implements ScenarioManager {
    private VirtualFile file;

    public DSLScenarioManager(@NotNull VirtualFile file){
        this.file = file;
    }

    @Override
    public void createScenario(String name, Scenario model) throws ScenarioManagerException {

    }

    @Override
    public Scenario retrieveScenario() {
        return null;
    }

    @Override
    public void updateScenario(Scenario scenarioModel, String actionCommand) {

    }


}
