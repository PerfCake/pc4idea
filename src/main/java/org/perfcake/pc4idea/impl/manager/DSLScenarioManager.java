package org.perfcake.pc4idea.impl.manager;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.manager.ScenarioManager;
import org.perfcake.pc4idea.api.manager.ScenarioManagerException;
import org.perfcake.pc4idea.api.util.Messages;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 27.2.2015
 */
public class DSLScenarioManager implements ScenarioManager {
    private static final Logger LOG = Logger.getInstance(DSLScenarioManager.class);

    private VirtualFile file;
    private Project project;

    public DSLScenarioManager(@Nullable VirtualFile file, @NotNull Project project){
        this.file = file;
        this.project = project;
    }

    @Override
    public void createScenario(@NotNull VirtualFile directoryFile,
                               @NotNull String name,
                               @NotNull Scenario model) throws ScenarioManagerException {
        throw new ScenarioManagerException(Messages.Exception.UNSUPPORTED_OPERATION);
    }

    @Override
    public Scenario retrieveScenario() throws ScenarioManagerException {
        throw new ScenarioManagerException(Messages.Exception.UNSUPPORTED_OPERATION);
    }

    @Override
    public void updateScenario(@NotNull Scenario model,
                               @NotNull String actionCommand) throws ScenarioManagerException {
        throw new ScenarioManagerException(Messages.Exception.UNSUPPORTED_OPERATION);
    }

    @Override
    public void deleteScenario() throws ScenarioManagerException {
        throw new ScenarioManagerException(Messages.Exception.UNSUPPORTED_OPERATION);
    }
}
