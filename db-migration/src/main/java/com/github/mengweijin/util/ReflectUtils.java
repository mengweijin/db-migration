package com.github.mengweijin.util;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Reflect util.
 *
 * @author mengweijin
 * @since 2024/3/10
 */
public class ReflectUtils {

    public static boolean hasStaticField(Class<?> cls, String fieldName) {
        return getAllStaticFieldName(cls).contains(fieldName);
    }

    /**
     * 获取某个类的所有静态属性
     */
    public static List<String> getAllStaticFieldName(Class<?> clazz) {
        List<String> list = new ArrayList<>();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            if (Modifier.isStatic(field.getModifiers())) {
                list.add(field.getName());
            }
        }
        return list;
    }

    public static List<Field> getAllFields(Class<?> clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        return new ArrayList<>(Arrays.asList(declaredFields));
    }

    public static Object getFieldValue(Object obj, Field field) {
        if (null == field) {
            return null;
        }
        if (obj instanceof Class) {
            // 静态字段获取时对象为null
            obj = null;
        }

        setAccessible(field);
        Object result;
        try {
            result = field.get(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static <T extends AccessibleObject> void setAccessible(T accessibleObject) {
        if (null != accessibleObject && !accessibleObject.isAccessible()) {
            accessibleObject.setAccessible(true);
        }
    }

    public static boolean isClassExist(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static Object getHikariProxyConnectionDelegateFieldValue(Connection connection) {
        try {
            Field delegate = connection.getClass().getSuperclass().getDeclaredField("delegate");
            return getFieldValue(connection, delegate);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
