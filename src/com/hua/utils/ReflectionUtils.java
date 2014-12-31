/*jadclipse*/// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.

package com.hua.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtils
{

    private ReflectionUtils()
    {
    }

    public static Class getClass(String className)
        throws Exception
    {
        return Class.forName(className);
    }

    public static Object getClassInstance(Class clazz, boolean isPublicConstructor, Class constructorArgsTypes[], Object constructorArgs[])
        throws Exception
    {
        Constructor constructor;
        if(isPublicConstructor)
            constructor = clazz.getConstructor(constructorArgsTypes);
        else
            constructor = clazz.getDeclaredConstructor(constructorArgsTypes);
        if(!constructor.isAccessible())
            constructor.setAccessible(true);
        return constructor.newInstance(constructorArgs);
    }

    public static Class getInnerClass(String outerClassName, String innerClassSimpleName)
        throws Exception
    {
        return getClass((new StringBuilder(String.valueOf(outerClassName))).append("$").append(innerClassSimpleName).toString());
    }

    public static Object getInnerClassInstance(Class clazz, boolean isPublicConstructor, Class constructorArgsTypes[], Object constructorArgs[])
        throws Exception
    {
        return getClassInstance(clazz, isPublicConstructor, constructorArgsTypes, constructorArgs);
    }

    // ("android.os.Build", "VERSION"), true, "SDK_INT")
    public static Field getField(Class clazz, boolean fieldDeclaredInClass, String fieldName)
        throws Exception
    {
        if(fieldDeclaredInClass)
            return clazz.getDeclaredField(fieldName);
        else
            return clazz.getField(fieldName);
    }

    public static Method getMethod(Class clazz, boolean methodDeclaredInClass, String methodName, Class methodArgsTypes[])
        throws Exception
    {
        Method method;
        if(methodDeclaredInClass)
            method = clazz.getDeclaredMethod(methodName, methodArgsTypes);
        else
            method = clazz.getMethod(methodName, methodArgsTypes);
        if(!method.isAccessible())
            method.setAccessible(true);
        return method;
    }

    public static Object getStaticFieldValue(Field field)
        throws Exception
    {
        if(!field.isAccessible())
            field.setAccessible(true);
        return field.get(null);
    }

    public static Object getInstanceFieldValue(Object instance, Field field)
        throws Exception
    {
        if(!field.isAccessible())
            field.setAccessible(true);
        return field.get(instance);
    }

    public static Object invokeStaticMethod(Method method, Object methodArgs[])
        throws Exception
    {
        if(!method.isAccessible())
            method.setAccessible(true);
        return method.invoke(null, methodArgs);
    }

    public static Object invokeInstanceMethod(Object instance, Method method, Object methodArgs[])
        throws Exception
    {
        if(!method.isAccessible())
            method.setAccessible(true);
        return method.invoke(instance, methodArgs);
    }
}


/*
	DECOMPILATION REPORT

	Decompiled from: E:\WorkSoftwareTool\NowEclipse\workspace2\nmplayer\trunk\nmplayer\libs\supportlib.jar
	Total time: 27 ms
	Jad reported messages/errors:
	Exit status: 0
	Caught exceptions:
*/