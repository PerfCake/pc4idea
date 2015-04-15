package org.perfcake.pc4idea.module;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.ModuleTypeManager;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Created by miron on 8.1.2014.  + changes
 */
public class PerfCakeModuleType extends ModuleType<PerfCakeModuleBuilder> {
    @NonNls
    public static final String ID = "PERFCAKE_MODULE";

    public PerfCakeModuleType() {
        super(ID);
    }

    public static PerfCakeModuleType getInstance() {
        return (PerfCakeModuleType) ModuleTypeManager.getInstance().findByID(ID);
    }

    public static boolean isOfType(Module module) {
        return get(module) instanceof PerfCakeModuleType;
    }


    @NotNull
    @Override
    public PerfCakeModuleBuilder createModuleBuilder() {
        return new PerfCakeModuleBuilder();
    }


    @NotNull
    @Override
    public String getName() {
        return "PerfCake Module";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "PerfCake scenario editor";
    }


    @Override
    public Icon getBigIcon() {
        return PerfCakeIconPatcher.loadIcon();
    }

    @Override
    public Icon getNodeIcon(boolean isOpened) {
        return PerfCakeIconPatcher.loadIcon();
    }
}
