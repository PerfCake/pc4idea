package org.perfcake.pc4idea.api.editor.swing;

import com.intellij.openapi.diagnostic.Logger;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * User: Stanislav Kaleta
 * * Date: 22.11.2014
 */
public abstract class ScenarioImportHandler extends TransferHandler{
    private static final Logger LOG = Logger.getInstance(ScenarioImportHandler.class);

    @Override
    public boolean canImport(TransferSupport support){
        return support.isDataFlavorSupported(DataFlavor.stringFlavor);
    }

    @Override
    public boolean importData(TransferSupport support){
        if (!canImport(support)) {
            return false;
        }
        Transferable t = support.getTransferable();
        String transferredData = "";
        try {
            transferredData = (String)t.getTransferData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException | IOException e) {
            LOG.error(e.getMessage());
        }

        performImport(transferredData);

        return true;
    }

    public abstract void performImport(String transferredData);
}
