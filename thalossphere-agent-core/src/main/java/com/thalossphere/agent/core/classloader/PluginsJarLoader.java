package com.thalossphere.agent.core.classloader;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;

public class PluginsJarLoader {

    public static List<JarFile> getJarFileList(List<URL> urlList) {
        List<JarFile> jarFileList = new ArrayList<>();
        for (URL url : urlList) {
            //读取到所有plugin jar
            try {
                jarFileList.add(new JarFile(url.getPath()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return jarFileList;
    }

}
