package com.worktimetracker.DataClasses;

public interface StringConverter<T> {
    String toString(T object);
    T fromString(String string);
}
