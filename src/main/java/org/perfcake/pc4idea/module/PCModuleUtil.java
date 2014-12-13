package org.perfcake.pc4idea.module;

import com.intellij.icons.AllIcons;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import org.perfcake.model.Scenario;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 4.12.2014
 */
public class PCModuleUtil {
    private static final Logger LOG = Logger.getInstance("#org.perfcake.pc4idea.module.PCModuleUtil");

    public static void createMessageFile(Scenario.Messages.Message message, final Module module) {
        if (module == null){
            //disabled in wizard and if null
            return;
        }

        final String fileName;
        String uri = message.getUri();
        if (uri == null){
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
        if (isPerfCakeModule(module)){
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
        if (result == 0){
            final VirtualFile dir = messagesDirectory;
            ApplicationManager.getApplication().runWriteAction(new Runnable() {
                public void run() {
                    try {
                        dir.createChildData(PCModuleUtil.class, fileName);
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

    public static VirtualFile findScenario(String fileName, Module module){
        VirtualFile scenariosDir = null;
        if (isPerfCakeModule(module)){
            VirtualFile moduleFile = module.getModuleFile();
            if (moduleFile != null) {
                for (VirtualFile file : moduleFile.getParent().getChildren()) {
                    if (file.getName().equals("scenarios")) {
                        scenariosDir = file;
                    }
                }
            }
        }
        if (scenariosDir == null){
            return null;
        }
        for (VirtualFile scenario : scenariosDir.getChildren()){
            if (scenario.getName().equals(fileName)){
                return scenario;
            }
        }
        return null;
    }

    public static boolean isPerfCakeModule(Module module){
        boolean isPerfCakeModule = false;
        if (module != null) {
            String type = module.getOptionValue("type");
            if (type != null) {
                if (type.equals("PERFCAKE_MODULE")) {
                    isPerfCakeModule = true;
                }
            }
        }
        return isPerfCakeModule;
    }

}
