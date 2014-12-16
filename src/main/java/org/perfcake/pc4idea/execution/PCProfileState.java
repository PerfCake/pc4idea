package org.perfcake.pc4idea.execution;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.BaseOSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.process.ProcessTerminatedListener;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.encoding.EncodingManager;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 13.12.2014
 */
public class PCProfileState extends CommandLineState{
    private VirtualFile scenario;
    public PCProfileState(ExecutionEnvironment environment, VirtualFile scenario) {
        super(environment);
        this.scenario = scenario;
    }

    @NotNull
    @Override
    protected ProcessHandler startProcess() throws ExecutionException {
        GeneralCommandLine commandLine = new GeneralCommandLine(){
            @Override
            public Process createProcess() throws ExecutionException {
                return new PCProcess(scenario);
            }
        };
        commandLine.setExePath(scenario.getPath());
        ProcessHandler processHandler = new BaseOSProcessHandler(commandLine.createProcess(),commandLine.getCommandLineString(), EncodingManager.getInstance().getDefaultCharset());
        ProcessTerminatedListener.attach(processHandler);

        return processHandler;
    }
}
