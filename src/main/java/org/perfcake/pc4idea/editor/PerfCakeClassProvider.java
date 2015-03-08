package org.perfcake.pc4idea.editor;

import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;
import org.perfcake.message.generator.AbstractMessageGenerator;
import org.perfcake.message.sender.AbstractSender;
import org.perfcake.reporting.destinations.Destination;
import org.perfcake.reporting.reporters.AbstractReporter;
import org.perfcake.validation.MessageValidator;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

/**
 * Created by miron on 18.3.2014.
 */
public class PerfCakeClassProvider {
    private static final Logger LOG = Logger.getInstance(PerfCakeClassProvider.class);    /*TODO problem a mozno static (author)*/

    /**
     * Finds all subclasses names in a jar file of the superclass
     * @param superclass for which to find subclasses
     * @param pckage package in superclass's jar to search
     * @return list of sorted subclass names, which are not abstract
     * @throws PerfCakeClassProviderException when an error occures during the search
     */
    @NotNull
    private String[] getSubclasses(@NotNull Class<?> superclass, @NotNull String pckage) throws PerfCakeClassProviderException {
        String pckgPath = pckage.replace('.', '/');
        //get URL of the superclass
        URL url = superclass.getResource("/" + pckgPath);
        if(url == null){
            throw new NullPointerException("Could not find superclass resource: " + superclass.getName());
        }


        //we dont support superclass that is not in jar file
        if(!url.toString().startsWith("jar:")){
            throw new UnsupportedOperationException("Superclass is not inside jar file");
        }
        JarURLConnection connection = null;
        try {
            connection = (JarURLConnection) url.openConnection();
        } catch (IOException e) {
            throw new PerfCakeClassProviderException("Error getting jar connection from superclass resource URL", e);
        }
        URL jarURL = connection.getJarFileURL();

        //find subclasses
        List<String> subclasses = new ArrayList<>();

        JarInputStream jar = null;
        try {
            jar = new JarInputStream(jarURL.openStream());
        } catch (IOException e) {
            throw new PerfCakeClassProviderException("Could not open jar file URL " + jarURL.toString() + " to get " + superclass.getName() + " subclasses", e);
        }
        ZipEntry entry;
        try {
            while ((entry = jar.getNextEntry()) != null) {
                String entryName = entry.getName();
                if (entryName.startsWith(pckgPath) && entryName.endsWith(".class")) {
                    String clazzName = entryName.replace('/', '.').substring(0, entryName.length() - 6); //get rid of .class suffix
                    try {
                        //construct class

                        try {
                            Class clazz = Class.forName(clazzName);
                            if(superclass.isAssignableFrom(clazz) && !Modifier.isAbstract(clazz.getModifiers())){
                                subclasses.add(clazz.getSimpleName());
                            }

                            /*TODO sender/reporting problem*/
                        } catch (NoClassDefFoundError e){
                            System.out.println("WTF:"+clazzName);
                        }
                    } catch (ClassNotFoundException e) {
                        LOG.warn("Could not get listed class from package", e);
                    }
                }
            }
        } catch (IOException e) {
            throw new PerfCakeClassProviderException("Error while getting classes from jar file", e);
        }
        //sort the result
        String[] arraySubclasses = subclasses.toArray(new String[0]);
        Arrays.sort(arraySubclasses);
        return arraySubclasses;
    }

    /**
     * Finds all sender class's names in perfcake library in package org.perfcake.message.sender
     * @return array of sender class names
     * @throws PerfCakeClassProviderException if an Exception occurs during the search
     */
    @NotNull
    public String[] findSenders() throws PerfCakeClassProviderException{
        return getSubclasses(AbstractSender.class, "org.perfcake.message.sender");
    }

    /**
     * Finds all generator class's names in PerfCake library in package org.perfcake.message.generator
     * @return array of generator class names
     * @throws PerfCakeClassProviderException if an Exception occurs during the search
     */
    @NotNull
    public String[] findGenerators() throws PerfCakeClassProviderException {
        return getSubclasses(AbstractMessageGenerator.class, "org.perfcake.message.generator");
    }

    /**
     * Finds all validator class's names in PerfCake library in package org.perfcake.message.validation
     *
     * @return array of balidator class names
     * @throws PerfCakeClassProviderException if an Exception occurs during the search
     */
    @NotNull
    public String[] findValidators() throws PerfCakeClassProviderException {
        return getSubclasses(MessageValidator.class, "org.perfcake.validation");
    }

    /**
     * Finds all validator class's names in PerfCake library in package org.perfcake.message.validation
     *
     * @return array of balidator class names
     * @throws PerfCakeClassProviderException if an Exception occurs during the search
     */
    @NotNull
    public String[] findDestinations() throws PerfCakeClassProviderException {
        return getSubclasses(Destination.class, "org.perfcake.reporting.destinations");
    }

    public String[] findReporters() throws PerfCakeClassProviderException {
        return getSubclasses(AbstractReporter.class, "org.perfcake.reporting.reporters");
    }




}
