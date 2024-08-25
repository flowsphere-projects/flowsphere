package com.flowsphere.test.config;

import com.flowsphere.agent.core.config.PointcutConfigLoader;
import com.flowsphere.agent.core.config.yaml.YamlClassPointcutConfig;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PointcutConfigLoaderTest {

    @Test
    public void loadTest() throws FileNotFoundException {
        List<YamlClassPointcutConfig> classPointcutConfigs = PointcutConfigLoader.load("example", PointcutConfigLoaderTest.class.getClassLoader());
        assertNotNull(classPointcutConfigs);
        assertTrue(classPointcutConfigs.size() > 0);
    }

}