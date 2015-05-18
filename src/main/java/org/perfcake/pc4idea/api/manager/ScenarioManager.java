package org.perfcake.pc4idea.api.manager;

import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.perfcake.model.Scenario;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 27.2.2015
 */
public interface ScenarioManager {
    /**
     *  creates new scenario file and updates it according to specified model
     *
     * @param directoryFile directory where scenario will be created
     * @param name name of new scenario
     * @param model model of scenario
     * @throws ScenarioManagerException
     */
    public void createScenario(@NotNull VirtualFile directoryFile,
                               @NotNull String name,
                               @NotNull Scenario model) throws ScenarioManagerException;

    /**
     *  retrieve model from scenario file
     *
     * @return model of scenario
     * @throws ScenarioManagerException if error occurred while retrieving model
     */
    public Scenario retrieveScenario() throws ScenarioManagerException;

    /**
     *  updates scenario according to specified model
     *
     * @param model model which will update scenario
     * @param actionCommand name of the modifying action
     * @throws ScenarioManagerException if error occurred while updating scenario
     */
    public void updateScenario(@NotNull Scenario model,
                               @NotNull String actionCommand) throws ScenarioManagerException;

    /**
     *  deletes scenario
     *
     * @throws ScenarioManagerException
     */
    public void deleteScenario() throws ScenarioManagerException;
}
