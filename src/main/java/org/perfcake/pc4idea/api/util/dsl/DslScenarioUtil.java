package org.perfcake.pc4idea.api.util.dsl;

import org.perfcake.model.Scenario;

/**
 * Created by Stanislav Kaleta on 5/14/15.
 */
public class DslScenarioUtil {

    public static String getDslScenarioFrom(Scenario model, String name){
        ScenarioBuilder builder = new ScenarioBuilder();
        return builder.buildScenario(model, name);
    }

    public static Scenario getModelFrom(String scenario){
        ScenarioParser builder = new ScenarioParser();
        return builder.parseScenario(scenario);
    }
}
