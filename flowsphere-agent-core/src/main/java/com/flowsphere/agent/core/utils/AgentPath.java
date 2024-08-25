package com.flowsphere.agent.core.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

@Slf4j
public class AgentPath {


    private static File AGENT_PACKAGE_PATH;

    public static File getPath() {
        if (AGENT_PACKAGE_PATH == null) {
            AGENT_PACKAGE_PATH = findPath();
        }

        return AGENT_PACKAGE_PATH;
    }

    private static File findPath() {
        String classResourcePath = AgentPath.class.getName().replaceAll("\\.", "/") + ".class";

        URL resource = ClassLoader.getSystemClassLoader().getResource(classResourcePath);
        if (resource != null) {
            String urlString = resource.toString();


            int insidePathIndex = urlString.indexOf('!');
            boolean isInJar = insidePathIndex > -1;

            if (isInJar) {
                urlString = urlString.substring(urlString.indexOf("file:"), insidePathIndex);
                File agentJarFile = null;
                try {
                    agentJarFile = new File(new URL(urlString).toURI());
                } catch (MalformedURLException | URISyntaxException e) {
                    log.error("", e);
                }
                if (agentJarFile.exists()) {
                    return agentJarFile.getParentFile();
                }
            } else {
                int prefixLength = "file:".length();
                String classLocation = urlString.substring(prefixLength, urlString.length() - classResourcePath.length());

                return new File(classLocation);
            }
        }

        throw new IllegalArgumentException("Can not locate agent jar file");
    }

}
