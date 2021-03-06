package com.wire.picklejar.scan;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Spliterator;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaSeeker {

    private static final Logger LOG = LoggerFactory.getLogger(JavaSeeker.class);

    private static final String CLASS_LOADER_CLASSES_FIELD_NAME = "classes";

    public static Collection<Class> getClasses(final String[] pkgs) throws IOException {
        final StandardJavaFileManager fileManager = ToolProvider.getSystemJavaCompiler().
                getStandardFileManager(null, null, null);
        Set<Class> classes = new LinkedHashSet<>();
        for (String pkg : pkgs) {
            LOG.debug(String.format("Loading classes from %s...", pkg));
            final Spliterator<JavaFileObject> classesSpliterator = fileManager.list(StandardLocation.CLASS_PATH, pkg, Collections.
                    singleton(JavaFileObject.Kind.CLASS), true).spliterator();
            classes.addAll(StreamSupport.stream(classesSpliterator, false)
                    .map(javaFileObject -> {
                        try {
                            LOG.trace("Processing class: {}", javaFileObject.getName());
                            final String fullClassName = javaFileObject.getName()
                                    .replace(".class", "")
                                    .replace(")", "")
                                    .replaceAll(Pattern.quote(File.separator), ".")
                                    .replaceAll(Pattern.quote("/"), ".")
                                    .replaceAll("(.*)" + pkg, pkg);
                            LOG.trace("Loading class: {}", fullClassName);
                            return Class.forName(fullClassName);
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }

                    })
                    .collect(Collectors.toCollection(ArrayList::new)));
        }
        // reverse order to have most important classes last
        ArrayList<Class> reversed = new ArrayList<>(classes);
        Collections.reverse(reversed);
        return reversed;

    }

    public static Collection<File> getResource(final String pack, String extension) throws IOException {
        final StandardJavaFileManager fileManager = ToolProvider.getSystemJavaCompiler().
                getStandardFileManager(null, null, null);
        final Spliterator<JavaFileObject> resourcesSpliterator = fileManager.list(StandardLocation.CLASS_PATH, pack,
                Collections.singleton(JavaFileObject.Kind.OTHER), true).spliterator();
        return StreamSupport.stream(resourcesSpliterator, false)
                .map(javaFileObject -> new File(javaFileObject.getName()))
                .filter((file) -> getFileExtension(file.getName()).equalsIgnoreCase(extension))
                .collect(Collectors.toList());
    }

    public static List<Class<?>> getLoadedClasses()
            throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        ClassLoader classLoader = getClassLoader();
        Class classLoaderClass = classLoader.getClass();
        while (classLoaderClass != java.lang.ClassLoader.class) {
            classLoaderClass = classLoaderClass.getSuperclass();
        }
        Field classLoaderClassesField = classLoaderClass.getDeclaredField(CLASS_LOADER_CLASSES_FIELD_NAME);
        classLoaderClassesField.setAccessible(true);
        ArrayList<Class<?>> classesList = new ArrayList((List) classLoaderClassesField.get(classLoader));
        return classesList;
    }

    private static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    private static String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    }
}
