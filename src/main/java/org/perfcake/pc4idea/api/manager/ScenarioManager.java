package org.perfcake.pc4idea.api.manager;

import org.perfcake.model.Scenario;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 27.2.2015                         TODO documentation
 */
public interface ScenarioManager {

    public void createScenario(String name, Scenario model) throws ScenarioManagerException;

    public Scenario retrieveScenario() throws ScenarioManagerException;

    public void updateScenario(Scenario scenarioModel, String actionCommand) throws ScenarioManagerException;
}
