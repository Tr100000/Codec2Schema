package io.github.tr100000.codec2schema.api;

public record NumberBound<T extends Number>(T value, boolean exclusive) {}
