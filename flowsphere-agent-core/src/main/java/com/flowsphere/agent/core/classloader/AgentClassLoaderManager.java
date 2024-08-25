package com.flowsphere.agent.core.classloader;


import com.flowsphere.agent.core.utils.URLUtils;

import java.net.URL;
import java.util.List;
import java.util.jar.JarFile;

public class AgentClassLoaderManager {

    public static AgentClassLoader getAgentPluginClassLoader(ClassLoader classLoader) {
        List<JarFile> jarFileList = getJarFileList();
        return new AgentClassLoader(classLoader, jarFileList);
    }

    public static List<JarFile> getJarFileList() {
        List<URL> urlList = URLUtils.getPluginURL();
        List<JarFile> jarFileList = PluginsJarLoader.getJarFileList(urlList);
        return jarFileList;
    }

}
