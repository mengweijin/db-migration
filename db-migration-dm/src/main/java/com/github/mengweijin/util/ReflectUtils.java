package com.github.mengweijin.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Reflect util.
 * @author mengweijin
 * @date 2024/3/10
 */
public class ReflectUtils {

    public static boolean hasStaticField(Class<?> cls, String fieldName){
        return getAllStaticFieldName(cls).contains(fieldName);
    }

    /**
     *  获取某个类的所有静态属性
     */
    public static List<String> getAllStaticFieldName(Class<?> clazz) {
        List<String> list = new ArrayList<>();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            if(Modifier.isStatic(field.getModifiers())){
                list.add(field.getName());
            }
        }
        return list;
    }
}
