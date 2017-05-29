package jp.s64.java.repoli.rxjava1.base;

import java.util.Collection;

import jp.s64.java.repoli.base.StorageHelper;
import jp.s64.java.repoli.core.IDataKey;
import jp.s64.java.repoli.core.IRepositoryDataContainer;
import jp.s64.java.repoli.core.ISerializer;
import jp.s64.java.repoli.core.ISerializerUser;
import jp.s64.java.repoli.internal.ReturningRepositoryDataContainer;
import jp.s64.java.repoli.rxjava1.core.IRxStorage;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by shuma on 2017/05/26.
 */

public abstract class BaseRxStorage<TB, AB> implements IRxStorage<TB, AB>, ISerializerUser {

    private final StorageHelper helper = new StorageHelper();

    @Override
    public void addSerializer(ISerializer serializer) {
        helper.addSerializer(serializer);
    }

    @Override
    public void addSerializer(Collection<ISerializer> serializer) {
        helper.addSerializer(serializer);
    }

    @Override
    public void removeSerializer(ISerializer serializer) {
        helper.removeSerializer(serializer);
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
    public <T extends TB, A extends AB> Observable<IRepositoryDataContainer<T, A>> getAsync(final IDataKey<T, A> key) {
        return getBySerializedKey(key.getSerialized())
                .map(new Func1<IRepositoryDataContainer<byte[], byte[]>, IRepositoryDataContainer<T, A>>() {
                    @Override
                    public IRepositoryDataContainer<T, A> call(IRepositoryDataContainer<byte[], byte[]> bytes) {
                        return helper.convertBytesToReturning(key, bytes);
                    }
                });
    }

    @Override
    public Observable<Integer> removeAsync(IDataKey<?, ?> key) {
        return removeBySerializedKey(key.getSerialized());
    }

    @Override
    public Observable<Integer> removeRelativesAsync(IDataKey<?, ?> key) {
        return removeRelativesByRelatedKey(key.getRelatedKey());
    }

    @Override
    public <T extends TB, A extends AB> Observable<IRepositoryDataContainer<T, A>> saveAsync(IDataKey<T, A> key, IRepositoryDataContainer<T, A> original) {
        final ReturningRepositoryDataContainer<T, A> container = new ReturningRepositoryDataContainer<>(original);
        {
            container.setSavedAtTimeMillis(System.currentTimeMillis());
        }
        IRepositoryDataContainer<byte[], byte[]> save = helper.convertContainerToBytes(key, container);
        return saveBySerializedKey(key.getSerialized(), key.getRelatedKey(), save)
                .map(new Func1<Void, IRepositoryDataContainer<T, A>>() {
                    @Override
                    public IRepositoryDataContainer<T, A> call(Void aVoid) {
                        return container;
                    }
                });
    }

    public abstract Observable<IRepositoryDataContainer<byte[], byte[]>> getBySerializedKey(String serializedKey);

    public abstract Observable<Integer> removeBySerializedKey(String serializedKey);

    public abstract Observable<Integer> removeRelativesByRelatedKey(String relatedKey);

    public abstract Observable<Void> saveBySerializedKey(String serializedKey, String relatedKey, IRepositoryDataContainer<byte[], byte[]> container);

}
