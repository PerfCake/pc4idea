package org.perfcake.pc4idea.editor;

import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;
import org.perfcake.message.generator.AbstractMessageGenerator;
import org.perfcake.message.sender.AbstractSender;
import org.perfcake.reporting.destinations.Destination;
import org.perfcake.reporting.reporters.AbstractReporter;
import org.perfcake.validation.MessageValidator;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

/**
 * Created by miron on 18.3.2014. + changes
 */
public class PerfCakeReflectUtil {
    private static final Logger LOG = Logger.getInstance(PerfCakeReflectUtil.class);    /*TODO este z lib priecinka plugin sendre by to chcelo*/

    /**
     * Finds all subclasses names in a jar file of the superclass + changes
     * @param superclass for which to find subclasses
     * @param pckage package in superclass's jar to search
     * @return list of sorted subclass names, which are not abstract
     */
    @NotNull
    private List<Class> getSubclasses(@NotNull Class<?> superclass, @NotNull String pckage) {
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
            LOG.error("Error getting jar connection from superclass resource URL", e);
        }
        URL jarURL = connection.getJarFileURL();

        //find subclasses
        List<Class> subclasses = new ArrayList<>();

        JarInputStream jar = null;
        try {
            jar = new JarInputStream(jarURL.openStream());
        } catch (IOException e) {
            LOG.error("Could not open jar file URL " + jarURL.toString() + " to get " + superclass.getName() + " subclasses", e);
        }
        ZipEntry entry;
        try {
            while ((entry = jar.getNextEntry()) != null) {
                String entryName = entry.getName();
                if (entryName.startsWith(pckgPath) && entryName.endsWith(".class")) {
                    String clazzName = entryName.replace('/', '.').substring(0, entryName.length() - 6); //get rid of .class suffix
                    try {
                        //construct class

                        Class clazz = null;
                        try {
                            clazz = Class.forName(clazzName, false, this.getClass().getClassLoader());
                        } catch (NoClassDefFoundError e) {
                            LOG.warn("Could not get listed class from package", e);
                        }

                        if (clazz != null && superclass.isAssignableFrom(clazz) && !Modifier.isAbstract(clazz.getModifiers())) {
                            subclasses.add(clazz);
                        }

                    } catch (ClassNotFoundException e) {
                        LOG.warn("Could not get listed class from package", e);
                    }
                }
            }
        } catch (IOException e) {
            LOG.error("Error while getting classes from jar file", e);
        }
        return subclasses;
    }

    private String[] getNamesFromClasses(List<Class> classes){
        String[] namesArray = new String[classes.size()];
        for (int i=0;i<classes.size();i++){
            namesArray[i] = classes.get(i).getSimpleName();
        }
        Arrays.sort(namesArray);
        return namesArray;
    }


    @NotNull
    public String findSenderProperties(String sender) {
        List<Class> classes = getSubclasses(AbstractSender.class, "org.perfcake.message.sender");
        Class clazz = null;
        for (Class c : classes){
            if (c.getSimpleName().equals(sender)){
                clazz = c;
            }
        }
        if (clazz == null){
            LOG.error("Sender " + sender + " not found!");
            return "";
        }

        StringBuilder sb = new StringBuilder();

        sb.append("SENDER: "+clazz.getSimpleName()+"\n-----------------\n");

        sb.append("get/setMethods:\n-----------------\n");

        Set<Method> getMethods = new HashSet<>();

        Method[] allMethods = null;
        try {
            allMethods = clazz.getMethods();
        } catch (NoClassDefFoundError e){
            LOG.warn(e.getMessage());
            sb.append(e.getCause());
            return sb.toString();
        }

        for (Method m : allMethods) {
            if (m.getName().substring(0,3).contains("get")) {
                String name = m.getName().substring(3, m.getName().length());
                for (Method m2: allMethods) {
                    if (m2.getName().substring(0,3).contains("set") && m2.getName().contains(name)){
                        getMethods.add(m);
                        sb.append(name+"\n");
                    }
                }
            }
        }
        sb.append("\n\n");

        Set<Field> allFields = new HashSet<>();
        Class c = clazz;
        while(c != null){
            Collections.addAll(allFields, c.getDeclaredFields());
            c = c.getSuperclass();
        }

        sb.append("matched fields & getMethod returns:\n-----------------\n");
        for (Field f : allFields){
            for (Method m : getMethods){
                if (m.getName().toLowerCase().contains(f.getName().toLowerCase())){
                    sb.append(f.getName() + " = ");

                    try {
                        m.setAccessible(true);
                        Object t = clazz.newInstance();
                        Object o = m.invoke(t);
                        sb.append(o.toString());
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NullPointerException e) {
                        sb.append(e.getClass().getSimpleName());
                    }

                    sb.append("\n");
                }
            }
        }


        return sb.toString();
    }




    /**
     * Finds all sender class's names in perfcake library in package org.perfcake.message.sender
     * @return array of sender class names
     * */
    @NotNull
    public String[] findSenderClassNames() {
        return getNamesFromClasses(getSubclasses(AbstractSender.class, "org.perfcake.message.sender"));
    }

    /**
     * Finds all generator class's names in PerfCake library in package org.perfcake.message.generator
     * @return array of generator class names
     */
    @NotNull
    public String[] findGeneratorClassNames() {
        return getNamesFromClasses(getSubclasses(AbstractMessageGenerator.class, "org.perfcake.message.generator"));
    }

    /**
     * Finds all validator class's names in PerfCake library in package org.perfcake.message.validation
     *
     * @return array of balidator class names
     */
    @NotNull
    public String[] findValidatorClassNames() {
        return getNamesFromClasses(getSubclasses(MessageValidator.class, "org.perfcake.validation"));
    }

    /**
     * Finds all validator class's names in PerfCake library in package org.perfcake.message.validation
     *
     * @return array of balidator class names
     */
    @NotNull
    public String[] findDestinationClassNames() {
        return getNamesFromClasses(getSubclasses(Destination.class, "org.perfcake.reporting.destinations"));
    }

    @NotNull
    public String[] findReporterClassNames() {
        return getNamesFromClasses(getSubclasses(AbstractReporter.class, "org.perfcake.reporting.reporters"));
    }




}
