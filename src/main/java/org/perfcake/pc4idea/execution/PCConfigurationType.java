package org.perfcake.pc4idea.execution;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.Extensions;
import org.jetbrains.annotations.NotNull;
import org.perfcake.pc4idea.editor.designer.common.PerfCakeIconPatcher;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 6.12.2014
 */
public class PCConfigurationType implements ConfigurationType {
    private static final Logger LOG = Logger.getInstance("#org.perfcake.pc4idea.execution.PCConfigurationType");

    public static PCConfigurationType getInstance() {
        PCConfigurationType pcConfigurationType = null;
        for (ConfigurationType configType : Extensions.getExtensions(CONFIGURATION_TYPE_EP)) {
            if (configType instanceof PCConfigurationType) {
                pcConfigurationType = (PCConfigurationType) configType;
            }
        }
        if (pcConfigurationType == null){
            LOG.error("PCConfigurationType not found!");
            return null;
        } else {
            return pcConfigurationType;
        }
    }

    @Override
    public String getDisplayName() {
        return "PerfCake";
    }

    @Override
    public String getConfigurationTypeDescription() {
        return "Configuration for PerfCake scenario execution.";
    }

    @Override
    public Icon getIcon() {
        return PerfCakeIconPatcher.loadIcon();
    }

    @NotNull
    @Override
    public String getId() {
        return "PCConfigurationType";
    }

    @Override
    public ConfigurationFactory[] getConfigurationFactories() {
        return new ConfigurationFactory[]{new PCConfigurationFactory(this)};
    }
}
