package org.perfcake.pc4idea.api.manager;

/**
 * Created by Stanislav Kaleta on 3/7/15.              TODO documentation
 */
public class ScenarioManagerException extends RuntimeException {

    public ScenarioManagerException(String message) {
        super(message);
    }

    public ScenarioManagerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScenarioManagerException(Throwable cause) {
        super(cause);
    }
}
