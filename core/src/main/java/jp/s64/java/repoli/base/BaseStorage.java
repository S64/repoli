package jp.s64.java.repoli.base;

import java.util.Collection;

import jp.s64.java.repoli.core.IDataKey;
import jp.s64.java.repoli.core.IRepositoryDataContainer;
import jp.s64.java.repoli.core.ISerializer;
import jp.s64.java.repoli.core.IStorage;
import jp.s64.java.repoli.internal.ByteArrayContainer;
import jp.s64.java.repoli.internal.ReturningRepositoryDataContainer;

/**
 * Created by shuma on 2017/05/19.
 */

public abstract class BaseStorage implements IStorage {

    private final SerializerUserHelper serializers = new SerializerUserHelper();

    @Override
    public void addSerializer(ISerializer serializer) {
        serializers.addSerializer(serializer);
    }

    @Override
    public void removeSerializer(ISerializer serializer) {
        serializers.removeSerializer(serializer);
    }

    @Override
    public void addSerializer(Collection<ISerializer> serializer) {
        serializers.addSerializer(serializer);
    }

    @Override
    public void removeSerializer(Collection<ISerializer> serializer) {
        serializers.removeSerializer(serializer);
    }

    @Override
    public void clearSerializer() {
        serializers.clearSerializer();
    }

    @Override
    public <T, A> IRepositoryDataContainer<T, A> get(IDataKey<T, A> key) {
        ReturningRepositoryDataContainer<T, A> ret = new ReturningRepositoryDataContainer<>();
        ByteArrayContainer raw;
        {
            raw = new ByteArrayContainer(getBySerializedKey(key.getSerialized()));
        }
        {
            ret.setBody(
                    serializers.deserializeByClass(
                            key.getBodyType(),
                            raw.getBody() != null ? raw.getBody() : new byte[0]
                    )
            );
            ret.setAttachment(
                    serializers.deserializeByClass(
                            key.getAttachmentType(),
                            raw.getAttachment() != null ? raw.getAttachment() : new byte[0]
                    )
            );
            ret.setSavedAtTimeMillis(raw.getSavedAtTimeMillis());
            ret.setRequestedAtTimeMillis(raw.getRequestedAtTimeMillis());
        }
        return ret;
    }

    @Override
    public int remove(IDataKey<?, ?> key) {
        return removeBySerializedKey(key.getSerialized());
    }

    @Override
    public int removeRelatives(IDataKey<?, ?> key) {
        return removeRelativesByRelatedKey(key.getRelatedKey());
    }

    @Override
    public <T, A> IRepositoryDataContainer<T, A> save(IDataKey<T, A> key, IRepositoryDataContainer<T, A> original) {
        ReturningRepositoryDataContainer<T, A> container = new ReturningRepositoryDataContainer<>(original);
        {
            container.setSavedAtTimeMillis(System.currentTimeMillis());
        }
        ByteArrayContainer save = new ByteArrayContainer();
        {
            byte[] bodyBytes = serializers.serializeByClass(
                    key.getBodyType(),
                    container.getBody()
            );
            save.setBody(bodyBytes.length > 0 ? bodyBytes : null);
        }
        {
            byte[] attachmentBytes = serializers.serializeByClass(
                    key.getAttachmentType(),
                    container.getAttachment()
            );
            save.setAttachment(attachmentBytes.length > 0 ? attachmentBytes : null);
        }
        {
            save.setRequestedAtTimeMillis(container.getRequestedAtTimeMillis());
            save.setSavedAtTimeMillis(container.getSavedAtTimeMillis());
        }
        {
            saveBySerializedKey(key.getSerialized(), key.getRelatedKey(), save);
        }
        return container;
    }

    public abstract ByteArrayContainer getBySerializedKey(String serializedKey);

    public abstract int removeBySerializedKey(String serializedKey);

    public abstract int removeRelativesByRelatedKey(String relatedKey);

    public abstract void saveBySerializedKey(String serializedKey, String relatedKey, ByteArrayContainer container);

}
