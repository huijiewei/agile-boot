package com.huijiewei.agile.core.consts;

import lombok.Getter;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

/**
 * @author huijiewei
 */

@Getter
public class ValueDescription<T extends ValueDescription<T, E>, E> {
    private static final Map<String, Map<?, ValueDescription<?, ?>>> ELEMENTS = new HashMap<>();

    private final E value;
    private final String description;

    public ValueDescription(E value, String description) {
        this.value = value;
        this.description = description;

        var element = getElement();

        if (element == null) {
            element = new HashMap<>();

            ELEMENTS.put(getClass().getName(), element);
        }

        element.put(value, this);
    }

    @SuppressWarnings("unchecked")
    protected static <T extends ValueDescription<T, E>, E> T valueOf(Class<T> enumType, E value) {
        return (T) ELEMENTS.get(enumType.getName()).get(value);
    }

    public static <T extends ValueDescription<T, E>, E> T valueOf(E value) {
        throw new IllegalStateException("Sub class of ValueDescription must implement method valueOf()");
    }

    public static <T extends ValueDescription<T, E>, E> ValueDescription<T, E>[] values() {
        throw new IllegalStateException("Sub class of ValueDescription must implement method values()");
    }

    @SuppressWarnings("unchecked")
    protected static <T extends ValueDescription<T, E>, E> T[] values(Class<T> enumType) {
        var values = ELEMENTS.get(enumType.getName()).values();

        var typedValues = (T[]) Array.newInstance(enumType, values.size());

        int i = 0;

        for (var value : values) {
            Array.set(typedValues, i, value);
            i++;
        }

        return typedValues;
    }


    @SuppressWarnings("unchecked")
    private Map<E, ValueDescription<?, ?>> getElement() {
        return (Map<E, ValueDescription<?, ?>>) ELEMENTS.get(getClass().getName());
    }

    public String toString() {
        return this.value.toString();
    }
}
