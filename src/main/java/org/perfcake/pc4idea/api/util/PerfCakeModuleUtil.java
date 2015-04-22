package org.perfcake.pc4idea.api.util;

import com.intellij.icons.AllIcons;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.perfcake.pc4idea.impl.module.PerfCakeModuleType;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 4.12.2014                              TODO documentation
 */
public class PerfCakeModuleUtil {
    private static final Logger LOG = Logger.getInstance(PerfCakeModuleUtil.class);

    public static void createMessageFile(String uri, final Module module) {
        if (module == null) {
            //disabled in wizard and if null
            return;
        }

        final String fileName;
        if (uri == null) {
            LOG.info("URI is null. Message can't be created.");
            return;
        }
        if (!uri.contains("/")) {
            fileName = (uri.contains(".")) ? uri : uri + ".txt";
        } else {
            LOG.info("URI isn't local. Only local message can be created.");
            return;
        }

        VirtualFile messagesDirectory = null;
        String directoryError = "";
        if (isPerfCakeModule(module)) {
            VirtualFile moduleFile = module.getModuleFile();
            if (moduleFile != null) {
                boolean dirExists = false;
                for (VirtualFile file : moduleFile.getParent().getChildren()) {
                    if (file.getName().equals("messages")) {
                        dirExists = true;
                        boolean fileExists = false;
                        for (VirtualFile m : file.getChildren()) {
                            if (m.getName().equals(fileName)) {
                                fileExists = true;
                            }
                        }
                        if (!fileExists) {
                            messagesDirectory = file;
                        } else {
                            directoryError = "File " + fileName + " already exists in " + file.getName() + " directory.";
                        }
                    }
                }
                if (!dirExists) {
                    directoryError = "Can't find directory \"messages\".";
                }
            } else {
                directoryError = "Module file is null.";
            }
        } else {
            directoryError = "Module type isn't PerfCake";
        }

        if (messagesDirectory == null) {
            LOG.info("Not possible to create message: " + directoryError);
            return;
        }

        int result = Messages.showYesNoDialog("Message is local and doesn't exist in messages directory.\n" +
                        "Would you like to create empty file " + fileName + " in messages directory?", "Create Message?",
                AllIcons.General.QuestionDialog);
        if (result == 0) {
            final VirtualFile dir = messagesDirectory;
            ApplicationManager.getApplication().runWriteAction(new Runnable() {
                public void run() {
                    try {
                        dir.createChildData(PerfCakeModuleUtil.class, fileName);
                    } catch (IOException e) {
                        LOG.info(e.getMessage() + " Message will not be created!");
                        Notifications.Bus.notify(new Notification("PerfCake Plugin", "IOException",
                                e.getMessage() + " Message " + fileName + " will not be created!",
                                NotificationType.INFORMATION), module.getProject());
                    }
                }
            });
        }
    }

    public static VirtualFile findScenario(String scenarioName, String moduleName, Project project) {
        Module module = ModuleManager.getInstance(project).findModuleByName(moduleName);
        if (module == null) {
            return null;
        }

        VirtualFile scenariosDir = null;
        if (isPerfCakeModule(module)) {
            VirtualFile moduleFile = module.getModuleFile();
            if (moduleFile != null) {
                for (VirtualFile file : moduleFile.getParent().getChildren()) {
                    if (file.getName().equals("scenarios")) {
                        scenariosDir = file;
                    }
                }
            }
        }
        if (scenariosDir == null) {
            return null;
        }

        for (VirtualFile scenario : scenariosDir.getChildren()) {
            if (scenario.getName().equals(scenarioName)) {
                return scenario;
            }
        }
        return null;
    }


    /**
     * TODO doc.
     *
     * @param module
     * @param file
     * @return
     */
    public static boolean isPerfCakeScenarioFile(@NotNull Module module, @NotNull VirtualFile file){
        /*TODO dorobit poriadne + dsl*/
        if (file.getFileType() == StdFileTypes.XML && !StdFileTypes.XML.isBinary()){
            Document document = FileDocumentManager.getInstance().getDocument(file);
            if (document != null){
                String text = document.getText();
                if (text.contains("<scenario xmlns=\"urn:perfcake:scenario:4.0\">")) {
                    return true;
                }
            }
        }

        if (file.getName().contains(".dsl")){/*TODO urn or someting*/
            return true;
        }
        return false;
    }



    /**
     * TODO doc.
     *
     * @param module
     * @return
     */
    public static boolean isPerfCakeModule(@NotNull Module module) {
        return PerfCakeModuleType.isOfType(module);
    }

    /**
     * Finds PerfCake related directories scenarios, messages and lib for the module and
     * returns them as VirtualFiles.
     *
     * @param module for which to find directories
     * @return array of directories as VirtualFile, where index 0 is "scenarios" directory,
     * index 1 is "messages" directory and index 2 is "lib" directory
     */
    public static VirtualFile[] findPerfCakeModuleDirs(@NotNull Module module) {
        VirtualFile scenariosDir = null;
        VirtualFile messagesDir = null;
        VirtualFile libDir = null;

        if (isPerfCakeModule(module)) {
            VirtualFile moduleFile = module.getModuleFile();
            if (moduleFile != null) {
                for (VirtualFile file : moduleFile.getParent().getChildren()) {
                    if (file.getName().equals("scenarios")) {
                        scenariosDir = file;
                    }
                    if (file.getName().equals("messages")) {
                        messagesDir = file;
                    }
                    if (file.getName().equals("lib")) {
                        libDir = file;
                    }
                }
            }
        }

        if (scenariosDir == null) {
            LOG.error("unable to find \"sceanrios\" directory in module root directory");
        }
        if (messagesDir == null) {
            LOG.error("unable to find \"messages\" directory in module root directory");
        }
        if (libDir == null) {
            LOG.error("unable to find \"lib\" directory in module root directory");
        }
        return new VirtualFile[]{scenariosDir, messagesDir, libDir};
    }
}