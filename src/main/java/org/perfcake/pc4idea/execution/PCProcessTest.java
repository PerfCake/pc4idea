package org.perfcake.pc4idea.execution;

import com.intellij.openapi.application.ApplicationManager;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: Stanislav Kaleta
 * Date: 13.12.2014
 */
public class PCProcessTest extends Process{
    private InputStream isN;
    private InputStream isE;
    private OutputStream osN;
    private OutputStream osE;
    public PCProcessTest() {/*TODO potriedit: pipedin/output do roznych vlakien + scenario vo vlastnom vlake urcite*/
        PrintStream originalOut = System.out;
        PrintStream originalErr = System.err;
        try {
            osN = new PipedOutputStream();      // System.out do zdroja
            osE = new PipedOutputStream();   // System.err do zdroja
            System.setOut(new PrintStream(osN));
            System.setErr(new PrintStream(osE));


            isN = new PipedInputStream((PipedOutputStream) osN);   //zdroj do conzoly
            isE = new PipedInputStream((PipedOutputStream) osE);  //zdroj do conzoly ako err

        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i=0;i<10;i++){
            System.out.println("run "+System.currentTimeMillis());
        }
        System.err.println("ERROR");


        System.setOut(originalOut);
        System.setErr(originalErr);
    }
    @Override
    public OutputStream getOutputStream() {
        return null;
    }

    @Override
    public InputStream getInputStream() {
        return isN;
    }

    @Override
    public InputStream getErrorStream() {
        return isE;
    }

    @Override
    public int waitFor() throws InterruptedException {
        return 0;
    }

    @Override
    public int exitValue() {
        return 0;
    }

    @Override
    public void destroy() {

    }
}
