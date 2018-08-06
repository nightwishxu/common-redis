package core.common.util;


import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ObjectUtils {
    public ObjectUtils() {
    }

    public static Object notNull(Object obj, Object obj1) {
        return obj != null && !"".equals(obj) ? obj : obj1;
    }

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean isInt(Object s) {
        try {
            Integer.valueOf(s.toString());
            return true;
        } catch (Exception var2) {
            return false;
        }
    }

    public static boolean isFloat(Object s) {
        try {
            Float.valueOf(s.toString());
            return true;
        } catch (Exception var2) {
            return false;
        }
    }

    public static boolean isLong(Object s) {
        try {
            Long.valueOf(s.toString());
            return true;
        } catch (Exception var2) {
            return false;
        }
    }

    public static boolean isBoolean(Object s) {
        try {
            Boolean.valueOf(s.toString());
            return true;
        } catch (Exception var2) {
            return false;
        }
    }

    public static void merge(Object merge_object, Object object) throws Exception {
        Class<?> classType = object.getClass();
        Field[] fields = classType.getDeclaredFields();

        for(int i = 0; i < fields.length; ++i) {
            Field field = fields[i];
            String fieldName = field.getName();
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getMethodName = "get" + firstLetter + fieldName.substring(1);
            String setMethodName = "set" + firstLetter + fieldName.substring(1);
            Method getMethod = classType.getMethod(getMethodName);
            Method setMethod = classType.getMethod(setMethodName, field.getType());
            Object value = getMethod.invoke(object);
            if (value != null && value.toString().length() > 0) {
                setMethod.invoke(merge_object, value);
            }
        }

    }

    public static Class<?> getObject(String Path) throws ClassNotFoundException {
        return Class.forName(Path);
    }
}
