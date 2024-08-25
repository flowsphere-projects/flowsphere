package com.flowsphere.test.classloader;

import com.flowsphere.agent.core.classloader.AgentClassLoader;
import com.flowsphere.agent.core.classloader.PluginsJarLoader;
import com.flowsphere.agent.core.utils.URLUtils;
import com.flowsphere.test.interceptor.CustomInterceptor;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.jar.JarFile;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class AgentClassLoaderTest {


    @Test
    public void loadTest() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        List<JarFile> jarFileList = PluginsJarLoader.getJarFileList(URLUtils.getPluginURL());
        AgentClassLoader classLoader = new AgentClassLoader(AgentClassLoaderTest.class.getClassLoader(), jarFileList);

        CustomInterceptor methodInterceptor = (CustomInterceptor) Class.forName("com.flowsphere.test.interceptor.CustomInterceptor", true, classLoader).newInstance();
        assertNotNull(methodInterceptor);
    }


}