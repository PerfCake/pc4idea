package org.perfcake.pc4idea.execution;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 6.12.2014
 */
public class PCRunProfileState implements RunProfileState {
    private static final Logger LOG = Logger.getInstance("#org.perfcake.pc4idea.execution.PCRunProfileState");

    private Project project;
    private String name;

    public PCRunProfileState(Project project, String name){
        this.name = name;
        this.project = project;
    }

    @Nullable
    @Override
    public ExecutionResult execute(Executor executor, @NotNull ProgramRunner programRunner) throws ExecutionException {
         if (name == null) {
             LOG.error("Scenario name is null");
             return null;
         }
        /*TODO for testing purpose*/System.out.println("RUN: " + name);



        /*TODO*/

        return null;  /*TODO*/
    }
}
