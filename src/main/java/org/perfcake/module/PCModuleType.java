package org.perfcake.module;

import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.ModuleTypeManager;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 7.9.2014
 * To change this template use File | Settings | File Templates.
 */
public class PCModuleType extends ModuleType<PCModuleBuilder> {
    @NonNls private static final String ID = "PERFCAKE_MODULE";

    public PCModuleType() {
        super(ID);
    }

    public static PCModuleType getInstance() {
        return (PCModuleType) ModuleTypeManager.getInstance().findByID(ID);
    }

//    public static boolean isOfType(@NotNull Module module) {
//        return get(module) instanceof MyModuleType;
//    }

    @NotNull
    @Override
    public PCModuleBuilder createModuleBuilder() {
        return new PCModuleBuilder();
    }


    @NotNull
    @Override
    public String getName() {
        return "PerfCake Module"; /*TODO bundle*/
    }

    @NotNull
    @Override
    public String getDescription() {
        return "PerfCake Module Description";  /*TODO bundle*/
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
