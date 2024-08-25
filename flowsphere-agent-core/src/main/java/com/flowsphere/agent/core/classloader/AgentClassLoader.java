package com.flowsphere.agent.core.classloader;

import com.google.common.io.ByteStreams;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class AgentClassLoader extends ClassLoader {

    static {
        registerAsParallelCapable();
    }

    private final List<JarFile> jarFileList;

    public AgentClassLoader(ClassLoader classLoader, List<JarFile> jarFileList) {
        super(classLoader);
        this.jarFileList = jarFileList;
    }

    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        String path = generatorClassPath(name);
        for (JarFile each : jarFileList) {
            ZipEntry entry = each.getEntry(path);
            if (null == entry) {
                continue;
            }
            try {
                return defineClass(name, each, entry);
            } catch (final IOException ex) {
                throw new ClassNotFoundException(name, ex);
            }
        }
        throw new ClassNotFoundException(name);
    }

    private String generatorClassPath(final String className) {
        return String.join("", className.replace(".", "/"), ".class");
    }


    private Class<?> defineClass(final String name, final JarFile extraJar, final ZipEntry entry) throws IOException {
        byte[] data = ByteStreams.toByteArray(extraJar.getInputStream(entry));
        return defineClass(name, data, 0, data.length);
    }

    @Override
    protected Enumeration<URL> findResources(final String name) {
        Collection<URL> result = new LinkedList<>();
        for (JarFile each : jarFileList) {
            findResource(name, each).ifPresent(result::add);
        }
        return Collections.enumeration(result);
    }

    @Override
    protected URL findResource(final String name) {
        return jarFileList.stream().map(each -> findResource(name, each)).filter(Optional::isPresent).findFirst().filter(Optional::isPresent).map(Optional::get).orElse(null);
    }

    private Optional<URL> findResource(final String name, final JarFile extraJar) {
        JarEntry entry = extraJar.getJarEntry(name);
        if (null == entry) {
            return Optional.empty();
        }
        try {
            return Optional.of(new URL(String.format("jar:file:%s!/%s", extraJar.getName(), name)));
        } catch (final MalformedURLException ignored) {
            return Optional.empty();
        }
    }

}
