package com.thalossphere.agent.core.utils;


import com.thalossphere.common.constant.CommonConstant;
import com.thalossphere.common.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class URLUtils {


    public static List<URL> getPluginURL() {
        File agentDictionary = AgentPath.getPath();
        File plugins = new File(agentDictionary, CommonConstant.PLUGINS);
        return resolveLib(plugins.getAbsolutePath());
    }

    private static List<URL> resolveLib(String agentLibPath) {
        final File libDir = new File(agentLibPath);
        if (checkDirectory(libDir)) {
            return Collections.emptyList();
        }
        final File[] libFileList = FileUtils.listFiles(libDir, new String[]{".jar"});

        List<URL> libURLList = toURLs(libFileList);
        return new ArrayList<URL>(libURLList);
    }

    private static boolean checkDirectory(File file) {
        if (!file.exists()) {

            return true;
        }
        if (!file.isDirectory()) {
            return true;
        }

        return false;
    }

    private static List<URL> toURLs(File[] jarFileList) {
        try {
            URL[] jarURLArray = FileUtils.toURLs(jarFileList);

            return Arrays.asList(jarURLArray);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }


}
