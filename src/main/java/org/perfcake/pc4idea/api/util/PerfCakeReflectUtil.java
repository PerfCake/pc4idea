package org.perfcake.pc4idea.api.util;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VirtualFile;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;
import org.perfcake.message.generator.AbstractMessageGenerator;
import org.perfcake.message.sender.AbstractSender;
import org.perfcake.model.Property;
import org.perfcake.reporting.destinations.Destination;
import org.perfcake.reporting.reporters.AbstractReporter;
import org.perfcake.validation.MessageValidator;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * This class provides abilities to retrieve classes, classes names and properties for
 * PerfCake Scenario components using reflection.
 *
 * @author Stanislav Kaleta
 */
public class PerfCakeReflectUtil {

    /**
     * A logger
     */
    private static final Logger LOG = Logger.getInstance(PerfCakeReflectUtil.class);

    /**
     * Constants which represent Scenario components
     */
    public static final int SENDER = 1;
    public static final int GENERATOR = 2;
    public static final int VALIDATOR = 3;
    public static final int REPORTER = 4;
    public static final int DESTINATION = 5;

    /**
     * Module in which scenario is placed. This util isn't able to use plugin jars, if module isn't
     * set or module type isn't PerfCake, i.e. "lib" dir can't be identified.
     */
    private Module module;

    public PerfCakeReflectUtil(Module module) {
        this.module = module;
    }

    /**
     * Finds all not abstract subclasses in the jar file of the superclass.
     * If the module type is PerfCake module, method also finds all subclasses in jars in the lib directory.
     *
     * @param superclass class for which to find subclasses
     * @return list of not abstract subclasses
     */
    @NotNull
    public List<Class> findSubclasses(@NotNull Class<?> superclass) {
        Set<String> pathToJars = new HashSet<>();
        if (module != null) {
            VirtualFile libDir = PerfCakeModuleUtil.getPerfCakeModuleDirsUri(module)[2];
            VirtualFile[] files = libDir.getChildren();
            for (VirtualFile file : files) {
                if (file.getFileType().equals(FileTypeManager.getInstance().getFileTypeByExtension("jar"))) {
                    pathToJars.add(file.getPath());
                }
            }
        }
        String packagePath = superclass.getPackage().getName();
        packagePath = packagePath.replace('.', '/');
        URL resourceUrl = superclass.getResource("/" + packagePath);
        try {
            JarURLConnection connection = (JarURLConnection) resourceUrl.openConnection();
            URL jarURL = connection.getJarFileURL();
            pathToJars.add(jarURL.getPath());
        } catch (IOException e) {
            LOG.error("Error getting jar connection from superclass resource URL", e);
        }

        List<Class> subclasses = new ArrayList<>();

        for (String pathToJar : pathToJars) {
            try {
                JarFile jarFile = new JarFile(pathToJar);
                Enumeration e = jarFile.entries();
                URL[] urls = {new URL("jar:file:" + pathToJar + "!/")};
                URLClassLoader cl = URLClassLoader.newInstance(urls, superclass.getClassLoader());

                while (e.hasMoreElements()) {
                    JarEntry je = (JarEntry) e.nextElement();
                    if (je.isDirectory() || !je.getName().endsWith(".class")) {
                        continue;
                    }
                    String className = je.getName().substring(0, je.getName().length() - 6);
                    className = className.replace('/', '.');
                    Class c = cl.loadClass(className);

                    if (c != null && superclass.isAssignableFrom(c) && !Modifier.isAbstract(c.getModifiers())) {
                        subclasses.add(c);
                    }
                }
            } catch (ClassNotFoundException e) {
                LOG.warn(e.getMessage());
            } catch (IOException e) {
                LOG.error(e.getMessage());
            }
        }

        return subclasses;
    }

    /**
     * Finds all class's names for Scenario component.
     *
     * @param component for which to find class's names
     * @return array of component class names
     */
    @NotNull
    public String[] findComponentClassNames(
            @MagicConstant(intValues = {SENDER, GENERATOR, VALIDATOR, REPORTER, DESTINATION}) int component) {
        return getNamesFromClasses(findSubclasses(getComponentSuperclass(component)));
    }

