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
    }

    public static class Label {
        public static final String SCENARIO_INVALID = labelBundle.getString("SCENARIO_INVALID");
    }

    public static class Log {
        public static final String UNSUPPORTED_MODULE = logBundle.getString("UNSUPPORTED_MODULE");
    }

    public static class Dialog {
        public static final String UNSUPPORTED_MODULE = dialogBundle.getString("UNSUPPORTED_MODULE");
    }

    public static class Title {
        public static final String UNSUPPORTED_MODULE = titleBundle.getString("UNSUPPORTED_MODULE");
    }
}
