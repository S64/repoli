package jp.s64.java.repoli.preset.serializer;

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
    public <T> T deserialize(Class<T> clazz, byte[] serialized) {
        return null;
    }

    @Override
    public <T> byte[] serialize(Class<T> clazz, T deserialized) {
        return new byte[]{};
    }

    @Override
    public boolean canSerialize(Class<?> clazz) {
        return Void.class.isAssignableFrom(clazz);
    }

}
