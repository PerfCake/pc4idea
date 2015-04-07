package org.perfcake.pc4idea.editor.manager;

import org.perfcake.model.Scenario;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 27.2.2015
 */
public interface ScenarioManager {

    public Scenario loadScenario() throws ScenarioManagerException;

    public void saveScenario(Scenario scenarioModel, String actionCommand) throws ScenarioManagerException;
}
