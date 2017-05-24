package jp.s64.java.repoli.base;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import jp.s64.java.repoli.core.ISerializer;
import jp.s64.java.repoli.core.ISerializerUser;
import jp.s64.java.repoli.preset.serializer.VoidSerializer;

/**
 * Created by shuma on 2017/05/19.
 */

public class SerializerUserHelper implements ISerializerUser {

    private final Set<ISerializer> serializers = new LinkedHashSet<>();
    private final VoidSerializer voidSerializer = VoidSerializer.INSTANCE;

    public SerializerUserHelper() {
        addSerializer(voidSerializer);
    }

    @Override
    public void addSerializer(ISerializer serializer) {
        serializers.add(serializer);
    }

    @Override
    public void removeSerializer(ISerializer serializer) {
        serializers.remove(serializer);
    }

    @Override
    public void clearSerializer() {
        serializers.clear();
    }

    public <T> T deserializeByClass(Class<T> clazz, byte[] serialized) {
        for (ISerializer serializer : serializers) {
            if (!serializer.canSerialize(clazz)) {
                continue;
            }
            return serializer.deserialize(clazz, serialized);
        }
        throw SerializerNotFoundException.instantiate(serializers, clazz);
    }

    public <T> byte[] serializeByClass(Class<T> clazz, T deserialized) {
        for (ISerializer serializer : serializers) {
            if (!serializer.canSerialize(clazz)) {
                continue;
            }
            return serializer.serialize(clazz, deserialized);
        }
        throw SerializerNotFoundException.instantiate(serializers, clazz);
    }

    public static class SerializerNotFoundException extends RuntimeException {

        public static SerializerNotFoundException instantiate(Collection<ISerializer> serializers, Class<?> clazz) {
            List<String> names = new LinkedList<>();
            for (ISerializer serializer : serializers) {
                names.add(serializer.getClass().getCanonicalName());
            }
            return new SerializerNotFoundException(names, clazz);
        }

        protected SerializerNotFoundException(Collection<String> serializerNames, Class<?> clazz) {
            super(String.format(
                    "Supported serializer is not found. Current registered serializers: %s. Required class is `%s`.",
                    serializerNames,
                    clazz.getCanonicalName()
            ));
        }

    }

}
