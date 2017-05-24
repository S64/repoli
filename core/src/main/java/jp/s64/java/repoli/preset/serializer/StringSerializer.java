package jp.s64.java.repoli.preset.serializer;

import com.google.common.reflect.TypeToken;

import java.util.Set;

import jp.s64.java.repoli.core.ISerializer;

/**
 * Created by shuma on 2017/05/24.
 */

public class StringSerializer implements ISerializer {

    public static StringSerializer INSTANCE = newInstance();

    public static StringSerializer newInstance() {
        return new StringSerializer();
    }

    protected StringSerializer() {
    }

    @Override
    public <T> T deserialize(TypeToken<T> type, byte[] serialized, Set<ISerializer> serializers) {
        return (T) new String(serialized);
    }

    @Override
    public <T> byte[] serialize(TypeToken<T> type, Object deserialized, Set<ISerializer> serializers) {
        return ((String) deserialized).getBytes();
    }

    @Override
    public boolean canSerialize(TypeToken<?> type) {
        return String.class.isAssignableFrom(type.getRawType());
    }

}
