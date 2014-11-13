package org.perfcake.pc4idea.module;

import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.ModuleTypeManager;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 7.9.2014
 */
public class PCModuleType extends ModuleType<PCModuleBuilder> {
    @NonNls private static final String ID = "PERFCAKE_MODULE";

    public PCModuleType() {
        super(ID);
    }

    public static PCModuleType getInstance() {
        return (PCModuleType) ModuleTypeManager.getInstance().findByID(ID);
    }

    @NotNull
    @Override
    public PCModuleBuilder createModuleBuilder() {
        return new PCModuleBuilder();
    }


    @NotNull
    @Override
    public String getName() {
        return "PerfCake Module";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "PerfCake Module Description";  /*TODO change*/
    }

    @Override
    public Icon getBigIcon() {
        return null;              /*TODO icon*/
    }

    @Override
    public Icon getNodeIcon(@Deprecated boolean b) {
        return null;             /*TODO icon*/
    }
}
