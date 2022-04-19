package com.core.utils;

import java.util.Collection;
import java.util.List;

public class H {

    public static <T> T checkNull(T value) {
        if (value == null) {
            return null;
        }
        return value;
    }
    public static Boolean isTrue(Object value) {

        if (value == null) return false;

        if (value instanceof String) return !((String) value).trim().isEmpty();

        if (value instanceof Boolean) return (Boolean) value;

        if (value instanceof Collection) return !((Collection) value).isEmpty();

        if (value instanceof Object[]) return ((Object[]) value).length > 0;

        return true;
    }
    public static <T> void each(List<T> list, StringUtils.Each<T> each) throws RuntimeException {

        if (!H.isTrue(list)) return;

        for (int index = 0; index < list.size(); index++)
            each.do_(index, list.get(index));
    }
}
