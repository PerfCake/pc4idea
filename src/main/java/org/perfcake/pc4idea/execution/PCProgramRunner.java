package org.perfcake.pc4idea.execution;

import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.runners.DefaultProgramRunner;
import org.jetbrains.annotations.NotNull;
import org.perfcake.pc4idea.configuration.PCRunConfiguration;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 6.12.2014
 */
public class PCProgramRunner extends DefaultProgramRunner {
    @NotNull
    @Override
    public String getRunnerId() {
        return "PerfCake";
    }

    @Override
    public boolean canRun(@NotNull String s, @NotNull RunProfile runProfile) {
        return s.equals("Run") && runProfile instanceof PCRunConfiguration && ((PCRunConfiguration) runProfile).isInitialized();
    }

}
