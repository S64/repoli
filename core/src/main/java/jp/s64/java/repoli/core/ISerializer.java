package jp.s64.java.repoli.core;

import com.google.common.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * Created by shuma on 2017/05/19.
 */

public interface ISerializer extends Comparable<ISerializer> {

    @Nullable <T> T deserialize(@NotNull TypeToken<T> type, @Nullable byte[] serialized, @NotNull Set<ISerializer> serializers);

    @Nullable <T> byte[] serialize(@NotNull TypeToken<T> type, @Nullable Object deserialized, @NotNull Set<ISerializer> serializers);

    boolean canSerialize(@NotNull TypeToken<?> type);

}
