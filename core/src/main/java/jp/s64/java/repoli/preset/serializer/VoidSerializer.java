package jp.s64.java.repoli.preset.serializer;

import com.google.common.reflect.TypeToken;

import java.util.Set;

import jp.s64.java.repoli.core.ISerializer;

/**
 * Created by shuma on 2017/05/22.
 */

public class VoidSerializer implements ISerializer {

    public static VoidSerializer INSTANCE = newInstance();

    public static VoidSerializer newInstance() {
        return new VoidSerializer();
    }

    protected VoidSerializer() {

    }

    @Override
    public <T> T deserialize(TypeToken<T> type, byte[] serialized, Set<ISerializer> serializers) {
        return null;
    }

    @Override
    public <T> byte[] serialize(TypeToken<T> type, Object deserialized, Set<ISerializer> serializers) {
        return new byte[0];
    }

    @Override
    public boolean canSerialize(TypeToken<?> type) {
        return Void.class.isAssignableFrom(type.getRawType());
    }

    @Override
    public int compareTo(ISerializer serializer) {
        return -1;
    }

}