    /**
     * Finds all properties for specific Scenario component defined with class name.
     *
     * @param component for which to find properties
     * @param componentClassName class name of component for which to find properties
     * @return list of properties
     */
    @NotNull
    public List<Property> findComponentProperties(
            @MagicConstant(intValues = {SENDER, GENERATOR, VALIDATOR, REPORTER, DESTINATION}) int component,
            String componentClassName) {
        List<Class> componentClasses = findSubclasses(getComponentSuperclass(component));

        Class componentClazz = null;
        for (Class c : componentClasses) {
            if (c.getSimpleName().equals(componentClassName)) {
                componentClazz = c;
            }
        }
        if (componentClazz == null) {
            LOG.error(componentClassName + " " + "class not found!");
            return new ArrayList<>();
        }

        Method[] allMethods = null;
        try {
            allMethods = componentClazz.getMethods();
        } catch (NoClassDefFoundError e) {
            LOG.warn(e.getMessage());
            return new ArrayList<>();
        }
        if (allMethods == null) {
            LOG.warn(componentClazz.getName() + " " + "methods array is null");
            return new ArrayList<>();
        }

        List<Property> properties = new ArrayList<>();
        for (Method possibleSetMethod : allMethods) {
            int possibleSetMethodModifiers = possibleSetMethod.getModifiers();
            String possibleSetMethodName = possibleSetMethod.getName();
            if (possibleSetMethodModifiers == Modifier.PUBLIC && possibleSetMethodName.substring(0, 3).equals("set")) {
                String possiblePropertyNameFromSetMethod = possibleSetMethodName.substring(3);
                for (Method possibleGetMethod : allMethods) {
                    int possibleGetMethodModifiers = possibleGetMethod.getModifiers();
                    String possibleGetMethodName = possibleGetMethod.getName();
                    String possiblePropertyNameFromGet = "";
                    if (possibleGetMethodModifiers == Modifier.PUBLIC) {
                        if (possibleGetMethodName.substring(0, 3).equals("get")) {
                            possiblePropertyNameFromGet = possibleGetMethodName.substring(3);
                        }
                        if (possibleGetMethodName.substring(0, 2).equals("is")) {
                            possiblePropertyNameFromGet = possibleGetMethodName.substring(2);
                        }
                    }
                    if (possiblePropertyNameFromSetMethod.equals(possiblePropertyNameFromGet)) {
                        String propertyName = possiblePropertyNameFromGet;
                        propertyName = propertyName.substring(0, 1).toLowerCase().concat(propertyName.substring(1));

                        String propertyValue = "";
                        try {
                            possibleGetMethod.setAccessible(true);
                            Object t = componentClazz.newInstance();
                            Object o = possibleGetMethod.invoke(t);
                            propertyValue = o.toString();
                        } catch (InstantiationException | IllegalAccessException |
                                InvocationTargetException | NullPointerException | NoClassDefFoundError e) {
                            // value remains ""
                        }
                        Property property = new Property();
                        property.setName(propertyName);
                        property.setValue(propertyValue);
                        properties.add(property);
                    }
                }
            }
        }
        return properties;
    }

    private Class<?> getComponentSuperclass(int component) {
        switch (component) {
            case 1:
                return AbstractSender.class;
            case 2:
                return AbstractMessageGenerator.class;
            case 3:
                return MessageValidator.class;
            case 4:
                return AbstractReporter.class;
            case 5:
                return Destination.class;
            default:
                throw new IllegalArgumentException("illegal parameter value (int component)");
        }
    }

    private String[] getNamesFromClasses(@NotNull List<Class> classes) {
        String[] namesArray = new String[classes.size()];
        for (int i = 0; i < classes.size(); i++) {
            namesArray[i] = classes.get(i).getSimpleName();
        }
        Arrays.sort(namesArray);
        return namesArray;
    }
}
