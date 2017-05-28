package jp.s64.java.repoli.realm;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;

import io.realm.Realm;
import io.realm.RealmResults;
import jp.s64.java.repoli.core.IRepositoryDataContainer;
import jp.s64.java.repoli.internal.ReturningRepositoryDataContainer;
import jp.s64.java.repoli.rxjava1.base.BaseRxStorage;
import rx.Observable;
import rx.Scheduler;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by shuma on 2017/05/27.
 */

public abstract class RealmStorage extends BaseRxStorage {

    private Scheduler scheduler;

    public RealmStorage(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public Observable<IRepositoryDataContainer<byte[], byte[]>> getBySerializedKey(final String serializedKey) {
        final Supplier<Realm> realm = Suppliers.memoize(new Supplier<Realm>() {
            @Override
            public Realm get() {
                return getRealmInstance();
            }
        });

        return Observable
                .<Void>just(null)
                .observeOn(scheduler)
                .map(new Func1<Void, RealmResults<StorageObject>>() {

                    @Override
                    public RealmResults<StorageObject> call(Void _) {
                        return realm.get()
                                .where(StorageObject.class)
                                .equalTo("serializedKey", serializedKey)
                                .findAll();
                    }

                })
                .map(new Func1<RealmResults<StorageObject>, IRepositoryDataContainer<byte[], byte[]>>() {
                    @Override
                    public IRepositoryDataContainer<byte[], byte[]> call(RealmResults<StorageObject> rows) {
                        return rows.size() > 0 ? new ReturningRepositoryDataContainer<byte[], byte[]>(rows.first()) : new ReturningRepositoryDataContainer<byte[], byte[]>();
                    }
                })
                .doOnNext(new Action1<IRepositoryDataContainer<byte[], byte[]>>() {
                    @Override
                    public void call(IRepositoryDataContainer<byte[], byte[]> _) {
                        if (closeAfter()) realm.get().close();
                    }
                });
    }

    @Override
    public Observable<Integer> removeBySerializedKey(final String serializedKey) {
        final Supplier<Realm> realm = Suppliers.memoize(new Supplier<Realm>() {
            @Override
            public Realm get() {
                return getRealmInstance();
            }
        });

        return Observable
                .<Void>just(null)
                .observeOn(scheduler)
                .doOnNext(new Action1<Void>() {
                    @Override
                    public void call(Void _) {
                        realm.get().beginTransaction();

                    }
                })
                .map(new Func1<Void, RealmResults<StorageObject>>() {

                    @Override
                    public RealmResults<StorageObject> call(Void _) {
                        return realm.get()
                                .where(StorageObject.class)
                                .equalTo("serializedKey", serializedKey)
                                .findAll();
                    }

                })
                .map(new Func1<RealmResults<StorageObject>, Integer>() {
                    @Override
                    public Integer call(RealmResults<StorageObject> rows) {
                        int ret = rows.size();
                        {
                            rows.deleteAllFromRealm();
                        }
                        return ret;
                    }
                })
                .doOnNext(new Action1<Integer>() {
                    @Override
                    public void call(Integer _) {
                        realm.get().commitTransaction();
                        if (closeAfter()) realm.get().close();
                    }
                });
    }

    @Override
    public Observable<Integer> removeRelativesByRelatedKey(final String relatedKey) {
        final Supplier<Realm> realm = Suppliers.memoize(new Supplier<Realm>() {
            @Override
            public Realm get() {
                return getRealmInstance();
            }
        });

        return Observable
                .<Void>just(null)
                .observeOn(scheduler)
                .doOnNext(new Action1<Void>() {
                    @Override
                    public void call(Void _) {
                        realm.get().beginTransaction();
                    }
                })
                .map(new Func1<Void, RealmResults<StorageObject>>() {

                    @Override
                    public RealmResults<StorageObject> call(Void _) {
                        return realm.get()
                                .where(StorageObject.class)
                                .equalTo("relatedKey", relatedKey)
                                .findAll();
                    }

                })
                .map(new Func1<RealmResults<StorageObject>, Integer>() {

                    @Override
                    public Integer call(RealmResults<StorageObject> rows) {
                        int ret = rows.size();
                        {
                            rows.deleteAllFromRealm();
                        }
                        return ret;
                    }

                })
                .doOnNext(new Action1<Integer>() {
                    @Override
                    public void call(Integer _) {
                        realm.get().commitTransaction();
                        if (closeAfter()) realm.get().close();
                    }
                });
    }

    @Override
    public Observable<Void> saveBySerializedKey(final String serializedKey, final String relatedKey, final IRepositoryDataContainer<byte[], byte[]> container) {
        final Supplier<Realm> realm = Suppliers.memoize(new Supplier<Realm>() {
            @Override
            public Realm get() {
                return getRealmInstance();
            }
        });

        return Observable
                .<Void>just(null)
                .observeOn(scheduler)
                .doOnNext(new Action1<Void>() {
                    @Override
                    public void call(Void _) {
                        realm.get().beginTransaction();
                    }
                })
                .map(new Func1<Void, StorageObject>() {
                    @Override
                    public StorageObject call(Void _) {
                        StorageObject obj = realm.get().createObject(StorageObject.class, serializedKey);
                        {
                            //obj.setSerializedKey(serializedKey);
                            obj.setRelatedKey(relatedKey);
                        }
                        {
                            obj.setSavedAtTimeMillis(container.getSavedAtTimeMillis());
                            obj.setRequestedAtTimeMillis(container.getRequestedAtTimeMillis());
                            obj.setBody(container.getBody());
                            obj.setAttachment(container.getAttachment());
                        }
                        return obj;
                    }
                })
                .doOnNext(new Action1<StorageObject>() {
                    @Override
                    public void call(StorageObject obj) {
                        realm.get().insertOrUpdate(obj);
                    }
                })
                .doOnNext(new Action1<StorageObject>() {
                    @Override
                    public void call(StorageObject obj) {
                        realm.get().commitTransaction();
                        if (closeAfter()) realm.get().close();
                    }
                })
                .map(new Func1<StorageObject, Void>() {
                    @Override
                    public Void call(StorageObject obj) {
                        return null; // succeed
                    }
                });
    }

    public abstract Realm getRealmInstance();

    public abstract boolean closeAfter();

}
