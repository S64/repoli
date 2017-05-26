package jp.s64.java.repoli.base;

import java.util.Collection;

import jp.s64.java.repoli.core.IDataKey;
import jp.s64.java.repoli.core.IProvider;
import jp.s64.java.repoli.core.IRepositoryDataContainer;
import jp.s64.java.repoli.core.ISerializer;
import jp.s64.java.repoli.internal.ReturningRepositoryDataContainer;

/**
 * Created by shuma on 2017/05/22.
 */

public abstract class BaseProvider implements IProvider {

    private final ProviderHelper helper = new ProviderHelper();

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
    public <T, A> IRepositoryDataContainer<T, A> request(IDataKey<T, A> key) {
        ReturningRepositoryDataContainer<T, A> ret;
        ReturningRepositoryDataContainer<byte[], byte[]> raw;
        {
            String serialized = key.getSerialized();
            raw = new ReturningRepositoryDataContainer<>(requestBySerializedKey(serialized));
            raw.setRequestedAtTimeMillis(System.currentTimeMillis());
            raw.setSavedAtTimeMillis(System.currentTimeMillis());
        }
        return helper.convertBytesToReturning(key, raw);
    }

    public abstract IRepositoryDataContainer<byte[], byte[]> requestBySerializedKey(String serializedKey);

}
