package org.perfcake.pc4idea.editor.wizard;

import org.perfcake.model.Scenario;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 28.10.2014
 */
public class MessagesEditor extends JPanel {





    public void setMessages(Scenario.Messages messages){

    }

    public boolean areInsertedValuesValid() {
        //boolean areValid = false;
        /*TODO check validity of inserted data - if false OK button disabled*/
        return true;
    }

    public Scenario.Messages getMessages(){
        return null;
    }
}
