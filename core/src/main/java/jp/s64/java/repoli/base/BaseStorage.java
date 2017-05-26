package jp.s64.java.repoli.base;

import java.util.Collection;

import jp.s64.java.repoli.core.IDataKey;
import jp.s64.java.repoli.core.IRepositoryDataContainer;
import jp.s64.java.repoli.core.ISerializer;
import jp.s64.java.repoli.core.IStorage;
import jp.s64.java.repoli.internal.ReturningRepositoryDataContainer;

/**
 * Created by shuma on 2017/05/19.
 */

public abstract class BaseStorage implements IStorage {

    private final StorageHelper helper = new StorageHelper();

    @Override
    public void addSerializer(ISerializer serializer) {
        helper.addSerializer(serializer);
    }

    @Override
    public void removeSerializer(ISerializer serializer) {
        helper.removeSerializer(serializer);
    }

    @Override
    public void addSerializer(Collection<ISerializer> serializer) {
        helper.addSerializer(serializer);
    }

    @Override
    public void removeSerializer(Collection<ISerializer> serializer) {
        helper.removeSerializer(serializer);
    }

    @Override
    public void clearSerializer() {
        helper.clearSerializer();
    }

    @Override
    public <T, A> IRepositoryDataContainer<T, A> get(IDataKey<T, A> key) {
        ReturningRepositoryDataContainer<T, A> ret;
        IRepositoryDataContainer<byte[], byte[]> raw;
        {
            raw = new ReturningRepositoryDataContainer<>(getBySerializedKey(key.getSerialized()));
        }
        return helper.convertBytesToReturning(key, raw);
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
        IRepositoryDataContainer<byte[], byte[]> save = helper.convertContainerToBytes(key, container);
        {
            saveBySerializedKey(key.getSerialized(), key.getRelatedKey(), save);
        }
        return container;
    }

    public abstract IRepositoryDataContainer<byte[], byte[]> getBySerializedKey(String serializedKey);

    public abstract int removeBySerializedKey(String serializedKey);

    public abstract int removeRelativesByRelatedKey(String relatedKey);

    public abstract void saveBySerializedKey(String serializedKey, String relatedKey, IRepositoryDataContainer<byte[], byte[]> container);

}
