package jp.s64.java.repoli.rxjava1.preset;

import jp.s64.java.repoli.core.IDataKey;
import jp.s64.java.repoli.core.IRepositoryDataContainer;
import jp.s64.java.repoli.core.IStorage;
import jp.s64.java.repoli.internal.ByteArrayContainer;
import jp.s64.java.repoli.preset.SimpleOnMemoryStorage;
import jp.s64.java.repoli.rxjava1.base.BaseRxStorage;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by shuma on 2017/05/26.
 */

public class SimpleRxOnMemoryStorage extends BaseRxStorage implements IStorage {

    private final SimpleOnMemoryStorage synchronous = new SimpleOnMemoryStorage();

    @Override
    public Observable<ByteArrayContainer> getBySerializedKey(String serializedKey) {
        return Observable.just(synchronous.getBySerializedKey(serializedKey));
    }

    @Override
    public Observable<Integer> removeBySerializedKey(String serializedKey) {
        return Observable.just(synchronous.removeBySerializedKey(serializedKey));
    }

    @Override
    public Observable<Integer> removeRelativesByRelatedKey(String relatedKey) {
        return Observable.just(synchronous.removeRelativesByRelatedKey(relatedKey));
    }

    @Override
    public Observable<Void> saveBySerializedKey(final String serializedKey, final String relatedKey, final ByteArrayContainer container) {
        return Observable
                .<Void>just(null)
                .doOnNext(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        synchronous.saveBySerializedKey(serializedKey, relatedKey, container);
                    }
                });
    }

    @Override
    public <T, A> IRepositoryDataContainer<T, A> get(IDataKey<T, A> key) {
        return synchronous.get(key);
    }

    @Override
    public int remove(IDataKey<?, ?> key) {
        return synchronous.remove(key);
    }

    @Override
    public int removeRelatives(IDataKey<?, ?> key) {
        return synchronous.removeRelatives(key);
    }

    @Override
    public <T, A> IRepositoryDataContainer<T, A> save(IDataKey<T, A> key, IRepositoryDataContainer<T, A> container) {
        return synchronous.save(key, container);
    }

}
