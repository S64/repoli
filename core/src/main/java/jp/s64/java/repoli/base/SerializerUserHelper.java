package jp.s64.java.repoli.base;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.s64.java.repoli.core.IDataKey;
import jp.s64.java.repoli.core.IRepositoryDataContainer;
import jp.s64.java.repoli.core.ISerializer;
import jp.s64.java.repoli.core.ISerializerUser;
import jp.s64.java.repoli.internal.ByteArrayContainer;
import jp.s64.java.repoli.internal.ReturningRepositoryDataContainer;
import jp.s64.java.repoli.preset.serializer.ListSerializer;
import jp.s64.java.repoli.preset.serializer.MapSerializer;
import jp.s64.java.repoli.preset.serializer.SerializableSerializer;
import jp.s64.java.repoli.preset.serializer.VoidSerializer;

/**
 * Created by shuma on 2017/05/19.
 */

public class SerializerUserHelper implements ISerializerUser {

    private final SortedSet<ISerializer> serializers = new TreeSet<>();

    public SerializerUserHelper() {
        addSerializer(Lists.newArrayList(
                VoidSerializer.INSTANCE,
                ListSerializer.INSTANCE,
                MapSerializer.INSTANCE,
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

    public SortedSet<ISerializer> getSerializers() {
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

    public <T, A> ReturningRepositoryDataContainer<T, A> convertBytesToReturning(IDataKey<T, A> key, ByteArrayContainer bytes) {
        ReturningRepositoryDataContainer<T, A> ret = new ReturningRepositoryDataContainer<>();
        {
            ret.setBody(deserializeByClass(
                    key.getBodyType(),
                    bytes.getBody() != null ? bytes.getBody() : new byte[0]
            ));
            ret.setAttachment(deserializeByClass(
                    key.getAttachmentType(),
                    bytes.getAttachment() != null ? bytes.getAttachment() : new byte[0]
            ));
        }
        {
            ret.setRequestedAtTimeMillis(bytes.getRequestedAtTimeMillis());
            ret.setSavedAtTimeMillis(bytes.getSavedAtTimeMillis());
        }
        return ret;
    }

    public <T, A> ByteArrayContainer convertContainerToBytes(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container) {
        ByteArrayContainer ret = new ByteArrayContainer();
        {
            byte[] bodyBytes = serializeByClass(
                    key.getBodyType(),
                    container.getBody()
            );
            ret.setBody(bodyBytes.length > 0 ? bodyBytes : null);
        }
        {
            byte[] attachmentBytes = serializeByClass(
                    key.getAttachmentType(),
                    container.getAttachment()
            );
            ret.setAttachment(attachmentBytes.length > 0 ? attachmentBytes : null);
        }
        {
            ret.setRequestedAtTimeMillis(container.getRequestedAtTimeMillis());
            ret.setSavedAtTimeMillis(container.getSavedAtTimeMillis());
        }
        return ret;
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
