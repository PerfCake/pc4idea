package org.perfcake.pc4idea.execution;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.filters.TextConsoleBuilderFactoryImpl;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowId;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
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

        ConsoleView console = TextConsoleBuilderFactoryImpl.getInstance().createBuilder(executionEnvironment.getProject()).getConsole();//.getInstance().createBuilder(executionEnvironment.getProject()).getConsole();
        ToolWindow toolWindow = ToolWindowManager.getInstance(executionEnvironment.getProject()).getToolWindow(ToolWindowId.RUN);

        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(console.getComponent(), scenarioFile.getName(), false);
        toolWindow.getContentManager().addContent(content);
        toolWindow.getContentManager().setSelectedContent(content);

        //console.print("aaa", ConsoleViewContentType.NORMAL_OUTPUT);

        //console.attachToProcess(new OSProcessHandler(new PCProcess(System.out),"a"));

//        OutputStream os = new BufferedOutputStream();       console.pr
//
//        BufferedOutputStream os = new BufferedOutputStream();
//        PrintStream scenarioOutput = new PrintStream(,true);

        Scenario scenario = null;
        try {
            System.out.println("PATH: "+scenarioFile.getPath());
            scenario = new ScenarioLoader().load(scenarioFile.getPath());
            scenario.init();
            scenario.run();


        } catch (PerfCakeException e) {
            System.out.println("PCEx.");
            e.printStackTrace();
        }


        /*TODO*/

        return null;  /*TODO*/
    }


//    @Nullable
//    @Override
//    protected ConsoleView createConsole(@NotNull final Executor executor) throws ExecutionException {
//        return TextConsoleBuilderFactory.getInstance().createBuilder(executionEnvironment.getProject()).getConsole();
//    }
//
//    @NotNull
//    @Override
//    protected ProcessHandler startProcess() throws ExecutionException {
//        if (name == null) {
//             LOG.error("Scenario name is null");
//             return null;
//         }
//        //ProcessHandler handler = new OSProcessHandler(createConsole(executionEnvironment.getExecutor()));
//
//
//        setConsoleBuilder(TextConsoleBuilderFactory.getInstance().createBuilder(executionEnvironment.getProject()));
//        this.getConsoleBuilder().getConsole().print("RLY", ConsoleViewContentType.NORMAL_OUTPUT);
//        PCProcess process = new PCProcess(System.out);
//        ProcessHandler handler = new OSProcessHandler(process,System.out.toString());
//
//
////        this.createConsole(executionEnvironment.getExecutor());
//
//        return  handler;
//    }
}
