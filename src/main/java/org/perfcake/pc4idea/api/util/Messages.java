package org.perfcake.pc4idea.api.util;

import java.util.ResourceBundle;

/**
 * Created by Stanislav Kaleta on 3/8/15.
 */
public class Messages {
    private static final ResourceBundle commandBundle = ResourceBundle.getBundle("strings/commandStrings");
    private static final ResourceBundle logBundle = ResourceBundle.getBundle("strings/logStrings");
    private static final ResourceBundle labelBundle = ResourceBundle.getBundle("strings/labelStrings");
    private static final ResourceBundle dialogBundle = ResourceBundle.getBundle("strings/dialogStrings");
    private static final ResourceBundle titleBundle = ResourceBundle.getBundle("strings/titleStrings");
    private static final ResourceBundle exceptionBundle = ResourceBundle.getBundle("strings/exceptionStrings");
    private static final ResourceBundle scenarioBundle = ResourceBundle.getBundle("strings/scenarioStrings");


    public static class Command {
        public static final String ADD = commandBundle.getString("ADD");
        public static final String EDIT = commandBundle.getString("EDIT");
        public static final String REORDER = commandBundle.getString("REORDER");
        public static final String DEL = commandBundle.getString("DEL");
        public static final String ENABLE = commandBundle.getString("ENABLE");
        public static final String DISABLE = commandBundle.getString("DISABLE");
        public static final String ATTACH = commandBundle.getString("ATTACH");
        public static final String UP = commandBundle.getString("UP");
        public static final String DOWN = commandBundle.getString("DOWN");
        public static final String CREATE = commandBundle.getString("CREATE");
    }

    public static class Label {
        public static final String SCENARIO_INVALID = labelBundle.getString("SCENARIO_INVALID");
        public static final String SCENARIO_INVALID_HINT = labelBundle.getString("SCENARIO_INVALID_HINT");
    }

    public static class Log {
        public static final String[] UNSUPPORTED_MODULE = logBundle.getString("UNSUPPORTED_MODULE").split("@");
        public static final String INVALID_TABLE_MODEL = logBundle.getString("INVALID_TABLE_MODEL");
        public static final String[] NOT_DIR_FILE = logBundle.getString("NOT_DIR_FILE").split("@");

    }

    public static class Dialog {
        public static final String UNSUPPORTED_MODULE = dialogBundle.getString("UNSUPPORTED_MODULE");
        public static final String[] FILE_EXISTS = dialogBundle.getString("FILE_EXISTS").split("@");
    }

    public static class Title {
        public static final String UNSUPPORTED_MODULE = titleBundle.getString("UNSUPPORTED_MODULE");
        public static final String FILE_EXISTS = titleBundle.getString("FILE_EXISTS");

    }

    public static class Exception {
        public static final String[] NULL_MODULE = exceptionBundle.getString("NULL_MODULE").split("@");
        public static final String ADD_NULL_VALIDATOR_REF = exceptionBundle.getString("ADD_NULL_VALIDATOR_REF");
        public static final String[] INVALID_VALIDATOR_ID = exceptionBundle.getString("INVALID_VALIDATOR_ID").split("@");
        public static final String ADD_NULL_MESSAGE = exceptionBundle.getString("ADD_NULL_MESSAGE");
        public static final String ADD_NULL_DESTINATION = exceptionBundle.getString("ADD_NULL_DESTINATION");
        public static final String ADD_NULL_REPORTER = exceptionBundle.getString("ADD_NULL_REPORTER");
        public static final String ADD_NULL_VALIDATOR = exceptionBundle.getString("ADD_NULL_VALIDATOR");
        public static final String[] UNABLE_TO_CREATE_SCENARIO = exceptionBundle.getString("UNABLE_TO_CREATE_SCENARIO").split("@");
        public static final String NULL_DOCUMENT = exceptionBundle.getString("NULL_DOCUMENT");
        public static final String UNSUPPORTED_OPERATION = exceptionBundle.getString("UNSUPPORTED_OPERATION");
    }

    public static class Scenario {
        public static final String GENERATOR = scenarioBundle.getString("GENERATOR");
        public static final String SENDER = scenarioBundle.getString("SENDER");
        public static final String MESSAGES = scenarioBundle.getString("MESSAGES");
        public static final String MESSAGE = scenarioBundle.getString("MESSAGE");
        public static final String VALIDATION = scenarioBundle.getString("VALIDATION");
        public static final String VALIDATOR = scenarioBundle.getString("VALIDATOR");
        public static final String REPORTING = scenarioBundle.getString("REPORTING");
        public static final String REPORTER = scenarioBundle.getString("REPORTER");
        public static final String DESTINATION = scenarioBundle.getString("DESTINATION");
        public static final String PROPERTIES = scenarioBundle.getString("PROPERTIES");
        public static final String PROPERTY = scenarioBundle.getString("PROPERTY");
        public static final String PERIOD = scenarioBundle.getString("PERIOD");
        public static final String HEADER = scenarioBundle.getString("HEADER");
        public static final String SCENARIO = scenarioBundle.getString("SCENARIO");
    }
}
