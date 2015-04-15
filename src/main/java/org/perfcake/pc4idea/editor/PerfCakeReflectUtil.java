package org.perfcake.pc4idea.editor;

import com.intellij.openapi.diagnostic.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.perfcake.message.generator.AbstractMessageGenerator;
import org.perfcake.message.sender.AbstractSender;
import org.perfcake.model.Property;
import org.perfcake.pc4idea.module.PerfCakeModuleUtil;
import org.perfcake.reporting.destinations.Destination;
import org.perfcake.reporting.reporters.AbstractReporter;
import org.perfcake.validation.MessageValidator;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

/**
 * Created by miron on 18.3.2014. + changes
 */
public class PerfCakeReflectUtil {
    private static final Logger LOG = Logger.getInstance(PerfCakeReflectUtil.class);

    private PerfCakeEditorUtil util;

    public PerfCakeReflectUtil(PerfCakeEditorUtil util) {
        this.util = util;
    }

    public PerfCakeReflectUtil() {
        this.util = null;
    }/*TODO do wizard - module par.*/


    /**
     * Finds all subclasses names in a jar file of the superclass + changes - if uri null - classpath
     * @param superclass for which to find subclasses
     * @param pckage package in superclass's jar to search
     * @return list of sorted subclass names, which are not abstract
     */
    @NotNull
    private List<Class> getSubclasses(@NotNull Class<?> superclass, @NotNull String pckage, @Nullable String dir) {
        URL url = null;
        String pckgPath = pckage.replace('.', '/');
        if (dir == null) {
            url = superclass.getResource("/" + pckgPath);
            if (url == null) {
                throw new NullPointerException("Could not find superclass resource: " + superclass.getName());
            }
        } else {
            try {
                url = new URL("file://" + dir);
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
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

    public List<Property> findSenderProperties(String sender) {
        List<Class> classes = getSubclasses(AbstractSender.class, "org.perfcake.message.sender", null);
        //String libDir = PerfCakeModuleUtil.getPerfCakeModuleDirsUri(util.getModule())[2];
        //classes.addAll(getSubclasses(AbstractSender.class, "org.perfcake.message.sender", libDir));
        Class clazz = null;
        for (Class c : classes){
            if (c.getSimpleName().equals(sender)){
                clazz = c;
            }
        }
        if (clazz == null){
            LOG.warn(sender + " " + "class not found!");
            return null;
        }

        Method[] allMethods = null;
        try {
            allMethods = clazz.getMethods();
        } catch (NoClassDefFoundError e){
            LOG.warn("NoClassDefFoundError " + e.getMessage());
            return null;
        }
        if (allMethods == null) {
            LOG.warn(clazz + " " + "methods array is null");
            return null;
        }

        List<Property> properties = new ArrayList<>();
        for (Method mSet : allMethods) {
            String modifiersSet = Modifier.toString(mSet.getModifiers());
            String nameSet = mSet.getName();
            if (modifiersSet.equals("public") && nameSet.substring(0, 3).equals("set")) {
                String possiblePropertySet = nameSet.substring(3).toLowerCase();
                for (Method mGet : allMethods) {
                    String modifiersGet = Modifier.toString(mGet.getModifiers());
                    String nameGet = mGet.getName();
                    String possiblePropertyGet = "";
                    if (modifiersGet.equals("public")) {
                        if (nameGet.substring(0, 3).equals("get")) {
                            possiblePropertyGet = nameGet.substring(3).toLowerCase();
                        }
                        if (nameGet.substring(0, 2).equals("is")) {
                            possiblePropertyGet = nameGet.substring(2).toLowerCase();
                        }
                    }
                    if (possiblePropertySet.equals(possiblePropertyGet)) {
                        Property property = new Property();
                        property.setName(possiblePropertySet);
                        try {
                            mGet.setAccessible(true);
                            Object t = clazz.newInstance();
                            Object o = mGet.invoke(t);
                            property.setValue(o.toString());
                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NullPointerException | NoClassDefFoundError e) {
                            property.setValue("");
                        }
                        properties.add(property);
                    }
                }
            }
        }
        return properties;
    }




    /**
     * Finds all sender class's names in perfcake library in package org.perfcake.message.sender
     * @return array of sender class names
     * */
    @NotNull
    public String[] findSenderClassNames() {
        List<Class> classes = getSubclasses(AbstractSender.class, "org.perfcake.message.sender", null);
        if (util != null) { /*TODO do wizard - module*/
            //String libDir = PerfCakeModuleUtil.getPerfCakeModuleDirsUri(util.getModule())[2];
            //classes.addAll(getSubclasses(AbstractSender.class, "org.perfcake.message.sender", libDir));
        }
        return getNamesFromClasses(classes);
    }

    /**
     * Finds all generator class's names in PerfCake library in package org.perfcake.message.generator
     * @return array of generator class names
     */
    @NotNull
    public String[] findGeneratorClassNames() {
        return getNamesFromClasses(getSubclasses(AbstractMessageGenerator.class, "org.perfcake.message.generator", null));
    }

    /**
     * Finds all validator class's names in PerfCake library in package org.perfcake.message.validation
     *
     * @return array of balidator class names
     */
    @NotNull
    public String[] findValidatorClassNames() {
        return getNamesFromClasses(getSubclasses(MessageValidator.class, "org.perfcake.validation", null));
    }

    /**
     * Finds all validator class's names in PerfCake library in package org.perfcake.message.validation
     *
     * @return array of balidator class names
     */
    @NotNull
    public String[] findDestinationClassNames() {
        return getNamesFromClasses(getSubclasses(Destination.class, "org.perfcake.reporting.destinations", null));
    }

    @NotNull
    public String[] findReporterClassNames() {
        return getNamesFromClasses(getSubclasses(AbstractReporter.class, "org.perfcake.reporting.reporters", null));
    }




}
