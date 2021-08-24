package com.SalarJavaDevGroup.ReflectUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SuperMethod{
    Method method;
    Object instance;
    public SuperMethod(Method method, Object obj) {
        this.method = method;
        this.instance = obj;
    }

    public void invoke(Object... args) throws InvocationTargetException, IllegalAccessException {
        method.invoke(instance, args);
    }
}
