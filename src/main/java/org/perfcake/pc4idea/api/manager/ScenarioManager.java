package org.perfcake.pc4idea.api.manager;

import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.perfcake.model.Scenario;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 27.2.2015                         TODO documentation
 */
public interface ScenarioManager {
    /**
     *
     * @param directoryFile
     * @param name
     * @param model
     * @throws ScenarioManagerException
     */
    public void createScenario(@NotNull VirtualFile directoryFile,
                               @NotNull String name,
                               @NotNull Scenario model) throws ScenarioManagerException;

    /**
     *
     * @return
     * @throws ScenarioManagerException
     */
    public Scenario retrieveScenario() throws ScenarioManagerException;

    /**
     *
     * @param model
     * @param actionCommand
     * @throws ScenarioManagerException
     */
    public void updateScenario(@NotNull Scenario model,
                               @NotNull String actionCommand) throws ScenarioManagerException;

    /**
     *
     * @throws ScenarioManagerException
     */
    public void deleteScenario() throws ScenarioManagerException;
}
