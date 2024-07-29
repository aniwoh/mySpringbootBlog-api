package org.aniwoh.myspringbootblogapi.utils;

import java.lang.reflect.Field;

public class AutoReload {
    /**
     * 哥们儿，自动装填了解一下。
     *
     * @param source 源对象
     * @param target 目标对象
     * @throws IllegalAccessException 抛出空对象异常
     */
    public static void reload(Object source, Object target) throws IllegalAccessException {
        if (source == null || target == null) {
            throw new IllegalArgumentException("Source and target objects must not be null");
        }

        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();

        Field[] sourceFields = sourceClass.getDeclaredFields();
        Field[] targetFields = targetClass.getDeclaredFields();

        for (Field sourceField : sourceFields) {
            sourceField.setAccessible(true);

            for (Field targetField : targetFields) {
                targetField.setAccessible(true);

                if (sourceField.getName().equals(targetField.getName())
                        && sourceField.getType().equals(targetField.getType())) {
                    targetField.set(target, sourceField.get(source));
                }
            }
        }
    }
}
