package com.huijiewei.agile.core.consts;

import lombok.Getter;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author huijiewei
 */

@Getter
public class ValueDescription<T> {
    private static final Map<String, Map<?, ValueDescription<?>>> ELEMENTS = new HashMap<>();

    private final T value;
    private final String description;

    public ValueDescription(T value, String description) {
        this.value = value;
        this.description = description;

        Map<T, ValueDescription<?>> element = getElement();

        if (element == null) {
            element = new HashMap<>();

            ELEMENTS.put(getClass().getName(), element);
        }

        element.put(value, this);
    }

    @SuppressWarnings("unchecked")
    protected static <E extends ValueDescription<T>, T> E valueOf(Class<E> enumType, T value) {
        return (E) ELEMENTS.get(enumType.getName()).get(value);
    }

    public static <E extends ValueDescription<T>, T> E valueOf(T value) {
        throw new IllegalStateException("Sub class of ValueDescription must implement method valueOf()");
    }

    public static <E> ValueDescription<E>[] values() {
        throw new IllegalStateException("Sub class of ValueDescription must implement method values()");
    }

    @SuppressWarnings("unchecked")
    protected static <E> E[] values(Class<E> enumType) {
        Collection<ValueDescription<?>> values = ELEMENTS.get(enumType.getName()).values();

        E[] typedValues = (E[]) Array.newInstance(enumType, values.size());

        int i = 0;

        for (ValueDescription<?> value : values) {
            Array.set(typedValues, i, value);
            i++;
        }

        return typedValues;
    }


    @SuppressWarnings("unchecked")
    private Map<T, ValueDescription<?>> getElement() {
        return (Map<T, ValueDescription<?>>) ELEMENTS.get(getClass().getName());
    }
}
