package jp.s64.java.repoli.base;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import jp.s64.java.repoli.core.ISerializer;
import jp.s64.java.repoli.core.ISerializerUser;
import jp.s64.java.repoli.preset.serializer.ListSerializer;
import jp.s64.java.repoli.preset.serializer.SerializableSerializer;
import jp.s64.java.repoli.preset.serializer.VoidSerializer;

/**
 * Created by shuma on 2017/05/19.
 */

public class SerializerUserHelper implements ISerializerUser {

    private final Set<ISerializer> serializers = new LinkedHashSet<>();

    public SerializerUserHelper() {
        addSerializer(Lists.newArrayList(
                VoidSerializer.INSTANCE,
                ListSerializer.INSTANCE,
                SerializableSerializer.INSTANCE
        ));
    }

    @Override
    public void addSerializer(ISerializer serializer) {
        serializers.add(serializer);
    }

    @Override
    public void addSerializer(Collection<ISerializer> serializers) {
        this.serializers.addAll(serializers);
    }

    @Override
    public void removeSerializer(ISerializer serializer) {
        serializers.remove(serializer);
    }

    @Override
    public void removeSerializer(Collection<ISerializer> serializers) {
        this.serializers.removeAll(serializers);
    }

    @Override
    public void clearSerializer() {
        serializers.clear();
    }

    public Set<ISerializer> getSerializers() {
        return serializers;
    }

    public <T> T deserializeByClass(TypeToken<T> type, byte[] serialized) {
        for (ISerializer serializer : serializers) {
            if (!serializer.canSerialize(type)) {
                continue;
            }
            return serializer.deserialize(type, serialized, serializers);
        }
        throw SerializerNotFoundException.instantiate(serializers, type);
    }

    public <T> byte[] serializeByClass(TypeToken<T> type, Object deserialized) {
        for (ISerializer serializer : serializers) {
            if (!serializer.canSerialize(type)) {
                continue;
            }
            return serializer.serialize(type, deserialized, serializers);
        }
        throw SerializerNotFoundException.instantiate(serializers, type);
    }

    public static class SerializerNotFoundException extends RuntimeException {

        public static SerializerNotFoundException instantiate(Collection<ISerializer> serializers, TypeToken<?> type) {
            List<String> names = new LinkedList<>();
            for (ISerializer serializer : serializers) {
                names.add(serializer.getClass().getCanonicalName());
            }
            return new SerializerNotFoundException(names, type);
        }

        protected SerializerNotFoundException(Collection<String> serializerNames, TypeToken<?> type) {
            super(String.format(
                    "Supported serializer is not found. Current registered serializers: %s. Required class is `%s`.",
                    serializerNames,
                    type.getRawType().getCanonicalName()
            ));
        }

    }

}
