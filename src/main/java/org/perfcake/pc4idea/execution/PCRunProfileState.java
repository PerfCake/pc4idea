package org.perfcake.pc4idea.execution;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.perfcake.PerfCakeException;
import org.perfcake.scenario.Scenario;
import org.perfcake.scenario.ScenarioLoader;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 6.12.2014
 */
public class PCRunProfileState implements RunProfileState {
    private static final Logger LOG = Logger.getInstance("#org.perfcake.pc4idea.execution.PCRunProfileState");

    private ExecutionEnvironment executionEnvironment;
    private VirtualFile scenarioFile;

    public PCRunProfileState(ExecutionEnvironment executionEnvironment, VirtualFile scenarioFile){
        this.scenarioFile = scenarioFile;
        this.executionEnvironment = executionEnvironment;
    }

    @Nullable
    @Override
    public ExecutionResult execute(Executor executor, @NotNull ProgramRunner programRunner) throws ExecutionException {
         if (scenarioFile == null) {
             LOG.error("Scenario name is null");
             return null;
         }
        /*TODO for testing purpose*/System.out.println("RUN: " + scenarioFile.getName());

        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                Scenario scenario = null;
                try {
                    System.out.println("PATH: "+scenarioFile.getPath());
                    scenario = ScenarioLoader.load(scenarioFile.getPath());
                    scenario.init();
                    scenario.run();

                } catch (PerfCakeException e) {
                    Messages.showInfoMessage(e.getMessage()+"\n"+e.getCause().toString(),"PerfCakeException");
                    System.out.println("PCEx.");
                    e.printStackTrace();
                }

            }
        });






        /*TODO*/

        return null;  /*TODO*/
    }
}
