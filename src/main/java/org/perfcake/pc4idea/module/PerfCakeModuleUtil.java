package org.perfcake.pc4idea.module;

import com.intellij.icons.AllIcons;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 4.12.2014
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

    public static boolean isPerfCakeModule(Module module) {
        return PerfCakeModuleType.isOfType(module);
    }

    public static String[] getPerfCakeModuleDirsUri(Module module) {
        String scenariosDirUri = null;
        String messagesDirUri = null;
        String libDirUri = null;
        if (module != null) {
            if (isPerfCakeModule(module)) {
                VirtualFile moduleFile = module.getModuleFile();
                if (moduleFile != null) {
                    for (VirtualFile file : moduleFile.getParent().getChildren()) {
                        if (file.getName().equals("scenarios")) {
                            scenariosDirUri = file.getPath() + "/";
                        }
                        if (file.getName().equals("messages")) {
                            messagesDirUri = file.getPath() + "/";
                        }
                        if (file.getName().equals("lib")) {
                            libDirUri = file.getPath() + "/";
                        }
                    }
                }
            }
        }
        return new String[]{scenariosDirUri, messagesDirUri, libDirUri};
    }

}