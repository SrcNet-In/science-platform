package org.opencadc.skaha.utils;

import java.lang.reflect.Field;

public class TestUtils {
    public static <T> void set(T object, String propertyName, Object value) {
        try {
            // Get the class of the object
            Class<?> clazz = object.getClass();
            // Get the field by name
            Field field = clazz.getDeclaredField(propertyName);
            // Make the field accessible if it's private
            field.setAccessible(true);
            // Set the value of the field
            field.set(object, value);
        } catch (NoSuchFieldException e) {
            System.err.println("No such field: " + propertyName);
        } catch (IllegalAccessException e) {
            System.err.println("Cannot access field: " + propertyName);
        }
    }
}
