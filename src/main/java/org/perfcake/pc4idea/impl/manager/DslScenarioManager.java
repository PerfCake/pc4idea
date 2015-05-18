package org.perfcake.pc4idea.impl.manager;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.CommandProcessor;
import com.intellij.openapi.command.UndoConfirmationPolicy;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileTypes.ex.FileTypeChooser;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.perfcake.model.Scenario;
import org.perfcake.pc4idea.api.manager.ScenarioManager;
import org.perfcake.pc4idea.api.manager.ScenarioManagerException;
import org.perfcake.pc4idea.api.util.Messages;
import org.perfcake.pc4idea.api.util.dsl.DslScenarioUtil;
import org.perfcake.pc4idea.api.util.dsl.ScenarioParserException;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 27.2.2015
 */
public class DslScenarioManager implements ScenarioManager {
    private static final Logger LOG = Logger.getInstance(DslScenarioManager.class);

    private VirtualFile file;
    private Project project;

    public DslScenarioManager(@NotNull VirtualFile file, @NotNull Project project){
        this.file = file;
        this.project = project;
    }

    public DslScenarioManager(@NotNull Project project){
        this.file = null ;
        this.project = project;
    }

    @Override
    public void createScenario(@NotNull VirtualFile directoryFile,
                               @NotNull String name,
                               @NotNull Scenario model) throws ScenarioManagerException {
        if (!directoryFile.isDirectory()){
            String wrongFileName = directoryFile.getName();
            directoryFile = directoryFile.getParent();
            String[] logMsg = Messages.Log.NOT_DIR_FILE;
            LOG.warn(logMsg[0] + wrongFileName + logMsg[1] + directoryFile.getName() + logMsg[2]);
        }
        for (VirtualFile vf : directoryFile.getChildren()) {
            if (vf.getName().equals(name)) {
                String[] msg = Messages.Dialog.FILE_EXISTS;
                int result = com.intellij.openapi.ui.Messages.showOkCancelDialog(project,
                        msg[0] + name + msg[1],
                        Messages.Title.FILE_EXISTS,
                        AllIcons.General.WarningDialog);
                if (result != 0) {
                    return;
                }
            }
        }

        name = name + ".dsl";

        final String finalName = name;
        final VirtualFile finalDirectoryFile = directoryFile;
        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            @Override
            public void run() {
                try {
                    file = finalDirectoryFile.createChildData(DslScenarioManager.this, finalName);
                    if (FileDocumentManager.getInstance().getDocument(file) == null){
                        FileTypeChooser.associateFileType("*.dsl");
                    }
                    DslScenarioManager.this.updateScenario(model,
                            Messages.Command.CREATE + " " + Messages.Scenario.SCENARIO);
                    Document document = FileDocumentManager.getInstance().getDocument(file);
                    if (document != null) {
                        FileDocumentManager.getInstance().saveDocument(document);
                    }
                    FileEditorManager.getInstance(project).openFile(file, true);
                } catch (IOException e) {
                    String[] msg = Messages.Exception.UNABLE_TO_CREATE_SCENARIO;
                    LOG.warn(msg[0] + finalName + msg[1]);
                    throw new ScenarioManagerException(msg[0] + finalName + msg[1]);
                }
            }
        });

    }

    @Override
    public Scenario retrieveScenario() throws ScenarioManagerException {
        if (file == null){
            String msg = Messages.Exception.NULL_VIRTUAL_FILE;
            LOG.error(msg);
            throw new ScenarioManagerException(msg);
        }

        Scenario scenarioModel = null;
        try {
            Document document = FileDocumentManager.getInstance().getDocument(file);
            if (document == null){
                document = FileDocumentManager.getInstance().getDocument(file);
                if (document == null){
                    throw new ScenarioManagerException("document is null!");
                }
            }
            scenarioModel = DslScenarioUtil.getModelFrom(document.getText());
        } catch (ScenarioParserException e){
            System.out.println(e.getMessage());
            LOG.warn(e.getMessage());
            throw new ScenarioManagerException(e);
        }

        return scenarioModel;
    }

    @Override
    public void updateScenario(@NotNull Scenario model,
                               @NotNull String actionCommand) throws ScenarioManagerException {
        if (file == null){
            String msg = Messages.Exception.NULL_VIRTUAL_FILE;
            LOG.error(msg);
            throw new ScenarioManagerException(msg);
        }

        Document document = FileDocumentManager.getInstance().getDocument(file);
        if (document == null){
            String msg = Messages.Exception.NULL_DOCUMENT;
            LOG.error(msg);
            throw new ScenarioManagerException(msg);
        }

        CommandProcessor.getInstance().executeCommand(project, new Runnable() {
            @Override
            public void run() {
                ApplicationManager.getApplication().runWriteAction(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String name = file.getName().substring(0,file.getName().length() - 4);
                            String scenario = DslScenarioUtil.getDslScenarioFrom(model, name);
                            if (scenario != null && !scenario.trim().isEmpty()) {
                                /*TODO for testing purpose*/System.out.println("TEST LOG: scenario successfully saved: " + actionCommand);
                                document.setText(scenario);
                            }
                        } catch (Exception e) {
                            LOG.warn(e.getMessage());
                            throw new ScenarioManagerException(e);
                        }
                    }
                });
            }
        }, actionCommand, null, UndoConfirmationPolicy.DEFAULT, document);


    }

    @Override
    public void deleteScenario() throws ScenarioManagerException {
        throw new ScenarioManagerException(Messages.Exception.UNSUPPORTED_OPERATION);
    }
}
