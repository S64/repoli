package jp.s64.java.repoli.base;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import jp.s64.java.repoli.core.IDataKey;
import jp.s64.java.repoli.core.IRepositoryDataContainer;
import jp.s64.java.repoli.core.ISerializer;
import jp.s64.java.repoli.core.ISerializerUser;
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
    public void addSerializer(@NotNull ISerializer serializer) {
        serializers.add(serializer);
    }

    @Override
    public void addSerializer(@NotNull Collection<ISerializer> serializers) {
        this.serializers.addAll(serializers);
    }

    @Override
    public void removeSerializer(@NotNull ISerializer serializer) {
        serializers.remove(serializer);
    }

    @Override
    public void removeSerializer(@NotNull Collection<ISerializer> serializers) {
        this.serializers.removeAll(serializers);
    }

    @Override
    public void clearSerializer() {
        serializers.clear();
    }

    @NotNull
    public SortedSet<ISerializer> getSerializers() {
        return serializers;
    }

    @NotNull
    public <T> T deserializeByClass(@NotNull TypeToken<T> type, @NotNull byte[] serialized) {
        for (ISerializer serializer : serializers) {
            if (!serializer.canSerialize(type)) {
                continue;
            }
            return serializer.deserialize(type, serialized, serializers);
        }
        throw SerializerNotFoundException.instantiate(serializers, type);
    }

    @NotNull
    public <T> byte[] serializeByClass(@NotNull TypeToken<T> type, @NotNull Object deserialized) {
        for (ISerializer serializer : serializers) {
            if (!serializer.canSerialize(type)) {
                continue;
            }
            return serializer.serialize(type, deserialized, serializers);
        }
        throw SerializerNotFoundException.instantiate(serializers, type);
    }

    @NotNull
    public <T, A> ReturningRepositoryDataContainer<T, A> convertBytesToReturning(@NotNull IDataKey<T, A> key, @NotNull IRepositoryDataContainer<byte[], byte[]> bytes) {
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

    @NotNull
    public <T, A> IRepositoryDataContainer<byte[], byte[]> convertContainerToBytes(@NotNull IDataKey<T, A> key, @NotNull IRepositoryDataContainer<T, A> container) {
        ReturningRepositoryDataContainer<byte[], byte[]> ret = new ReturningRepositoryDataContainer<>();
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

        @NotNull
        public static SerializerNotFoundException instantiate(@NotNull Collection<ISerializer> serializers, @NotNull TypeToken<?> type) {
            List<String> names = new LinkedList<>();
            for (ISerializer serializer : serializers) {
                names.add(serializer.getClass().getCanonicalName());
            }
            return new SerializerNotFoundException(names, type);
        }

        protected SerializerNotFoundException(@NotNull Collection<String> serializerNames, @NotNull TypeToken<?> type) {
            super(String.format(
                    "Supported serializer is not found. Current registered serializers: %s. Required class is `%s`.",
                    serializerNames,
                    type.getRawType().getCanonicalName()
            ));
        }

    }

}
