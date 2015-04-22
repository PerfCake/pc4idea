package org.perfcake.pc4idea.impl.run.execution;

import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;
import org.perfcake.PerfCakeConst;
import org.perfcake.PerfCakeException;
import org.perfcake.pc4idea.impl.run.configuration.PerfCakeRunConfiguration;
import org.perfcake.scenario.Scenario;
import org.perfcake.scenario.ScenarioLoader;

import javax.xml.bind.JAXBException;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Thread that runs PerfCake scenario using PerfCake API
 * Created by miron on 9.3.2014.  + me: paths, plugin dir
 */
public class ScenarioThread implements Runnable {
    private static final Logger LOG = Logger.getInstance(ScenarioThread.class);
    Scenario scenario = null;
    private PerfCakeRunConfiguration runConfiguration;
    private PrintStream scenarioOutput;
    private PrintStream scenarioErrOutput;


    public ScenarioThread(@NotNull PerfCakeRunConfiguration runConfiguration, @NotNull OutputStream scenarioOutput, @NotNull OutputStream scenarioErrOutput) {
        this.runConfiguration = runConfiguration;
        this.scenarioOutput = new PrintStream(scenarioOutput, true);
        this.scenarioErrOutput = new PrintStream(scenarioErrOutput, true);
    }

    @Override
    public void run() {
        PrintStream systemOut = System.out;
        System.setOut(scenarioOutput);

        //redirect System error (perfcake) to scenarioErrOutput
        PrintStream errOut = System.err;
        System.setErr(scenarioErrOutput);

        setPerfCakeProperties();

        //run scenario
        try {
            //fix UnknownHostException for Windows
            String path = runConfiguration.getPaths()[0];
            path = path.startsWith("C:") ? path.substring(2) : path;
            scenario = ScenarioLoader.load(path);
            scenario.init();
            scenario.run();
        } catch (Exception e) {
            LOG.info("Run error", e);
            System.err.println("Run Error: " + e);
            Throwable cause = e.getCause();
            if (cause != null && cause instanceof JAXBException) {
                System.err.println("JAXB problem detected: " + cause);
            } else {
                System.err.println("Unknown cause. Showing stacktrace for cause detection:\n");
                e.printStackTrace();
            }

        } finally {
            if (scenario != null) try {
                scenario.close();
            } catch (PerfCakeException e) {
                System.err.println("Scenario close error: " + e.getMessage() + ". Cause: " + (e.getCause() == null ? "" : e.getCause().getMessage()) + "\nStackTrace:");
                e.printStackTrace();
            } finally {
                scenario = null;
            }
        }


        //send message to console thread, that it can stop
        System.out.println(Constants.SCENARIO_FINISHED_MARK);

        System.setOut(systemOut);
        System.setErr(errOut);

        scenarioOutput.close();
        scenarioErrOutput.close();
    }

    /**
     * Creates arguments for PerfCake to run the scenario
     */
    private void setPerfCakeProperties() {
        String scenariosDirPath = runConfiguration.getPaths()[1];
        String messagesDirPath = runConfiguration.getPaths()[2];
        String libDirPath = runConfiguration.getPaths()[3];

        //fix UnknownHostException for Windows
        scenariosDirPath = scenariosDirPath.startsWith("C:") ?
                scenariosDirPath.substring(2) : scenariosDirPath;
        messagesDirPath = messagesDirPath.startsWith("C:") ?
                messagesDirPath.substring(2) : messagesDirPath;
        libDirPath = libDirPath.startsWith("C:") ?
                libDirPath.substring(2) : libDirPath;

        System.setProperty(PerfCakeConst.SCENARIOS_DIR_PROPERTY, scenariosDirPath);
        System.setProperty(PerfCakeConst.MESSAGES_DIR_PROPERTY, messagesDirPath);
        System.setProperty(PerfCakeConst.PLUGINS_DIR_PROPERTY, libDirPath);
    }

    public void stop() {
        if (scenario != null) {
            System.out.println("Stopping scenario...");
            scenario.stop();
        }
    }
}
