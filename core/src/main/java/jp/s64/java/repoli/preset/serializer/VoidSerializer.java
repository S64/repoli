package jp.s64.java.repoli.preset.serializer;

import jp.s64.java.repoli.core.ISerializer;
import jp.s64.java.repoli.core.TypeReference;

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
    public <T> T deserialize(TypeReference<T> type, byte[] serialized) {
        return null;
    }

    @Override
    public <T> byte[] serialize(TypeReference<T> type, T deserialized) {
        return new byte[0];
    }

    @Override
    public boolean canSerialize(TypeReference<?> type) {
        return Void.class.isAssignableFrom(type.getRawType());
    }
}
