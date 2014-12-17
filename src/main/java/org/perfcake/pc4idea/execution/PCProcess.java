package org.perfcake.pc4idea.execution;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import org.perfcake.PerfCakeException;
import org.perfcake.scenario.Scenario;
import org.perfcake.scenario.ScenarioLoader;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 13.12.2014
 */
public class PCProcess extends Process{
    private static final Logger LOG = Logger.getInstance("#org.perfcake.pc4idea.execution.PCProcess");
    private InputStream inputStream;
    private InputStream errorStream;
    private PipedOutputStream pcSystemOut;
    private PipedOutputStream pcSystemErr;
    private VirtualFile scenarioFile;
    private Scenario scenario;
    private int exitCode;


    public PCProcess(VirtualFile scenarioFile) {
        this.scenarioFile = scenarioFile;
        scenario = null;
        exitCode = -1;

        pcSystemOut = new PipedOutputStream();
        pcSystemErr = new PipedOutputStream();

        try {
            errorStream = new PipedInputStream(pcSystemErr);
            inputStream = new PipedInputStream(pcSystemOut);
        } catch (IOException e) {
            LOG.error("Error connecting streams",e.getCause());
            Notifications.Bus.notify(new Notification("PerfCake Plugin", "IOException",
                    "Error interconnecting streams. Scenario will not be executed.",
                    NotificationType.ERROR), null);
        }
    }
    @Override
    public OutputStream getOutputStream() {
        return null;
    }

    @Override
    public InputStream getInputStream() {
        return inputStream;
    }

    @Override
    public InputStream getErrorStream() {
        return errorStream;
    }

    @Override
    public int waitFor() throws InterruptedException {
        PrintStream originalSystemOut = System.out;
        PrintStream originalSystemErr = System.err;
        System.setOut(new PrintStream(pcSystemOut));
        System.setErr(new PrintStream(pcSystemErr));

        if (scenarioFile.getName().equals("test.xml")) {
            for (int i = 0; i < 10; i++) {
                System.out.println("run " + i);
                Thread.sleep(500);
            }
            System.out.println("run finished");
            System.err.println("ERROR");
        } else {
            try {
                System.out.println("PATH: " + scenarioFile.getPath());
                scenario = ScenarioLoader.load(scenarioFile.getPath());
                scenario.init();
                scenario.run();
                exitCode = 0;
            } catch (PerfCakeException e) {
                e.printStackTrace();
            }
            /*TODO scenario close*/
        }
        System.setOut(originalSystemOut);
        System.setErr(originalSystemErr);

        return exitCode;
    }

    @Override
    public int exitValue() {
        return exitCode;
    }

    @Override
    public void destroy() {
        /*TODO stop scenario + close the streams*/
    }
}
