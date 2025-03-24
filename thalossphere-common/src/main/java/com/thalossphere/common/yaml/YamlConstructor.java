package com.thalossphere.common.yaml;

import org.yaml.snakeyaml.constructor.Constructor;

public class YamlConstructor extends Constructor {

    private final Class<?> rootClass;

    public YamlConstructor(Class<?> rootClass) {
        this.rootClass = rootClass;
    }

    @Override
    protected Class<?> getClassForName(String name) throws ClassNotFoundException {
        if (!name.equals(rootClass.getName())) {
            throw new RuntimeException();
        }
        return super.getClassForName(name);
    }
}
