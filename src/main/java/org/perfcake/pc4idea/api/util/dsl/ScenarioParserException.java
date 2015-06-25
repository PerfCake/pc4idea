package org.perfcake.pc4idea.api.util.dsl;

/**
 * Created by Stanislav Kaleta on 5/14/15.
 */
public class ScenarioParserException extends RuntimeException {
    public ScenarioParserException(String message) {
        super(message);
    }

    public ScenarioParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScenarioParserException(Throwable cause) {
        super(cause);
    }
}
