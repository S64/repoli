package jp.s64.java.repoli.core;

import com.google.common.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Set;

/**
 * Created by shuma on 2017/05/19.
 */

public interface ISerializer {

    Comparator<ISerializer> COMPARATOR = new SerializerComparator();

    @Nullable
    <T> T deserialize(@NotNull TypeToken<T> type, @Nullable byte[] serialized, @NotNull Set<ISerializer> serializers);

    @Nullable
    <T> byte[] serialize(@NotNull TypeToken<T> type, @Nullable Object deserialized, @NotNull Set<ISerializer> serializers);

    boolean canSerialize(@NotNull TypeToken<?> type);

    float getPriority();

    class SerializerComparator implements Comparator<ISerializer> {

        @Override
        public int compare(ISerializer o1, ISerializer o2) {
            return Float.compare(o1.getPriority(), o2.getPriority());
        }

    }

}
