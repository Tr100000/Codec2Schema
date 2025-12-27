package io.github.tr100000.codec2schema.impl.wrapped;

import io.github.tr100000.codec2schema.api.codec.CodecWithValuePairs;
import net.minecraft.util.StringRepresentable;

public interface WrappedStringRepresentableCodec<S extends StringRepresentable> extends CodecWithValuePairs<S> {}
