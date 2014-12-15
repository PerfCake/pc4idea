package org.perfcake.pc4idea.execution;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessTerminatedListener;
import com.intellij.execution.process.RunnerMediator;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 13.12.2014
 */
public class PCExecutionState extends CommandLineState{
    private VirtualFile scenario;
    public PCExecutionState(ExecutionEnvironment environment, VirtualFile scenario) {
        super(environment);
        this.scenario = scenario;
    }

    @NotNull
    @Override
    protected ProcessHandler startProcess() throws ExecutionException {

        GeneralCommandLine commandLine = new GeneralCommandLine(){
            @Override
            public Process createProcess() throws ExecutionException {
                return new PCProcessTest();
            }
        };
        commandLine.setExePath(scenario.getPath());
        ProcessHandler processHandler = new OSProcessHandler(commandLine.createProcess(),commandLine.getCommandLineString());


        ProcessTerminatedListener.attach(processHandler);


        return processHandler;
    }
}
