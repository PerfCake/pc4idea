package org.perfcake.pc4idea.editor.designer.common;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 22.11.2014
 */
public abstract class ScenarioImportHandler extends TransferHandler{
    @Override
    public boolean canImport(TransferHandler.TransferSupport support){
        //support.setDropAction(COPY);
        return support.isDataFlavorSupported(DataFlavor.stringFlavor);
    }

    @Override
    public boolean importData(TransferHandler.TransferSupport support){
        if (!canImport(support)) {
            return false;
        }
        Transferable t = support.getTransferable();
        String transferredData = "";
        try {
            transferredData = (String)t.getTransferData(DataFlavor.stringFlavor);
        } catch (UnsupportedFlavorException e) {
            e.printStackTrace();   /*TODO log*/
        } catch (IOException e) {
            e.printStackTrace();   /*TODO log*/
        }

        performImport(transferredData);

        return true;
    }

    public abstract void performImport(String transferredData);
}
