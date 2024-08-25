package com.flowsphere.common.utils;


import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

public final class FileUtils {

    public static File[] listFiles(final File path, final String[] fileExtensions) {
        return path.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                String path = pathname.getName();
                for (String extension : fileExtensions) {
                    if (path.lastIndexOf(extension) != -1) {
                        return true;
                    }
                }
                return false;
            }
        });
    }

    public static URL toURL(final File file) throws IOException {
        return toURL(file, new FileFunction());
    }

    public static URL[] toURLs(final File[] files) throws IOException {
        return toURLs(files, new FileFunction());
    }

    private static <T> URL toURL(final T source, final Function<T, URI> function) throws IOException {
        URI uri = function.apply(source);
        return uri.toURL();
    }

    private static <T> URL[] toURLs(final T[] source, final Function<T, URI> function) throws IOException {
        final URL[] urls = new URL[source.length];
        for (int i = 0; i < source.length; i++) {
            T t = source[i];
            urls[i] = toURL(t, function);
        }
        return urls;
    }

    private interface Function<T, R> {
        R apply(T t);
    }

    private static class FileFunction implements Function<File, URI> {
        @Override
        public URI apply(File file) {
            return file.toURI();
        }
    }


}